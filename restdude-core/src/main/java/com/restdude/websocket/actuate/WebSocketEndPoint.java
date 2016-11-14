package com.restdude.websocket.actuate;


import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

/**
 * WebSocket stats {@link Endpoint} 
 *
 */
@ConfigurationProperties(prefix = "endpoints.websocket", ignoreUnknownFields = true)
public class WebSocketEndPoint extends AbstractEndpoint<WebSocketMessageBrokerStats> {

	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
	
	public WebSocketEndPoint(WebSocketMessageBrokerStats webSocketMessageBrokerStats) {
		super("websocketstats");
		this.webSocketMessageBrokerStats = webSocketMessageBrokerStats;
	}

	@Override
	public WebSocketMessageBrokerStats invoke() {
		return webSocketMessageBrokerStats;
	}
}