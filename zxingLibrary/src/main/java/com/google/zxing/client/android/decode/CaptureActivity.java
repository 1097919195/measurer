package com.google.zxing.client.android.decode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.view.ViewfinderView;

import java.io.IOException;
import java.util.Collection;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final int ENTER_OK = 2;


    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private TextView toolbarTitle;
    private LinearLayout view_enter_qrcode;
    private RelativeLayout to_manual_enter_qrcode;
    private SurfaceView surfaceView;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getDrawable(R.mipmap.left));
        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        MenuItem item = toolbar.getMenu().getItem(0);
        toolbarTitle = ((TextView) findViewById(R.id.toolbar_title));
        toolbarTitle.setText("扫描二维码");
        item.setTitle("帮助");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(CaptureActivity.this, "前往帮助界面", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/3 返回到上一个界面 ，把这个库整合到项目中
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderView);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer.onResume();

        decodeFormats = null;
        characterSet = null;

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        //手动输入二维码编号

        view_enter_qrcode = (LinearLayout) findViewById(R.id.view_enter_qrcode);
        to_manual_enter_qrcode = (RelativeLayout) findViewById(R.id.to_manual_enter_qrcode);
        findViewById(R.id.enter_qrcode_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterCodeView();
            }
        });
        findViewById(R.id.enter_qrcode_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterCodeView();
            }
        });
        AppCompatButton btnEnterCode = (AppCompatButton) findViewById(R.id.action_enter_qrcode);
        final EditText inputQrcode = (EditText) findViewById(R.id.input_qrcode);
        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = inputQrcode.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    // TODO: 2017/9/1 发起请求获取的待测用户信息
                    bundle.putString("result", code);
                    resultIntent.putExtras(bundle);
                    CaptureActivity.this.setResult(ENTER_OK, resultIntent);
                } else {
                    Toast.makeText(CaptureActivity.this, getString(R.string.plz_enter_qrcode), Toast.LENGTH_SHORT).show();
                }
                CaptureActivity.this.finish();
            }
        });

        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    private void showEnterCodeView() {
        surfaceView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.GONE);
        view_enter_qrcode.setVisibility(View.VISIBLE);
        toolbarTitle.setText("输入二维码编号");
        to_manual_enter_qrcode.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d("wxl", "rawResult=" + rawResult);

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            String resultString = rawResult.getText();
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            resultIntent.putExtras(bundle);
            this.setResult(1, resultIntent);
        } else {
            Toast.makeText(CaptureActivity.this, "扫描失败，请重试", Toast.LENGTH_SHORT).show();
        }
        CaptureActivity.this.finish();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
}
