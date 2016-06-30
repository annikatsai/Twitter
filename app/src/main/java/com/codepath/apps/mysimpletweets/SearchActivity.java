package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mysimpletweets.fragments.SearchTweetsFragment;

public class SearchActivity extends AppCompatActivity {

 //   TwitterClient client;
    SearchTweetsFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra("q");

        if (savedInstanceState == null) {
//            searchFragment = (SearchTweetsFragment)
//                    getSupportFragmentManager().findFragmentById(R.id.flSearchFragment);
            searchFragment = SearchTweetsFragment.newInstance(query);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flSearchFragment, searchFragment);
            ft.commit();
        }

//        String query = getIntent().getStringExtra("q");
//        searchFragment = SearchTweetsFragment.newInstance(query);

//        client = TwitterApplication.getRestClient();
//        client.searchTweets(query, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("DEBUG", json.toString());
//                searchFragment.addAll(Tweet.fromJSONArray(json));
//            }
//
//            // Failure
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });


    }
}
