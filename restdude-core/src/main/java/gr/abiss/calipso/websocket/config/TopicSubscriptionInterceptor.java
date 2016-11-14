package gr.abiss.calipso.websocket.config;

import com.restdude.domain.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//TODO. setup for secirity in WebSocketConfig
public class TopicSubscriptionInterceptor extends ChannelInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopicSubscriptionInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) headerAccessor.getHeader("simpUser");

        	LOGGER.debug("preSend, userToken: {}", userToken);
            if(!validateSubscription((User)userToken.getPrincipal(), headerAccessor.getDestination()))
            {
                throw new IllegalArgumentException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscription(User principal, String topicDestination)
    {
        LOGGER.debug("Validate subscription for {} to topic {}", principal, topicDestination);
        //Validation logic coming here
        return true;
    }
}