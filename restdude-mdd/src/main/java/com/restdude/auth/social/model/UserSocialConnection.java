/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.auth.social.model;

import com.restdude.domain.base.model.CalipsoPersistable;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "UserConnection")
public class UserSocialConnection implements CalipsoPersistable<UserSocialConnectionId> {

	@NotNull
	@ApiModelProperty(required = true)
	@EmbeddedId
	private UserSocialConnectionId pk;

	private Integer rank;

    @NotNull
    @Column(nullable = false)
	private String displayName;
    @NotNull
    @Column(length = 1000, nullable = false)
	private String profileUrl;
	private String imageUrl;
    @NotNull
    @Column(nullable = false)
	private String accessToken;
	private String secret;
	private String refreshToken;
	private Long expireTime;

	public UserSocialConnection(){
		
	}
	
	public UserSocialConnection(String userId, String providerId,
			String providerUserId, int rank, String displayName,
			String profileUrl, String imageUrl, String accessToken,
			String secret, String refreshToken, Long expireTime) {
		this.setPk(new UserSocialConnectionId(userId, providerId, providerUserId));
		this.setRank(rank);
		this.setDisplayName(displayName);
		this.setProfileUrl(profileUrl);
		this.setImageUrl(imageUrl);
		this.setAccessToken(accessToken);
		this.setSecret(secret);
		this.setRefreshToken(refreshToken);
		this.setExpireTime(expireTime);

	}

	public UserSocialConnectionId getPk() {
		return pk;
	}

	public void setPk(UserSocialConnectionId pk) {
		this.pk = pk;
	}

	@Override
	public void preSave() {

	}

	@Override
	public boolean isNew() {
		return true;
	}

	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

}
