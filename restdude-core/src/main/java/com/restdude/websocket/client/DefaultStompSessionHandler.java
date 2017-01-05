/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
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
package com.restdude.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class DefaultStompSessionHandler implements StompSessionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStompSessionHandler.class);
	
	private String username;
	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        LOGGER.info("afterConnected, user: " +this.username +", session: " + session + ". connectedHeaders: " + connectedHeaders);
        this.username = connectedHeaders.getFirst("user-name");
	}

	/**
	 * This implementation returns String as the expected payload type
	 * for STOMP ERROR frames.
	 */
	@Override
	public Type getPayloadType(StompHeaders headers) {
        LOGGER.info("getPayloadType: headers: " + headers);
		return String.class;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
        LOGGER.info("handleFrame, user: " +this.username +", headers: " + headers + ", payload: " + payload);
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers,
			byte[] payload, Throwable exception) {

        LOGGER.error("handleException, user: " +this.username +", session: " + session + ", command: " + command + ", headers: " + headers + ", payload: " + payload, exception);
        throw new RuntimeException(exception);
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
        LOGGER.error("Transport ERROR, user: " +this.username +",  session: " + session.getSessionId(), exception);
        throw new RuntimeException(exception);
	}

}