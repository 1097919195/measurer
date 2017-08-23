package stuido.tsing.iclother.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.UserSex;
import stuido.tsing.iclother.measure.MeasureActivity;
import stuido.tsing.iclother.measurementdetail.MeasurementDetailActivity;

import static android.content.ContentValues.TAG;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends Fragment implements HomeContract.View {
    @BindView(R.id.measurements_list)
    ListView measurementsList;
    @BindView(R.id.measurementsLL)
    LinearLayout measurementsLL;
    @BindView(R.id.noMeasurementsIcon)
    ImageView noMeasurementsIcon;
    @BindView(R.id.noMeasurementsMain)
    TextView noMeasurementsMain;
    @BindView(R.id.noMeasurementsAdd)
    TextView noMeasurementsAdd;
    @BindView(R.id.noMeasurements)
    LinearLayout noMeasurements;
    Unbinder unbinder;
    @BindView(R.id.measurementsContainer)
    RelativeLayout measurementsContainer;
    @BindView(R.id.refresh_layout)
    ScrollChildSwipeRefreshLayout refreshLayout;
    @BindView(R.id.today_measurement)
    TextView today_measurement;
    private HomeContract.Presenter mPresenter;
    private MeasurementAdapter measurementAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measurement_list_frag, container, false);
        unbinder = ButterKnife.bind(this, root);
        //set up measurement view
        measurementsList.setAdapter(measurementAdapter);
        today_measurement.setText(getString(R.string.today_measurement) + ": (" + getDate() + ")");
        //set up no measurement view
        noMeasurements.setOnClickListener(__ -> showScanButton());
        // Set up progress indicator
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        refreshLayout.setScrollUpChild(measurementsList);
        refreshLayout.setOnRefreshListener(() -> mPresenter.loadMeasurements(false));

        return root;
    }

    @NonNull
    private String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        return year + "年" + month + "月" + day + "日";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measurementAdapter = new MeasurementAdapter(new ArrayList<>(), __ -> mPresenter.openMeasurementDetails(__));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showMeasurementList(List<Measurement> measureList) {
        measurementAdapter.replaceData(measureList);

        measurementsLL.setVisibility(View.VISIBLE);
        noMeasurements.setVisibility(View.GONE);
    }

    @Override
    public void showScanButton() {
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        startActivityForResult(intent, MeasureActivity.REQUEST_ADD_MEASUREMENT);
    }

    @Override
    public void showMeasurementDetail(String measurementId) {
        Intent intent = new Intent(getActivity(), MeasurementDetailActivity.class);
        intent.putExtra(MeasurementDetailActivity.EXTRA_MEASUREMENT_ID, measurementId);
        startActivity(intent);
    }

    @Override
    public void showLoadingMeasurementError(Throwable e) {
        setLoadingIndicator(false);
        showMessage(getString(R.string.loading_measurement_error));
        Log.e(TAG, "测量数据加载错误：" + e.getMessage());
    }

    @Override
    public void showNoMeasurementView(boolean showAddView) {
        String mainText = getString(R.string.no_measurements_all);
        int iconRes = R.drawable.ic_assignment_turned_in_24dp;

        measurementsLL.setVisibility(View.GONE);
        noMeasurements.setVisibility(View.VISIBLE);

        noMeasurementsMain.setText(mainText);
        noMeasurementsIcon.setImageDrawable(getResources().getDrawable(iconRes));
        noMeasurementsAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.save_measurement_success));
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class MeasurementAdapter extends BaseAdapter {
        private List<Measurement> measurementList;
        private MeasurementItemListener measurementItemListener;

        private MeasurementAdapter(List<Measurement> list, MeasurementItemListener listener) {
            setList(list);
            measurementItemListener = listener;
        }

        @Override
        public int getCount() {
            return measurementList.size();
        }

        @Override
        public Measurement getItem(int i) {
            return measurementList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private void replaceData(List<Measurement> list) {
            setList(list);
            notifyDataSetChanged();
        }

        private void setList(List<Measurement> list) {
            measurementList = checkNotNull(list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.measurement_list_item, viewGroup, false);
            }

            final Measurement measurement = getItem(i);

            ((TextView) rowView.findViewById(R.id.measurement_item_user_name)).setText(measurement.getUser().getNickname());
            TextView gender = rowView.findViewById(R.id.measurement_item_user_gender);
            if (measurement.getUser().getSex() == UserSex.MALE) {
                gender.setText("男");
            } else {
                gender.setText("女");
            }
            ((TextView) rowView.findViewById(R.id.measurement_item_user_xw)).setText(measurement.getmData().getItemJ().getValue());
            ((TextView) rowView.findViewById(R.id.measurement_item_user_yw)).setText(measurement.getmData().getItemM().getValue());
            ((TextView) rowView.findViewById(R.id.measurement_item_user_tw)).setText(measurement.getmData().getItemQ().getValue());

            rowView.setOnClickListener(__ -> measurementItemListener.onMeasurementClick(measurement));

            return rowView;
        }
    }

    private interface MeasurementItemListener {
        void onMeasurementClick(Measurement clickMeasurement);
    }
}