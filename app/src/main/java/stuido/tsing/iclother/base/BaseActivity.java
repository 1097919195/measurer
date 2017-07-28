package stuido.tsing.iclother.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;
import stuido.tsing.iclother.home.HomeActivity;
import stuido.tsing.iclother.utils.view.ChangeColorIconWithTextView;

/**
 * Created by Endless on 2017/7/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.btn_main)
    ChangeColorIconWithTextView btnMain;
    @BindView(R.id.btn_measure)
    ChangeColorIconWithTextView btnMeasure;
    @BindView(R.id.btn_help)
    ChangeColorIconWithTextView btnHelp;
    @BindView(R.id.btn_about)
    ChangeColorIconWithTextView btnAbout;
    protected List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitView();
        initView();
        ButterKnife.bind(this);
        initTabIndicator();
        initEvent();
    }

    protected void beforeInitView() {
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

    protected void initTabIndicator() {
        mTabIndicator.add(btnMain);
        mTabIndicator.add(btnMeasure);
        mTabIndicator.add(btnHelp);
        mTabIndicator.add(btnAbout);

        btnMain.setIconAlpha(1.0f);
    }


    @OnClick({R.id.btn_main, R.id.btn_measure, R.id.btn_help, R.id.btn_about})
    public void onViewClicked(View view) {
        resetOtherTabs();
        String id = String.valueOf(view.getId());
        switch (view.getId()) {
            case R.id.btn_main:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                if (!(this instanceof HomeActivity)) nav(HomeActivity.class);
                break;
            case R.id.btn_measure:
                mTabIndicator.get(1).setIconAlpha(1.0f);
//                if (!(this instanceof MeasureActivity)) nav(MeasureActivity.class);
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_help:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_about:
                mTabIndicator.get(3).setIconAlpha(1.0f);
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void nav(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}