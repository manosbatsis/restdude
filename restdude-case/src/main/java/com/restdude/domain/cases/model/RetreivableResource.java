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

import static com.restdude.domain.CommentableModel.MAX_DETAIL_LENGTH;
import static com.restdude.domain.CommentableModel.MAX_TITLE_LENGTH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restdude.mdd.model.AbstractPersistableHierarchicalModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a retreivable resource with id, name, title and description.
 */
@Entity
@Table(name = "retreivable_resource")
@Inheritance(strategy = InheritanceType.JOINED)
public class RetreivableResource<P extends RetreivableResource, T extends RetreivableResource> extends AbstractPersistableHierarchicalModel<RetreivableResource, RetreivableResource> {

	@NotNull
	@Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
	@ApiModelProperty(value = "Short description, up to a handful of words", required = true, notes = "Max byte length: " + MAX_TITLE_LENGTH)
	@Getter @Setter
	private String title;

	@NotNull
	@Column(name = "detailed_description", nullable = false, length = MAX_DETAIL_LENGTH)
	@ApiModelProperty(value = "Detailed description text, supports markdown.", required = true, notes = "Max byte length: " + MAX_DETAIL_LENGTH)
	@Getter @Setter
	private String detail;

	public RetreivableResource(String name) {
		super(name);
	}

	public RetreivableResource() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preSave() {
		super.preSave();
		if (StringUtils.isNotEmpty(this.getTitle()) && this.getTitle().length() > MAX_TITLE_LENGTH) {
			this.setTitle(StringUtils.abbreviate(this.getTitle(), MAX_TITLE_LENGTH));
		}
		if (StringUtils.isNotEmpty(this.getDetail()) && this.getDetail().length() > MAX_DETAIL_LENGTH) {
			this.setDetail(StringUtils.abbreviate(this.getDetail(), MAX_DETAIL_LENGTH));
		}
	}

	public P getParentCasted(){
		return (P) this.getParent();
	}

	public T getSameAsCasted(){
		return (T) this.getSameAs();
	}
}
