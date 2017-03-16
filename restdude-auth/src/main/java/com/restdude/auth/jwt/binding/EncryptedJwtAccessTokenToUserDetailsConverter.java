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

import com.restdude.auth.jwt.model.RawAccessJwtToken;
import com.restdude.mdd.model.UserDetails;
import com.restdude.util.Constants;
import com.restdude.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link RawAccessJwtToken} to {@link UserDetails} if the token is valid, has not expired and contains roles
 */
@Slf4j
@Component
public class EncryptedJwtAccessTokenToUserDetailsConverter extends AbstractEncryptedJwtAccessTokenConverter implements Converter<String, UserDetails> {


    public UserDetails convert(HttpServletRequest request) {
        return this.convert(CookieUtil.getCookieValue(request, Constants.JWT_AUTHENTICATION_TOKEN_COOKIE_NAME));
    }
    /**
     * Main conversion method.
     *
     * @param rawAccessJwtToken
     */
    @Override
    public UserDetails convert(String rawAccessJwtToken) {
        return totUserDetails(rawAccessJwtToken).orElse(null);
    }


}
