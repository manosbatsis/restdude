/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-framework, https://manosbatsis.github.io/restdude/restdude-framework
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.util.git;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@Component
public class GitProperties {

    private String branch;
    private final BuildProperties build;
    private final CommitProperties commit;
    private final boolean dirty;
    private final String remoteOriginUrl;
    private final String tags;

    @Autowired
    public GitProperties(@Value("${git.branch}") String branch,
                         BuildProperties build,
                         CommitProperties commit,
                         @Value("${git.dirty}") boolean dirty,
                         @Value("${git.remote.origin.url}") String remoteOriginUrl,
                         @Value("${git.tags}") String tags) {
        this.branch = branch;
        this.build = build;
        this.commit = commit;
        this.dirty = dirty;
        this.remoteOriginUrl = remoteOriginUrl;
        this.tags = tags;
    }

    //Getters are omitted for the sake of clarity
}