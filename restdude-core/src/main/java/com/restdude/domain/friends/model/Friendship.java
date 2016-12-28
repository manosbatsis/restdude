package com.restdude.domain.friends.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.auth.spel.annotations.PreAuthorizeDelete;
import com.restdude.domain.base.model.AbstractPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A model representing a directional connection between two users. 
 * 
 */
@Entity
@Table(name = "friendships")
@ModelResource(path = Friendship.API_PATH, apiName = "Friendships", apiDescription = "Operations about friendships")
@ApiModel(value = "Friendship", description = "A model representing a directional connection between two users. ")
@PreAuthorizeDelete(controller = " hasRole('ROLE_USER') ", service = " hasRole('ROLE_USER') ")
public class Friendship extends AbstractPersistable<FriendshipId> implements CalipsoPersistable<FriendshipId> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Friendship.class);
	private static final long serialVersionUID = 1L;

	public static final String API_PATH = "friendships";
	
	@NotNull
	@ApiModelProperty(required = true)
	@EmbeddedId
	private FriendshipId id;
	
	@ApiModelProperty(required = true, allowableValues = "NEW, CONFIRMED, BLOCK, DELETE")
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "current_status", nullable = false)
	private FriendshipStatus status = FriendshipStatus.SENT;

	@JsonIgnore
	@Formula(" (current_status) ")
	private String previousStatus;

	public Friendship() {
	}

	public Friendship(FriendshipId id) {
		this.id = id;
	}
	
	public Friendship(FriendshipStatus status) {
		this.status = status;
	}
	
	public Friendship(FriendshipId id, FriendshipStatus status) {
		this(id);
		this.status = status;
	}

	public Friendship(User sender, User recipient) {

		this.id = new FriendshipId();
		this.id.setLeft(sender);
		this.id.setRight(recipient);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.getId())
			.append("status", this.getStatus())
			.append("new", this.isNew())
			.toString();
	}

	@JsonIgnore
	public FriendshipId getInverseId() {
		FriendshipId inverse = null;
		FriendshipId thisId = this.getId();
		if (thisId != null) {
			inverse = new FriendshipId();
			inverse.setLeft(thisId.getRight());
			inverse.setRight(thisId.getLeft());
		}
		return inverse;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return this.getPreviousStatus() != null;
	}

	/**
	 * @return the id
	 */
	public FriendshipId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(FriendshipId id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public FriendshipStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(FriendshipStatus status) {
		this.status = status;
	}

	/**
	 * @return the previousStatus
	 */
	public String getPreviousStatus() {
		return previousStatus;
	}

	/**
	 * @param previousStatus the previousStatus to set
	 */
	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}


	/**
	 * {@inheritDoc}
	 */
	public void preSave() {

	}

	
}