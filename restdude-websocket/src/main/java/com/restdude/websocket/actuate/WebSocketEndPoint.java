/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-websocket, https://manosbatsis.github.io/restdude/restdude-websocket
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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