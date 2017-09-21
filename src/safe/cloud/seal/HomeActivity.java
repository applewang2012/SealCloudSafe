package safe.cloud.seal;


import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import safe.cloud.seal.fragment.MyFragment;
import safe.cloud.seal.fragment.SealReceiverFragment;
import safe.cloud.seal.fragment.SealStatusFragment;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;

public class HomeActivity extends BaseActivity {

	private HoursePresenter mPresenter;
	//private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private String mUpdateAction="http://tempuri.org/CheckUpgrade";
	private String mUserName, mPassword;
	private String mUserInfoString = null;
	private String mCity = null;
	private int mVersionCode = -1;
	private SealStatusFragment mStatusFragment;
	private SealReceiverFragment mReceiverFragment;
	private MyFragment mSelfFragment;
	private String mPhone;
	private String mRealName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_home_layout); 
		
		mUserName = getIntent().getStringExtra("user_name");
		mPassword = getIntent().getStringExtra("user_password");
		mPhone = getIntent().getStringExtra("user_phone");
		mRealName = getIntent().getStringExtra("user_realname");
		initView();
		checkVersionUpdate();
	}
	
	private void initView(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		if (mStatusFragment == null){
			mStatusFragment = new SealStatusFragment();
			Bundle args = new Bundle();  
		    args.putString("phone", mPhone);  
		    mStatusFragment.setArguments(args);
			fragmentTransaction.add(R.id.id_home_content, mStatusFragment);
			fragmentTransaction.commitAllowingStateLoss();
		}else{
			fragmentTransaction.show(mStatusFragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
		
		Button addSealButton = (Button)findViewById(R.id.id_home_tab_add_sign_button);
		addSealButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, ApplyForSealActivity.class));
			}
		});
		
		LinearLayout selfCenter = (LinearLayout)findViewById(R.id.id_home_seal_self_center);
		final ImageView myIcon = (ImageView)findViewById(R.id.id_home_seal_self_center_icon);
		final TextView myText = (TextView)findViewById(R.id.id_home_seal_self_center_text);
		
		LinearLayout sealReceiver = (LinearLayout)findViewById(R.id.id_home_seal_receive);
		final ImageView sealReceiverIcon = (ImageView)findViewById(R.id.id_home_seal_receive_icon);
		final TextView sealReceiverText = (TextView)findViewById(R.id.id_home_seal_receive_text);
		
		LinearLayout searchSeal = (LinearLayout)findViewById(R.id.id_home_seal_search);
		final ImageView searchSealIcon = (ImageView)findViewById(R.id.id_home_seal_search_icon);
		final TextView searchSealText = (TextView)findViewById(R.id.id_home_seal_search_text);
		
		LinearLayout sealStatus = (LinearLayout)findViewById(R.id.id_home_seal_status);
		final ImageView statusIcon = (ImageView)findViewById(R.id.id_home_seal_status_icon);
		final TextView statusText = (TextView)findViewById(R.id.id_home_seal_status_text);
		sealStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				hideAllFragments(fragmentTransaction);
				if (mStatusFragment == null){
					mStatusFragment = new SealStatusFragment();
					Bundle args = new Bundle();  
				    args.putString("phone", mPhone);  
				    Log.i("mingguo", "user phone  "+mPhone);
				    mStatusFragment.setArguments(args);
					fragmentTransaction.add(R.id.id_home_content, mStatusFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}else{
					fragmentTransaction.show(mStatusFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}
				statusIcon.setBackgroundResource(R.drawable.home_tab_sign_status_press);
				statusText.setTextColor(Color.parseColor("#d43c33"));
				sealReceiverIcon.setBackgroundResource(R.drawable.home_tab_receive_normal);
				sealReceiverText.setTextColor(Color.parseColor("#999999"));
				searchSealIcon.setBackgroundResource(R.drawable.home_tab_search_normal);
				searchSealText.setTextColor(Color.parseColor("#999999"));
				myIcon.setBackgroundResource(R.drawable.home_tab_self_normal);
				myText.setTextColor(Color.parseColor("#999999"));
				
			}
		});
		
		sealReceiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				hideAllFragments(fragmentTransaction);
				if (mReceiverFragment == null){
					mReceiverFragment = new SealReceiverFragment();
					Bundle args = new Bundle();  
				    args.putString("phone", mPhone);  
				    Log.i("mingguo", "user phone  "+mPhone);
				    mReceiverFragment.setArguments(args);
					fragmentTransaction.add(R.id.id_home_content, mReceiverFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}else{
					fragmentTransaction.show(mReceiverFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}
				statusIcon.setBackgroundResource(R.drawable.home_tab_sign_status_normal);
				statusText.setTextColor(Color.parseColor("#999999"));
				sealReceiverIcon.setBackgroundResource(R.drawable.home_tab_receive_press);
				sealReceiverText.setTextColor(Color.parseColor("#d43c33"));
				searchSealIcon.setBackgroundResource(R.drawable.home_tab_search_normal);
				searchSealText.setTextColor(Color.parseColor("#999999"));
				myIcon.setBackgroundResource(R.drawable.home_tab_self_normal);
				myText.setTextColor(Color.parseColor("#999999"));
			}
		});
		
		searchSeal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//				hideAllFragments(fragmentTransaction);
