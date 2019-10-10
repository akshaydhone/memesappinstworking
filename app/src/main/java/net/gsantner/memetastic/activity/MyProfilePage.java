package net.gsantner.memetastic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.gsantner.memetastic.R;

public class MyProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_page);
        getSupportActionBar().setTitle("MyProfile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
}
