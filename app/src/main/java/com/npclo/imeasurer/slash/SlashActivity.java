package com.npclo.imeasurer.slash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;
import com.npclo.imeasurer.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Endless
 * @date 2017/7/19
 */
public class SlashActivity extends AppCompatActivity {
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    private Context context;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
        context = this;
        initView();
        SharedPreferences sp = getSharedPreferences(getResources().getString(R.string.app_config), MODE_PRIVATE);
        String token = sp.getString("token", "");
        if (!TextUtils.isEmpty(token)) {
            handler.postDelayed(this::goToMain, 900);
        } else {
            handler.postDelayed(this::goToSignIn, 900);
        }
    }

    private void initView() {
        new Handler().postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(SlashActivity.this, R.anim.rotation_anim);
            ivLoading.setAnimation(animation);
            // FIXME: 10/01/2018 动画优雅
            ivLoading.startAnimation(animation);
        }, 0);
    }

    private void goToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void goToSignIn() {
        startActivity(new Intent(context, AccountActivity.class));
        finish();
    }
}