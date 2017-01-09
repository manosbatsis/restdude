/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-framework, https://manosbatsis.github.io/restdude/restdude-framework
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