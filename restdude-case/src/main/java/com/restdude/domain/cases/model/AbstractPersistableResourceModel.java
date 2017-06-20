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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.restdude.mdd.model.AbstractPersistableHierarchicalModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a retreivable resource with id, name, title and description.
 */
@MappedSuperclass
public class AbstractPersistableResourceModel extends AbstractPersistableHierarchicalModel<BaseContext> {

	@NotNull
	@Column(name = "title", nullable = false, unique = true)
	@Getter @Setter
	@ApiModelProperty(value = "Short description, up to a handful of words", required = true)
	private String title;

	@NotNull
	@Column(name = "description", length = 500, nullable = false)
	@Getter @Setter
	@ApiModelProperty(value = "Regular Description text, i.e. a paragraph", required = true)
	private String description;

	public AbstractPersistableResourceModel(String name) {
		super(name);
	}

	public AbstractPersistableResourceModel() {
		super();
	}
}
