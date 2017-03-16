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
package com.restdude.auth.config;

import com.restdude.auth.jwt.model.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT configuration, prefix: restdude.jwt, properties: tokenIssuer, accessTokenMinutes, refreshTokenMinutes, tokenSigningKey
 */
@Slf4j
@Component
public class JwtSettings implements InitializingBean {

    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_FIRST_NAME = "given_name";
    public static final String CLAIM_LAST_NAME = "family_name";
    public static final String CLAIM_USERNAME = "preferred_username";
    public static final String CLAIM_LOCALE = "locale";

    /**
     * {@link JwtToken} will expire after this time.
     */
    @Value("${restdude.jwt.accessTokenMinutes}")
    private Integer accessTokenMinutes;

    /**
     * Token issuer.
     */
    @Value("${restdude.jwt.tokenIssuer}")
    private String tokenIssuer;
    
    /**
     * Key is used to sign {@link JwtToken}.
     */
    @Value("${restdude.jwt.tokenSigningKey}")
    private String tokenSigningKey;
    
    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    @Value("${restdude.jwt.refreshTokenMinutes}")
    private Integer refreshTokenMinutes;


    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Initialized with tokenIssuer: {},accessTokenMinutes: {}, refreshTokenMinutes: {}", tokenIssuer, accessTokenMinutes, refreshTokenMinutes);
    }
    
    public Integer getRefreshTokenMinutes() {
        return refreshTokenMinutes;
    }

    public void setRefreshTokenMinutes(Integer refreshTokenMinutes) {
        this.refreshTokenMinutes = refreshTokenMinutes;
    }

    public Integer getAccessTokenMinutes() {
        return accessTokenMinutes;
    }
    
    public void setAccessTokenMinutes(Integer accessTokenMinutes) {
        this.accessTokenMinutes = accessTokenMinutes;
    }
    
    public String getTokenIssuer() {
        return tokenIssuer;
    }
    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }
    
    public String getTokenSigningKey() {
        return tokenSigningKey;
    }
    
    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

}
