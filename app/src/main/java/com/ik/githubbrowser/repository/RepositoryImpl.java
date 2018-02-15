package com.ik.githubbrowser.repository;


import com.ik.githubbrowser.BuildConfig;
import com.ik.githubbrowser.repository.db.entity.RepoItem;
import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.commits.CommitList;
import com.ik.githubbrowser.repository.db.entity.commits.CommitObject;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;
import com.ik.githubbrowser.repository.db.entity.followings.Following;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;
import com.ik.githubbrowser.repository.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import timber.log.Timber;


public class RepositoryImpl implements Repository{

    private ApiService apiService;

    public RepositoryImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public Observable<UserInfo> getUserInfo(String userName) {
        //TODO: check in local db
        return apiService.getUserInfo(userName, BuildConfig.ACCESS_TOKEN);

    }

    @Override
    public Observable<List<Event>> getUserEvents(String userName) {
        //TODO: check in local db

        return apiService.getUserEvents(userName, BuildConfig.ACCESS_TOKEN);
    }

    @Override
    public Observable<List<RepoItem>> getUserRepos(String userName) {
        //TODO: check in local db
        return apiService.getUserRepos(userName, BuildConfig.ACCESS_TOKEN)
                .flatMap(repos -> {
                    List<RepoItem> repoItemList = new ArrayList<>();
                    return Observable.create(e -> {
                        for (Repo repo : repos) {
                            RepoItem repoItem = new RepoItem();
                            String repoName = repo.getName();
                            String repoDesc = repo.getDescription();

                            List<CommitObject> commits = apiService.getRepoCommits(userName, repoName, BuildConfig.ACCESS_TOKEN).execute().body();
                            repoItem.setRepoName(repoName);
                            repoItem.setRepoDesc(repoDesc == null ? "No description" : repoDesc);
                            repoItem.setCommitCount(commits == null ? 0 : commits.size());
                            //TODO: get issues
                            repoItem.setIssueCount(0);
                            repoItemList.add(repoItem);
                            e.onNext(repoItemList);
                        }
                        e.onComplete();
                    });
                });
    }

    @Override
    public Observable<List<Follower>> getUserFollowers(String userName) {
        return apiService.getUserFollowers(userName, BuildConfig.ACCESS_TOKEN);
    }

    @Override
    public Observable<List<Following>> getUserFollowings(String userName) {
        return apiService.getUserFollowing(userName, BuildConfig.ACCESS_TOKEN);
    }


}
