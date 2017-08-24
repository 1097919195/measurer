package stuido.tsing.iclother.user;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;

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
