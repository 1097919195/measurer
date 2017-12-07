package com.npclo.imeasurer.main.feedback;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;

/**
 * Created by Endless on 2017/9/4.
 */

public interface FeedbackContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
    }
}
