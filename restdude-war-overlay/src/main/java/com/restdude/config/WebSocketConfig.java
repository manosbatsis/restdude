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
package com.restdude.config;


import com.restdude.auth.userdetails.util.SecurityUtil;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
//@ComponentScan(basePackages = "**.restdude.controller")
@EnableScheduling
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
        implements WebSocketMessageBrokerConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    /**
     * Registers the "/ws" controller, enabling SockJS fallback options so that alternative
     * messaging options may be used if WebSocket is not available.
     * <p>
     * This controller, when prefixed with "/app", is the controller that the
     * controller methods are mapped to handle.
     *
     * @see WebSocketMessageBrokerConfigurer#registerStompEndpoints(StompEndpointRegistry)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws")
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOrigins("*"/*domain, originWithPort*/);
    }

    /**
     * Configure the message broker with eenableSimpleBroker(), to enablee a simple memory-based message broker
     * to carry messages back to the client on destinations prefixed with "/topic".
     * <p>
     * The "/app" prefix is designated for messages that are bound for @MessageMapping-annotated methods.
     *
     * @see AbstractWebSocketMessageBrokerConfigurer#configureMessageBroker(MessageBrokerRegistry)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {

        messageBrokerRegistry.enableSimpleBroker("/queue", "/topic").setTaskScheduler(heartbeatTaskScheduler());
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
//		messageBrokerRegistry.setUserDestinationPrefix("/user");
    }


    /**
     * For Jetty, we need to supply a pre-configured Jetty
     * WebSocketServerFactory and plug that into Spring’s
     * DefaultHandshakeHandler through your WebSocket Java config:
     *
     * @return
     */
    @Bean
    public HandshakeHandler handshakeHandler() {
        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setInputBufferSize(8192);
        policy.setIdleTimeout(600000);

        return new CalipsoHandshakeHandler(new JettyRequestUpgradeStrategy(policy));
    }

    @Bean
    ThreadPoolTaskScheduler heartbeatTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    public static class CalipsoHandshakeHandler extends DefaultHandshakeHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(CalipsoHandshakeHandler.class);

        public CalipsoHandshakeHandler(RequestUpgradeStrategy requestUpgradeStrategy) {
            super(requestUpgradeStrategy);
        }

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            Principal auth = SecurityUtil.getAuthentication();
//			LOGGER.debug("determineUser: {}", auth);
            return auth;
        }

    }

}