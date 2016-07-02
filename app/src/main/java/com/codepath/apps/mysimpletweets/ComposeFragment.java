package com.codepath.apps.mysimpletweets;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeFragment extends DialogFragment implements View.OnClickListener {

    Tweet tweet;

    private ImageView ivProfileImage;
    private ImageView ivExit;
    private EditText editText;
    private Button btnTweet;
    private TextView tvCharCount;
    static int MAX_COUNT = 140;

    public interface ComposeFragmentListener {
        public void onReturnValue (Tweet tweet);
    }

    public ComposeFragment() {
    }

    static Bundle args;
    public static ComposeFragment newInstance(String title, String profileImageUrl) {
        ComposeFragment frag = new ComposeFragment();
        args = new Bundle();
        args.putString("title", title);
        args.putString("profileImageUrl", profileImageUrl);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    User user;
    TwitterClient client;
    @Override
    public void onClick(View v) {
        client = TwitterApplication.getRestClient();

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // Return input text to activity
                // Log.d("DEBUG", editText.getText().toString());
                ComposeFragmentListener activity = (ComposeFragmentListener) getActivity();

                //activity.onReturnValue(editText.getText().toString());
                tweet.setUser(user);
                tweet.setBody(editText.getText().toString());
                tweet.setUid(0);
                tweet.setCreatedAt("Now");
                activity.onReturnValue(tweet);
                ComposeFragment.this.dismiss();
            }
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tweet = new Tweet();

        // Get field from view
        ivExit = (ImageView) view.findViewById(R.id.ivExit);
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeFragment.this.dismiss();
            }
        });
        editText = (EditText) view.findViewById(R.id.etPost);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view
        Picasso.with(getContext())
                .load(args.getString("profileImageUrl"))
                .into(ivProfileImage);

        //btnExit = (Button) view.findViewById(R.id.btnExit);
        tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void afterTextChanged(Editable s) {

                // Display Remaining Character with respective color
                int count = MAX_COUNT - s.length();
                tvCharCount.setText(Integer.toString(count));
                tvCharCount.setTextColor(Color.DKGRAY);
                if (count < 0) {
                    tvCharCount.setTextColor(Color.RED);
                }
            }
        });


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}




