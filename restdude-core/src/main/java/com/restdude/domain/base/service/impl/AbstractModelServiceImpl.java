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
package com.restdude.domain.base.service.impl;

import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.util.email.service.EmailService;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.message.IActivityNotificationMessage;
import com.restdude.websocket.message.IMessageResource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.Serializable;

public abstract class AbstractModelServiceImpl<T extends CalipsoPersistable<ID>, ID extends Serializable, R extends ModelRepository<T, ID>>
		extends AbstractAclAwareServiceImpl<T, ID, R>
        implements ModelService<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelServiceImpl.class);
	
	protected UserRepository userRepository;
	protected EmailService emailService;
	
	protected SimpMessageSendingOperations messagingTemplate;

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
		if (principal != null) {
			String username = principal.getUsername();
			if(StringUtils.isBlank(username)){
				username = principal.getEmail();
			}
			if(StringUtils.isNotBlank(username) && !"anonymous".equals(username)){
				user = this.userRepository.findByUsernameOrEmail(username);
			}
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

/*

	// TODO: refactor to single query then parse results to find errors
	protected List<String> validateUniqueConstraintss(T resource) {
		List<String> errors = new LinkedList<String>();
		Field[] fields = FieldUtils.getFieldsWithAnnotation(this.getDomainClass(), Column.class);
		if(fields.length > 0){
			for(int i = 0; i < fields.length; i++){
				Field field = fields[i];
				Column column = field.getAnnotation(Column.class);

				try {
					// if unique field
					if(column.unique() && !field.getName().equals("id")){
						Object value = PropertyUtils.getProperty(resource, field.getName());
						// match the given value if any
						if(value != null){
							// unwrap ID if entity type
							if(CalipsoPersistable.class.isAssignableFrom(value.getClass())){
								value = ((CalipsoPersistable) value).getId();
							}
							// create criteria
							HashMap<String, String[]> parameters = new HashMap<String, String[]>();
							String[] match = {value.toString()};
							parameters.put(field.getName(), match);
							Page<T> page = this.findAll(new ParameterMapBackedPageRequest(parameters, 0 , 1, Direction.ASC, "id"));
							// if a match exists and is not given resource
							if(page.hasContent() && !page.getContent().get(0).getId().equals(resource.getId())){
								errors.add("Value already exists for field " + field.getName());
							}
						}
					}
				} catch (Exception e) {
					LOGGER.warn("Failed validating unique constrains for property: " + field.getName(), e);
				}

			}


		}
		return errors;
	}
 */
}