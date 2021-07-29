package com.floridakeys.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.floridakeys.MainActivity;
import com.floridakeys.R;
import com.floridakeys.util.MyLocation;

/**
 * @description     Splash Activity
 *
 * @author          Adrian
 */
public class SplashActivity extends FragmentActivity {
    private final int SPLASH_DURATION = 2 * 1000;       // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MyLocation location = MyLocation.getInstance(this);
        if (!location.isEnabledLocation()) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.app_name);
            dialog.setMessage(getString(R.string.request_gps_enable));
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            dialog.show();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, SPLASH_DURATION);
        }
    }
}
