package stuido.tsing.iclother.home;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/19.
 */

public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = "HomePresenter";
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
        homeView.showScanButton();
    }

    @Override
    public void openMeasurementDetails(@NonNull Measurement measurement) {
        checkNotNull(measurement);
        homeView.showMeasurementDetail(measurement.getcId());
    }

    /**
     * homeFragment创建后，首次加载数据
     *
     * @param forceUpdate   强制加载
     * @param showLoadingUI 是否显示loading动画
     */
    private void loadMeasurements(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            homeView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mRepository.refreshMeasurements();
        }
        mSubscriptions.clear(); // TODO: 2017/8/16 ?
        Subscription subscribe = mRepository.getMeasurements()
                .flatMap(new Func1<List<Measurement>, Observable<Measurement>>() {
                    @Override
                    public Observable<Measurement> call(List<Measurement> list) {
                        return Observable.from(list);
                    }
                })
                .toList()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(this::processMeasurementList,
                        e -> homeView.showLoadingMeasurementError(e),
                        () -> homeView.setLoadingIndicator(false));
        mSubscriptions.add(subscribe);
    }

    private void processMeasurementList(List<Measurement> measurements) {
        if (measurements.isEmpty()) {
            homeView.showNoMeasurementView(false);
        } else {
            homeView.showMeasurementList(measurements);
        }
    }
}