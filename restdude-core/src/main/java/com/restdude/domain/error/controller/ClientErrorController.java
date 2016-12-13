package com.restdude.domain.error.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.restdude.domain.base.controller.AbstractModelController;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import com.restdude.domain.error.model.ClientError;
import com.restdude.domain.error.service.ClientErrorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;


@RestController
@Api(tags = "Client Error", description = "Client Error Operations")
@RequestMapping(value = "/api/rest/" + ClientError.API_PATH, produces = {"application/json", "application/xml"})
public class ClientErrorController extends AbstractModelController<ClientError, String, ClientErrorService> {

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

    @ApiOperation(value = "Update files",
            notes = "The files are saved using the parameter names of the multipart files contained in this request. "
                    + "These are the field names of the form (like with normal parameters), not the original file names.")
    @RequestMapping(value = "{id}/files",
            method = {RequestMethod.POST},
            headers = ("content-type=multipart/*"),
            produces = {"application/json", "application/xml"})
    public ClientError updateFiles(@PathVariable String id,
                                   MultipartHttpServletRequest request, HttpServletResponse response) {
        return this.service.updateFiles(id, request, response);
    }

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    public void deleteFiles(String id, String... filenames) {
        this.service.deleteFiles(id, filenames);
        ;
    }

}
