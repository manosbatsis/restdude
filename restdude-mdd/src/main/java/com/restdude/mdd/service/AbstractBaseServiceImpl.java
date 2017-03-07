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
package com.restdude.mdd.service;


import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.mdd.model.UserDetailsModel;
import com.restdude.mdd.model.UserModel;
import com.restdude.util.email.service.EmailService;
import com.restdude.websocket.Destinations;
import com.restdude.websocket.message.IActivityNotificationMessage;
import com.restdude.websocket.message.IMessageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class AbstractBaseServiceImpl implements BaseService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseServiceImpl.class);

    private ConversionService conversionService;
    protected UserRepository userRepository;
    protected EmailService emailService;
    protected RepositoryRegistryService repositoryRegistryService;
    protected FilePersistenceService filePersistenceService;
    protected SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Inject
    @Qualifier(FilePersistenceService.BEAN_ID)
    public void setFilePersistenceService(FilePersistenceService filePersistenceService) {
        this.filePersistenceService = filePersistenceService;
    }

    @Override
    public FilePersistenceService  getFilePersistenceService() {
        return this.filePersistenceService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setRepositoryRegistryService(RepositoryRegistryService repositoryRegistryService) {
        this.repositoryRegistryService = repositoryRegistryService;
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
    public UserDetailsModel getPrincipal() {
        return SecurityUtil.getPrincipal();
    }

    @Override
    public UserModel getPrincipalLocalUser() {
        UserDetailsModel principal = getPrincipal();
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

}
