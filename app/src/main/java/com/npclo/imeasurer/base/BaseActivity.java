package com.npclo.imeasurer.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.account.AccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    Toolbar toolbarBase;
    @BindView(R.id.base_toolbar_title)
    TextView base_toolbar_title;
    @BindView(R.id.act_content_view)
    protected RelativeLayout act_content_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        beforeInit();
        setContentView(R.layout.base_act);
        ButterKnife.bind(this);
        initToolbar();
        initFragment();
    }

    /**
     * 初始化toolbar的一些默认属性
     */
    private void initToolbar() {
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.toolbar_text));//设置主标题颜色
        toolbarBase.inflateMenu(R.menu.base_toolbar_menu);
        toolbarBase.getMenu().getItem(0).setTitle(getActionMenuTitle());

//        toolbarBase.setTitle(getToolbarTitle());
        base_toolbar_title.setText(getToolbarTitle());
        //todo 根据页面更新图标
        toolbarBase.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_menu:
                    actionMenuClickEvent();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    protected abstract CharSequence getToolbarTitle();

    protected void beforeInit() {
        SharedPreferences loginState = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);

        if (!isLogin) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
    }

    protected abstract void initFragment();

    protected abstract void actionMenuClickEvent();

    private void nav(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    protected abstract CharSequence getActionMenuTitle();
}