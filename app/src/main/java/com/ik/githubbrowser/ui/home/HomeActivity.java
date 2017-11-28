package com.ik.githubbrowser.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ik.githubbrowser.BaseActivity;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.ui.home.events.EventsFragment;
import com.ik.githubbrowser.ui.home.events.EventsFragmentViewModelFactory;
import com.ik.githubbrowser.ui.home.followers.FollowersFragment;
import com.ik.githubbrowser.ui.home.followers.FollowersFragmentViewModelFactory;
import com.ik.githubbrowser.ui.home.followings.FollowingFragmentViewModelFactory;
import com.ik.githubbrowser.ui.home.followings.FollowingsFragment;
import com.ik.githubbrowser.ui.home.repos.ReposFragment;
import com.ik.githubbrowser.ui.home.repos.ReposFragmentViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;

public class HomeActivity extends BaseActivity implements
        EventsFragment.FragmentInteraction,
        ReposFragment.FragmentInteraction,
        FollowersFragment.FragmentInteraction,
        FollowingsFragment.FragmentInteraction {

    private final int NUM_PAGES = 4;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.tv_username)
    TextView mTvUsername;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String username;

    public static void open(Context context, String username){
        Intent intent = new Intent(context, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, username);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString(KEY_USERNAME);

        if (TextUtils.isEmpty(username)) {
            showMessage(R.string.invalid_username);
            finish();
        }

        if (savedInstanceState == null) {
            EventsFragmentViewModelFactory.clear();
            ReposFragmentViewModelFactory.clear();
            FollowersFragmentViewModelFactory.clear();
            FollowingFragmentViewModelFactory.clear();
        }

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTvUsername.setText(username);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onEventClicked(String userName, String repoName) {
        String repoUrl = "https://github.com/"+userName+"/"+repoName;
        WebViewActivity.startActivity(this, repoUrl );
    }

    @Override
    public void onRepoClicked(String reposName) {
        String repoUrl = "https://github.com/"+username+"/"+reposName+"?files=1";
        WebViewActivity.startActivity(this, repoUrl);
    }

    @Override
    public void onFollowerClicked(String followerUserName) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, followerUserName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onFollowingClicked(String followerUserName) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_USERNAME, followerUserName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return EventsFragment.newInstance(username);
                case 1:
                    return ReposFragment.newInstance(username);
                case 2:
                    return FollowersFragment.newInstance(username);
                case 3:
                    return FollowingsFragment.newInstance(username);
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.label_events);
                case 1:
                    return getString(R.string.label_repo);
                case 2:
                    return getString(R.string.label_followers);
                case 3:
                    return getString(R.string.label_followings);
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
