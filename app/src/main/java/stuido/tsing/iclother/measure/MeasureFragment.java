package stuido.tsing.iclother.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import stuido.tsing.iclother.R;
import stuido.tsing.iclother.data.measure.Measurement;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/8/1.
 */

public class MeasureFragment extends Fragment implements MeasureContract.View {
    @BindView(R.id.radio_male)
    RadioButton radioMale;
    @BindView(R.id.radio_female)
    RadioButton radioFemale;
    @BindView(R.id.sex_rg)
    RadioGroup sexRadioGroup;
    @BindView(R.id.measure_height_input)
    EditText measureHeightInput;
    @BindView(R.id.measure_weight_input)
    EditText measureWeightInput;
    @BindView(R.id.scan_device)
    AppCompatButton scanDevice;
    @BindView(R.id.disconnect_device)
    AppCompatButton disconnectDevice;
    @BindView(R.id.ruler_state)
    TextView rulerState;
    @BindView(R.id.ruler_battery)
    TextView rulerBattery;
    @BindView(R.id.measure_button)
    AppCompatButton measureButton;
    @BindView(R.id.xiongwei_et)
    EditText xiongweiEt;
    @BindView(R.id.yaowei_et)
    EditText yaoweiEt;
    @BindView(R.id.tunwei_et)
    EditText tunweiEt;
    @BindView(R.id.save_measure_result)
    AppCompatButton saveMeasureResult;
    Unbinder unbinder;
    private MeasureContract.Presenter mPresenter;
    private Map<String, String> mData = new LinkedHashMap<>();

    public MeasureFragment() {
    }

    public static MeasureFragment newInstance() {
        return new MeasureFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.measure_frag, container, false);

        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void setPresenter(MeasureContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.scan_device, R.id.disconnect_device, R.id.measure_button, R.id.save_measure_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_device:
                break;
            case R.id.disconnect_device:
                break;
            case R.id.measure_button:
                break;
            case R.id.save_measure_result:
                saveMeasurement();
                break;
        }
    }

    private void saveMeasurement() {
        String height = measureHeightInput.getText().toString();
        checkInputValid(height, "身高");
        String weight = measureWeightInput.getText().toString();
        checkInputValid(weight, "体重");
        String xiongwei = xiongweiEt.getText().toString();
        checkInputValid(xiongwei, "胸围");
        String yaowei = yaoweiEt.getText().toString();
        checkInputValid(yaowei, "腰围");
        String tunwei = tunweiEt.getText().toString();
        checkInputValid(tunwei, "臀围");
        mData.put("xiongwei", xiongwei);
        mData.put("yaowei", yaowei);
        mData.put("tunwei", tunwei);
        int sex = 0;
        if (sexRadioGroup.getCheckedRadioButtonId() == radioFemale.getId()) {
            sex = 1;
        }

        String data = new GsonBuilder().enableComplexMapKeySerialization().create().toJson(mData);
        Measurement measurement = new Measurement("11", data, sex);
        mPresenter.saveMeasurement(measurement);
    }

    @Override
    public void showSuccessSave() {

    }

    @Override
    public void showSaveError() {

    }

    @Override
    public void setLoadingIndicator(boolean b) {

    }

    private void checkInputValid(String v, String field) {
        if (TextUtils.isEmpty(v)) showInputError(field);
    }

    private void showInputError(String field) {
        Toast.makeText(getActivity(), field + "不能为空", Toast.LENGTH_SHORT).show();
    }
}
