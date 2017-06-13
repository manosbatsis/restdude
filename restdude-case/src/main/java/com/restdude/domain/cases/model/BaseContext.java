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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.cases.IBaseContext;
import com.restdude.domain.cases.model.enums.ContextVisibilityType;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserDTO;
import com.restdude.mdd.annotation.model.CurrentPrincipal;
import com.restdude.mdd.annotation.model.FilePersistence;
import com.restdude.mdd.annotation.model.FilePersistencePreview;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.model.AbstractPersistableNamedModel;
import com.restdude.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "context_base")
@Inheritance(strategy = InheritanceType.JOINED)
@ModelResource(pathFragment = BaseContext.API_PATH_FRAGMENT,
		apiName = "AbstractContext",
		apiDescription = "Contexts management")
@ApiModel(description = BaseContext.API_MODEL_DESCRIPTION)
public class BaseContext extends AbstractPersistableNamedModel implements IBaseContext<User, Membership, MembershipRequest> {

	public static final String API_PATH_FRAGMENT = "contexts";
	public static final String API_MODEL_DESCRIPTION = "A model representing a context, such as an organization, team, or process type.";

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

	@FilePersistence(maxWidth = 130, maxHeight = 130)
	@FilePersistencePreview(maxWidth = 100, maxHeight = 100)
	@FilePersistencePreview(maxWidth = 50, maxHeight = 50)
	@Column(name = "avatar_url")
	@Getter @Setter
	@ApiModelProperty(value = "The avatar URL")
	private String avatarUrl;// = Constants.DEFAULT_AVATAR_URL;

	@FilePersistence(maxWidth = 1920, maxHeight = 1080)
	@FilePersistencePreview(maxWidth = 1280, maxHeight = 720)
	@FilePersistencePreview(maxWidth = 720, maxHeight = 480)
	@Column(name = "banner_url")
	@Getter @Setter
	@ApiModelProperty(value = "The banner URL")
	private String bannerUrl;

	@NotNull
	@Column(name = "visibility", nullable = false)
	@Getter @Setter
	@ApiModelProperty(value = "Visibility settings", allowableValues = "SECRET, CLOSED, OPEN, PUBLIC", required = true)
	ContextVisibilityType visibility = ContextVisibilityType.CLOSED;

	@CurrentPrincipal
	@NotNull
	@ManyToOne
	@JoinColumn(name = "owner", nullable = false)
	private User owner;

	@Override
	@JsonIgnore
	public User getOwner() {
		return owner;
	}

	@JsonGetter("owner")
	public UserDTO getOwnerDTO() {
		return UserDTO.fromUser(this.owner);
	}

	@Override
	public void setOwner(User owner) {
		this.owner = owner;
	}


	@OneToMany(mappedBy = "context", orphanRemoval = true)
    @JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "User memberships")
	private Set<Membership> memberships;

    @OneToMany(mappedBy = "context", orphanRemoval = true)
    @JsonIgnore
	@Getter @Setter
	@ApiModelProperty(value = "The membership requests/invites")
    private List<MembershipRequest> membershipRequests;



	public BaseContext() {
		super();
	}

	public BaseContext(String name, String title, String description, String avatarUrl, String bannerUrl, ContextVisibilityType visibility, User owner, Set<Membership> memberships, List<MembershipRequest> membershipRequests) {
		super(name);
		this.title = title;
		this.description = description;
		this.avatarUrl = avatarUrl;
		this.bannerUrl = bannerUrl;
		this.visibility = visibility;
		this.owner = owner;
		this.memberships = memberships;
		this.membershipRequests = membershipRequests;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("name", this.getName())
				.append("visibility", this.getVisibility())
				.append("owner", this.getOwner())
				.toString();
	}


	public static class Builder {
		private String name;
		private String title;
		private String description;
		private String avatarUrl;
		private String bannerUrl;
		private User owner;
		private ContextVisibilityType visibility;
		private Set<Membership> memberships;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder avatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
			return this;
		}

		public Builder bannerUrl(String bannerUrl) {
			this.bannerUrl = bannerUrl;
			return this;
		}

		public Builder owner(User owner) {
			this.owner = owner;
			return this;
		}

		public Builder visibility(ContextVisibilityType visibility) {
			this.visibility = visibility;
			return this;
		}

		public BaseContext build() {
			return new BaseContext(this);
		}
	}

	private BaseContext(Builder builder) {
		this.setName(builder.name);
		this.setTitle(builder.title);
		this.setDescription(builder.description);
		this.setAvatarUrl(builder.avatarUrl);
		this.setBannerUrl(builder.bannerUrl);
		this.setOwner(builder.owner);
		this.setVisibility(builder.visibility);
	}

}
