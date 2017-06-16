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
package com.restdude.domain.cases.service.impl;

import java.util.Set;

import javax.inject.Named;

import com.restdude.domain.UserDetails;
import com.restdude.domain.cases.model.ActivityLog;
import com.restdude.domain.cases.model.Space;
import com.restdude.domain.cases.model.dto.BaseContextInfo;
import com.restdude.domain.cases.model.enums.SpaceActivity;
import com.restdude.domain.cases.repository.ActivityLogRepository;
import com.restdude.domain.cases.service.BusinessContextMembersActivityMessanger;
import com.restdude.domain.cases.service.ActivityLogService;
import com.restdude.domain.friends.repository.FriendshipRepository;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.util.exception.http.UnauthorizedException;
import com.restdude.websocket.message.StompActivityNotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

@Named(ActivityLogService.BEAN_NAME)
public class ActivityLogServiceImpl
		extends AbstractPersistableModelServiceImpl<ActivityLog, String, ActivityLogRepository>
		implements ActivityLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogServiceImpl.class);



}