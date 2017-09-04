package safe.cloud.seal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import safe.cloud.seal.model.ActivityController;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;

public class LoginUserActivity extends BaseActivity{

	private TextView mTitleBar;
	private View mLoadingView;
	private HoursePresenter mPresenter;
	private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private String mCommonServiceAction = "http://tempuri.org/GetAreas";
	private String mUserName, mPassword;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private boolean mValidUserPassword = false;
	private String mPhone , mRealName;;
	private String mUserInfoAction = "http://tempuri.org/GetUserInfo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.aty_seal_login_user); 
		ActivityController.addActivity(LoginUserActivity.this);
		initView();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView(){
	    
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		dismissLoadingView();
		userNameEditText = (EditText)findViewById(R.id.id_login_username);
		passwordEditText = (EditText)findViewById(R.id.id_login_password);
		mUserName = getIntent().getStringExtra("user_name");
		mPassword = getIntent().getStringExtra("user_password");
		if (mUserName != null && !mUserName.equals("")){
			userNameEditText.setText(mUserName);
			mValidUserPassword = true;
		}
		if (mPassword != null && !mPassword.equals("")){
			passwordEditText.setText(mPassword);
		}
		
		final Button login = (Button)findViewById(R.id.id_login_user_button);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mValidUserPassword){
					return;
				}
				mUserName = userNameEditText.getEditableText().toString();
				mPassword = passwordEditText.getEditableText().toString();
				
