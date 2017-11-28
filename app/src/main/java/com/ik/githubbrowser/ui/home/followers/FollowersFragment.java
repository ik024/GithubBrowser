package com.ik.githubbrowser.ui.home.followers;

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
import com.ik.githubbrowser.repository.db.entity.followers.Follower;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.EmptyLayout;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;

public class FollowersFragment extends Fragment implements LifecycleOwner, RecyclerViewItemClickListener {

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private FragmentInteraction mListener;

    @BindView(R.id.rv_followers)
    MyRecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.layout_empty_view)
    View mEmptyView;

    private EmptyLayout mIncludedEmptyLayout = new EmptyLayout();

    private String mUsername;
    private FollowerItemAdapter mAdapter;
    private FollowersFragmentViewModel mViewModel;
    private BaseActivity mActivity;


    public FollowersFragment() {
        // Required empty public constructor
    }


    public static FollowersFragment newInstance(String username) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(KEY_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, view);

        ButterKnife.bind(mIncludedEmptyLayout, mEmptyView);
        mIncludedEmptyLayout.tvEmptyView.setText(R.string.empty_followers_list);

        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mActivity = (BaseActivity) getActivity();

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        FollowersFragmentViewModelFactory factory = new FollowersFragmentViewModelFactory(repository, mUsername);
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(FollowersFragmentViewModel.class);

        mViewModel.getFollowers().observe(this, followers -> {
            mProgressBar.setVisibility(View.GONE);
            if (mAdapter == null) {
                mAdapter = new FollowerItemAdapter(followers);
                mAdapter.registerItemClickListener(FollowersFragment.this);
                mRecyclerView.setEmptyView(mEmptyView);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.updateList(followers);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.registerItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.unregisterClickListener();
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
        View view  = (View) object;
        int position = mRecyclerView.getChildLayoutPosition(view);
        Follower follower = mAdapter.getFollowerAtPosition(position);
        mListener.onFollowerClicked(follower.getLogin());
    }

    public interface FragmentInteraction {
       void onFollowerClicked(String followerUserName);
    }
}
