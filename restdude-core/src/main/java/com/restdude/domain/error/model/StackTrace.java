package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractReadOnlyModelController;
import com.restdude.domain.base.model.AbstractAssignedidPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 */
@ModelResource(path = "stackTraces", controllerSuperClass = AbstractReadOnlyModelController.class,
        apiName = "Stack Traces", apiDescription = "SystemError Stacktraces")

@Entity
@Table(name = "stack_trace")
@ApiModel(value = "StackTrace", description = "Used to persist SystemError stacktraces using the corresponding hash as the ID. The generated hash ignores line numbers and is thus tolerant of small code changes, like adding a comment line.")
public class StackTrace extends AbstractAssignedidPersistable<String> {

    public static final int MAX_MESSAGE_LENGTH = 500;
    public static final int MAX_STACKTRACE_LENGTH = 20000;

    @ApiModelProperty(value = "The root cause message.")
    @Column(name = "root_cause_msg", length = MAX_MESSAGE_LENGTH)
    String rootCauseMessage;

    @Column(name = "last_occurred")
    private Date lastOccurred;

    @Formula(" (select count(*) from system_error syserr where syserr.stacktrace_id = id) ")
    @ApiModelProperty(value = "The number of errors corresponding to this stacktrace.")
    private Integer errorCount = 0;

    @ApiModelProperty(value = "The actual stacktrace.")
    @Column(length = MAX_STACKTRACE_LENGTH)
    @NotNull
    private String stacktrace;

    public StackTrace() {
    }

    public StackTrace(String hash, String rootCauseMessage, Date lastOccured, String stacktrace) {
        this.setId(hash);
        this.rootCauseMessage = rootCauseMessage;
        this.lastOccurred = lastOccured;
        this.stacktrace = stacktrace;
    }

    @Override
    public void preSave() {
        if (StringUtils.isNotEmpty(this.rootCauseMessage) && this.rootCauseMessage.length() > MAX_MESSAGE_LENGTH) {
            this.rootCauseMessage = StringUtils.abbreviate(this.rootCauseMessage, MAX_MESSAGE_LENGTH);
        }
        if (StringUtils.isNotEmpty(this.stacktrace) && this.stacktrace.length() > MAX_STACKTRACE_LENGTH) {
            this.stacktrace = StringUtils.abbreviate(this.stacktrace, MAX_STACKTRACE_LENGTH);
        }
    }

    public String getRootCauseMessage() {
        return rootCauseMessage;
    }

    public void setRootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage;
    }

    public Date getLastOccurred() {
        return lastOccurred;
    }

    public void setLastOccurred(Date lastOccurred) {
        this.lastOccurred = lastOccurred;
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
