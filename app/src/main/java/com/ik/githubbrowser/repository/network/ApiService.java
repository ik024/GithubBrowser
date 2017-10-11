package com.ik.githubbrowser.repository.network;

import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("users/{userName}")
    Observable<UserInfo> getUserInfo(@Path("userName") String userName);

    @GET("users/{userName}/events")
    Observable<List<Event>> getUserEvents(@Path("userName") String userName);

    @GET("users/{userName}/repos")
    Observable<List<Repo>> getUserRepos(@Path("userName") String userName);
}
