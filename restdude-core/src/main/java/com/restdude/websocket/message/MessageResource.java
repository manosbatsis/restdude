package com.restdude.websocket.message;

import java.io.Serializable;

/**
 * Base class for subject and object parts of a {@link IActivityNotificationMessage}
 */
public class MessageResource<ID extends Serializable> implements IMessageResource<ID> {

	/**
	 * The resource ID
	 */
	protected ID id;
	
	/**
	 * The resource human-readable name
	 */
	protected String name;

	public MessageResource() {
		super();
	}

	public MessageResource(ID id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
