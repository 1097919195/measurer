package stuido.tsing.iclother.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.account.AccountActivity;

/**
 * Created by Endless on 2017/7/18.
 */

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences loginState = getSharedPreferences("Iclother", MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);
        if (!isLogin) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main_layout);
        }
    }
}
