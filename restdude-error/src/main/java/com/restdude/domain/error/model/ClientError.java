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

import com.restdude.domain.CommentableModel;
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


import static com.restdude.domain.CommentableModel.*;

@Entity
@Table(name = "error_client")
@ModelResource(pathFragment = ClientError.API_PATH,
        apiName = "Client Errors", apiDescription = "Client Error Operations", controllerSuperClass = ClientErrorController.class)
@ApiModel(value = "ClientError", description = "Client errors are created upon client request and refer to exceptions occurred " +
        "specifically within client application code. ")

@Slf4j
public class ClientError extends BaseError implements PersistableError {

    public static final String API_PATH = "clientErrors";

    @ApiModelProperty(value = "A client application screenshot demonstrating the issue.")
    @FilePersistence(maxWidth = 1920, maxHeight = 1080)
    @FilePersistencePreview(maxWidth = 200, maxHeight = 200)
    @Column(name = "screenshot_url", length = CommentableModel.MAX_TITLE_LENGTH)
    private String screenshotUrl;


    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Getter @Setter
    @ApiModelProperty(value = "A textual representation of the client application state, e.g. the DOM tree.")
    private String state;

    @ApiModelProperty(value = "The URL relevant to application view state during the error e.g. window.location.href, if any", notes = "Values will be trimmed to max byte length, i.e. " + MAX_DETAIL_LENGTH)
    @Column(name = "view_url", updatable = false, length = MAX_DETAIL_LENGTH)
    @Getter @Setter
    private String viewUrl;

    public ClientError() {
        super();
    }

    public ClientError(HttpServletRequest request, String title, String screenshotUrl, String detail) {
        super(request, title);
        this.screenshotUrl = screenshotUrl;
        this.setDetail(detail);
    }

    @Override
    public void addRequestInfo(HttpServletRequest request) {
        super.addRequestInfo(request);
    }

    @Override
    public void preSave() {
        log.debug("preSave, before: {}", this);
        super.preSave();
        if (StringUtils.isNotEmpty(this.viewUrl) && this.viewUrl.length() > MAX_DETAIL_LENGTH) {
            this.viewUrl = StringUtils.abbreviate(this.viewUrl, MAX_DETAIL_LENGTH);
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
        private String title;
        private User user;
        private String screenshotUrl;
        private String detail;

        public Builder message(String message) {
            this.title = message;
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
            this.detail = description;
            return this;
        }

        public ClientError build() {
            return new ClientError(this);
        }
    }

    private ClientError(Builder builder) {
        this.setTitle(builder.title);
        this.setCreatedBy(builder.user);
        this.screenshotUrl = builder.screenshotUrl;
        this.setDetail(builder.detail);
    }
}