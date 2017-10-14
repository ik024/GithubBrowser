package com.ik.githubbrowser.repository.network;

import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.commits.CommitList;
import com.ik.githubbrowser.repository.db.entity.commits.CommitObject;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;
import com.ik.githubbrowser.repository.db.entity.followings.Following;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("users/{userName}")
    Observable<UserInfo> getUserInfo(@Path("userName") String userName);

    @GET("users/{userName}/events")
    Observable<List<Event>> getUserEvents(@Path("userName") String userName);

    @GET("users/{userName}/repos")
    Observable<List<Repo>> getUserRepos(@Path("userName") String userName);

    @GET("repos/{userName}/{repoName}/commits")
    Call<List<CommitObject>> getRepoCommits(@Path("userName") String userName, @Path("repoName") String repoName);

    @GET("users/{userName}/followers")
    Observable<List<Follower>> getUserFollowers(@Path("userName") String userName);

    @GET("users/{userName}/following")
    Observable<List<Following>> getUserFollowing(@Path("userName") String userName);
}
