package com.npclo.imeasurer.user.feedback;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Endless
 * @date 2017/9/4
 */

public class FeedbackFragment extends BaseFragment implements FeedbackContract.View {
    @BindView(R.id.support_frag_toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_content)
    AppCompatEditText inputContent;
    //    @BindView(R.id.action_add_feedback_img)
//    ImageView actionAddFeedbackImg;
//    @BindView(R.id.line_feedback_img)
//    LinearLayout lineFeedbackImg;
    @BindView(R.id.action_submit)
    AppCompatButton actionSubmit;
    Unbinder unbinder;

    @Override
    public void setPresenter(FeedbackContract.Presenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feedback;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        toolbar.setTitle(getString(R.string.prompt_feedback));
        initToolbar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.action_submit)
    public void onViewClicked() {
        String s = inputContent.getText().toString();
        if (TextUtils.isEmpty(s)) {
            showToast("请输入描述内容");
        } else {
            showToast("保存成功");
        }
    }

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }
}