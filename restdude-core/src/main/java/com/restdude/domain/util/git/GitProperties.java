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