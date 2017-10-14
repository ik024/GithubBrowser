package com.ik.githubbrowser.repository;

import com.ik.githubbrowser.repository.db.entity.RepoItem;
import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;
import com.ik.githubbrowser.repository.db.entity.followings.Following;

import java.util.List;

import io.reactivex.Observable;


public interface Repository {

    Observable<UserInfo> getUserInfo(String userName);
    Observable<List<Event>> getUserEvents(String userName);
    Observable<List<RepoItem>> getUserRepos(String userName);
    Observable<List<Follower>> getUserFollowers(String userName);
    Observable<List<Following>> getUserFollowings(String userName);
}
