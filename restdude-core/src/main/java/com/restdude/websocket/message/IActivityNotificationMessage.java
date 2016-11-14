package com.restdude.websocket.message;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Generic interface for "subject > predicate > object"-style messages. 
 * The semantics are generic enough to denote events like user > posted > comment or user > status > active
 *
 * @param <S> the message subject type
 * @param <P> the predicate type
 * @param <S> the message object type
 * @see ActivityNotificationMessage
 */
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.MINIMAL_CLASS,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "@class")
public interface IActivityNotificationMessage<S extends IMessageResource<?>, P extends Enum<P>, O extends IMessageResource<?>> {

	S getSubject();

	void setSubject(S subject);

	P getPredicate();

	void setPredicate(P predicate);

	O getObject();

	void setObject(O object);

}