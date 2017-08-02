package stuido.tsing.iclother.measure;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;
import stuido.tsing.iclother.data.measure.Measurement;

/**
 * Created by Endless on 2017/8/1.
 */

public interface MeasureContract {
    interface View extends BaseView<Presenter> {

        void showSuccessSave();

        void showSaveError();

        void setLoadingIndicator(boolean b);
    }

    interface Presenter extends BasePresenter {
        void saveMeasurement(Measurement measurement);
    }
}
