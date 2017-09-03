package com.npclo.imeasurer.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;

import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        beforeInit();
    }

    protected void beforeInit() {
        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences loginState = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);

        if (!isLogin) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
    }
}