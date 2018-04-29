package com.yas.utils.bases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.yas.R;



public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
      super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent("PERMISSION_RECEIVER");
        intent.putExtra("requestCode",requestCode);
        intent.putExtra("permissions",permissions);
        intent.putExtra("grantResults",grantResults);
        sendBroadcast(intent);
    }
}
