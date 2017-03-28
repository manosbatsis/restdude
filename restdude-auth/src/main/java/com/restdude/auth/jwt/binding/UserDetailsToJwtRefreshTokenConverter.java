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


import com.restdude.auth.config.JwtSettings;
import com.restdude.auth.jwt.model.JwtRefreshToken;
import com.restdude.mdd.model.Roles;
import com.restdude.mdd.model.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Creates Access and Refresh JWT tokens
 */
@Slf4j
@Component
public class UserDetailsToJwtRefreshTokenConverter extends AbstractJwtConverter implements Converter<UserDetails, JwtRefreshToken> {

    /**
     * Convert the source object of type {@link UserDetails} to a {@link JwtRefreshToken}.
     *
     * @param userDetails the source user details object
     * @return the resulting JWT Refresh Token
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public JwtRefreshToken convert(@NonNull UserDetails userDetails) {
        if (StringUtils.isBlank(userDetails.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        JwtSettings settings = this.getSettings();

        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("scopes", Arrays.asList(Roles.ROLE_REFRESH_TOKEN));

        String s = getCompact(claims, settings, settings.getAccessTokenMinutes());

        SimpleJwtToken token = new SimpleJwtToken(s, claims);
        log.debug("convert, userDetails: {}, token: {}", userDetails, token);

        return token;
    }


}
