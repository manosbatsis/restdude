package gr.abiss.calipso.websocket.config;

import com.restdude.auth.userdetails.util.SecurityUtil;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
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
//@ComponentScan(basePackages = "**.calipso.controller")
@EnableScheduling
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
		implements WebSocketMessageBrokerConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

	/**
	 * Registers the "/ws" endpoint, enabling SockJS fallback options so that alternative 
	 * messaging options may be used if WebSocket is not available. 
	 *
     * This endpoint, when prefixed with "/domain", is the endpoint that the
     * controller methods are mapped to handle.
	 * 
	 * @see org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer#registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry)
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
	 *
     * The "/domain" prefix is designated for messages that are bound for @MessageMapping-annotated methods.
     *
	 * @see org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer#configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry)
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {

		messageBrokerRegistry.enableSimpleBroker("/queue", "/topic").setTaskScheduler(heartbeatTaskScheduler());
        messageBrokerRegistry.setApplicationDestinationPrefixes("/domain");
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

		return new CalipsoHandshakeHandler(new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
	}
	
	@Bean
    ThreadPoolTaskScheduler heartbeatTaskScheduler() {
        return new ThreadPoolTaskScheduler();
	}
	
	public static class CalipsoHandshakeHandler extends DefaultHandshakeHandler{

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