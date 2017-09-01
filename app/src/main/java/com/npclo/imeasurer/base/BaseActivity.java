package com.npclo.imeasurer.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {
    @BindView(R.id.base_toolbar)
    Toolbar toolbarBase;
    @BindView(R.id.base_toolbar_title)
    TextView base_toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        beforeInit();
        setContentView(R.layout.base_act);
        ButterKnife.bind(this);
        initToolbar();
        init();
    }

    /**
     * 初始化toolbar的一些默认属性
     * 一些页面需要重写该方法
     */
    protected void initToolbar() {
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.toolbar_text));//设置主标题颜色
        toolbarBase.inflateMenu(R.menu.base_toolbar_menu);
        toolbarBase.getMenu().getItem(0).setTitle(getActionMenuTitle());
        base_toolbar_title.setText(getToolbarTitle());
    }

    protected abstract CharSequence getToolbarTitle();

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

    protected abstract void init();

    protected abstract CharSequence getActionMenuTitle();
}