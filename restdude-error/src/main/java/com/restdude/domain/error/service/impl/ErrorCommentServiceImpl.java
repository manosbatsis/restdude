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
package com.restdude.domain.error.service.impl;

import javax.inject.Named;

import com.restdude.domain.cases.service.impl.AbstractCaseCommentServiceImpl;
import com.restdude.domain.error.model.BaseError;
import com.restdude.domain.error.model.ErrorComment;
import com.restdude.domain.error.repository.ErrorCommentRepository;
import com.restdude.domain.error.service.ErrorCommentService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named(ErrorCommentService.BEAN_ID)
public class ErrorCommentServiceImpl extends AbstractCaseCommentServiceImpl<ErrorComment, BaseError, ErrorCommentRepository>
        implements ErrorCommentService {

    /**
     * {@inheritDoc}
     */
    public Integer getEntryIndex(ErrorComment persisted){
        return this.repository.getEntryIndex(persisted);
    }

}