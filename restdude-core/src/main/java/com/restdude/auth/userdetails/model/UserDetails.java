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
package com.restdude.auth.userdetails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.restdude.domain.base.binding.SkipPropertyDeserializer;
import com.restdude.domain.base.binding.SkipPropertySerializer;
import com.restdude.domain.users.model.Role;
import com.restdude.domain.users.model.User;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;



//@ApiModel
@XmlRootElement(name = "loggedInUserDetails")
public class UserDetails implements  ICalipsoUserDetails{
	
	private static final long serialVersionUID = 5206010308112791343L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDetails.class);



	private String id;
	
	private String username;

	@JsonSerialize(using = SkipPropertySerializer.class)
	private String password;

    private Date lastPassWordChangeDate;

	private String email;
	private String emailHash;

	private String firstName;
	private String lastName;

	private String avatarUrl;

	private String telephone;
	private String cellphone;
	
	private String locale = "en";
	private String dateFormat;

	private Date birthDay;
	private Date lastVisit;
	private Date lastPost;
	private Short loginAttempts = 0;

	private String redirectUrl = null;

	private Boolean active = false;

	@JsonSerialize(using = SkipPropertySerializer.class)
	private String inactivationReason;

	private Date inactivationDate;
	private boolean isAdmin = false;
	private boolean isSiteAdmin = false;


	@JsonDeserialize(using = SkipPropertyDeserializer.class)
	@JsonProperty(value = "roles")
	private List<? extends GrantedAuthority> authorities;
	private Map<String, String> metadata;



	private Boolean isResetPasswordReguest = false;

	public static ICalipsoUserDetails fromUser(User user) {
		UserDetails details = null;
		if (user != null) {
			details = new UserDetails();
			BeanUtils.copyProperties(user, details);
			if(user.getId() != null){
				details.setId(user.getId().toString());
			}
			if (user.getCredentials() != null) {
				BeanUtils.copyProperties(user.getCredentials(), details, "id");
			}
			// init global roles
			if (!CollectionUtils.isEmpty(user.getRoles())) {
				details.setAuthorities(user.getRoles());
				for (GrantedAuthority authority : user.getRoles()) {
					if (authority.getAuthority().equals(Role.ROLE_ADMIN)) {
						details.isAdmin = true;
					} else if (authority.getAuthority()
							.equals(Role.ROLE_SITE_OPERATOR)) {
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
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("username", username)
			.append("email", email)
				.append("password", this.password)
				.append("active", this.active)
			.append("metadata", metadata)
			.append("authorities", authorities)
			.toString();
	}

	/**
     * @see ICalipsoUserDetails#getId()
     */
	@Override
	public String getId() {
		return id;
	}

	/**
     * @see ICalipsoUserDetails#setId(java.lang.String)
     */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
     * @see ICalipsoUserDetails#getFirstName()
     */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
     * @see ICalipsoUserDetails#setFirstName(java.lang.String)
     */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
     * @see ICalipsoUserDetails#getLastName()
     */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
     * @see ICalipsoUserDetails#setLastName(java.lang.String)
     */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
     * @see ICalipsoUserDetails#getLastPassWordChangeDate()
     */
	@Override
	public Date getLastPassWordChangeDate() {
		return lastPassWordChangeDate;
	}

	/**
     * @see ICalipsoUserDetails#setLastPassWordChangeDate(java.util.Date)
     */
	@Override
	public void setLastPassWordChangeDate(Date lastPassWordChangeDate) {
		this.lastPassWordChangeDate = lastPassWordChangeDate;
	}

	/**
     * @see ICalipsoUserDetails#getEmail()
     */
	@Override
	public String getEmail() {
		return email;
	}

	/**
     * @see ICalipsoUserDetails#setEmail(java.lang.String)
     */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/**
     * @see ICalipsoUserDetails#getEmailOrUsername()
     */
	@Override
	public String getEmailOrUsername() {
		return email != null ? email : username;
	}
	
	/**
     * @see ICalipsoUserDetails#getEmailHash()
     */
	@Override
	public String getEmailHash() {
		return emailHash;
	}

	/**
     * @see ICalipsoUserDetails#setEmailHash(java.lang.String)
     */
	@Override
	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	/**
     * @see ICalipsoUserDetails#getAvatarUrl()
     */
	@Override
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
     * @see ICalipsoUserDetails#setAvatarUrl(java.lang.String)
     */
	@Override
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	

	/**
     * @see ICalipsoUserDetails#getTelephone()
     */
	@Override
	public String getTelephone() {
		return telephone;
	}

	/**
     * @see ICalipsoUserDetails#setTelephone(java.lang.String)
     */
	@Override
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
     * @see ICalipsoUserDetails#getCellphone()
     */
	@Override
	public String getCellphone() {
		return cellphone;
	}

	/**
     * @see ICalipsoUserDetails#setCellphone(java.lang.String)
     */
	@Override
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
     * @see ICalipsoUserDetails#getBirthDay()
     */
	@Override
	public Date getBirthDay() {
		return birthDay;
	}

	/**
     * @see ICalipsoUserDetails#setBirthDay(java.util.Date)
     */
	@Override
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	/**
     * @see ICalipsoUserDetails#getLastVisit()
     */
	@Override
	public Date getLastVisit() {
		return lastVisit;
	}

	/**
     * @see ICalipsoUserDetails#setLastVisit(java.util.Date)
     */
	@Override
	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	/**
     * @see ICalipsoUserDetails#getLastPost()
     */
	@Override
	public Date getLastPost() {
		return lastPost;
	}

	/**
     * @see ICalipsoUserDetails#setLastPost(java.util.Date)
     */
	@Override
	public void setLastPost(Date lastPost) {
		this.lastPost = lastPost;
	}

	/**
     * @see ICalipsoUserDetails#getLoginAttempts()
     */
	@Override
	public Short getLoginAttempts() {
		return loginAttempts;
	}

	/**
     * @see ICalipsoUserDetails#setLoginAttempts(java.lang.Short)
     */
	@Override
	public void setLoginAttempts(Short loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	/**
     * @see ICalipsoUserDetails#getActive()
     */
	@Override
	public Boolean getActive() {
		return active;
	}

	/**
     * @see ICalipsoUserDetails#setActive(java.lang.Boolean)
     */
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
     * @see ICalipsoUserDetails#getInactivationReason()
     */
	@Override
	public String getInactivationReason() {
		return inactivationReason;
	}

	/**
     * @see ICalipsoUserDetails#setInactivationReason(java.lang.String)
     */
	@Override
	public void setInactivationReason(String inactivationReason) {
		this.inactivationReason = inactivationReason;
	}

	/**
     * @see ICalipsoUserDetails#getInactivationDate()
     */
	@Override
	public Date getInactivationDate() {
		return inactivationDate;
	}

	/**
     * @see ICalipsoUserDetails#setInactivationDate(java.util.Date)
     */
	@Override
	public void setInactivationDate(Date inactivationDate) {
		this.inactivationDate = inactivationDate;
	}

	/**
     * @see ICalipsoUserDetails#getLocale()
     */
	@Override
	public String getLocale() {
		return locale;
	}

	/**
     * @see ICalipsoUserDetails#setLocale(java.lang.String)
     */
	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
     * @see ICalipsoUserDetails#getDateFormat()
     */
	@Override
	public String getDateFormat() {
		return dateFormat;
	}

	/**
     * @see ICalipsoUserDetails#setDateFormat(java.lang.String)
     */
	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
     * @see ICalipsoUserDetails#isAdmin()
     */
	@Override
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
     * @see ICalipsoUserDetails#setAdmin(boolean)
     */
	@Override
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
     * @see ICalipsoUserDetails#isSiteAdmin()
     */
	@Override
	public boolean isSiteAdmin() {
		return isSiteAdmin;
	}

	/**
     * @see ICalipsoUserDetails#setSiteAdmin(boolean)
     */
	@Override
	public void setSiteAdmin(boolean isSiteAdmin) {
		this.isSiteAdmin = isSiteAdmin;
	}

	/**
     * @see ICalipsoUserDetails#setRedirectUrl(java.lang.String)
     */
	@Override
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
     * @see ICalipsoUserDetails#getRedirectUrl()
     */
	@Override
	public String getRedirectUrl() {
		return this.redirectUrl;
	}



	/**
     * @see ICalipsoUserDetails#getMetadata()
     */
	@Override
	public Map<String, String> getMetadata() {
		return this.metadata;
	}

	/**
     * @see ICalipsoUserDetails#setMetadata(java.util.Map)
     */
	@Override
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
     * @see ICalipsoUserDetails#addMetadatum(java.lang.String, java.lang.String)
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
     * @see ICalipsoUserDetails#setUsername(java.lang.String)
     */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/**
     * @see ICalipsoUserDetails#setPassword(java.lang.String)
     */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * @see ICalipsoUserDetails#setAuthorities(List)
     */
	@Override
	public void setAuthorities(
			List<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	// SocialUserDetails

	/**
     * @see ICalipsoUserDetails#getAuthorities()
     */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
     * @see ICalipsoUserDetails#getPassword()
     */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
     * @see ICalipsoUserDetails#getUsername()
     */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
     * @see ICalipsoUserDetails#isAccountNonExpired()
     */
	@Override
	public boolean isAccountNonExpired() {
		return this.active;
	}

	/**
     * @see ICalipsoUserDetails#isAccountNonLocked()
     */
	@Override
	public boolean isAccountNonLocked() {
		return this.active;
	}

	/**
     * @see ICalipsoUserDetails#isCredentialsNonExpired()
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.active;
	}

	/**
     * @see ICalipsoUserDetails#isEnabled()
     */
	@Override
	public boolean isEnabled() {
		return BooleanUtils.isTrue(this.active);
	}

	/**
     * @see ICalipsoUserDetails#getUserId()
     */
	@Override
	public String getUserId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.getUsername();
	}

}
