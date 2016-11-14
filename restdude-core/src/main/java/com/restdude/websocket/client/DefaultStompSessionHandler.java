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