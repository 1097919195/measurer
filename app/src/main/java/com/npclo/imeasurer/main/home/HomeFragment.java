package com.npclo.imeasurer.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.zxing.client.android.decode.CaptureActivity;
import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.base.BaseFragment;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.npclo.imeasurer.main.measure.MeasureFragment;
import com.npclo.imeasurer.main.measure.MeasurePresenter;
import com.npclo.imeasurer.user.UserActivity;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.scan_img)
    ImageView scanImg;
    @BindView(R.id.scan_hint)
    TextView scanHint;
    @BindView(R.id.base_toolbar)
    Toolbar toolbarBase;
    @BindView(R.id.ble_state)
    TextView bleState;
    Unbinder unbinder;
    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.input_byte)
    EditText inputByte;
    @BindView(R.id.byte_result)
    TextView byteResult;
    @BindView(R.id.btn_result)
    Button btnResult;
    private HomeContract.Presenter mPresenter;
    private MaterialDialog dialog;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_frag;
    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        initToolbar();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.scan_img, R.id.scan_hint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_img:
            case R.id.scan_hint:
                startScan();
                break;
        }
    }

    private void startScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        String result = bundle.getString("result");
        switch (resultCode) {
            case 1:
                toMeasure(result);
                break;
            case 2:
                mPresenter.getUserInfoWithCode(result);
                break;
        }
    }

    /**
     * 初始化toolbar的一些默认属性
     */
    protected void initToolbar() {
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.toolbar_text));//设置主标题颜色
        toolbarBase.inflateMenu(R.menu.base_toolbar_menu);
        Intent intent = new Intent(getActivity(), UserActivity.class);
        toolbarBase.getMenu().getItem(0).setIntent(intent);
        // TODO: 2017/9/7 蓝牙未连接时，点击未连接按钮前往连接

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        RxBleDevice rxBleDevice = BaseApplication.getRxBleDevice(getActivity());
        if (rxBleDevice != null && rxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED) {
            Log.e(TAG, "获取到蓝牙状态" + rxBleDevice.toString());
            bleState.setText(getString(R.string.connected));
            bleState.setTextColor(getResources().getColor(R.color.green));
            bleState.setEnabled(false);//TODO 未连接按钮不能再点击前往连接
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showLoading(boolean b) {
        if (b) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .progress(true, 100)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void showGetInfoSuccess(WechatUser user) {
        showLoading(false);
        String s = new Gson().toJson(user);
        toMeasure(s);
    }

    private void toMeasure(String s) {
        MeasureFragment measureFragment = findFragment(MeasureFragment.class);
        if (measureFragment == null) {
            measureFragment = MeasureFragment.newInstance();
            Bundle bundle = new Bundle();
//            s = "{\"openID\":\"oaym60wV0nUG2KUmD84enwm5CAzE\",\"gender\":0,\"height\":\"170\"," +
//                    "\"weight\":\"60\",\"name\":\"MapleImage\",\"nickname\":\"MapleImage\"," +
//                    "\"avatar\":\"http:\\/\\/wx.qlogo.cn\\/mmopen\\/vi_32\\/icBY913leXwE7HP1pqlQnt7sJJwLL" +
//                    "VguLicMTvPYsDUHRtp0qNuKHrpPeUJJgNpsfxg62k5ENTl8NPhsiceWRKMSg\\/0\"}";

            bundle.putString("user", s);
            measureFragment.setArguments(bundle);
            measureFragment.setPresenter(new MeasurePresenter(measureFragment, SchedulerProvider.getInstance()));
            start(measureFragment, SINGLETASK);
        }
    }

    @Override
    public void showGetInfoError(Throwable e) {
        super.handleError(e, TAG);
    }

    @Override
    public void showCompleteGetInfo() {
        showLoading(false);
    }


    // TODO: 2017/9/7 test 开始
    @OnClick(R.id.btn_result)
    public void onViewClicked() {
        String s = inputByte.getText().toString();
        int code = Integer.parseInt("8D6A", 16);
        int length = Integer.parseInt(s.substring(0, 4), 16);
        int angle = Integer.parseInt(s.substring(4, 8), 16);
        int battery = Integer.parseInt(s.substring(8, 12), 16);
        int a1 = length ^ code;
        int a2 = angle ^ code;
        int a3 = battery ^ code;
        byteResult.setText("length: " + a1 + " battery: " + a2 + " angle: " + a3);
    }
}