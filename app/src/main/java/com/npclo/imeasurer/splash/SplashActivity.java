package com.npclo.imeasurer.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;

import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.main.MainActivity;
import com.npclo.imeasurer.utils.PreferencesUtils;

/**
 * @author Endless
 * @date 2017/7/19
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        String token = PreferencesUtils.getInstance(this).getToken(true);
        Handler handler = new Handler();
        if (!TextUtils.isEmpty(token)) {
            handler.postDelayed(this::goToMain, 500);
        } else {
            handler.postDelayed(this::goToSignIn, 500);
        }
    }

    private void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void goToSignIn() {
        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
        finish();
    }
}