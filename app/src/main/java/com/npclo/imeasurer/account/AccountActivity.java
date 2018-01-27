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
 *
 * @author Endless
 */
public class AccountActivity extends SupportActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_account);

        SignInFragment fragment = findFragment(SignInFragment.class);
        if (fragment == null) {
            fragment = SignInFragment.newInstance();
            loadRootFragment(R.id.content_frame, fragment);
            new SignInPresenter(fragment, SchedulerProvider.getInstance());
        }
    }
}