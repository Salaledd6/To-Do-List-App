package com.example.ToDoList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Objects;
                                                                                                  //launch screen which appears for a specific amount of time, generally shows for the first time when the app is launched.

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();                                      //Hides the action bar from the app



        final Intent I = new Intent(SplashActivity.this, MainActivity.class);                               //Starts MainActivity after SplashActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {                                                 //schedules runnable to be executed after the specified amount of time elapses.

            @Override
            public void run() {


                I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(I);
                finish();                                                                           //Calls this when activity is done and should be closed
            }
        }, 2000);
    }
}