package com.restdude.websocket.message;

/**
 * Convenient generic base type for "subject > predicate > object"-style messages. 
 * The semantics are generic enough to denote events like user > posted > comment or user > status > active
 *
 * @param <S> the message subject type
 * @param <P> the predicate type
 * @param <S> the message object type
 */
public class ActivityNotificationMessage<S extends IMessageResource<?>, P extends Enum<P>, O extends IMessageResource<?>> 
	 implements IActivityNotificationMessage<S, P, O> {

	private S subject;
	
	private P predicate;

	private O object;

	public ActivityNotificationMessage() {
		super();
	}
	
	public ActivityNotificationMessage(S subject, P predicate, O object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	@Override
	public S getSubject() {
		return subject;
	}

	@Override
	public void setSubject(S subject) {
		this.subject = subject;
	}
	
	@Override
	public P getPredicate() {
		return predicate;
	}

	@Override
	public void setPredicate(P predicate) {
		this.predicate = predicate;
	}

	@Override
	public O getObject() {
		return object;
	}

	@Override
	public void setObject(O object) {
		this.object = object;
	}
}
