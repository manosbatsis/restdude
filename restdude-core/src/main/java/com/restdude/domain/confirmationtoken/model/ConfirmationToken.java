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
