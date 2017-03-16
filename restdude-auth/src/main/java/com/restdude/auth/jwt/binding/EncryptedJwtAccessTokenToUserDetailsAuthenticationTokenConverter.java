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
package com.restdude.auth.jwt.binding;

import com.restdude.auth.model.UserDetailsAuthenticationToken;
import com.restdude.mdd.model.UserDetails;
import com.restdude.util.Constants;
import com.restdude.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Converts a raw encrypted JWT access token} to {@link UserDetailsAuthenticationToken} if the token is valid, has not expired and contains roles
 */
@Slf4j
@Component
public class EncryptedJwtAccessTokenToUserDetailsAuthenticationTokenConverter extends AbstractEncryptedJwtAccessTokenConverter implements Converter<String, UserDetailsAuthenticationToken> {


    public UserDetailsAuthenticationToken convert(HttpServletRequest request) {
        return this.toUserDetailsAuthenticationToken(CookieUtil.getCookieValue(request, Constants.JWT_AUTHENTICATION_TOKEN_COOKIE_NAME))
                .orElse(null);
    }
    /**
     * Main conversion method.
     *
     * @param rawAccessJwtToken
     */
    @Override
    public UserDetailsAuthenticationToken convert(String rawAccessJwtToken) {
        return toUserDetailsAuthenticationToken(rawAccessJwtToken).orElseGet(null);
    }

    protected Optional<UserDetailsAuthenticationToken> toUserDetailsAuthenticationToken(String rawAccessJwtToken) {
        UserDetailsAuthenticationToken token = null;
        Optional<UserDetails> userDetails = this.totUserDetails(rawAccessJwtToken);
        if(userDetails.isPresent()){
            token = new UserDetailsAuthenticationToken(userDetails.orElse(null));
        }
        return Optional.ofNullable(token);
    }


}
