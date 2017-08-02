package stuido.tsing.iclother.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;
import stuido.tsing.iclother.base.BaseActivity;


public class MyActivity extends BaseActivity {
    @BindView(R.id.action_logout)
    AppCompatButton button;

    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.person_center);
    }

//    @Override
//    protected void initView() {
//        setContentView(R.layout.me_act);
//    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.action_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_logout:
                SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
                edit.putBoolean("loginState", false);
                edit.putString("id", null);
                edit.apply();
                Toast.makeText(this, "login out", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void actionMenuClickEvent() {

    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return null;
    }
}
