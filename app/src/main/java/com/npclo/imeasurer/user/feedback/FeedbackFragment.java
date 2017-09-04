package com.npclo.imeasurer.user.feedback;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.user.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Endless on 2017/9/4.
 */

public class FeedbackFragment extends BaseFragment implements FeedbackContract.View {
    @BindView(R.id.base_toolbar_title)
    TextView baseToolbarTitle;
    @BindView(R.id.base_toolbar)
    Toolbar baseToolbar;
    @BindView(R.id.input_content)
    AppCompatEditText inputContent;
    @BindView(R.id.action_add_feedback_img)
    ImageView actionAddFeedbackImg;
    @BindView(R.id.line_feedback_img)
    LinearLayout lineFeedbackImg;
    @BindView(R.id.action_submit)
    AppCompatButton actionSubmit;
    Unbinder unbinder;

    @Override
    public void setPresenter(FeedbackContract.Presenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.feedback_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        baseToolbar.setNavigationIcon(R.mipmap.left);
        baseToolbarTitle.setText(getString(R.string.prompt_feedback));
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

    @OnClick(R.id.action_submit)
    public void onViewClicked() {
    }

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }
}
