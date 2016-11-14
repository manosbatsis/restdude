/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.controller;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.cms.model.BinaryFile;
import com.restdude.domain.cms.service.BinaryFileService;
import com.restdude.mdd.specifications.GenericSpecifications;
import com.restdude.util.ConfigurationFactory;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public abstract class AbstractModelWithAttachmentsController<T extends CalipsoPersistable<ID>, ID extends Serializable, S extends ModelService<T, ID>>
		extends AbstractModelController<T, ID, S> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelWithAttachmentsController.class);

	private BinaryFileService binaryFileService;

	@Inject
	@Qualifier("binaryFileService")
	public void setService(BinaryFileService binaryFileService) {
		this.binaryFileService = binaryFileService;
	}

	@RequestMapping(value = "{subjectId}/uploads/{propertyName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get file uploads by property")
	public @ResponseBody List<BinaryFile> getUploadsByProperty(@PathVariable ID subjectId,
			@PathVariable String propertyName) {
		LOGGER.info("uploadGet called");
		List<BinaryFile> uploads = null;
		// attach file
		Field fileField = GenericSpecifications.getField(this.service.getDomainClass(), propertyName);
		Class clazz = fileField.getType();
		if (BinaryFile.class.isAssignableFrom(clazz)) {
			uploads = this.service.getUploadsForProperty(subjectId, propertyName);
		}
		return uploads;
	}

	@RequestMapping(value = "{subjectId}/uploads/{propertyName}/thumbs/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get file thumb/preview image of a file upload	")
	public void thumbnail(HttpServletResponse response, @PathVariable String subjectId,
			@PathVariable String propertyName, @PathVariable String id) {
		Configuration config = ConfigurationFactory.getConfiguration();
		String fileUploadDirectory = config.getString(ConfigurationFactory.FILES_DIR);
		BinaryFile file = binaryFileService.findById(id);
		File fileFile = new File(fileUploadDirectory + file.getParentPath() + "/" + file.getThumbnailFilename());
		response.setContentType(file.getContentType());
		response.setContentLength(file.getThumbnailSize().intValue());
		try {
			InputStream is = new FileInputStream(fileFile);
			IOUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("Could not show thumbnail " + id, e);
		}
	}

	@ApiOperation(value = "Get an uploaded file")
	@RequestMapping(value = "{subjectId}/uploads/{propertyName}/files/{id}", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response, @PathVariable String subjectId, @PathVariable String propertyName,
			@PathVariable String id) {
		Configuration config = ConfigurationFactory.getConfiguration();
		String fileUploadDirectory = config.getString(ConfigurationFactory.FILES_DIR);
		BinaryFile file = binaryFileService.findById(id);
		File fileFile = new File(fileUploadDirectory + file.getParentPath() + "/" + file.getNewFilename());
		response.setContentType(file.getContentType());
		response.setContentLength(file.getSize().intValue());
		try {
			InputStream is = new FileInputStream(fileFile);
			IOUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("Could not show picture " + id, e);
		}
	}

	@ApiOperation(value = "Delete an uploaded file")
	@RequestMapping(value = "{subjectId}/uploads/{propertyName}/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List deleteById(@PathVariable String subjectId, @PathVariable String propertyName,
			@PathVariable String id) {
		Configuration config = ConfigurationFactory.getConfiguration();
		String fileUploadDirectory = config.getString(ConfigurationFactory.FILES_DIR);
		BinaryFile file = binaryFileService.findById(id);
		File fileFile = new File(fileUploadDirectory + "/" + file.getNewFilename());
		fileFile.delete();
		File thumbnailFile = new File(fileUploadDirectory + "/" + file.getThumbnailFilename());
		thumbnailFile.delete();
		binaryFileService.delete(file);
		List<Map<String, Object>> results = new ArrayList();
		Map<String, Object> success = new HashMap();
		success.put("success", true);
		results.add(success);
		return results;
	}

	@ApiOperation(value = "Add a file uploads to property")
	@RequestMapping(value = "{subjectId}/uploads/{propertyName}", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = {})
	public @ResponseBody BinaryFile addUploadsToProperty(@PathVariable ID subjectId, @PathVariable String propertyName,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("uploadPost called");

		Configuration config = ConfigurationFactory.getConfiguration();
		String fileUploadDirectory = config.getString(ConfigurationFactory.FILES_DIR);
		String baseUrl = config.getString("calipso.baseurl");

		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf;
		BinaryFile bf = new BinaryFile();
		try {
			if (itr.hasNext()) {

				mpf = request.getFile(itr.next());
				LOGGER.info("Uploading {}", mpf.getOriginalFilename());

				bf.setName(mpf.getOriginalFilename());
				bf.setFileNameExtention(
						mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf(".") + 1));

				bf.setContentType(mpf.getContentType());
				bf.setSize(mpf.getSize());

				// request targets specific path?
				StringBuffer uploadsPath = new StringBuffer('/')
						.append(this.service.getDomainClass().getDeclaredField("PATH_FRAGMENT").get(String.class))
						.append('/').append(subjectId).append("/uploads/").append(propertyName);
				bf.setParentPath(uploadsPath.toString());
				LOGGER.info("Saving image entity with path: " + bf.getParentPath());
				bf = binaryFileService.create(bf);

				LOGGER.info("file name: {}", bf.getNewFilename());
				bf = binaryFileService.findById(bf.getId());
				LOGGER.info("file name: {}", bf.getNewFilename());

				File storageDirectory = new File(fileUploadDirectory + bf.getParentPath());

				if (!storageDirectory.exists()) {
					storageDirectory.mkdirs();
				}

				LOGGER.info("storageDirectory: {}", storageDirectory.getAbsolutePath());
				LOGGER.info("file name: {}", bf.getNewFilename());

				File newFile = new File(storageDirectory, bf.getNewFilename());
				newFile.createNewFile();
				LOGGER.info("newFile path: {}", newFile.getAbsolutePath());
				Files.copy(mpf.getInputStream(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

				BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);
				File thumbnailFile = new File(storageDirectory, bf.getThumbnailFilename());
				ImageIO.write(thumbnail, "png", thumbnailFile);
				bf.setThumbnailSize(thumbnailFile.length());

				bf = binaryFileService.update(bf);

				// attach file
				// TODO: add/update to collection
				Field fileField = GenericSpecifications.getField(this.service.getDomainClass(), propertyName);
				Class clazz = fileField.getType();
				if (BinaryFile.class.isAssignableFrom(clazz)) {
					T target = this.service.findById(subjectId);
					BeanUtils.setProperty(target, propertyName, bf);
					this.service.update(target);
				}

				bf.setUrl(baseUrl + "/api/rest/" + bf.getParentPath() + "/files/" + bf.getId());
				bf.setThumbnailUrl(baseUrl + "/api/rest/" + bf.getParentPath() + "/thumbs/" + bf.getId());
				bf.setDeleteUrl(baseUrl + "/api/rest/" + bf.getParentPath() + "/" + bf.getId());
				bf.setDeleteType("DELETE");
				bf.addInitialPreview("<img src=\"" + bf.getThumbnailUrl() + "\" class=\"file-preview-image\" />");

			}

		} catch (Exception e) {
			LOGGER.error("Could not upload file(s) ", e);
		}

		return bf;
	}

}
