package com.npclo.imeasurer.user;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;

/**
 * Created by Endless on 2017/8/24.
 */

public interface UserContract {
    interface View extends BaseView<Presenter> {

        void logout();
    }

    interface Presenter extends BasePresenter {
        void logout();
    }
}
