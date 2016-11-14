/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.service.impl;

import com.restdude.domain.base.service.AbstractModelServiceImpl;
import com.restdude.domain.users.model.UserRegistrationCode;
import com.restdude.domain.users.model.UserRegistrationCodeBatch;
import com.restdude.domain.users.model.UserRegistrationCodeInfo;
import com.restdude.domain.users.repository.UserRegistrationCodeBatchRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.service.UserRegistrationCodeBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userRegistrationCodeBatch")
@Transactional(readOnly = true)
public class UserRegistrationCodeBatchServiceImpl extends AbstractModelServiceImpl<UserRegistrationCodeBatch, String, UserRegistrationCodeBatchRepository>
        implements UserRegistrationCodeBatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationCodeBatchServiceImpl.class);

    UserRegistrationCodeRepository userRegistrationCodeRepository;

    @Autowired
    public void setCredentialsRepository(UserRegistrationCodeRepository userRegistrationCodeRepository) {
        this.userRegistrationCodeRepository = userRegistrationCodeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(UserRegistrationCodeBatch.PRE_AUTHORIZE_CREATE)
    public UserRegistrationCodeBatch create(UserRegistrationCodeBatch resource) {
        resource = super.create(resource);

        // create codes
        List<UserRegistrationCode> codes = new ArrayList<UserRegistrationCode>(resource.getBatchSize());
        for (int i = 0; i < resource.getBatchSize(); i++) {
            codes.add(new UserRegistrationCode(resource));
        }

        // persist codes
        this.userRegistrationCodeRepository.save(codes);

        // int formula property
        resource.setAvailable(resource.getBatchSize());

        return resource;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(UserRegistrationCodeBatch.PRE_AUTHORIZE_SEARCH)
    public List<UserRegistrationCodeInfo> findBatchCodes(String batchId) {

        LOGGER.info("findBatchCodes, id: {}", batchId);
        List<UserRegistrationCodeInfo> rows = this.repository.findBatchCodes(batchId);
        LOGGER.info("findBatchCodes, rows: {}", rows);

        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(UserRegistrationCodeBatch.PRE_AUTHORIZE_SEARCH)
    public List<UserRegistrationCodeInfo> findBatchCodes() {

        List<UserRegistrationCodeInfo> rows = this.repository.findBatchCodes();
        LOGGER.info("findBatchCodes, rows: {}", rows);

        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize(UserRegistrationCodeBatch.PRE_AUTHORIZE_SEARCH)
    public String findBatchName(String batchId) {
        UserRegistrationCodeBatch batch = this.repository.getOne(batchId);
        LOGGER.debug("findBatchName, batchId: {}, batch: {}", batchId, batch);
        String name = this.repository.findBatchName(batchId);
        LOGGER.debug("findBatchName, batchId: {}, name: {}", batchId, name);
        return name;
    }
}