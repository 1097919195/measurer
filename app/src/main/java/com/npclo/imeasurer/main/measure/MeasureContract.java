package com.npclo.imeasurer.main.measure;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;

/**
 * Created by Endless on 2017/9/1.
 */

public interface MeasureContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
    }
}
