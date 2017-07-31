package stuido.tsing.iclother.home;

import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/19.
 */

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View homeView;
    private final boolean mIsDataMissing;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private final MeasurementDataSource mRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;


    private boolean mFirstLoad = true;

    public HomePresenter(@NonNull MeasurementDataSource measureDataSource,
                         @NonNull HomeContract.View view, boolean shouldLoadDataFromRepo,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = checkNotNull(measureDataSource);
        homeView = checkNotNull(view);
        mIsDataMissing = shouldLoadDataFromRepo;
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        homeView.setPresenter(this);
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadMeasurements(boolean forceUpdate) {

    }

    @Override
    public void addNewMeasurement() {

    }

    @Override
    public void openMeasurementDetails(@NonNull Measurement measurement) {

    }
}
