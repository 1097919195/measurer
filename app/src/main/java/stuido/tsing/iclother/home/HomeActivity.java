package stuido.tsing.iclother.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences loginState = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);

        if (!isLogin) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main_layout);
            //todo del it
            findViewById(R.id.login_out).setOnClickListener(__ -> {
                SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.app_name), MODE_APPEND).edit();
                edit.putBoolean("loginState", false);
                edit.apply();
                Toast.makeText(this, "login out", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
            });
        }

    }
}
