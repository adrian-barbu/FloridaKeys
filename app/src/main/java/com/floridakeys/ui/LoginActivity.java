package com.floridakeys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.floridakeys.MainActivity;
import com.floridakeys.R;

/**
 * @description     Login Activity
 *
 * @author          Adrian
 */
public class LoginActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Facebook Sign Up Event Handler
     */
    public void onFacebookSignUp(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Email Sign Up Event Handler
     */
    public void onEmailSignUp(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
