package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeFragment extends DialogFragment implements View.OnClickListener {

    Tweet tweet;

    private EditText editText;
    private Button btnTweet;

    public interface ComposeFragmentListener {
        public void onReturnValue (Tweet tweet);
    }

    public ComposeFragment() {
    }


    public static ComposeFragment newInstance(String title) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onClick(View v) {
        TwitterClient client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet.setUser(User.fromJSON(response));
            }
        });
        // Return input text to activity
        // Log.d("DEBUG", editText.getText().toString());
        ComposeFragmentListener activity = (ComposeFragmentListener) getActivity();
        //activity.onReturnValue(editText.getText().toString());
        tweet.setBody(editText.getText().toString());
        tweet.setUid(0);
        tweet.setCreatedAt("Now");
        activity.onReturnValue(tweet);
        this.dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tweet = new Tweet();

        // Get field from view
        editText = (EditText) view.findViewById(R.id.etPost);

        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }



}




