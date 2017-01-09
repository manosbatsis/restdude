/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-util, https://manosbatsis.github.io/restdude/restdude-util
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
