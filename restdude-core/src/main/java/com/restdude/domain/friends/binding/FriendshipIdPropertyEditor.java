package com.restdude.domain.friends.binding;

import com.restdude.domain.friends.model.FriendshipId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

public class FriendshipIdPropertyEditor extends PropertyEditorSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipIdPropertyEditor.class);
	
    @Override
	public void setAsText(String stringRepresentation) {
        this.setValue(new FriendshipId(stringRepresentation));
	}

    @Override
    public String getAsText() {
    	FriendshipId c = (FriendshipId) this.getValue();
        return c != null ? c.toStringRepresentation() : null;
    }
}
