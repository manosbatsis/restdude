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
package com.restdude.domain.cases.model;

import com.restdude.mdd.annotation.model.ModelResource;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 *
 */
@Entity
@Table(name = "application_case")
@ModelResource(pathFragment = CaseModel.API_PATH_FRAGMENT,
        apiName = "CaseModel",
        apiDescription = "CaseModel management")
public class CaseModel extends AbstractCaseModel<CasesApplication, CaseCommentModel> {

    public static final String API_PATH_FRAGMENT = "cases";
}
