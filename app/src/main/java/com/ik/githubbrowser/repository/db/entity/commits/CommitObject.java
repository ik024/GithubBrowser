package com.ik.githubbrowser.repository.db.entity.commits;

public class CommitObject {

    private Commit commit;
    private String url;

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
