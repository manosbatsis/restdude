package com.restdude.domain.error.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.controller.IFilesModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.error.model.ClientError;
import com.restdude.domain.error.service.ClientErrorService;
import com.restdude.domain.fs.FilePersistenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;


@RestController
@Api(tags = "Client Error", description = "Client Error Operations")
@RequestMapping(value = "/api/rest/" + ClientError.API_PATH, produces = {"application/json", "application/xml"})
public class ClientErrorController
        extends AbstractModelController<ClientError, String, ClientErrorService> implements IFilesModelController<ClientError, String, ClientErrorService> {

    FilePersistenceService filePersistenceService;

    @Inject
    @Qualifier(FilePersistenceService.BEAN_ID)
    public void setFilePersistenceService(FilePersistenceService filePersistenceService) {
        this.filePersistenceService = filePersistenceService;
    }

    @Override
    public FilePersistenceService getFilePersistenceService() {
        return this.filePersistenceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new resource")
    @JsonView(AbstractSystemUuidPersistable.ItemView.class)
    public ClientError create(@RequestBody ClientError resource) {
        resource.addRequestInfo(this.request);
        return this.service.create(resource);
    }

}
