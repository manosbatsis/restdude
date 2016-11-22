package com.restdude.domain.error.model;

import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.fs.FilePersistence;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@ModelResource(path = "clientErrors", controllerSuperClass = AbstractModelController.class,
        apiName = "Client Errors", apiDescription = "Client Error Operations")
@Entity
@Table(name = "client_error")
@ApiModel(value = "ClientError", description = "Client errors are created upon client request and refer to exceptions occurred specifically within client application code. ")
public class ClientError extends AbstractError {


    @FilePersistence(maxWidth = 1920, maxHeight = 1080)
    @Column(name = "screenshot_url")
    private String screenshotUrl;

    public ClientError() {
    }
}