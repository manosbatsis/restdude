package com.restdude.websocket.model;

import com.restdude.domain.base.model.AbstractAssignedidPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Persistent class corresponding to a websocket STOMP session. The entities are only 
 */
@Entity
@Table(name = "stomp_session")
@ModelResource(path = StompSession.API_PATH, apiName = "STOMP Sessions", apiDescription = "STOMP Session Operations")
@ApiModel(value = "STOMP Session", description = "A model representing a websocket STOMP session")
public class StompSession extends AbstractAssignedidPersistable<String> {

	private static final long serialVersionUID = 1L;

	public static final String API_PATH = "stompSessions";

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
	private User user;

	public StompSession() {
		super();
	}

	public StompSession(String id) {
		this();
		this.setId(id);
	}

	
	@Override
	public String toString() {
		return "StompSession [id=" + this.getId() + ", user=" + user + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static class Builder {
		private String id;
		private User user;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder user(User user) {
			this.user = user;
			return this;
		}

		public StompSession build() {
			return new StompSession(this);
		}
	}

	private StompSession(Builder builder) {
		this.setId(builder.id);
		this.user = builder.user;
	}
}
