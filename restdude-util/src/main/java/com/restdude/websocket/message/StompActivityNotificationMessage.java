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
package com.restdude.websocket.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Convenient generic base type for "subject > predicate > object"-style messages. 
 * The semantics are generic enough to denote events like user > posted > comment or user > status > active
 *
 * @param <S> the message subject type
 * @param <S> the message object type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StompActivityNotificationMessage<S extends IMessageResource<?>, O extends IMessageResource<?>>
        implements IActivityNotificationMessage<S, String, O> {

    private S subject;
    private String predicate;
    private O object;


}
