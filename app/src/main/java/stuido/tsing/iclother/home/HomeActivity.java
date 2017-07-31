package stuido.tsing.iclother.home;

import android.widget.Toast;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.base.BaseActivity;

public class HomeActivity extends BaseActivity {
    HomePresenter presenter;
    @Override
    protected CharSequence getToolbarTitle() {
        return getString(R.string.main_act_title);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.main_act);
//        act_content_view.addView();
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void actionMenuClickEvent() {
        Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected CharSequence getActionMenuTitle() {
        return getString(R.string.main_act_action_menu_title);
    }

}