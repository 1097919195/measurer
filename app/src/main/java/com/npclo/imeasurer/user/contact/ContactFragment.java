package com.npclo.imeasurer.user.contact;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Endless
 * @date 2017/9/4
 */

public class ContactFragment extends BaseFragment implements ContactContract.View {
    Unbinder unbinder;
    @BindView(R.id.support_frag_toolbar)
    Toolbar toolbar;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_contact;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        toolbar.setTitle(getString(R.string.prompt_contact));
        navOfToolbar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
