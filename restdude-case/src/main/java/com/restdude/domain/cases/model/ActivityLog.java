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

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.PersistableModel;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.CurrentPrincipal;
import com.restdude.mdd.model.AbstractPersistableModel;
import com.restdude.mdd.model.AbstractPersistableNamedModel;
import com.restdude.mdd.model.AbstractSystemUuidPersistableModel;
import com.restdude.websocket.message.IActivityNotificationMessage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.javers.core.metamodel.annotation.DiffIgnore;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by manos on 14/6/2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@DiffIgnore
@Entity
@Table(name = "space_user_activity")
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog extends AbstractSystemUuidPersistableModel {

	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@ApiModelProperty(value = "Date created", readOnly = true)
	@Column(name = "date_created", nullable = false, updatable = false)
	@Getter @Setter
	private LocalDateTime createdDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@ApiModelProperty(readOnly = true)
	@Column(nullable = false, updatable = false)
	private String predicate;

	@CurrentPrincipal
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private User subject;

	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	private BaseContext context;

	@Any(
		metaDef = "SpaceUserActivityObjectMetaDef",
		metaColumn = @Column(name = "discriminator"),
		optional = true
	)
	@JoinColumn( name = "object_id" )
	private AbstractPersistableModel object;

}
