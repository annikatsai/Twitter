package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.ComposeFragmentListener {

    private SmartFragmentStatePagerAdapter adapterViewPager;
    public TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

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

    private final int REQUEST_CODE = 20;

    public void onComposeView(MenuItem mi) {
//        Intent i = new Intent(this, ComposeActivity.class);
//        startActivityForResult(i, REQUEST_CODE);
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance("");
        composeFragment.show(fm, "fragment_edit_name");
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


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            String post = data.getExtras().getString("post");
//            Toast.makeText(this, post, Toast.LENGTH_SHORT).show();
//            client.postTweet(post, new JsonHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                    super.onSuccess(statusCode, headers, response);
//                }
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                }
//            });
//        }
//    }

//    private void post(String body) {
//        client.postTweet(body, new JsonHttpResponseHandler() {
//            @Override
//                public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONArray response) {
//                    super.onSuccess(statusCode, headers, response);
//                }
//            @Override
//                public void onFailure(int statusCode, PreferenceActivity.Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                }
//
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
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

//    public void onViewFriendProfile(View view) {
// //       Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(TimelineActivity.this, FriendProfileActivity.class);
//        TextView tvScreenName = (TextView) findViewById(R.id.tvUserName);
//        String screen_name = tvScreenName.getText().toString();
//        i.putExtra("screen_name", screen_name);
//        startActivity(i);
//    }

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
