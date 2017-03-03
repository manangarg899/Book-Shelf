package com.example.manan.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

//This activity ijs used to create a splash screen for 2000ms or 2 seconds
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, BookActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
