package stuido.tsing.iclother.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;
import stuido.tsing.iclother.base.BaseActivity;

public class HomeActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_layout);
    }

    @Override
    protected void initEvent() {
        //todo del it
        findViewById(R.id.login_out).setOnClickListener(__ -> {
            SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
            edit.putBoolean("loginState", false);
            edit.putString("id", null);
            edit.apply();
            Toast.makeText(this, "login out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
    }
}