package com.restdude.domain.error;

import com.restdude.domain.base.controller.AbstractNoDeleteModelController;
import com.restdude.domain.base.controller.IFilesModelController;
import com.restdude.domain.error.model.ClientError;
import com.restdude.domain.error.service.ClientErrorService;
import com.restdude.domain.fs.FilePersistenceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@Api(tags = "Client Errors", description = "Client Error Operations")
@RequestMapping(value = "/api/rest/" + ClientError.API_PATH, produces = {"application/json", "application/xml"})
public class ClientErrorController extends AbstractNoDeleteModelController<ClientError, String, ClientErrorService>
        implements IFilesModelController<ClientError, String, ClientErrorService> {

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

}
