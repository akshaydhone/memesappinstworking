package net.gsantner.memetastic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.gsantner.memetastic.R;


public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
