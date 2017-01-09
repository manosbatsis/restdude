/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-websocket, https://manosbatsis.github.io/restdude/restdude-websocket
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
package com.restdude.websocket.model;

import com.restdude.domain.base.model.AbstractAssignedIdPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Persistent class corresponding to a websocket STOMP session. The entities are only 
 */
@Entity
@Table(name = "stomp_session")
@ModelResource(path = StompSession.API_PATH, apiName = "STOMP Sessions", apiDescription = "STOMP Session Operations")
@ApiModel(value = "STOMP Session", description = "A model representing a websocket STOMP session")
public class StompSession extends AbstractAssignedIdPersistable<String> {

    private static final long serialVersionUID = 1L;

    public static final String API_PATH = "stompSessions";

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    public StompSession() {
        super();
    }

    public StompSession(String id) {
        this();
        this.setId(id);
    }


    @Override
    public String toString() {
        return "StompSession [id=" + this.getId() + ", user=" + user + "]";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class Builder {
        private String id;
        private User user;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public StompSession build() {
            return new StompSession(this);
        }
    }

    private StompSession(Builder builder) {
        this.setId(builder.id);
        this.user = builder.user;
    }
}
