package com.restdude.websocket.actuate;


import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * WebSocket message mappings {@link Endpoint}
 */
@ConfigurationProperties(prefix = "endpoints.websocket-mappings", ignoreUnknownFields = true)
public class MessageMappingEndPoint extends AbstractEndpoint<Map<String, Object>> 
		implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	
	public MessageMappingEndPoint() {
		super("websocketmappings");
	}

	@Override
	public Map<String, Object> invoke() {
		SimpAnnotationMethodMessageHandler handler = applicationContext
				.getBean(SimpAnnotationMethodMessageHandler.class);

		Map<SimpMessageMappingInfo, HandlerMethod> messageHandlerMethods = 
				this.filterByAnnotation(handler.getHandlerMethods(), MessageMapping.class);
		Map<SimpMessageMappingInfo, HandlerMethod> subscribeHandlerMethods = 
				this.filterByAnnotation(handler.getHandlerMethods(), SubscribeMapping.class);

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("prefixes", handler.getDestinationPrefixes());
		info.put("messageMappings",extractMapping(messageHandlerMethods));
		info.put("subscribeMappings", extractMapping(subscribeHandlerMethods));

		return info;
	}

	private Map<String, Object> extractMapping(
			Map<SimpMessageMappingInfo, HandlerMethod> handlerMethods) {
		
		Map<String, Object> mappings = new HashMap<String, Object>();

		for (SimpMessageMappingInfo info : handlerMethods.keySet()) {
			HandlerMethod handlerMethod = handlerMethods.get(info);

			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("bean", handlerMethod.getBean().toString());
			map.put("method", handlerMethod.toString());

			mappings.put(info.getDestinationConditions().toString(), map);
		}

		return mappings;
	}

	private Map<SimpMessageMappingInfo, HandlerMethod> filterByAnnotation(
			Map<SimpMessageMappingInfo, HandlerMethod> handlerMethods, Class<? extends Annotation> annotation) {
		
		Map<SimpMessageMappingInfo, HandlerMethod> filteredHandlerMethods = 
				new LinkedHashMap<SimpMessageMappingInfo, HandlerMethod>();

		for (SimpMessageMappingInfo info : handlerMethods.keySet()) {
			HandlerMethod handlerMethod = handlerMethods.get(info);

			if (handlerMethod.getMethodAnnotation(annotation) != null) {
				filteredHandlerMethods.put(info, handlerMethod);
			}
		}

		return filteredHandlerMethods;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}