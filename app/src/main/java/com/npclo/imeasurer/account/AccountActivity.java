package com.npclo.imeasurer.account;

import android.os.Bundle;
import android.view.Window;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.signin.SignInFragment;
import com.npclo.imeasurer.account.signin.SignInPresenter;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 管理用户登录和注册的界面，使用两个不同的fragment来分别承载
 */
public class AccountActivity extends SupportActivity {
    private static final String TAG = AccountActivity.class.getSimpleName();
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_act);

        SignInFragment signInFragment = (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (signInFragment == null) {
            signInFragment = SignInFragment.newInstance();
            AccountFragment.addFragmentToActivity(getSupportFragmentManager(), signInFragment, R.id.content_frame);
        }
        new SignInPresenter(signInFragment, SchedulerProvider.getInstance());
    }
}