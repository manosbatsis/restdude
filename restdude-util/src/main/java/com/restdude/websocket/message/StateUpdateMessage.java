/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-util, https://manosbatsis.github.io/restdude/restdude-util
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
package com.restdude.websocket.message;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * A message about modifications of a {@link IMessageResource}
 *
 * @param <S> the message subject type
 */
@JsonPropertyOrder({"@class", "id", "name"})
public class StateUpdateMessage<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ID id;

    @JsonProperty("@class")
    private String resourceClass;

    private Map<String, Serializable> modifications;

    public StateUpdateMessage() {

    }

    @Override
    public String toString() {
        return "StateUpdateMessage [id=" + id + ", resourceClass=" + resourceClass + ", modifications=" + modifications + "]";
    }

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String getResourceClass() {
        return this.resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    @JsonAnyGetter
    public Map<String, Serializable> getModifications() {
        return this.modifications;
    }

    @JsonAnySetter
    public void addModification(String key, Serializable value) {
        if (this.modifications == null) {
            this.modifications = new HashMap<String, Serializable>();
        }
        this.modifications.put(key, value);
    }

    public void setModifications(Map<String, Serializable> modifications) {
        this.modifications = modifications;
    }

}
