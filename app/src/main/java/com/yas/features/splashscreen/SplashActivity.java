package com.yas.features.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yas.R;
import com.yas.features.main.MainActivity;
import com.yas.utils.bases.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },1500);
    }
}
