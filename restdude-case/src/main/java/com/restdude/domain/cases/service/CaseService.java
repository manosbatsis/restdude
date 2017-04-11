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
package com.restdude.domain.cases.service;

import com.restdude.domain.cases.CaseModel;
import com.restdude.domain.cases.model.CaseStatus;
import com.restdude.mdd.service.PersistableModelService;

import java.util.List;


public interface CaseService<T extends CaseModel<?>> extends PersistableModelService<T, String> {



    /**
     * Get the status suitable for this new transient entry
     *
     * @param unpersisted
     * @return
     */
    CaseStatus getStatusForNew(T unpersisted);

    /**
     * Get the status options suitable for this entry
     *
     * @param unpersisted
     * @return
     */
    List<CaseStatus> getNextStatusOptions(T unpersisted);

}
