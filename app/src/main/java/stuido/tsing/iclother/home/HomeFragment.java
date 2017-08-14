package stuido.tsing.iclother.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import stuido.tsing.iclother.R;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.UserSex;
import stuido.tsing.iclother.data.measure.item.MeasurementFemaleItem;
import stuido.tsing.iclother.data.measure.item.MeasurementItem;
import stuido.tsing.iclother.data.measure.item.MeasurementMaleItem;
import stuido.tsing.iclother.measure.MeasureActivity;
import stuido.tsing.iclother.measurementdetail.MeasurementDetailActivity;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends Fragment implements HomeContract.View {
    private HomeContract.Presenter mPresenter;
    private MeasurementAdapter measurementAdapter;
    private View mNoMeasurementView;
    private ImageView mNoMeasurementIcon;
    private TextView mNoMeasurementMainView;
    private TextView mNoMeasurementAddView;
    private LinearLayout mMeasurementView;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measurement_list_frag, container, false);
        //set up measurement view
        ListView listview = root.findViewById(R.id.measurements_list);
        listview.setAdapter(measurementAdapter);
        mMeasurementView = root.findViewById(R.id.measurementsLL);

        //set up no measurement view
        mNoMeasurementView = root.findViewById(R.id.noMeasurements);
        mNoMeasurementMainView = root.findViewById(R.id.noMeasurementsMain);
        mNoMeasurementIcon = root.findViewById(R.id.noMeasurementsIcon);
        mNoMeasurementAddView = root.findViewById(R.id.noMeasurementsAdd);
        mNoMeasurementAddView.setOnClickListener(__ -> showScanView());

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listview);

        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadMeasurements(false));

        return root;
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
        mMeasurementView.setVisibility(View.VISIBLE);
        mNoMeasurementView.setVisibility(View.GONE);

        measurementAdapter = new MeasurementAdapter(measureList, __ -> mPresenter.openMeasurementDetails(__));
        measurementAdapter.setData(measureList);
    }

    @Override
    public void showScanView() {
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        startActivityForResult(intent, MeasureActivity.REQUEST_ADD_MEASUREMENT);
    }

    @Override
    public void showMeasurementDetailsUi(String measurementId) {
        Intent intent = new Intent(getActivity(), MeasurementDetailActivity.class);
        intent.putExtra(MeasurementDetailActivity.EXTRA_MEASUREMENT_ID, measurementId);
        startActivity(intent);
    }

    @Override
    public void showLoadingMeasurementError() {
        setLoadingIndicator(false);
        showMessage(getString(R.string.loading_measurement_error));
    }

    @Override
    public void showNoMeasurementView() {
        String mainText = getString(R.string.no_measurements_all);
        int iconRes = R.drawable.ic_assignment_turned_in_24dp;
        boolean showAddView = true;

        mMeasurementView.setVisibility(View.GONE);
        mNoMeasurementView.setVisibility(View.VISIBLE);

        mNoMeasurementMainView.setText(mainText);
        mNoMeasurementIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoMeasurementAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
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

    private static class MeasurementAdapter extends BaseAdapter {
        private List<Measurement> measurementList;
        private MeasurementItemListener measurementItemListener;

        public MeasurementAdapter(List<Measurement> list, MeasurementItemListener listener) {
            setData(list);
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

        public void replaceData(List<Measurement> list) {
            setData(list);
            notifyDataSetChanged();
        }

        private void setData(List<Measurement> list) {
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

            ((TextView) rowView.findViewById(R.id.measurement_item_user)).setText(measurement.getUser().getNickname());
            MeasurementItem measurementItem;
            if (measurement.getUser().getSex() == UserSex.MALE) {
                measurementItem = new Gson().fromJson(measurement.getmData(), MeasurementMaleItem.class);
            } else {
                measurementItem = new Gson().fromJson(measurement.getmData(), MeasurementFemaleItem.class);
            }
//            ((TextView) rowView.findViewById(R.id.measurement_item_user_xw)).setText(measurementItem.getXiongwei());
//            ((TextView) rowView.findViewById(R.id.measurement_item_user_yw)).setText(measurementItem.getYaowei());
//            ((TextView) rowView.findViewById(R.id.measurement_item_user_tw)).setText(measurementItem.getTunwei());

            rowView.setOnClickListener(__ -> measurementItemListener.onMeasurementClick(measurement));

            return rowView;
        }
    }

    public interface MeasurementItemListener {
        void onMeasurementClick(Measurement clickMeasurement);
    }
}