package stuido.tsing.iclother.home;


import android.support.annotation.NonNull;

import java.util.List;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;
import stuido.tsing.iclother.data.measure.Measurement;


public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMeasurementList(List<Measurement> measureList);

        void showScanView();

        void showMeasurementDetailsUi(String measurementId);

        void showLoadingMeasurementError();

        void showNoMeasurement();

        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadMeasurements(boolean forceUpdate);

        void addNewMeasurement();

        void openMeasurementDetails(@NonNull Measurement measurement);
    }
}