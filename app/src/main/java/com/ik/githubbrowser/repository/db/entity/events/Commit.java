package com.ik.githubbrowser.repository.db.entity.events;

/**
 * Created by ismailkhan on 25/11/17.
 */

public class Commit {
    String sha;
    String message;
    String url;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