				if (mUserName == null || mUserName.equals("")){
					GlobalUtil.shortToast(LoginUserActivity.this, getString(R.string.please_input_username), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				if (mPassword == null || mPassword.equals("")){
					GlobalUtil.shortToast(LoginUserActivity.this, getString(R.string.please_input_username), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
//				if (CommonUtil.mUserHost == null || CommonUtil.mUserHost.equals("")){
//					GlobalUtil.shortToast(getApplication(), "您尚未选择所在区域", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
//					return;
//				}
				showLoadingView();
				loginUser();
			}
		});
		if (mUserName != null && mPassword != null){
			login.setBackgroundResource(R.drawable.button_click_clickable);
			login.setTextColor(Color.parseColor("#ffffff"));
		}
		
		userNameEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (passwordEditText.getText().length() > 5 && userNameEditText.getText().length() > 5){
					login.setBackgroundResource(R.drawable.button_click_clickable);
					login.setTextColor(Color.parseColor("#ffffff"));
					mValidUserPassword = true;
				}else{
					login.setBackgroundResource(R.drawable.button_click_unclickable);
					login.setTextColor(Color.parseColor("#888888"));
					mValidUserPassword = false;
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (passwordEditText.getText().length() > 5 && userNameEditText.getText().length() > 5){
					login.setBackgroundResource(R.drawable.button_click_clickable);
					login.setTextColor(Color.parseColor("#ffffff"));
					mValidUserPassword = true;
				}else{
					login.setBackgroundResource(R.drawable.button_click_unclickable);
					login.setTextColor(Color.parseColor("#888888"));
					mValidUserPassword = false;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
				
			}
		});
		Button registerButton = (Button)findViewById(R.id.id_login_user_register);
		Button modifyButton = (Button)findViewById(R.id.id_login_user_modify_password);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (CommonUtil.mUserHost == null || CommonUtil.mUserHost.equals("")){
					GlobalUtil.shortToast(LoginUserActivity.this, "您尚未选择所在区域", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				
				startActivity(new Intent(LoginUserActivity.this, RegisterUserStep1Activity.class));
			}
		});
		modifyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (CommonUtil.mUserHost == null || CommonUtil.mUserHost.equals("")){
					GlobalUtil.shortToast(LoginUserActivity.this, "您尚未选择所在区域", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				//startActivityForResult(new Intent(LoginUserActivity.this, ModifyPasswordActivity.class), 1);
			}
		});
	}
	
	private void loginUser(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=ValidateLogin";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mLoginAction));
		rpc.addProperty("username", mUserName);
		rpc.addProperty("password", mPassword);
		rpc.addProperty("userType", "0");
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mLoginAction, rpc);
		mPresenter.startPresentServiceTask();
	}
	
	
	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1){
			if (data != null){
				mPassword = data.getStringExtra("new_password");
				passwordEditText.setText(mPassword);
				Log.i("mingguo", "login activity  onActivityResult password  "+mPassword);
			}
		}
	}
	
	@SuppressLint("NewApi")
	private void showSelectAlertDialog(final String title, final List<String[]> data) {  
		  AlertDialog.Builder builder =new AlertDialog.Builder(LoginUserActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		  builder.setTitle(title);
		  builder.setIcon(android.R.drawable.ic_dialog_info);
		  builder.setItems(data.get(0), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sharedata = getApplication().getSharedPreferences("user_info", 0);
				SharedPreferences.Editor editor = sharedata.edit();
			    editor.putString("area", data.get(0)[which]);
			    editor.putString("host", data.get(1)[which]);
			    editor.commit();
			    CommonUtil.mUserArea = data.get(0)[which];
			    CommonUtil.mUserHost = data.get(1)[which];
			    Log.w("mingguo", "CommonUtil.mUserArea  "+CommonUtil.mUserArea+"  CommonUtil.mUserHost  "+CommonUtil.mUserHost);
			}
		});
		builder.setCancelable(false);
		builder.show();
		
	}
	
	public static List<String[]> parseCommonService(String value) {
		String [] areaName = null;
		String [] areaHost;
		List<String[]> list = new ArrayList<>();
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				areaName = new String[array.length()];
				areaHost = new String[array.length()];
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					areaName[item] = itemJsonObject.optString("AreaName");
					areaHost[item] = itemJsonObject.optString("RentHost");
				}
				list.add(areaName);
				list.add(areaHost);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}

	private  HashMap<String,String> parseUserInfo(String value) {
		HashMap<String,String> userInfo = null;
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				Log.i("house", "parse house info "+array.length());
				//for (int item = 0; item < array.length(); item++){
					
					JSONObject itemJsonObject = array.optJSONObject(0);
					userInfo = new HashMap<>();
					userInfo.put("NickName", itemJsonObject.optString("NickName"));
					userInfo.put("LoginName", itemJsonObject.optString("LoginName"));
					userInfo.put("Address", itemJsonObject.optString("Address"));
					userInfo.put("IDCard", itemJsonObject.optString("IDCard"));
					userInfo.put("Phone", itemJsonObject.optString("Phone"));
					userInfo.put("QQ", itemJsonObject.optString("QQ"));
					//CommonUtil.mRegisterName = itemJsonObject.optString("RealName");
					//CommonUtil.mRegisterIdcard = itemJsonObject.optString("IDCard");
			}
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return userInfo;
		}
	}
	

	private void getUserInfo(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetUserInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mUserInfoAction));
		rpc.addProperty("username", CommonUtil.mRegisterRealName);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mUserInfoAction, rpc);
		mPresenter.startPresentServiceTask();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100){
				dismissLoadingView();
			    getUserInfo();
			}else if (msg.what == 101){
				dismissLoadingView();
				GlobalUtil.shortToast(LoginUserActivity.this, getString(R.string.login_failed), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
			}else if (msg.what == 110){
//				dismissLoadingView();
//				showSelectAlertDialog("请选择所在区域", parseCommonService((String)msg.obj));
			}else if (msg.what == 111){
				HashMap<String, String> userMap = parseUserInfo((String)msg.obj);
				GlobalUtil.shortToast(LoginUserActivity.this, getString(R.string.login_success), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_yes));
				Intent intent = new Intent(LoginUserActivity.this, HomeActivity.class);
				intent.putExtra("user_name", mUserName);
				intent.putExtra("user_password", mPassword);
				intent.putExtra("user_phone", mPhone);
				intent.putExtra("user_realname", mRealName);
				SharedPreferences sharedata = getApplicationContext().getSharedPreferences("user_info", 0);
				SharedPreferences.Editor editor = sharedata.edit();
			    editor.putString("user_name", mUserName);
			    editor.putString("user_password", mPassword);
			    editor.putString("user_idcard", mPassword);
			    editor.commit();
			    CommonUtil.mUserLoginName = mUserName;
			    CommonUtil.mRegisterIdcard = userMap.get("QQ");
				startActivity(intent);
				finish();
			}
		}
	};
	
	


	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE){
					dismissLoadingView();
					return false;
				}
			}
			return super.onKeyDown(keyCode, event);
		}

	@Override
	public void onStatusStart() {
		
		
	}
	


	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.i("mingguo", "on success  action "+action+"  msg  "+templateInfo);
		//{"ret":"0","UserID":"26","LoginName":"kezhang","Phone":"13920887566","RealName":"刻章长"}
		if (action != null && templateInfo != null){
			if (action.equals(mLoginAction)){
				JSONObject object;
				try {
					object = new JSONObject(templateInfo);
					String ret = (String)object.optString("ret");
					if (ret != null && ret.equals("0")){
						mPhone = object.optString("Phone");
						mRealName = object.optString("RealName");
						Log.w("mingguo", "login user  phone  "+mPhone);
						mHandler.sendEmptyMessage(100);
					}else{
						mHandler.sendEmptyMessage(101);
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				
			}else if (action.equals(mCommonServiceAction)){
				Message msg = mHandler.obtainMessage();
				msg.what = 110;
				msg.obj = templateInfo;
				mHandler.sendMessage(msg);
			}else if (action.equals(mUserInfoAction)){
				Message msg = mHandler.obtainMessage();
				msg.what = 111;
				msg.obj = templateInfo;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	

}
