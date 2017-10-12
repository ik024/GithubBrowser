package com.ik.githubbrowser.repository.db.entity.commits;

import java.util.List;

public class  CommitList {

    private List<CommitObject> commitObjectList;


    public List<CommitObject> getCommitObjectList() {
        return commitObjectList;
    }

    public void setCommitObjectList(List<CommitObject> commitObjectList) {
        this.commitObjectList = commitObjectList;
    }
}
