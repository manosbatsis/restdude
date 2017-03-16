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
package com.restdude.auth.jwt.aervice;

import com.restdude.auth.jwt.binding.UserDetailsToJwtAccessTokenConverter;
import com.restdude.auth.jwt.binding.UserDetailsToJwtRefreshTokenConverter;
import com.restdude.auth.jwt.model.JwtAccessToken;
import com.restdude.auth.jwt.model.JwtRefreshToken;
import com.restdude.mdd.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Creates Access and Refresh JWTs
 */
@Slf4j
@Service
public class JwtTokenService {

    private UserDetailsToJwtAccessTokenConverter jwtAccessTokenConverter;
    private UserDetailsToJwtRefreshTokenConverter jwtRefreshTokenConverter;

    /**
     * Create an Access Token from the given {@link UserDetails}
     */
    public JwtAccessToken createAccessJwtToken(UserDetails userDetails) {
        log.debug("createAccessJwtToken, userDetails: {}", userDetails);
        return jwtAccessTokenConverter.convert(userDetails);
    }

    /**
     * Create an Refresh Token from the given {@link UserDetails}
     */
    public JwtRefreshToken createRefreshToken(UserDetails userDetails) {
        log.debug("createRefreshToken, userDetails: {}", userDetails);
        return jwtRefreshTokenConverter.convert(userDetails);
    }

    @Autowired
    public void setJwtAccessTokenConverter(UserDetailsToJwtAccessTokenConverter jwtAccessTokenConverter) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Autowired
    public void setJwtRefreshTokenConverter(UserDetailsToJwtRefreshTokenConverter jwtRefreshTokenConverter) {
        this.jwtRefreshTokenConverter = jwtRefreshTokenConverter;
    }




}
