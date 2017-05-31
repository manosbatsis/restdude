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
import com.restdude.auth.jwt.model.JwtAccessToken;
import com.restdude.domain.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Converts {@link UserDetails} to JWT Access Token
 *
 * @see <a href="https://www.iana.org/assignments/jwt/jwt.xhtml">https://www.iana.org/assignments/jwt/jwt.xhtml</a>
 */
@Slf4j
@Component
public class UserDetailsToJwtAccessTokenConverter extends AbstractJwtConverter implements Converter<UserDetails, JwtAccessToken> {


    /**
     * Main conversion method.
     *
     * @param userDetails
     */
    @Override
    public JwtAccessToken convert(UserDetails userDetails) {
        if (StringUtils.isBlank(userDetails.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        // create claims object, add subject
        Claims claims = Jwts.claims().setSubject("users/" + userDetails.getId());
        // add name
        claims.put(JwtSettings.CLAIM_NAME, userDetails.getName());
        claims.put(JwtSettings.CLAIM_FIRST_NAME, userDetails.getFirstName());
        claims.put(JwtSettings.CLAIM_LAST_NAME, userDetails.getLastName());
        claims.put(JwtSettings.CLAIM_USERNAME, userDetails.getUsername());

        claims.put(JwtSettings.CLAIM_LOCALE, userDetails.getLocale());



        // add roles
        claims.put("scopes", userDetails.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));


        JwtSettings settings = this.getSettings();
        Integer minutes = settings.getAccessTokenMinutes();

        String s = getCompact(claims, settings, minutes);

        SimpleJwtToken token = new SimpleJwtToken(s, claims);

        log.debug("convert, userDetails: {}, token: {}", userDetails, token);

        return token;
    }



}
