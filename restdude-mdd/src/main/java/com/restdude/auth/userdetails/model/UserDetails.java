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
package com.restdude.auth.userdetails.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.mdd.binding.SkipPropertyDeserializer;
import com.restdude.mdd.binding.SkipPropertySerializer;
import com.restdude.domain.users.model.Role;
import com.restdude.mdd.model.Roles;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.model.UserDetailsModel;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


//@ApiModel
@XmlRootElement(name = "loggedInUserDetails")
public class UserDetails implements UserDetailsModel {
	
	private static final long serialVersionUID = 5206010308112791343L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDetails.class);

    public static final List<GrantedAuthority> ROLES_ANONYMOUD = Collections.unmodifiableList(Arrays.asList(new Role(Roles.ROLE_ANONYMOUS)));


	private String pk;
	
	private String username;

	@JsonSerialize(using = SkipPropertySerializer.class)
	private String password;

	private LocalDateTime lastPassWordChangeDate;

	private String emailHash;

	private String firstName;
	private String lastName;

	private String avatarUrl;

	private String telephone;
	private String cellphone;
	
	private String locale = "en";
	private String dateFormat;

	private LocalDate birthDay;
	private LocalDateTime lastVisit;
	private LocalDateTime lastPost;
	private Short loginAttempts = 0;

	private String redirectUrl = null;

	private Boolean active = false;
	private Integer stompSessionCount = 0;

	@JsonSerialize(using = SkipPropertySerializer.class)
	private String inactivationReason;

	private LocalDateTime inactivationDate;
	private boolean isAdmin = false;
	private boolean isSiteAdmin = false;


	@JsonDeserialize(using = SkipPropertyDeserializer.class)
	@JsonProperty(value = "roles")
	private List<? extends GrantedAuthority> authorities;
	private Map<String, String> metadata;

	private Boolean isResetPasswordReguest = false;


    public static UserDetailsModel anonymous() {
        UserDetails details = new UserDetails();
        details.setUsername("anonymousUser");
        details.authorities = ROLES_ANONYMOUD;
        return details;
    }

	public static UserDetailsModel fromUser(User user) {
		UserDetails details = null;
		if (user != null) {
			details = new UserDetails();
			BeanUtils.copyProperties(user, details);
			if (user.getPk() != null) {
				details.setPk(user.getPk().toString());
			}
			if (user.getCredentials() != null) {
				BeanUtils.copyProperties(user.getCredentials(), details, "pk");
			}
			// init global roles
			if (!CollectionUtils.isEmpty(user.getRoles())) {
				details.setAuthorities(user.getRoles());
				for (GrantedAuthority authority : user.getRoles()) {
					if (authority.getAuthority().equals(Roles.ROLE_ADMIN)) {
						details.isAdmin = true;
					} else if (authority.getAuthority()
							.equals(Roles.ROLE_SITE_OPERATOR)) {
						details.isSiteAdmin = true;
					}
				}
			}

			
		}
		return details;
	}

	/**
	 * Default constructor
	 */
	public UserDetails() {

	}
	public UserDetails(LoginSubmission loginSubmission) {
		this();
		BeanUtils.copyProperties(loginSubmission, this);
		if (this.username == null) {
			this.username = loginSubmission.getEmail();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("pk", pk)
			.append("username", username)
                //.append("email", email)
                .append("password", this.password)
				.append("active", this.active)
			.append("metadata", metadata)
			.append("authorities", authorities)
			.toString();
	}

	/**
	 * @see UserDetailsModel#getPk()
	 */
	@Override
	public String getPk() {
		return pk;
	}

	/**
	 * @see UserDetailsModel#setPk(java.lang.String)
	 */
	@Override
	public void setPk(String pk) {
		this.pk = pk;
	}

    @Override
    public void preSave() {

    }

	public Integer getStompSessionCount() {
		return stompSessionCount;
	}

	public void setStompSessionCount(Integer stompSessionCount) {
		this.stompSessionCount = stompSessionCount;
	}

	/**
     * @see UserDetailsModel#getFirstName()
     */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
     * @see UserDetailsModel#setFirstName(java.lang.String)
     */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
     * @see UserDetailsModel#getLastName()
     */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
     * @see UserDetailsModel#setLastName(java.lang.String)
     */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
     * @see UserDetailsModel#getLastPassWordChangeDate()
     */
	@Override
	public LocalDateTime getLastPassWordChangeDate() {
		return lastPassWordChangeDate;
	}

	/**
	 * @see UserDetailsModel#setLastPassWordChangeDate(LocalDateTime)
	 */
	@Override
	public void setLastPassWordChangeDate(LocalDateTime lastPassWordChangeDate) {
		this.lastPassWordChangeDate = lastPassWordChangeDate;
	}
	
	/**
     * @see UserDetailsModel#getEmailHash()
     */
	@Override
	public String getEmailHash() {
		return emailHash;
	}

	/**
     * @see UserDetailsModel#setEmailHash(java.lang.String)
     */
	@Override
	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	/**
     * @see UserDetailsModel#getAvatarUrl()
     */
	@Override
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
     * @see UserDetailsModel#setAvatarUrl(java.lang.String)
     */
	@Override
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	

	/**
     * @see UserDetailsModel#getTelephone()
     */
	@Override
	public String getTelephone() {
		return telephone;
	}

	/**
     * @see UserDetailsModel#setTelephone(java.lang.String)
     */
	@Override
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
     * @see UserDetailsModel#getCellphone()
     */
	@Override
	public String getCellphone() {
		return cellphone;
	}

	/**
     * @see UserDetailsModel#setCellphone(java.lang.String)
     */
	@Override
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
     * @see UserDetailsModel#getBirthDay()
     */
	@Override
	public LocalDate getBirthDay() {
		return birthDay;
	}

	/**
	 * @see UserDetailsModel#setBirthDay(LocalDate)
	 */
	@Override
	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}

	/**
     * @see UserDetailsModel#getLastVisit()
     */
	@Override
	public LocalDateTime getLastVisit() {
		return lastVisit;
	}

	/**
	 * @see UserDetailsModel#setLastVisit(LocalDateTime)
	 */
	@Override
	public void setLastVisit(LocalDateTime lastVisit) {
		this.lastVisit = lastVisit;
	}

	/**
     * @see UserDetailsModel#getLastPost()
     */
	@Override
	public LocalDateTime getLastPost() {
		return lastPost;
	}

	/**
	 * @see UserDetailsModel#setLastPost(LocalDateTime)
	 */
	@Override
	public void setLastPost(LocalDateTime lastPost) {
		this.lastPost = lastPost;
	}

	/**
     * @see UserDetailsModel#getLoginAttempts()
     */
	@Override
	public Short getLoginAttempts() {
		return loginAttempts;
	}

	/**
     * @see UserDetailsModel#setLoginAttempts(java.lang.Short)
     */
	@Override
	public void setLoginAttempts(Short loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	/**
     * @see UserDetailsModel#getActive()
     */
	@Override
	public Boolean getActive() {
		return active;
	}

	/**
     * @see UserDetailsModel#setActive(java.lang.Boolean)
     */
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
     * @see UserDetailsModel#getInactivationReason()
     */
	@Override
	public String getInactivationReason() {
		return inactivationReason;
	}

	/**
     * @see UserDetailsModel#setInactivationReason(java.lang.String)
     */
	@Override
	public void setInactivationReason(String inactivationReason) {
		this.inactivationReason = inactivationReason;
	}

	/**
     * @see UserDetailsModel#getInactivationDate()
     */
	@Override
	public LocalDateTime getInactivationDate() {
		return inactivationDate;
	}

	/**
	 * @see UserDetailsModel#setInactivationDate(LocalDateTime)
	 */
	@Override
	public void setInactivationDate(LocalDateTime inactivationDate) {
		this.inactivationDate = inactivationDate;
	}

	/**
     * @see UserDetailsModel#getLocale()
     */
	@Override
	public String getLocale() {
		return locale;
	}

	/**
     * @see UserDetailsModel#setLocale(java.lang.String)
     */
	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
     * @see UserDetailsModel#getDateFormat()
     */
	@Override
	public String getDateFormat() {
		return dateFormat;
	}

	/**
     * @see UserDetailsModel#setDateFormat(java.lang.String)
     */
	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
     * @see UserDetailsModel#isAdmin()
     */
	@Override
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
     * @see UserDetailsModel#setAdmin(boolean)
     */
	@Override
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
     * @see UserDetailsModel#isSiteAdmin()
     */
	@Override
	public boolean isSiteAdmin() {
		return isSiteAdmin;
	}

	/**
     * @see UserDetailsModel#setSiteAdmin(boolean)
     */
	@Override
	public void setSiteAdmin(boolean isSiteAdmin) {
		this.isSiteAdmin = isSiteAdmin;
	}

	/**
     * @see UserDetailsModel#setRedirectUrl(java.lang.String)
     */
	@Override
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
     * @see UserDetailsModel#getRedirectUrl()
     */
	@Override
	public String getRedirectUrl() {
		return this.redirectUrl;
	}



	/**
     * @see UserDetailsModel#getMetadata()
     */
	@Override
	public Map<String, String> getMetadata() {
		return this.metadata;
	}

	/**
     * @see UserDetailsModel#setMetadata(java.util.Map)
     */
	@Override
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
     * @see UserDetailsModel#addMetadatum(java.lang.String, java.lang.String)
     */
	@Override
	public void addMetadatum(String predicate, String object) {
		if (this.metadata == null) {
			this.metadata = new HashMap<String, String>();
		}
		LOGGER.info("addMetadatum predicate: " + predicate + ", object: "
				+ object);
		this.metadata.put(predicate, object);
	}


	/**
     * @see UserDetailsModel#setUsername(java.lang.String)
     */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/**
     * @see UserDetailsModel#setPassword(java.lang.String)
     */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * @see UserDetailsModel#setAuthorities(List)
     */
	@Override
	public void setAuthorities(
			List<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	// SocialUserDetails

	/**
     * @see UserDetailsModel#getAuthorities()
     */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
     * @see UserDetailsModel#getPassword()
     */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
     * @see UserDetailsModel#getUsername()
     */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
     * @see UserDetailsModel#isAccountNonExpired()
     */
	@Override
	public boolean isAccountNonExpired() {
		return this.active;
	}

	/**
     * @see UserDetailsModel#isAccountNonLocked()
     */
	@Override
	public boolean isAccountNonLocked() {
		return this.active;
	}

	/**
     * @see UserDetailsModel#isCredentialsNonExpired()
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.active;
	}

	/**
     * @see UserDetailsModel#isEnabled()
     */
	@Override
	public boolean isEnabled() {
		return BooleanUtils.isTrue(this.active);
	}

	/**
     * @see UserDetailsModel#getUserId()
     */
	@Override
	public String getUserId() {
		return this.pk;
	}

	@Override
	public String getName() {
		return this.getUsername();
    }

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
	}


}
