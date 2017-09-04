package com.npclo.imeasurer.user.contact;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.user.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Endless on 2017/9/4.
 */

public class ContactFragment extends BaseFragment implements ContactContract.View {
    @BindView(R.id.base_toolbar_title)
    TextView baseToolbarTitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    Unbinder unbinder;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbarTitle.setText(getString(R.string.prompt_contact));
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbar.setNavigationOnClickListener(__ -> {
            HomeFragment homeFragment = HomeFragment.newInstance();
            start(homeFragment, SINGLETASK);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
