package com.ik.githubbrowser.ui.home.repos;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ik.githubbrowser.BaseActivity;
import com.ik.githubbrowser.MyRecyclerView;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.db.entity.RepoItem;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.EmptyLayout;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;


public class ReposFragment extends Fragment implements LifecycleOwner, RecyclerViewItemClickListener {

    @BindView(R.id.rv_repos)
    MyRecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.layout_empty_view)
    View mEmptyView;

    private EmptyLayout mIncludedEmptyLayout = new EmptyLayout();

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private FragmentInteraction mListener;

    private ReposFragmentViewModel mViewModel;
    private RepoItemAdapter mAdapter;
    private BaseActivity mActivity;
    private String username;

    public ReposFragment() {
        // Required empty public constructor
    }

    public static ReposFragment newInstance(String username) {
        ReposFragment fragment = new ReposFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(KEY_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repos, container, false);
        ButterKnife.bind(this, view);

        ButterKnife.bind(mIncludedEmptyLayout, mEmptyView);
        mIncludedEmptyLayout.tvEmptyView.setText(R.string.empty_repo_list);

        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mActivity = (BaseActivity) getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        ReposFragmentViewModelFactory factory = new ReposFragmentViewModelFactory(repository, username);
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ReposFragmentViewModel.class);

        mProgressBar.setVisibility(View.VISIBLE);
        mViewModel.observerMessages().observe(this, msg -> {

                    mProgressBar.setVisibility(View.GONE);
                    mActivity.showMessage(msg);
                }
        );


        mViewModel.getUserRepos().observe(this, repos -> {
            if (mAdapter == null) {
                mAdapter = new RepoItemAdapter(repos);
                mAdapter.registerItemClikcListener(ReposFragment.this);
                mRecyclerView.setEmptyView(mEmptyView);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.updateList(repos);
            }

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.registerItemClikcListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.unregisterItemClickListener();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            mListener = (FragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mViewModel.clear();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    @Override
    public void onClick(Object object) {
        View view = (View) object;
        int position = mRecyclerView.getChildLayoutPosition(view);
        RepoItem repoItem  = mAdapter.getRepoAtPosition(position);
        String reposName = repoItem.getRepoName();
        mListener.onRepoClicked(reposName);
    }


    public interface FragmentInteraction {
        void onRepoClicked(String url);
    }
}
