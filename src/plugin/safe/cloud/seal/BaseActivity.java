package plugin.safe.cloud.seal;

import com.ryg.dynamicload.DLBasePluginActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import plugin.safe.cloud.seal.presenter.DataStatusInterface;


public class BaseActivity extends DLBasePluginActivity implements DataStatusInterface {

	private int mRequestCode;
	private void showActivityLoadingView(Context ctx, View loadingView){
		if (loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
			ImageView imageView = (ImageView) loadingView.findViewById(R.id.id_progressbar_img);
			if (imageView != null) {
				RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(ctx, R.anim.anim_rotate);
				imageView.startAnimation(rotate);
			}
		}
	}
	private void dismissActivityLoadingView(View loadingView){
		if (loadingView != null) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}

	private Handler mHandler = new Handler(){

		private View mBaseLoadingView;

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 900){
				dismissActivityLoadingView(mBaseLoadingView);
				Toast.makeText(BaseActivity.this, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
			}else if (msg.what == 1000){
				Activity parentActivity = (Activity)msg.obj;
				mBaseLoadingView = parentActivity.findViewById(R.id.id_data_loading);
				showActivityLoadingView(parentActivity, mBaseLoadingView);
			}else if (msg.what == 800){
				dismissActivityLoadingView(mBaseLoadingView);
			}

		}
		
	};
	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Message msg = mHandler.obtainMessage();
		msg.what = 800;
		mHandler.sendMessage(msg);
		if (isFinishing()){
			templateInfo = null;
		}
		
	}

	@Override
	public void onStatusStart(Activity loading) {
		Message msg = mHandler.obtainMessage();
		msg.what = 1000;
		msg.obj = loading;
		mHandler.sendMessage(msg);
		
	}

	@Override
	public void onStatusError(String action, String error) {
		Message msg = mHandler.obtainMessage();
		msg.what = 900;
		msg.obj = action + " " + error;
		mHandler.sendMessage(msg);
	}
	
	public void backFinish(View view){
		finish();
	}
	

	/*protected void performRequestPermissions(String desc, String[] permissions, int requestCode, PermissionsResultListener listener) {
		if (permissions == null || permissions.length == 0) {
			return;
		}
		mRequestCode = requestCode;
		mListener = listener;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkEachSelfPermission(permissions)) {// 检查是否声明了权限
				requestEachPermissions(desc, permissions, requestCode);
			} else {// 已经申请权限
				if (mListener != null) {
					mListener.onPermissionGranted();
				}
			}
		} else {
			if (mListener != null) {
				mListener.onPermissionGranted();
			}
		}
	}

	*//**
	 * 检察每个权限是否申请
	 *
	 * @param permissions
	 * @return true 需要申请权限,false 已申请权限
	 *//*
	private boolean checkEachSelfPermission(String[] permissions) {
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				return true;
			}
		}
		return false;
	}

	*//**
	 * 申请权限前判断是否需要声明
	 *
	 * @param desc
	 * @param permissions
	 * @param requestCode
	 *//*
	private void requestEachPermissions(String desc, String[] permissions, int requestCode) {
		if (shouldShowRequestPermissionRationale(permissions)) {// 需要再次声明
			showRationaleDialog(desc, permissions, requestCode);
		} else {
			ActivityCompat.requestPermissions(that, permissions, requestCode);
		}
	}

	*//**
	 * 再次申请权限时，是否需要声明
	 *
	 * @param permissions
	 * @return
	 *//*
	private boolean shouldShowRequestPermissionRationale(String[] permissions) {
		for (String permission : permissions) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
				return true;
			}
		}
		return false;
	}*/

}
