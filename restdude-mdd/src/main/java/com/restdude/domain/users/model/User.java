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
package com.restdude.domain.users.model;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.auth.spel.annotations.PreAuthorizeCreate;
import com.restdude.auth.spel.annotations.PreAuthorizeFindById;
import com.restdude.auth.spel.annotations.PreAuthorizePatch;
import com.restdude.auth.spel.annotations.PreAuthorizeUpdate;
import com.restdude.auth.spel.binding.SpelUtil;
import com.restdude.domain.UserModel;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.friends.model.Friendship;
import com.restdude.domain.metadata.model.AbstractMetadataSubjectModel;
import com.restdude.domain.users.controller.UserController;
import com.restdude.mdd.annotation.model.FilePersistence;
import com.restdude.mdd.annotation.model.FilePersistencePreview;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.util.Constants;
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
import org.springframework.hateoas.core.Relation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


@ShallowReference
@Entity
@ModelResource(pathFragment = "users", apiDescription = "User management operations", apiName = "Users", controllerSuperClass = UserController.class)
@ApiModel(description = "Human users")
@Relation(value = "user", collectionRelation = "users")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@PreAuthorizeCreate(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizePatch(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeUpdate(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
@PreAuthorizeFindById(controller = SpelUtil.PERMIT_ALL, service = SpelUtil.PERMIT_ALL)
public class User extends AbstractMetadataSubjectModel<UserMetadatum> implements UserModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
    private static final long serialVersionUID = -7942906897981646998L;


    @ApiModelProperty(hidden = true)
    @Formula("concat(first_name, ' ', last_name )")
    private String searchName;

    @Formula("concat(first_name, ' ', last_name )")
    private String name;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Transient
    @JsonIgnore
    Locale localeObject = null;

    @NotNull
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;


    @NotNull
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

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    @NotNull
    @Column(name = "locale", nullable = false)
    private String locale = "en";

    @Formula(" (select uc.active from user_credentials as uc where uc.user_id = pk) ")
    private Boolean active = false;

    @Formula(" (select count(*) from stomp_session as stmpSess where stmpSess.user_id = pk) ")
    private Integer stompSessionCount;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    UserCredentials credentials;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    ContactDetails contactDetails;

    // @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {
            @JoinColumn(name = "user_id")})
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "pk.left")
    private List<Friendship> friendships;


    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<String> stompSessions;

    public User() {
    }

    public User(UserDTO dto) {
        this.setPk(dto.getId());
        this.setFirstName(dto.getFirstName());
        this.setLastName(dto.getLastName());
        this.username = dto.getUsername();
        this.emailHash = dto.getEmailHash();
    }

    public User(String id) {
        this.setPk(id);
    }


    @Override
    public Locale getLocaleObject() {
        if (this.localeObject == null) {
            this.localeObject = new Locale(this.getLocale() != null ? this.getLocale() : "en");
        }
        return localeObject;
    }

    @Override
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
        return new HashCodeBuilder(11, 13).appendSuper(super.hashCode()).append(this.username).toHashCode();
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
        builder.append(getUsername(), other.getUsername());
        return builder.isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("username", this.getUsername()).append("name", this.getName()).append("roles", this.getRoles())
                .toString();
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getStringCount() {
        return stompSessionCount;
    }

    public void setStringCount(Integer stompSessionCount) {
        this.stompSessionCount = stompSessionCount;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void preSave() {
        if (!StringUtils.isNotBlank(this.getLocale())) {
            this.setLocale("en");
        }
        // enforce low-case
        if (!StringUtils.isNotBlank(this.getUsername())) {
            this.setUsername(this.getUsername().toLowerCase());
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

    // serialize user name to response
    @Override
    @JsonProperty
    public String getUsername() {
        return username;
    }

    // but ignore when de-serializing from request
    @Override
    @JsonIgnore
    public void setUsername(String userName) {
        this.username = userName;
    }

    @Override
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getEmailHash() {
        return emailHash;
    }

    @Override
    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    @Override
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getBannerUrl() {
        return bannerUrl;
    }

    @Override
    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    @Override
    public LocalDateTime getLastVisit() {
        return lastVisit;
    }

    @Override
    public void setLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public List<? extends GrantedAuthority> getRoles() {
        return this.roles;
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

    public List<String> getStrings() {
        return stompSessions;
    }

    public void setStrings(List<String> stompSessions) {
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

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private String username;
        private String emailHash;
        private String avatarUrl;
        private String bannerUrl;
        private LocalDateTime lastVisit;
        private String locale;
        private UserCredentials credentials;
        private ContactDetails contactDetails;

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

        public Builder username(String username) {
            this.username = username;
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

        public Builder lastVisit(LocalDateTime lastVisit) {
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

        public Builder contactDetails(ContactDetails contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.setPk(builder.id);
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.emailHash = builder.emailHash;
        this.avatarUrl = builder.avatarUrl;
        this.bannerUrl = builder.bannerUrl;
        this.lastVisit = builder.lastVisit;
        this.locale = builder.locale;
        this.credentials = builder.credentials;
        this.contactDetails = builder.contactDetails;
    }
}