/**
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Calipso, a software platform by www.Abiss.gr.
 *
 * Calipso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Calipso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Calipso. If not, see http://www.gnu.org/licenses/agpl.html
 */
package gr.abiss.calipso.model.attrs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "custom_attribute_long")
@Inheritance(strategy = InheritanceType.JOINED)
public class LongCustomAttribute extends CustomAttribute<Long> {

	private static final long serialVersionUID = 808705172204599477L;

	@NotNull
	@Column(name = "value", nullable = false)
	private Long value;

	@Override
	public Long getValue() {
		return value;
	}

	@Override
	public void setValue(Long value) {
		this.value = value;
	}

}
