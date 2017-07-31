package stuido.tsing.iclother.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;
import stuido.tsing.iclother.home.HomeActivity;
import stuido.tsing.iclother.me.MyActivity;
import stuido.tsing.iclother.utils.view.ChangeColorIconWithTextView;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.btn_main)
    ChangeColorIconWithTextView btnMain;
    @BindView(R.id.btn_measure)
    ChangeColorIconWithTextView btnMeasure;
    @BindView(R.id.btn_help)
    ChangeColorIconWithTextView btnHelp;
    @BindView(R.id.btn_about)
    ChangeColorIconWithTextView btnAbout;
    @BindView(R.id.base_toolbar)
    Toolbar toolbarBase;
    @BindView(R.id.base_toolbar_title)
    TextView base_toolbar_title;
    @BindView(R.id.act_content_view)
    protected RelativeLayout act_content_view;
    protected List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        beforeInit();
        initView();
        ButterKnife.bind(this);
        initToolbar();
        initTabIndicator();
        initEvent();
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

    protected abstract void initView();

    protected abstract void initEvent();

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    private void initTabIndicator() {
        mTabIndicator.add(btnMain);
        mTabIndicator.add(btnMeasure);
        mTabIndicator.add(btnHelp);
        mTabIndicator.add(btnAbout);

        btnMain.setIconAlpha(1.0f);
    }

    @OnClick({R.id.btn_main, R.id.btn_measure, R.id.btn_help, R.id.btn_about})
    public void onViewClicked(View view) {
        resetOtherTabs();
        switch (view.getId()) {
            case R.id.btn_main:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                if (!(this instanceof HomeActivity)) nav(HomeActivity.class);
                break;
            case R.id.btn_measure:
                mTabIndicator.get(1).setIconAlpha(1.0f);
//                if (!(this instanceof MeasureActivity)) nav(MeasureActivity.class);
                break;
            case R.id.btn_help:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                break;
            case R.id.btn_about:
                mTabIndicator.get(3).setIconAlpha(1.0f);
                if (!(this instanceof MyActivity)) nav(MyActivity.class);
                break;
        }
    }

    protected abstract void actionMenuClickEvent();

    private void nav(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    protected abstract CharSequence getActionMenuTitle();
}