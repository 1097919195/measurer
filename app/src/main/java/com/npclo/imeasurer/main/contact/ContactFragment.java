package com.npclo.imeasurer.main.contact;

import android.view.View;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * @author Endless
 * @date 2017/9/4
 */

public class ContactFragment extends BaseFragment implements ContactContract.View {
    Unbinder unbinder;

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
    }

    @Override
    protected String setFragmentTitle() {
        return getString(R.string.prompt_contact);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
