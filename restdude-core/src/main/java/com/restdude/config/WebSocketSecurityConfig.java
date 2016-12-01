package com.restdude.config;

//@Configuration
public class WebSocketSecurityConfig {


//extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpMessageDestMatchers("/queue/**", "/topic/**").denyAll()
//                .simpSubscribeDestMatchers("/queue/**/*-user*", "/topic/**/*-user*").denyAll()
//                .anyMessage().authenticated();
//
//    }
//
//    /**
//     * Disables CSRF for Websockets.
//     */
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
}