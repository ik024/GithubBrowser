package com.ik.githubbrowser.repository.db.entity.events;

/**
 * Created by Ismail.Khan2 on 10/10/2017.
 */

public class Payload {
    private Forkee forkee;

    public Payload(){}

    public Forkee getForkee() {
        return forkee;
    }

    public void setForkee(Forkee forkee) {
        this.forkee = forkee;
    }
}
