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
package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restdude.domain.error.controller.ClientErrorController;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.FilePersistence;
import com.restdude.mdd.annotation.model.FilePersistencePreview;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ModelResource(pathFragment = ClientError.API_PATH,
        apiName = "Client Errors", apiDescription = "Client Error Operations", controllerSuperClass = ClientErrorController.class)
@Entity
@Table(name = "error_client")
@ApiModel(value = "ClientError", description = "Client errors are created upon client request and refer to exceptions occurred " +
        "specifically within client application code. ")
@JsonIgnoreProperties("pk")
public class ClientError extends BaseError implements PersistableError<String> {

    public static final String API_PATH = "clientErrors";

    @ApiModelProperty(value = "A client application screenshot demonstrating the issue.")
    @FilePersistence(maxWidth = 1920, maxHeight = 1080)
    @FilePersistencePreview(maxWidth = 200, maxHeight = 200)
    @Column(name = "screenshot_url", length = MAX_MESSAGE_LENGTH)
    private String screenshotUrl;

    @ApiModelProperty(value = "The textual content typically provided by the user, if any.", notes = "Values will be trimmed to max byte length, i.e. " + BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Column(length = BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Getter @Setter
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Getter @Setter
    @ApiModelProperty(value = "A textual representation of the client application state, e.g. the DOM tree.")
    private String state;

    @ApiModelProperty(value = "The URL relevant to application view state during the error e.g. window.location.href, if any", notes = "Values will be trimmed to max byte length, i.e. " + BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Column(name = "view_url", updatable = false, length = BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH)
    @Getter @Setter
    private String viewUrl;

    public ClientError() {
    }

    public ClientError(HttpServletRequest request, String message, String screenshotUrl, String description) {
        super(request, message);
        this.screenshotUrl = screenshotUrl;
        this.description = description;
    }

    @Override
    public void addRequestInfo(HttpServletRequest request) {
        super.addRequestInfo(request);
    }

    @Override
    public void preSave() {
        log.debug("preSave, before: {}", this);
        super.preSave();
        if (StringUtils.isNotEmpty(this.description) && this.description.length() > BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH) {
            this.description = StringUtils.abbreviate(this.description, BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH);
        }
        if (StringUtils.isNotEmpty(this.viewUrl) && this.viewUrl.length() > BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH) {
            this.viewUrl = StringUtils.abbreviate(this.viewUrl, BaseError.DEFAULT_MAX_DESCRIPTION_LENGTH);
        }

        log.debug("preSave, after: {}", this);
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }


    public static class Builder {
        private String message;
        private User user;
        private String screenshotUrl;
        private String description;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder screenshotUrl(String screenshotUrl) {
            this.screenshotUrl = screenshotUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ClientError build() {
            return new ClientError(this);
        }
    }

    private ClientError(Builder builder) {
        this.setMessage(builder.message);
        this.setCreatedBy(builder.user);
        this.screenshotUrl = builder.screenshotUrl;
        this.description = builder.description;
    }
}