//				if (mStatusFragment == null){
//					mStatusFragment = new SealStatusFragment();
//					Bundle args = new Bundle();  
//				    args.putString("phone", mPhone);  
//				    Log.i("mingguo", "user phone  "+mPhone);
//				    mStatusFragment.setArguments(args);
//					fragmentTransaction.add(R.id.id_home_content, mStatusFragment);
//					fragmentTransaction.commitAllowingStateLoss();
//				}else{
//					fragmentTransaction.show(mStatusFragment);
//					fragmentTransaction.commitAllowingStateLoss();
//				}
				statusIcon.setBackgroundResource(R.drawable.home_tab_sign_status_normal);
				statusText.setTextColor(Color.parseColor("#999999"));
				sealReceiverIcon.setBackgroundResource(R.drawable.home_tab_receive_normal);
				sealReceiverText.setTextColor(Color.parseColor("#999999"));
				searchSealIcon.setBackgroundResource(R.drawable.home_tab_search_press);
				searchSealText.setTextColor(Color.parseColor("#d43c33"));
				myIcon.setBackgroundResource(R.drawable.home_tab_self_normal);
				myText.setTextColor(Color.parseColor("#999999"));
			}
		});
		
		
		selfCenter.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				hideAllFragments(fragmentTransaction);
				if (mSelfFragment == null){
					mSelfFragment = new MyFragment();
					Bundle args = new Bundle();  
				    args.putString("phone", mPhone);  
				    args.putString("realname", mRealName);
				    mSelfFragment.setArguments(args);
					fragmentTransaction.add(R.id.id_home_content, mSelfFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}else{
					fragmentTransaction.show(mSelfFragment);
					fragmentTransaction.commitAllowingStateLoss();
				}
				statusIcon.setBackgroundResource(R.drawable.home_tab_sign_status_normal);
				statusText.setTextColor(Color.parseColor("#999999"));
				sealReceiverIcon.setBackgroundResource(R.drawable.home_tab_receive_normal);
				sealReceiverText.setTextColor(Color.parseColor("#999999"));
				searchSealIcon.setBackgroundResource(R.drawable.home_tab_search_normal);
				searchSealText.setTextColor(Color.parseColor("#999999"));
				myIcon.setBackgroundResource(R.drawable.home_tab_self_press);
				myText.setTextColor(Color.parseColor("#d43c33"));
			}
		});
	}
	
	
	
	private void hideAllFragments(FragmentTransaction transaction) {
		if (mStatusFragment != null && !mStatusFragment.isHidden()) {
			transaction.hide(mStatusFragment);
		}
		if (mReceiverFragment != null && !mReceiverFragment.isHidden()) {
			transaction.hide(mReceiverFragment);
		}
		if (mSelfFragment != null && !mSelfFragment.isHidden()) {
			transaction.hide(mSelfFragment);
		}
	}
	
	private  void parseUpdateVersion(String value) {
		try{
			if (value != null){
				//{"Result":"1","AppId":"0","PackageName":"tenant.guardts.house","VersionID":"2","MSG":"Success","IsEnforced":"True",
					//"APKUrl":"UpgradeFolder\\APK20170731135631.apk","IOSUrl":"","CreatedDate":"2017-07-31 13:56:32"}
					JSONObject itemJsonObject = new JSONObject(value);
					String versionId = itemJsonObject.optString("VersionID");
					if (versionId != null){
						int versionCode = Integer.parseInt(versionId);
						if (versionCode > mVersionCode){
							String downloadUrl = itemJsonObject.optString("APKUrl");
							if (downloadUrl != null && downloadUrl.length() > 5){
								CommonUtil.DOWLOAD_URL = CommonUtil.UPDATE_VERSION_HOST+downloadUrl;
							}
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showUpdateVersionAlertDialog() {  
		if (CommonUtil.DOWLOAD_URL == null || CommonUtil.DOWLOAD_URL.equals("")){
			Log.w("mingguo", "home activity  delete installed file  "+GlobalUtil.deleteInstalledApkFile());
			return;
		}
		
		  AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		  builder.setTitle("升级安云印");
		  builder.setIcon(android.R.drawable.ic_dialog_info);
		  builder.setPositiveButton(getString(R.string.button_ok),new DialogInterface.OnClickListener() {
		         @Override  
		  
		         public void onClick(DialogInterface dialog, int which) {
		        	 startActivity(new Intent(HomeActivity.this, DownloadAppActivity.class));
		        	 finish();
		         }  
			
		});
		builder.setCancelable(false);
		builder.show();
	}
	
	private void checkVersionUpdate(){
		mVersionCode = GlobalUtil.getVersionCode(getApplicationContext());
		String url = "http://www.guardts.com/UpgradeService/SystemUpgradeService.asmx?op=CheckUpgrade";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mUpdateAction));
		rpc.addProperty("packageName", GlobalUtil.getPackageName(getApplicationContext()));
		rpc.addProperty("versionId", GlobalUtil.getVersionCode(getApplicationContext()));
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mUpdateAction, rpc);
		mPresenter.startPresentServiceTask();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 200){
				if (msg.obj != null){
					parseUpdateVersion((String)msg.obj);
					showUpdateVersionAlertDialog();
				}
			}
		}
	};

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.i("mingguo", "on success  action "+action+"  msg  "+templateInfo);
//		if (action != null && templateInfo != null){}
//			if (action.equals(mUserInfoAction)){
//				Message message = mHandler.obtainMessage();
//				message.what = 100;
//				message.obj = templateInfo;
//				mHandler.sendMessage(message);
			if (action.equals(mUpdateAction)){
				Message message = mHandler.obtainMessage();
				message.what = 200;
				message.obj = templateInfo;
				mHandler.sendMessageDelayed(message, 500);
			}
	}
	
	


}
