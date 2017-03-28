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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Base class for converting from/to JWT with {@link Autowired} instance of a {@link JwtSettings} bean
 */
public class AbstractJwtConverter {
    protected static final String JWT_SUBJECT_PREFIX = "users/";
    private JwtSettings settings;

    @Autowired
    public void setSettings(JwtSettings settings) {
        this.settings = settings;
    }

    protected JwtSettings getSettings() {
        return settings;
    }


    protected String getCompact(Claims claims, JwtSettings settings, Integer minutes) {
        String s = null;
        // create, sign and compact KWT token
        LocalDateTime currentTime = LocalDateTime.now();

        JwtBuilder builder = Jwts.builder()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(new Date(currentTime.toInstant(ZoneOffset.UTC).toEpochMilli()))
                .setClaims(claims)
                .setExpiration(new Date(currentTime.plusMinutes(minutes).toInstant(ZoneOffset.UTC).toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey());
        s = builder.compact();
        return s;
    }
}
