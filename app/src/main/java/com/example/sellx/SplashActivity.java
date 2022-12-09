package com.example.sellx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.window.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //directly sends the user to the main activity if previous logged
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                String haslogged = sharedPreferences.getString("haslogged","");

                if (haslogged.equals("true")){
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,IntroActivity.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                }




            }
        },3000);


    }
}