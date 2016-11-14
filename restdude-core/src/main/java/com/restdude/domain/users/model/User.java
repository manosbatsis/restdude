/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.fs.FilePersistence;
import com.restdude.domain.fs.FilePersistencePreview;
import com.restdude.domain.geography.model.Country;
import com.restdude.domain.metadata.model.AbstractMetadataSubject;
import com.restdude.mdd.uischema.annotation.FormSchemaEntry;
import com.restdude.mdd.uischema.annotation.FormSchemas;
import com.restdude.util.Constants;
import com.restdude.util.MD5Utils;
import com.restdude.websocket.model.StompSession;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 */

@ShallowReference
@Entity
@ApiModel(description = "Human users")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractMetadataSubject<UserMetadatum> implements CalipsoPersistable<String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
	private static final long serialVersionUID = -7942906897981646998L;
	public static final String PRE_AUTHORIZE_SEARCH = "isAuthenticated()";
	public static final String PRE_AUTHORIZE_CREATE = "hasAnyRole('" + Role.ROLE_ADMIN + "', '" + Role.ROLE_SITE_OPERATOR + "')";
	public static final String PRE_AUTHORIZE_UPDATE = " #id == principal.id or " + PRE_AUTHORIZE_CREATE;
	public static final String PRE_AUTHORIZE_PATCH = PRE_AUTHORIZE_UPDATE;
	public static final String PRE_AUTHORIZE_VIEW = "isAuthenticated()";
	public static final String PRE_AUTHORIZE_DELETE = "denyAll";

	public static final String PRE_AUTHORIZE_DELETE_BY_ID = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_ALL = "denyAll";
	public static final String PRE_AUTHORIZE_DELETE_WITH_CASCADE = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_BY_IDS = "denyAll";
	public static final String PRE_AUTHORIZE_FIND_ALL = "denyAll";
	public static final String PRE_AUTHORIZE_COUNT = "denyAll";

	@ApiModelProperty(hidden = true)
	@Formula("concat(first_name, ' ', last_name )")
	private String searchName;

	@Formula("concat(first_name, ' ', last_name )")
	private String name;

	@Column(name = "first_name", nullable = true)
	private String firstName;

	@Column(name = "last_name", nullable = true)
	private String lastName;

	@Transient
	@JsonIgnore
	Locale localeObject = null;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "email_hash", nullable = false)
	private String emailHash;

	@FilePersistence(maxWidth = 130, maxHeight = 130)
	@FilePersistencePreview(maxWidth = 100, maxHeight = 100)
	@FilePersistencePreview(maxWidth = 50, maxHeight = 50)
	@Column(name = "avatar_url")
	private String avatarUrl;

	@FilePersistence(maxWidth = 1920, maxHeight = 1080)
	@FilePersistencePreview(maxWidth = 1280, maxHeight = 720)
	@FilePersistencePreview(maxWidth = 720, maxHeight = 480)
	@Column(name = "banner_url")
	private String bannerUrl;

	@Column(nullable = true)
	private String telephone;

	@Column(nullable = true)
	private String cellphone;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private String postCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id", referencedColumnName = "id", nullable = true)
	private Country country;

	@Column(name = "birthday")
	@FormSchemas({ @FormSchemaEntry(json = FormSchemaEntry.TYPE_DATE) })
	private Date birthDay;

	@Column(name = "last_visit")
	private Date lastVisit;

	@Column(name = "locale", nullable = false)
	private String locale = "en";

	@Formula(" (select uc.active from user_credentials as uc where uc.user_id = id) ")
	private Boolean active = false;

	@Formula(" (select count(*) from stomp_session as stmpSess where stmpSess.user_id = id) ")
	private Integer stompSessionCount;

	@JsonIgnore
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	UserCredentials credentials;

	// @OneToOne(optional = true, fetch=FetchType.LAZY)
	// @MapsId
	// private LocalRegionMailingAddress mailingAddress;

	// @JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private List<Role> roles = new ArrayList<Role>(0);

	@JsonIgnore
	@OneToMany(mappedBy = "id.owner")
	private List<Friendship> friendships;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<StompSession> stompSessions;

	public User() {
	}

	public User(UserDTO dto) {
		this.setId(dto.getId());
		this.setFirstName(dto.getFirstName());
		this.setLastName(dto.getLastName());
		this.email = dto.getEmail();
		this.emailHash = dto.getEmailHash();
	}

	public User(String id) {
		this.setId(id);
	}

	public Locale getLocaleObject() {
		if (this.localeObject == null) {
			this.localeObject = new Locale(this.getLocale() != null ? this.getLocale() : "en");
		}
		return localeObject;
	}

	public boolean hasRole(String roleName) {
		Assert.notNull(roleName, "Role name cannot be null");
		boolean hasIt = false;
		if (CollectionUtils.isNotEmpty(this.roles)) {
			for (Role role : roles) {
				if (roleName.equalsIgnoreCase(role.getName())) {
					hasIt = true;
					break;
				}
			}
		}
		return hasIt;
	}

	@Override
	public Class<UserMetadatum> getMetadataDomainClass() {
		return UserMetadatum.class;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(this.name).append(this.email).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!User.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		User other = (User) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(getName(), other.getName());
		builder.append(getEmail(), other.getEmail());
		return builder.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("firstName", this.getFirstName()).append("lastName", this.getLastName())
				.append("email", this.getEmail()).append("new", this.isNew()).append("roles", this.getRoles())
				.toString();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getStompSessionCount() {
		return stompSessionCount;
	}

	public void setStompSessionCount(Integer stompSessionCount) {
		this.stompSessionCount = stompSessionCount;
	}

	/**
	 * Called by Hibernate <code>@PreUpdate</code> to keep the email hash of the
	 * user up-to date
	 */
	@PreUpdate
	public void onBeforeUpdate() {
		this.onBeforeSave();
	}

	/**
	 * Called by Hibernate <code>@PrePersist</code> > to keep the email hash of
	 * the user up-to date
	 */
	@PrePersist
	public void onBeforeCreate() {
		this.onBeforeSave();
	}

	protected void onBeforeSave() {
		if (!StringUtils.isNotBlank(this.getLocale())) {
			this.setLocale("en");
		}
		// create the email hash,
		// use email as username if latter is empty
		if (this.getEmail() != null) {

			// make sure it's trimmed
			this.setEmail(this.getEmail().trim());
			// update the hash
			this.setEmailHash(MD5Utils.md5Hex(this.getEmail()));

		}
		// fallback to gravatar
		if (StringUtils.isBlank(this.getAvatarUrl())) {
			initDefaultAvatarUrl();
		}
		// default banner
		if (StringUtils.isBlank(this.getBannerUrl())) {
			this.setBannerUrl(Constants.DEFAULT_BANNER_URL);
		}

	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	/**
	 * Add a role to this principal.
	 * 
	 * @param role
	 *            the role to add
	 */
	public void addRole(Role role) {
		if (this.roles == null) {
			this.roles = new LinkedList<Role>();
		}
		this.roles.add(role);
	}

	/**
	 * Remove a role from this principal.
	 * 
	 * @param role
	 *            the role to remove
	 */
	public void removeRole(Role role) {
		if (!CollectionUtils.isEmpty(roles)) {
			this.roles.remove(role);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	@JsonGetter("fullName")
	public String getFullName() {
		StringBuffer s = new StringBuffer("");
		if (StringUtils.isNotBlank(this.getFirstName())) {
			s.append(this.getFirstName());
			if (StringUtils.isNotBlank(this.getLastName())) {
				s.append(' ');
			}
		}
		if (StringUtils.isNotBlank(this.getLastName())) {
			s.append(this.getLastName());
		}
		return s.toString();

	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailHash() {
		return emailHash;
	}

	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Date getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<? extends GrantedAuthority> getRoles() {
		return this.roles;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return the credentials
	 */
	public UserCredentials getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(UserCredentials credentials) {
		this.credentials = credentials;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Friendship> getFriendships() {
		return friendships;
	}

	public void setFriendships(List<Friendship> friendships) {
		this.friendships = friendships;
	}

	public List<StompSession> getStompSessions() {
		return stompSessions;
	}

	public void setStompSessions(List<StompSession> stompSessions) {
		this.stompSessions = stompSessions;
	}

	/**
	 * Use Gravatar only if application is running on port 80.
	 * See also Gravatar <a href="http://en.gravatar.com/site/implement/images/#default-image">default image</a>
	 */
	protected void initDefaultAvatarUrl() {
		try {
			this.setAvatarUrl(new StringBuffer(Constants.GRAVATAR_BASE_IMG_URL).append(this.getEmailHash())
					.append("?d=").append(URLEncoder.encode(Constants.DEFAULT_AVATAR_URL, CharEncoding.UTF_8))
					.toString());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Failed encoding avatar url");
		}
	}

	public static class Builder {
		private String id;
		private String firstName;
		private String lastName;
		private String email;
		private String emailHash;
		private String avatarUrl;
		private String bannerUrl;
		private String telephone;
		private String cellphone;
		private String address;
		private String postCode;
		private Country country;
		private Date birthDay;
		private Date lastVisit;
		private String locale;
		private UserCredentials credentials;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder emailHash(String emailHash) {
			this.emailHash = emailHash;
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

		public Builder telephone(String telephone) {
			this.telephone = telephone;
			return this;
		}

		public Builder cellphone(String cellphone) {
			this.cellphone = cellphone;
			return this;
		}

		public Builder address(String address) {
			this.address = address;
			return this;
		}

		public Builder postCode(String postCode) {
			this.postCode = postCode;
			return this;
		}

		public Builder country(Country country) {
			this.country = country;
			return this;
		}

		public Builder birthDay(Date birthDay) {
			this.birthDay = birthDay;
			return this;
		}

		public Builder lastVisit(Date lastVisit) {
			this.lastVisit = lastVisit;
			return this;
		}

		public Builder locale(String locale) {
			this.locale = locale;
			return this;
		}

		public Builder credentials(UserCredentials credentials) {
			this.credentials = credentials;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	private User(Builder builder) {
		this.setId(builder.id);
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.email = builder.email;
		this.emailHash = builder.emailHash;
		this.avatarUrl = builder.avatarUrl;
		this.bannerUrl = builder.bannerUrl;
		this.telephone = builder.telephone;
		this.cellphone = builder.cellphone;
		this.address = builder.address;
		this.postCode = builder.postCode;
		this.country = builder.country;
		this.birthDay = builder.birthDay;
		this.lastVisit = builder.lastVisit;
		this.locale = builder.locale;
		this.credentials = builder.credentials;
	}
}