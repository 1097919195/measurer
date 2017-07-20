package stuido.tsing.iclother.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.login.LoginActivity;

/**
 * Created by Endless on 2017/7/18.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences loginState = getSharedPreferences("Iclother", MODE_PRIVATE);
        boolean isLogin = loginState.getBoolean("loginState", false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main_layout);
        }
    }
}
