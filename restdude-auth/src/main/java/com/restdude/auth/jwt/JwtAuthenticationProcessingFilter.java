/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.jwt;

import com.restdude.auth.jwt.binding.EncryptedJwtAccessTokenToUserDetailsConverter;
import com.restdude.auth.model.UserDetailsAuthenticationToken;
import com.restdude.mdd.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationProcessingFilter  extends OncePerRequestFilter {


    private EncryptedJwtAccessTokenToUserDetailsConverter jwtRawAccessTokenToUserDetailsConverter;

    @Autowired
    public void setJwtRawAccessTokenToUserDetailsConverter(EncryptedJwtAccessTokenToUserDetailsConverter jwtRawAccessTokenToUserDetailsConverter) {
        this.jwtRawAccessTokenToUserDetailsConverter = jwtRawAccessTokenToUserDetailsConverter;
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try{
            UserDetails userDetails = jwtRawAccessTokenToUserDetailsConverter.convert(request);

            // if a JWT/UserDetails was found/built
            if (userDetails != null) {

                // set authentication
                try {
                    String username = userDetails.getUsername();
                    log.debug("JWT access token found for user '{}'", username);

                    if (authenticationIsRequired(username)) {
                        Authentication authResult = new UserDetailsAuthenticationToken(userDetails);
                        log.debug("Authentication success: {}", authResult);
                        SecurityContextHolder.getContext().setAuthentication(authResult);
                        onSuccessfulAuthentication(request, response, authResult);
                    }

                }
                catch (AuthenticationException failed) {
                    SecurityContextHolder.clearContext();
                    log.debug("Authentication request for failed: {}", failed);
                    onUnsuccessfulAuthentication(request, response, failed);
                }
            }
        }
        catch (Exception e){
            log.warn("Failed processing request, ignoring", e);
        }

        chain.doFilter(request, response);
    }


    protected boolean authenticationIsRequired(String username) {
        // Only reauthenticate if username doesn't match SecurityContextHolder and user
        // isn't authenticated
        // (see SEC-53)
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();

        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }

        // Limit username comparison to providers which use usernames (ie
        // UsernamePasswordAuthenticationToken)
        // (see SEC-348)

        if (existingAuth instanceof UsernamePasswordAuthenticationToken
                && !existingAuth.getName().equals(username)) {
            return true;
        }

        // Handle unusual condition where an AnonymousAuthenticationToken is already
        // present
        // This shouldn't happen very often, as BasicProcessingFitler is meant to be
        // earlier in the filter
        // chain than AnonymousAuthenticationFilter. Nevertheless, presence of both an
        // AnonymousAuthenticationToken
        // together with a BASIC authentication request header should indicate
        // reauthentication using the
        // BASIC protocol is desirable. This behaviour is also consistent with that
        // provided by form and digest,
        // both of which force re-authentication if the respective header is detected (and
        // in doing so replace
        // any existing AnonymousAuthenticationToken). See SEC-610.
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }

        return false;
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, Authentication authResult) throws IOException {
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
                                                HttpServletResponse response, AuthenticationException failed)
            throws IOException {
    }
}