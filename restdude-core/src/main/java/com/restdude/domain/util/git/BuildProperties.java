package com.restdude.domain.util.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@Component
public class BuildProperties {
 
    private final String time;
    private final String userEmail;
    private final String userName;
 
    @Autowired
    public BuildProperties(@Value("${git.build.time}") String time,
                           @Value("${git.build.user.email}") String userEmail,
                           @Value("${git.build.user.name}") String userName) {
        this.time = time;
        this.userEmail = userEmail;
        this.userName = userName;
    }

	public String getTime() {
		return time;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserName() {
		return userName;
	}
     
    //Getters are omitted for the sake of clarity
}