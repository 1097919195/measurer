package stuido.tsing.iclother.measure;

import android.support.annotation.NonNull;

import com.polidea.rxandroidble.RxBleClient;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.utils.http.measurement.MeasurementHelper;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class MeasurePresenter implements MeasureContract.Presenter {
    @NonNull
    private RxBleClient rxBleClient;
    @NonNull
    private MeasureContract.View measurement_fragment;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscription;

    public MeasurePresenter(@NonNull RxBleClient client, @NonNull MeasureContract.View measure_view,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        rxBleClient = checkNotNull(client);
        measurement_fragment = checkNotNull(measure_view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        measurement_fragment.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscription.clear();
    }

    @Override
    public void saveMeasurement(Measurement measurement) {
        Subscription subscribe = new MeasurementHelper().saveMeasurement(measurement)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(__ -> measurement_fragment.showSuccessSave(),
                        e -> measurement_fragment.showSaveError(),
                        () -> measurement_fragment.setLoadingIndicator(false)
                );
        mSubscription.add(subscribe);
    }
}
