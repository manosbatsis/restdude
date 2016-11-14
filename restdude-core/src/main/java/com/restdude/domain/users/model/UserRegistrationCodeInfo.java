/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonPropertyOrder({"id", "available", "username", "batchId", "batchName"})
public class UserRegistrationCodeInfo extends AbstractSystemUuidPersistable implements CalipsoPersistable<String> {

    private static final long serialVersionUID = 1L;

    private String id;

    private Boolean available;

    private String username;

    private String batchId;

    private String batchName;

    public UserRegistrationCodeInfo() {
    }

    public UserRegistrationCodeInfo(UserRegistrationCode code) {
        this(code.getId(), code.getAvailable(), code.getCredentials(), code.getBatch());
    }

    public UserRegistrationCodeInfo(String id, Boolean available, String username, String batchId, String batchName) {
        this.id = id;
        this.available = available;
        this.username = username;
        this.batchId = batchId;
        this.batchName = batchName;
    }

    public UserRegistrationCodeInfo(String id, Boolean available, UserCredentials credentials, UserRegistrationCodeBatch batch) {
        this.id = id;
        this.available = available;
        if (credentials != null) {
            this.username = credentials.getUsername();
        }
        if (batch != null) {
            this.batchId = batch.getId();
            this.batchName = batch.getName();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("available", this.available)
                .append("username", this.username)
                .append("batchId", this.batchId)
                .append("batchName", this.batchName)
                .toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}