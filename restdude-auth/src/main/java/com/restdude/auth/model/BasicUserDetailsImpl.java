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
package com.restdude.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.mdd.model.Roles;
import com.restdude.mdd.model.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@ApiModel
@XmlRootElement(name = "loggedInUserDetails")
public class BasicUserDetailsImpl implements UserDetails {


	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicUserDetailsImpl.class);



	private String pk;

	private String username;

	@JsonIgnore
	private String password;

	private LocalDateTime lastPassWordChangeDate;

	private String emailHash;

	private String firstName;
	private String lastName;
	private String name;

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

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String inactivationReason;

	private LocalDateTime inactivationDate;

	private Boolean admin = null;
	private Boolean siteAdmin = null;


	@JsonProperty(value = "roles", access = JsonProperty.Access.READ_ONLY)
	private List<? extends GrantedAuthority> authorities;
	private Map<String, String> metadata;

	private Boolean isResetPasswordReguest = false;

	/**
	 * Default constructor
	 */
	public BasicUserDetailsImpl() {

	}

	public BasicUserDetailsImpl(LoginRequest loginSubmission) {
		this();
		BeanUtils.copyProperties(loginSubmission, this);
		if (this.getUsername() == null) {
			this.setUsername(loginSubmission.getEmail());
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("pk", pk)
			.append("username", username)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("active", active)
			.append("metadata", metadata)
			.append("authorities", authorities)
			.toString();
	}

	/**
	 * @see UserDetails#getPk()
	 */
	@Override
	public String getPk() {
		return pk;
	}

	/**
	 * @see UserDetails#setPk(String)
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
     * @see UserDetails#getFirstName()
     */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
     * @see UserDetails#setFirstName(String)
     */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
     * @see UserDetails#getLastName()
     */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
     * @see UserDetails#setLastName(String)
     */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @see UserDetails#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see UserDetails#setName(String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
     * @see UserDetails#getLastPassWordChangeDate()
     */
	@Override
	public LocalDateTime getLastPassWordChangeDate() {
		return lastPassWordChangeDate;
	}

	/**
	 * @see UserDetails#setLastPassWordChangeDate(LocalDateTime)
	 */
	@Override
	public void setLastPassWordChangeDate(LocalDateTime lastPassWordChangeDate) {
		this.lastPassWordChangeDate = lastPassWordChangeDate;
	}

	/**
     * @see UserDetails#getEmailHash()
     */
	@Override
	public String getEmailHash() {
		return emailHash;
	}

	/**
     * @see UserDetails#setEmailHash(String)
     */
	@Override
	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	/**
     * @see UserDetails#getAvatarUrl()
     */
	@Override
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
     * @see UserDetails#setAvatarUrl(String)
     */
	@Override
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}


	/**
     * @see UserDetails#getTelephone()
     */
	@Override
	public String getTelephone() {
		return telephone;
	}

	/**
     * @see UserDetails#setTelephone(String)
     */
	@Override
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
     * @see UserDetails#getCellphone()
     */
	@Override
	public String getCellphone() {
		return cellphone;
	}

	/**
     * @see UserDetails#setCellphone(String)
     */
	@Override
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
     * @see UserDetails#getBirthDay()
     */
	@Override
	public LocalDate getBirthDay() {
		return birthDay;
	}

	/**
	 * @see UserDetails#setBirthDay(LocalDate)
	 */
	@Override
	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}

	/**
     * @see UserDetails#getLastVisit()
     */
	@Override
	public LocalDateTime getLastVisit() {
		return lastVisit;
	}

	/**
	 * @see UserDetails#setLastVisit(LocalDateTime)
	 */
	@Override
	public void setLastVisit(LocalDateTime lastVisit) {
		this.lastVisit = lastVisit;
	}

	/**
     * @see UserDetails#getLastPost()
     */
	@Override
	public LocalDateTime getLastPost() {
		return lastPost;
	}

	/**
	 * @see UserDetails#setLastPost(LocalDateTime)
	 */
	@Override
	public void setLastPost(LocalDateTime lastPost) {
		this.lastPost = lastPost;
	}

	/**
     * @see UserDetails#getLoginAttempts()
     */
	@Override
	public Short getLoginAttempts() {
		return loginAttempts;
	}

	/**
     * @see UserDetails#setLoginAttempts(Short)
     */
	@Override
	public void setLoginAttempts(Short loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	/**
     * @see UserDetails#getActive()
     */
	@Override
	public Boolean getActive() {
		return active;
	}

	/**
     * @see UserDetails#setActive(Boolean)
     */
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
     * @see UserDetails#getInactivationReason()
     */
	@Override
	public String getInactivationReason() {
		return inactivationReason;
	}

	/**
     * @see UserDetails#setInactivationReason(String)
     */
	@Override
	public void setInactivationReason(String inactivationReason) {
		this.inactivationReason = inactivationReason;
	}

	/**
     * @see UserDetails#getInactivationDate()
     */
	@Override
	public LocalDateTime getInactivationDate() {
		return inactivationDate;
	}

	/**
	 * @see UserDetails#setInactivationDate(LocalDateTime)
	 */
	@Override
	public void setInactivationDate(LocalDateTime inactivationDate) {
		this.inactivationDate = inactivationDate;
	}

	/**
     * @see UserDetails#getLocale()
     */
	@Override
	public String getLocale() {
		return locale;
	}

	/**
     * @see UserDetails#setLocale(String)
     */
	@Override
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
     * @see UserDetails#getDateFormat()
     */
	@Override
	public String getDateFormat() {
		return dateFormat;
	}

	/**
     * @see UserDetails#setDateFormat(String)
     */
	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
     * @see UserDetails#isAdmin()
     */
	@Override
	public boolean isAdmin(){
		if(this.admin == null){
			this.admin = false;
			if(CollectionUtils.isNotEmpty(this.getAuthorities())){
				for(GrantedAuthority authority : authorities){
					if (authority.getAuthority().equals(Roles.ROLE_ADMIN)) {
						this.admin = true;
						break;
					}
				}
			}
		}
		return this.admin;
	}

	/**
     * @see UserDetails#setAdmin(boolean)
     */
	@Override
	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;

	}

	/**
     * @see UserDetails#isSiteAdmin()
     */
	@Override
	public boolean isSiteAdmin() {
		if(this.siteAdmin == null){
			this.siteAdmin = false;
			if(CollectionUtils.isNotEmpty(this.getAuthorities())){
				for(GrantedAuthority authority : authorities){
					if (authority.getAuthority().equals(Roles.ROLE_SITE_OPERATOR)) {
						this.siteAdmin = true;
						break;
					}
				}
			}
		}
		return this.siteAdmin;
	}

	/**
     * @see UserDetails#setSiteAdmin(boolean)
     */
	@Override
	public void setSiteAdmin(boolean isSiteAdmin) {
		this.siteAdmin = isSiteAdmin;
	}

	/**
     * @see UserDetails#setRedirectUrl(String)
     */
	@Override
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
     * @see UserDetails#getRedirectUrl()
     */
	@Override
	public String getRedirectUrl() {
		return this.redirectUrl;
	}



	/**
     * @see UserDetails#getMetadata()
     */
	@Override
	public Map<String, String> getMetadata() {
		return this.metadata;
	}

	/**
     * @see UserDetails#setMetadata(Map)
     */
	@Override
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/**
     * @see UserDetails#addMetadatum(String, String)
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
     * @see UserDetails#setUsername(String)
     */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/**
     * @see UserDetails#setPassword(String)
     */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * @see UserDetails#setAuthorities(List)
     */
	@Override
	public void setAuthorities(
			List<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	// SocialUserDetails

	/**
     * @see UserDetails#getAuthorities()
     */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
     * @see UserDetails#getPassword()
     */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
     * @see UserDetails#getUsername()
     */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
     * @see UserDetails#isAccountNonExpired()
     */
	@Override
	public boolean isAccountNonExpired() {
		return this.active;
	}

	/**
     * @see UserDetails#isAccountNonLocked()
     */
	@Override
	public boolean isAccountNonLocked() {
		return this.active;
	}

	/**
     * @see UserDetails#isCredentialsNonExpired()
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.active;
	}

	/**
     * @see UserDetails#isEnabled()
     */
	@Override
	public boolean isEnabled() {
		return BooleanUtils.isTrue(this.active);
	}

	/**
     * @see UserDetails#getUserId()
     */
	@Override
	public String getUserId() {
		return this.pk;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
	}


}
