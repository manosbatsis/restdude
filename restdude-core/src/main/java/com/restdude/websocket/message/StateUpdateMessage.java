package com.restdude.websocket.message;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * A message about modifications of a {@link IMessageResource}
 * 
 * @param <S> the message subject type
 */
@JsonPropertyOrder({ "@class", "id", "name" })
public class StateUpdateMessage<ID extends Serializable> implements Serializable{

	private static final long serialVersionUID = 1L;

	private ID id;
	
	@JsonProperty("@class")
	private String resourceClass;
	
	private Map<String, Serializable> modifications;
	
	public StateUpdateMessage(){
		
	}
	
	@Override
	public String toString() {
		return "StateUpdateMessage [id=" + id + ", resourceClass=" + resourceClass + ", modifications=" + modifications + "]";
	}

	public ID getId(){
		return this.id;
	}

	public void setId(ID id){
		this.id = id;
	}
	
	public String getResourceClass(){
		return this.resourceClass;
	}

	public void setResourceClass(String resourceClass){
		this.resourceClass = resourceClass;
	}

	@JsonAnyGetter
	public Map<String, Serializable> getModifications(){
		return this.modifications;
	}
	
	@JsonAnySetter
	public void addModification(String key, Serializable value){
		if(this.modifications == null){
			this.modifications = new HashMap<String, Serializable>();
		}
		this.modifications.put(key, value);
	}
	
	public void setModifications(Map<String, Serializable> modifications){
		this.modifications = modifications;
	}
	
}
