package com.npclo.imeasurer.home;


import android.support.annotation.NonNull;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.measure.Measurement;

import java.util.List;


public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMeasurementList(List<Measurement> measureList);

        void showScanButton();

        void showMeasurementDetail(String measurementId);

        void showLoadingMeasurementError(Throwable e);

        void showNoMeasurementView(boolean showAddView);

        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadMeasurements(boolean forceUpdate);

        void addNewMeasurement();

        void openMeasurementDetails(@NonNull Measurement measurement);
    }
}
