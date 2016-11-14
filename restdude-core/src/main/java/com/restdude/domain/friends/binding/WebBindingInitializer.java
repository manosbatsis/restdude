package com.restdude.domain.friends.binding;

import com.restdude.domain.friends.model.FriendshipId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class WebBindingInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebBindingInitializer.class);
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FriendshipId.class, new FriendshipIdPropertyEditor());
    }
}
