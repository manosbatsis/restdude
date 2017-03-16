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
package com.restdude.auth.controller;

import com.restdude.auth.jwt.aervice.JwtTokenService;
import com.restdude.auth.jwt.binding.EncryptedJwtAccessTokenToUserDetailsConverter;
import com.restdude.auth.jwt.model.JwtToken;
import com.restdude.auth.model.SimpleLoginRequest;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.mdd.model.UserDetails;
import com.restdude.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RestController
public class JwtController {

    private UserDetailsConfig userDetailsConfig = new SimpleUserDetailsConfig();
    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private EncryptedJwtAccessTokenToUserDetailsConverter jwtRawAccessTokenToUserDetailsConverter;

    @Autowired
    public void setJwtRawAccessTokenToUserDetailsConverter(EncryptedJwtAccessTokenToUserDetailsConverter jwtRawAccessTokenToUserDetailsConverter) {
        this.jwtRawAccessTokenToUserDetailsConverter = jwtRawAccessTokenToUserDetailsConverter;
    }

    @RequestMapping(value="/api/auth/jwt/access", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetails createAccessToken(@RequestBody SimpleLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // verify request
        // TODO: wire BeanValidator for nice error response
        if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("A SimpleLoginRequest JSON request body with username and password members is required");
        }

        // authenticate
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        log.debug("createAccessToken, authentication: {}", authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.debug("createAccessToken, userDetails: {}", userDetails);

        // create JWT tokens
        JwtToken accessToken = jwtTokenService.createAccessJwtToken(userDetails);
        JwtToken refreshToken = jwtTokenService.createRefreshToken(userDetails);

        // add JWT token cookies
        CookieUtil.addCookie(request, response, "access_token", accessToken.getToken(), true, this.userDetailsConfig);
        CookieUtil.addCookie(request, response, "refresh_token", refreshToken.getToken(), true, this.userDetailsConfig);

        // return userDetails info
        return userDetails;

    }

    @RequestMapping(value="/api/auth/jwt/refresh", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public JwtToken createRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        UserDetails userDetails = jwtRawAccessTokenToUserDetailsConverter.convert(request);

        return this.jwtTokenService.createRefreshToken(userDetails);
    }

    @Autowired(required = false)
    public void setUserDetailsConfig(UserDetailsConfig userDetailsConfig) {
        this.userDetailsConfig = userDetailsConfig;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }



}
