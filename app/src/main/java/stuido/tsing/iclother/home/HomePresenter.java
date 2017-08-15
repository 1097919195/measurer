package stuido.tsing.iclother.home;

import android.support.annotation.NonNull;
import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/19.
 */

public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = "HomeFragment";
    private final HomeContract.View homeView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private final MeasurementDataSource mRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private boolean mFirstLoad = true;

    public HomePresenter(@NonNull MeasurementDataSource measureDataSource,
                         @NonNull HomeContract.View view,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = checkNotNull(measureDataSource);
        homeView = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        homeView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadMeasurements(false);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // TODO: 2017/8/1 测量完保存成功跳转首页
        homeView.showSuccessfullySavedMessage();
    }

    @Override
    public void loadMeasurements(boolean forceUpdate) {
        loadMeasurements(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void addNewMeasurement() {
        homeView.showScanView();
    }

    @Override
    public void openMeasurementDetails(@NonNull Measurement measurement) {
        checkNotNull(measurement);
        homeView.showMeasurementDetailsUi(measurement.getcId());
    }

    private void loadMeasurements(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            homeView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mRepository.refreshMeasurements();
        }
        mSubscriptions.clear();
        Subscription subscribe = mRepository.getMeasurements()
//                .flatMap(new Func1<List<Measurement>, Observable<Measurement>>() {
//                    @Override
//                    public Observable<Measurement> call(List<Measurement> list) {
//                        return Observable.from(list);
//                    }
//                })
//                .toList()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(measurements -> {
                            if (measurements.isEmpty()) {
                                homeView.showNoMeasurementView();
                            } else {
                                homeView.showMeasurementList(measurements);
                            }
                        },
                        e -> {
                            homeView.showLoadingMeasurementError();
                            Log.e(TAG, e.getMessage());
                        },
                        () -> homeView.setLoadingIndicator(false));
//        mSubscriptions.add(subscribe);
    }
}