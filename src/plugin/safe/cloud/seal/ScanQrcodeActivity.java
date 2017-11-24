package plugin.safe.cloud.seal;

import com.ryg.dynamicload.internal.DLIntent;
import com.yanzhenjie.zbar.camera.CameraPreview;
import com.yanzhenjie.zbar.camera.ScanCallback;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ScanQrcodeActivity extends BaseActivity {

    private RelativeLayout mScanCropView;
    private ImageView mScanLine;
    private ValueAnimator mScanAnimator;

    private CameraPreview mPreviewView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_capture);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        TextView titlebar = (TextView) findViewById(R.id.id_titlebar);
        titlebar.setText("扫码登陆");
        mPreviewView = (CameraPreview) findViewById(R.id.capture_preview);
        mScanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        mScanLine = (ImageView) findViewById(R.id.capture_scan_line);

        mPreviewView.setScanCallback(resultCallback);
//        mPreviewView.start();
//        if (mPreviewView.start()) {
//            mScanAnimator.start();
//        }
        Button btn = (Button) findViewById(R.id.capture_restart_scan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreviewView.start();
                if (mPreviewView.start()) {
                    mScanAnimator.start();
                }
            }
        });

    }

    /**
     * Accept scan result.
     */
    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(String result) {
            stopScan();
//            Intent intent = new Intent();
//            intent.putExtra("result",result);
//            setResult(RESULT_OK,intent);
            DLIntent intent = new DLIntent();
            intent.putExtra("result",result);
            setResult(RESULT_OK,intent);
            finish();
        }
    };


    @Override
    public void onPause() {
        stopScan();
        super.onPause();
    }


    /**
     * Stop scan.
     */
    private void stopScan() {
        mScanAnimator.cancel();
        mPreviewView.stop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mScanAnimator == null) {
            int height = mScanCropView.getMeasuredHeight() - 25;
            mScanAnimator = ObjectAnimator.ofFloat(mScanLine, "translationY", 0F, height).setDuration(3000);
            mScanAnimator.setInterpolator(new LinearInterpolator());
            mScanAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mScanAnimator.setRepeatMode(ValueAnimator.REVERSE);
        }
    }


}