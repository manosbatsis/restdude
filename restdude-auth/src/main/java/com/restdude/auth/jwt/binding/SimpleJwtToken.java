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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.auth.jwt.model.JwtAccessToken;
import com.restdude.auth.jwt.model.JwtRefreshToken;
import io.jsonwebtoken.Claims;
import lombok.Getter;

/**
 * A simple JWT Token implementation
 */
public final class SimpleJwtToken implements JwtAccessToken, JwtRefreshToken {

    @Getter
    private final String token;

    @Getter
    @JsonIgnore
    private Claims claims;

    protected SimpleJwtToken(final String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }

}
