package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.ComposeFragmentListener {

    private SmartFragmentStatePagerAdapter adapterViewPager;
    public TwitterClient client;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        adapterViewPager =  new TweetsPagerAdapter(getSupportFragmentManager());
        // Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter for the pager
      //  vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        vpPager.setAdapter(adapterViewPager);
        // Find the pager sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the pager tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);

        client = TwitterApplication.getRestClient();

    }

    public void onComposeView(MenuItem mi) {
        showEditDialog();
    }

    User user;
    private void showEditDialog() {
        final FragmentManager fm = getSupportFragmentManager();

        TwitterClient twitterClient = TwitterApplication.getRestClient();
        twitterClient.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                String profileImageUrl = user.getProfileImageUrl();
                ComposeFragment composeFragment = ComposeFragment.newInstance("", profileImageUrl);
                composeFragment.show(fm, "fragment_compose");
            }
        });
    }

    @Override
    public void onReturnValue(Tweet tweet) {
        //Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
        client.postTweet(tweet.getBody(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        HomeTimelineFragment fragmentHomeTweets =
                (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
        fragmentHomeTweets.appendTweet(tweet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        int searchTextID = android.support.v7.appcompat.R.id.search_src_text;
        TextView textView = (TextView) searchView.findViewById(searchTextID);
        textView.setTextColor(Color.BLACK);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(Color.BLACK);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(TimelineActivity.this, SearchActivity.class);
                i.putExtra("q", query);
                startActivity(i);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi) {
        // Launch Profile
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    // Return the order of the fragments in the ViewPager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};

        // Adapter gets the manager insert or remove from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        // Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // How many fragments there are to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
