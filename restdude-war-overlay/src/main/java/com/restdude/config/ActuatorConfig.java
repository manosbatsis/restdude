/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-war-overlay, https://manosbatsis.github.io/restdude/restdude-war-overlay
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
package com.restdude.config;


import com.restdude.websocket.actuate.MessageMappingEndPoint;
import com.restdude.websocket.actuate.WebSocketEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.boot.actuate.endpoint.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.InfoEndpoint;
import org.springframework.boot.actuate.endpoint.RequestMappingEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import java.util.Collection;

/**
 * Actuator config. Endpoint paths are set in restdude.(defaults.)properties (see endpoints.actuator.*).
 */
@Configuration
@Import({EndpointAutoConfiguration.class, EndpointWebMvcAutoConfiguration.class,
        ManagementServerPropertiesAutoConfiguration.class, EndpointAutoConfiguration.class,
        HealthIndicatorAutoConfiguration.class, PublicMetricsAutoConfiguration.class})
public class ActuatorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorConfig.class);

    @Bean
    @Autowired
    // Define the HandlerMapping similar to RequestHandlerMapping to expose the
    // endpoint
    public EndpointHandlerMapping endpointHandlerMapping(Collection<? extends MvcEndpoint> endpoints) {
        return new EndpointHandlerMapping(endpoints);
    }

    @Bean
    @Autowired
    // define the HealthPoint endpoint
    public HealthMvcEndpoint healthMvcEndpoint(HealthEndpoint delegate) {
        return new HealthMvcEndpoint(delegate, false);
    }

    @Bean
    @Autowired
    // define the Info endpoint
    public EndpointMvcAdapter infoMvcEndPoint(InfoEndpoint delegate) {
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Autowired
    // define the beans endpoint
    public EndpointMvcAdapter beansEndPoint(BeansEndpoint delegate) {
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Autowired
    // define the mappings endpoint
    public EndpointMvcAdapter requestMappingEndPoint(RequestMappingEndpoint delegate) {
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Description("Spring Actuator endpoint to expose WebSocket stats")
    public WebSocketEndPoint websocketEndpoint(WebSocketMessageBrokerStats stats) {
        return new WebSocketEndPoint(stats);
    }

    @Bean
    @Description("Spring Actuator endpoint to expose WebSocket message mappings")
    public MessageMappingEndPoint messageMappingEndpoint() {
        return new MessageMappingEndPoint();
    }
}