package com.npclo.imeasurer.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseActivity;
import com.npclo.imeasurer.user.contact.ContactFragment;
import com.npclo.imeasurer.user.feedback.FeedbackFragment;
import com.npclo.imeasurer.user.manage.ManageFragment;
import com.npclo.imeasurer.user.manage.ManagePresenter;
import com.npclo.imeasurer.utils.Constant;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

/**
 * @author Endless
 */
public class UserActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    private void initFragment(int supportType) {
        switch (supportType) {
            case Constant.USER_PWD:
                ManageFragment manageFragment = ManageFragment.newInstance();
                loadRootFragment(R.id.content_frame, manageFragment);
                manageFragment.setPresenter(new ManagePresenter(manageFragment, SchedulerProvider.getInstance()));
                break;
            case Constant.USER_FEEDBACK:
                FeedbackFragment feedbackFragment = FeedbackFragment.newInstance();
                loadRootFragment(R.id.content_frame, feedbackFragment);
                break;
            case Constant.USER_CONTACT:
                ContactFragment contactFragment = ContactFragment.newInstance();
                loadRootFragment(R.id.content_frame, contactFragment);
                break;
            case Constant.USER_INSTRUCTION:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int supportType = intent.getIntExtra("support_type", 0);
        initFragment(supportType);
    }
}