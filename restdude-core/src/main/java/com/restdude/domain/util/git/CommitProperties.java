/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
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
public class CommitProperties {
 
    private final String describe;
    private final String describeShort;
    private final String fullMessage;
    private final String id;
    private final String idAbbrev;
    private final String shortMessage;
    private final String time;
    private final String userEmail;
    private final String userName;
 
    @Autowired
    public CommitProperties(@Value("${git.commit.id.describe}") String describe,
                            @Value("${git.commit.id.describe-short}") String describeShort,
                            @Value("${git.commit.message.full}") String fullMessage,
                            @Value("${git.commit.id}") String id,
                            @Value("${git.commit.id.abbrev}") String idAbbrev,
                            @Value("${git.commit.message.short}") String shortMessage,
                            @Value("${git.commit.time}") String time,
                            @Value("${git.commit.user.email}") String userEmail,
                            @Value("${git.commit.user.name}") String userName) {
        this.describe = describe;
        this.describeShort = describeShort;
        this.fullMessage = fullMessage;
        this.id = id;
        this.idAbbrev = idAbbrev;
        this.shortMessage = shortMessage;
        this.time = time;
        this.userEmail = userEmail;
        this.userName = userName;
    }
 
    //Getters are omitted for the sake of clarity
}