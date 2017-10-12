package com.ik.githubbrowser.repository;


import com.ik.githubbrowser.repository.db.entity.RepoItem;
import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.commits.CommitList;
import com.ik.githubbrowser.repository.db.entity.commits.CommitObject;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;
import com.ik.githubbrowser.repository.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class RepositoryImpl implements Repository{

    private ApiService apiService;

    public RepositoryImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public Observable<UserInfo> getUserInfo(String userName) {
        //TODO: check in local db
        return apiService.getUserInfo(userName);

    }

    @Override
    public Observable<List<Event>> getUserEvents(String userName) {
        //TODO: check in local db

        return apiService.getUserEvents(userName);
    }

    @Override
    public Observable<List<RepoItem>> getUserRepos(String userName) {
        //TODO: check in local db
        return apiService.getUserRepos(userName)
                .flatMap(repos -> {
                    List<RepoItem> repoItemList = new ArrayList<>();
                    for (Repo repo : repos) {
                        RepoItem repoItem = new RepoItem();
                        String repoName = repo.getName();
                        String repoDesc = repo.getDescription();

                        List<CommitObject> commits = apiService.getRepoCommits(userName, repoName).execute().body();
                        repoItem.setRepoName(repoName);
                        repoItem.setRepoDesc(repoDesc == null ? "No description" : repoDesc);
                        repoItem.setCommitCount(commits == null ? 0 : commits.size());
                        //TODO: get issues
                        repoItem.setIssueCount(0);
                        repoItemList.add(repoItem);
                    }
                    return Observable.just(repoItemList);
                });
    }


}
