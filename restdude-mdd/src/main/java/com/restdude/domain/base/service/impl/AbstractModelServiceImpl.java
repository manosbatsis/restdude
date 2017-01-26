/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
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
package com.restdude.domain.base.service.impl;

import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.fs.FilePersistence;
import com.restdude.domain.fs.FilePersistenceService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.mdd.specifications.SpecificationUtils;
import com.restdude.util.email.service.EmailService;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.message.IActivityNotificationMessage;
import com.restdude.websocket.message.IMessageResource;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractModelServiceImpl<T extends CalipsoPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>>
		extends CrudServiceImpl<T, ID, R>
		implements ModelService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelServiceImpl.class);
	
	protected UserRepository userRepository;
	protected EmailService emailService;
	
	protected SimpMessageSendingOperations messagingTemplate;

	FilePersistenceService filePersistenceService;

	@Inject
	@Qualifier(FilePersistenceService.BEAN_ID)
	public void setFilePersistenceService(FilePersistenceService filePersistenceService) {
		this.filePersistenceService = filePersistenceService;
	}
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public ICalipsoUserDetails getPrincipal() {
		return SecurityUtil.getPrincipal();
	}
	
	@Override
	public User getPrincipalLocalUser() {
		ICalipsoUserDetails principal = getPrincipal();
		User user = null;
		if (principal != null && principal.getPk() != null) {
			user = this.userRepository.getOne(principal.getPk());
		}

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("getPrincipalUser, user: " + user);
		}
		return user;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public void sendStompActivityMessage(IActivityNotificationMessage msg, Iterable<String> useernames) {
		LOGGER.debug("sendStompActivityMessage, useernames: {}", useernames);
		for(String useername : useernames){
			this.messagingTemplate.convertAndSendToUser(useername, Destinations.USERQUEUE_UPDATES_ACTIVITY, msg);

		}
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public void sendStompActivityMessage(IActivityNotificationMessage msg, String useername) {
		LOGGER.debug("sendStompActivityMessage, useername: {}", useername);
		this.messagingTemplate.convertAndSendToUser(useername, Destinations.USERQUEUE_UPDATES_ACTIVITY, msg);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public void sendStompStateChangeMessage(IMessageResource msg, Iterable<String> useernames) {
		LOGGER.debug("sendStompStateChangeMessage, useernames: {}", useernames);
		for(String useername : useernames){
			this.messagingTemplate.convertAndSendToUser(useername, Destinations.USERQUEUE_UPDATES_STATE, msg);

		}
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public void sendStompStateChangeMessage(IMessageResource msg, String useername) {
		LOGGER.debug("sendStompStateChangeMessage, useername: {}", useername);
		this.messagingTemplate.convertAndSendToUser(useername, Destinations.USERQUEUE_UPDATES_STATE, msg);
	}

	@Transactional(readOnly = false)
	public T updateFiles(@PathVariable ID id, MultipartHttpServletRequest request, HttpServletResponse response) {
		T entity = this.findById(id);
		LOGGER.debug("Entity before uploading files: {}", entity);
		try {
			String basePath = new StringBuffer(this.getDomainClass().getSimpleName())
					.append('/').append(id).append('/').toString();
			String propertyName;
			for (Iterator<String> iterator = request.getFileNames(); iterator.hasNext(); ) {
				// get the property name
				propertyName = iterator.next();

				// verify the property exists
                Field fileField = SpecificationUtils.getField(this.getDomainClass(), propertyName);
                if (fileField == null || !fileField.isAnnotationPresent(FilePersistence.class)) {
					throw new IllegalArgumentException("No FilePersistence annotation found for member: " + propertyName);
				}

				// store the file and update the property URL
				String url = this.filePersistenceService.saveFile(fileField, request.getFile(propertyName), basePath + propertyName);
				BeanUtils.setProperty(entity, propertyName, url);

			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to update files", e);
		}
		// return the updated entity
		entity = this.update(entity);

		LOGGER.debug("Entity after uploading files: {}", entity);
		return entity;
	}

	/**
	 * Utility method to be called by implementations
	 *
	 * @param id
	 * @param filenames
	 */
	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public void deleteFiles(ID id, String... filenames) {
		String basePath = new StringBuffer(this.getDomainClass().getSimpleName())
				.append('/').append(id).append('/').toString();
		List<String> keys = new LinkedList<String>();

		for (String propertyName : filenames) {
			// verify the property exists
            Field fileField = SpecificationUtils.getField(this.getDomainClass(), propertyName);
            if (fileField == null || !fileField.isAnnotationPresent(FilePersistence.class)) {
				throw new IllegalArgumentException("No FilePersistence annotation found for member: " + propertyName);
			}

			// store the file key
			keys.add(basePath + propertyName);
		}

		// delete files
		this.filePersistenceService.deleteFiles(keys.toArray(new String[keys.size()]));
	}

}