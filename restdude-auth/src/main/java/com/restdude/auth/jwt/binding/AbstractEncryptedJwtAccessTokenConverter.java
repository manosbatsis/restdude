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
import com.restdude.auth.model.BasicUserDetailsImpl;
import com.restdude.domain.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Base class for converting from raw encrypted JWT access token. Autowires {@link JwtSettings} config bean
 *
 * @see <a href="https://www.iana.org/assignments/jwt/jwt.xhtml">https://www.iana.org/assignments/jwt/jwt.xhtml</a>
 */
@Slf4j
public abstract class AbstractEncryptedJwtAccessTokenConverter extends AbstractJwtConverter {

    /**
     * Parse the given raw JWT access token to a {@link UserDetails} instance
     * @param rawAccessJwtToken
     * @return
     */
    protected Optional<UserDetails> totUserDetails(String rawAccessJwtToken) {
        UserDetails userDetails = null;
        Jws<Claims> claims = StringUtils.isNotBlank(rawAccessJwtToken)
                ? Jwts.parser()
                    .setSigningKey(getSettings().getTokenSigningKey())
                    .parseClaimsJws(rawAccessJwtToken)
                : null;

        // parsing expired should throw ExpiredJwtException but lets make sure
        if(claims != null && claims.getBody().getExpiration().after(new Date())){

            log.debug("Claims: {}", claims);
            String subject = claims.getBody().getSubject();
            if(StringUtils.isNotBlank(subject) && subject.startsWith(JWT_SUBJECT_PREFIX)){

                Object oScopes = claims.getBody().get("scopes");
                List scopes = List.class.isAssignableFrom(oScopes.getClass()) ? (List) oScopes : null;
                // only convert if user has roles
                if(CollectionUtils.isNotEmpty(scopes)){

                    // parse authorities
                    List<SimpleGrantedAuthority> authorities = new LinkedList<>();
                    for(Object role : scopes){
                        if(role != null && role.toString().startsWith("ROLE_")){
                            authorities.add(new SimpleGrantedAuthority(role.toString()));
                        }
                    }

                    // ensure non-empty authorities
                    if(CollectionUtils.isNotEmpty(authorities)){
                        userDetails = new BasicUserDetailsImpl();
                        userDetails.setPk(subject.substring(JWT_SUBJECT_PREFIX.length()));
                        userDetails.setUsername(claims.getBody().get(JwtSettings.CLAIM_USERNAME, String.class));
                        userDetails.setName(claims.getBody().get(JwtSettings.CLAIM_NAME, String.class));
                        userDetails.setFirstName(claims.getBody().get(JwtSettings.CLAIM_FIRST_NAME, String.class));
                        userDetails.setLastName(claims.getBody().get(JwtSettings.CLAIM_LAST_NAME, String.class));
                        userDetails.setLocale(claims.getBody().get(JwtSettings.CLAIM_LOCALE, String.class));
                        userDetails.setAuthorities(authorities);

                    }
                }
            }
            else{
                throw new ExpiredJwtException(claims.getHeader(), claims.getBody(), "Cannot convert expired token");
            }
        }
        log.debug("convert, userDetails: {}, token: {}", userDetails, rawAccessJwtToken);

        return Optional.ofNullable(userDetails);
    }
}
