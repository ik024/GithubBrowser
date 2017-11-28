package com.ik.githubbrowser.ui.home.events;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ik.githubbrowser.BaseActivity;
import com.ik.githubbrowser.MyRecyclerView;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.EmptyLayout;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;


public class EventsFragment extends Fragment implements LifecycleOwner, RecyclerViewItemClickListener {

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private String mUsername;
    private BaseActivity mActivity;
    private FragmentInteraction mListener;

    @BindView(R.id.rv_events)
    MyRecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.layout_empty_view)
    View mEmptyView;

    private EventItemAdapter mAdapter;
    private EventsFragmentViewModel mViewModel;
    private EmptyLayout mIncludedEmptyLayout = new EmptyLayout();

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(String username) {
        EventsFragment fragment = new EventsFragment();
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
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        ButterKnife.bind(mIncludedEmptyLayout, mEmptyView);
        mIncludedEmptyLayout.tvEmptyView.setText(R.string.empty_events_list);

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        EventsFragmentViewModelFactory factory
                = new EventsFragmentViewModelFactory(repository, mUsername);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(EventsFragmentViewModel.class);

        mActivity = (BaseActivity) getActivity();

        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel.observerMsgs().observe(this, msg -> mActivity.showMessage(msg));

        mViewModel.getEvents().observe(this, this::populateRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter !=null)
            mAdapter.registerItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.unregisterItemClickListener();
    }

    private void populateRecyclerView(List<Event> list) {
        mProgressBar.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new EventItemAdapter(list);
            mAdapter.registerItemClickListener(this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setEmptyView(mEmptyView);
        } else {
            mAdapter.updateList(list);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            mListener = (FragmentInteraction) context;
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
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
        Event event = mAdapter.getEventAtPosition(position);

        String[] repoUrlSegment = event.getRepo().getUrl().split("/");
        String repoName = repoUrlSegment[repoUrlSegment.length-1];
        String userName = repoUrlSegment[repoUrlSegment.length-2];
        mListener.onEventClicked(userName, repoName);
    }

    public interface FragmentInteraction {
        void onEventClicked(String repoName, String userName);
    }

}
