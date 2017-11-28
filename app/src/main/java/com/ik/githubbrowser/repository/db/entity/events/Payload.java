package com.ik.githubbrowser.repository.db.entity.events;

import java.util.List;

/**
 * Created by Ismail.Khan2 on 10/10/2017.
 */

public class Payload {
    private Forkee forkee;
    private List<Commit> commits;

    public Payload(){}

    public Forkee getForkee() {
        return forkee;
    }

    public void setForkee(Forkee forkee) {
        this.forkee = forkee;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
