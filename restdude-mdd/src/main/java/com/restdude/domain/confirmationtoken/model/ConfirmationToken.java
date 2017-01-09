/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.confirmationtoken.model;

import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "confirmation_token")
@ShallowReference
public class ConfirmationToken extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final StringKeyGenerator generator = KeyGenerators.string();

    @NotNull
    @Column(name = "token_value", nullable = false, updatable = false)
    private String tokenValue;

    @NotNull
    @Column(name = "target_id", nullable = false, updatable = false)
    private String targetId;

    public ConfirmationToken(String verificationToken) {
        this.tokenValue = verificationToken;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public static class Builder {
        private String tokenValue;
        private String targetId;

        public Builder tokenValue(String tokenValue) {
            this.tokenValue = tokenValue;
            return this;
        }

        public Builder targetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        public ConfirmationToken build() {
            return new ConfirmationToken(this);
        }
    }

    private ConfirmationToken(Builder builder) {
        this.tokenValue = builder.tokenValue != null ? builder.tokenValue : generator.generateKey();
        this.targetId = builder.targetId;
    }
}
