////package com.codepath.apps.mysimpletweets;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.support.v7.app.AppCompatActivity;
////import android.view.View;
////import android.widget.EditText;
////
////public class ComposeActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_compose);
////    }
////
////    public void onSubmit(View v) {
////        EditText etPost = (EditText) findViewById(R.id.etPost);
////        // Prepare data intent
////        Intent data = new Intent();
////        // Pass relevant data back as a result
////        data.putExtra("post", etPost.getText().toString());
////        // Activity finished ok, return the data
////        setResult(RESULT_OK, data); // set result code and bundle data for response
////        finish(); // closes the activity, pass data to parent
////    }
////}
////
//
//package com.codepath.apps.mysimpletweets;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//
//public class ComposeActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_compose);
//        showEditDialog();
//    }
//
//    private void showEditDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        ComposeFragment composeFragment = ComposeFragment.newInstance("Some Title");
//        composeFragment.show(fm, "fragment_edit_name");
//    }
//
//    public void onSubmit(View v) {
//        EditText etPost = (EditText) findViewById(R.id.etPost);
//        // Prepare data intent
//        Intent data = new Intent();
//        // Pass relevant data back as a result
//        data.putExtra("post", etPost.getText().toString());
//        // Activity finished ok, return the data
//        setResult(RESULT_OK, data); // set result code and bundle data for response
//        finish(); // closes the activity, pass data to parent
//    }
//}
//
