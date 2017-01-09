/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-error, https://manosbatsis.github.io/restdude/restdude-error
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractAssignedIdPersistable;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.HashUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ModelResource(path = ErrorLog.API_PATH, controllerSuperClass = AbstractReadOnlyModelController.class, apiName = "ErrorLogs",
        apiDescription = "Stacktrace or other error log details.")

@Entity
@Table(name = "error_log")
@ApiModel(value = "ErrorLog", description = "Used to persist error stacktraces using the corresponding hash as the ID. "
        + "The generated hash ignores line numbers (in case of SystemError) and is thus tolerant of small code changes, like adding a comment line.")
public class ErrorLog extends AbstractAssignedIdPersistable<String> {

    public static final String API_PATH = "errorLogs";

    @Formula(" (id) ")
    private String name;

    @ApiModelProperty(value = "The root cause message.")
    @Column(name = "root_cause_msg", length = PersistableError.MAX_MESSAGE_LENGTH)
    private String rootCauseMessage;

    @ApiModelProperty(value = "First occurrence date", required = true)
    @Column(name = "first_occurred")
    private LocalDateTime firstOccurred;

    @ApiModelProperty(value = "Lats occurrence date.", required = true)
    @Column(name = "last_occurred")
    private LocalDateTime lastOccurred;

    @Formula(" (select count(*) from error_abstract errS where errS.error_log_id = id) ")
    @ApiModelProperty(value = "The number of errors corresponding to this stacktrace.")
    private Integer errorCount = 0;

    @NotNull
    @ApiModelProperty(value = "The actual stacktrace.", required = true)
    @Column(length = PersistableError.MAX_MSTACKTRACE_LENGTH, nullable = false)
    private String stacktrace = "None";

    public ErrorLog() {
        super();
    }


    public ErrorLog(Throwable ex) {
        this(HashUtils.buildHash(ex), ExceptionUtils.getRootCauseMessage(ex), ExceptionUtils.getStackTrace(ex));
    }

    public ErrorLog(String rootCauseMessage, String stacktrace) {
        this(HashUtils.buildHash(stacktrace), rootCauseMessage, stacktrace);
    }

    protected ErrorLog(String hash, String rootCauseMessage, String stacktrace) {
        super(hash);
        this.rootCauseMessage = rootCauseMessage;
        this.stacktrace = stacktrace;
    }


    @Override
    public void preSave() {

        // set ID if needed
        if (this.getId() == null && StringUtils.isNotEmpty(this.stacktrace)) {
            this.setId(HashUtils.buildHash(this.stacktrace));
        }

        // trim  message if needed
        if (StringUtils.isNotEmpty(this.rootCauseMessage)
                && this.rootCauseMessage.length() > PersistableError.MAX_MESSAGE_LENGTH) {
            this.rootCauseMessage = StringUtils.abbreviate(this.rootCauseMessage, PersistableError.MAX_MESSAGE_LENGTH);
        }

        // trim  stacktrace if needed
        if (StringUtils.isNotEmpty(this.stacktrace) && this.stacktrace.length() > PersistableError.MAX_MSTACKTRACE_LENGTH) {
            this.stacktrace = StringUtils.abbreviate(this.stacktrace, PersistableError.MAX_MSTACKTRACE_LENGTH);
        }
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("rootCauseMessage", this.getRootCauseMessage())
                .append("stacktrace", StringUtils.abbreviate(this.getStacktrace(), 120))
                .toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootCauseMessage() {
        return rootCauseMessage;
    }

    public void setRootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage;
    }

    public LocalDateTime getLastOccurred() {
        return lastOccurred;
    }

    public void setLastOccurred(LocalDateTime lastOccurred) {
        this.lastOccurred = lastOccurred;
    }

    public LocalDateTime getFirstOccurred() {
        return firstOccurred;
    }

    public void setFirstOccurred(LocalDateTime firstOccurred) {
        this.firstOccurred = firstOccurred;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }


}
