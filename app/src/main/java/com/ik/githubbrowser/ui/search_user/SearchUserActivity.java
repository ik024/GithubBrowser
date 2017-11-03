package com.ik.githubbrowser.ui.search_user;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ik.githubbrowser.BaseActivity;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.ui.home.HomeActivity;
import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class SearchUserActivity extends BaseActivity implements LifecycleRegistryOwner {

    @BindView(R.id.et_username)
    EditText etUserName;

    @BindView(R.id.bt_login)
    Button btLogin;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_error)
    TextView tvError;

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private NetworkInstance networkInstance;
    private SearchUserViewModel viewModel;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        networkInstance = NetworkInstance.getInstance(getApplicationContext());

        ApiService apiService = networkInstance.getApiService();

        RepositoryImpl repository = new RepositoryImpl(apiService);
        SearchViewModelFactory factory = new SearchViewModelFactory(repository);

        viewModel = ViewModelProviders.of(this, factory).get(SearchUserViewModel.class);

        btLogin.setOnClickListener(v -> {
            username = etUserName.getText().toString();
            tvError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.doesUserExists(username)
                    .observe(SearchUserActivity.this, this::navigateToHomeIfUserExists);
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    private void navigateToHomeIfUserExists(boolean userExists) {
        Timber.i("test-navigateToHomeIfUserExists: "+userExists);
        progressBar.setVisibility(View.GONE);

        if (userExists) {
            HomeActivity.start(SearchUserActivity.this, username);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }

}
