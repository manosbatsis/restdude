/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
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
package com.restdude.auth.userAccount.model;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "UsernameChangeRequest", description = "A request to change one's username, ")
public class UsernameChangeRequest implements Serializable {

    private static final long serialVersionUID = 5206010308112791343L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameChangeRequest.class);

    @NotNull
    private String username;
    @NotNull
    private String password;

    /**
     * Default constructor
     */
    public UsernameChangeRequest() {

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("username", username).toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {
        private String username;
        private String password;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UsernameChangeRequest build() {
            return new UsernameChangeRequest(this);
        }
    }

    private UsernameChangeRequest(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
    }
}
