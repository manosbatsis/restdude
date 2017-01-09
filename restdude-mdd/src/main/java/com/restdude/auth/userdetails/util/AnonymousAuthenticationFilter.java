/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.userdetails.util;

import com.restdude.auth.userdetails.model.UserDetails;
import com.restdude.domain.users.model.Role;
import com.restdude.domain.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Detects if there is no {@code Authentication} object in the
 * {@code SecurityContextHolder}, and populates it with one if needed.
 */
public class AnonymousAuthenticationFilter extends GenericFilterBean implements
        InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousAuthenticationFilter.class);
    // ~ Instance fields
    // ================================================================================================

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private String key;
    private Object principal;
    private List<GrantedAuthority> authorities;

    /**
     * Creates a filter with a principal named "anonymousUser" and the single authority
     * "ROLE_ANONYMOUS".
     *
     * @param key the key to identify tokens created by this filter
     */
    public AnonymousAuthenticationFilter(String key) {
        Assert.hasLength(key, "key cannot be null or empty");

        // set key
        this.key = key;

        // set principal
        User user = new User();
        user.setUsername("anonymousUser");
        user.addRole(new Role(Role.ROLE_ANONYMOUS));
        this.principal = UserDetails.fromUser(user);

        // set authorities
        this.authorities = new ArrayList<>(user.getRoles().size());
        this.authorities.addAll(user.getRoles());
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public void afterPropertiesSet() {
        Assert.hasLength(key);
        Assert.notNull(principal, "Anonymous authentication principal must be set");
        Assert.notNull(authorities, "Anonymous authorities must be set");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(
                    createAuthentication((HttpServletRequest) req));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Populated SecurityContextHolder with anonymous token: '{}'", SecurityContextHolder.getContext().getAuthentication());
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '{}'", SecurityContextHolder.getContext().getAuthentication());
            }
        }

        chain.doFilter(req, res);
    }

    protected Authentication createAuthentication(HttpServletRequest request) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,
                principal, authorities);
        auth.setDetails(authenticationDetailsSource.buildDetails(request));

        return auth;
    }

    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,
                "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public Object getPrincipal() {
        return principal;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
