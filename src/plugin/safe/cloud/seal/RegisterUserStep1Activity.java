package plugin.safe.cloud.seal;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.internal.DLIntent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import plugin.safe.cloud.seal.model.ActivityController;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;


public class RegisterUserStep1Activity extends BaseActivity{

	private TextView mTitleBar;
	private HoursePresenter mPresenter;
	private String mPhone, mPassword, mPasswordAgain, mVerifyCode;
	private String mValidAction = "http://tempuri.org/IsExistsLoginName";
	private boolean mUsernameValid;
	private String mSendVerifyCodeAction = "http://tempuri.org/SendIdentifyCodeMsg";
	private String mCheckVerifyCodeAction = "http://tempuri.org/ValidateIdentifyCode";
	private int mTimeCount = -1;
	private Button mVerifyCodeButton;
	private View mLoadingView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_register_user_step1_layout);
		ActivityController.addActivity(that);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		mTitleBar = (TextView)findViewById(R.id.id_titlebar);
		mTitleBar.setText("手机注册");

		initView();
	}
	
	private void sendPhoneVerifyCode(String phone){
		String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=SendIdentifyCodeMsg";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mSendVerifyCodeAction));
		rpc.addProperty("phone", phone); 
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mSendVerifyCodeAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void initView(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		
		final EditText phone = (EditText)findViewById(R.id.id_register_input_phone);
		final EditText passowrd = (EditText)findViewById(R.id.id_register_input_password);
		final EditText passowrdAgain = (EditText)findViewById(R.id.id_register_input_password_again);
		
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		
		mVerifyCodeButton = (Button)findViewById(R.id.id_register_password_button);
		mVerifyCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPhone = phone.getEditableText().toString();
				if (mPhone == null || mPhone.equals("")){
					Toast.makeText(that, getString(R.string.phone_not_null), Toast.LENGTH_SHORT).show();
					return;
				}else if (mPhone.length() < 11){
					Toast.makeText(that, getString(R.string.phone_input_error), Toast.LENGTH_SHORT).show();
					return;
				}
				Log.w("mingguo", "register step 1  phone  "+mPhone);
				if (mTimeCount < 0){
					mTimeCount = 60;
					sendPhoneVerifyCode(mPhone);
					mHandler.sendEmptyMessage(1000);
				}else{
					return;
				}
				
			}
		});
		
		final EditText inputVerify = (EditText)findViewById(R.id.id_register_input_verify_code);
		Button nextButton = (Button)findViewById(R.id.id_register_user_step1_next);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhone = phone.getEditableText().toString();
				mPassword = passowrd.getEditableText().toString();
				mPasswordAgain = passowrdAgain.getEditableText().toString();
				mVerifyCode = inputVerify.getEditableText().toString();
				
				if (mPhone == null || mPhone.equals("")){
					Toast.makeText(that, getString(R.string.user_name_not_null), Toast.LENGTH_SHORT).show();
					return;
				}
				if (mPassword == null || mPassword.equals("")){
					Toast.makeText(that, getString(R.string.pwd_not_null), Toast.LENGTH_SHORT).show();
					return;
				}else  if (mPassword.length() < 6){
					Toast.makeText(that, getString(R.string.pwd_input_error), Toast.LENGTH_SHORT).show();
					return;
				}
				if (mPasswordAgain == null || mPasswordAgain.equals("") || !mPassword.equals(mPasswordAgain)){
					Toast.makeText(that, getString(R.string.pwd_again_not_same), Toast.LENGTH_SHORT).show();
					return;
				}
				showLoadingView();
				
				checkUserNameValid(mPhone);
				
//				checkPhoneVerifyCode(mPhone, mVerifyCode);

			}
		});
	}
	
	
	
	private void checkPhoneVerifyCode(String phone, String code){
		String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=ValidateIdentifyCode";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mCheckVerifyCodeAction));
		rpc.addProperty("phone", phone); 
		rpc.addProperty("number", code); 
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mCheckVerifyCodeAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}
	
	private void showLoadingView(){
		
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(that, R.anim.anim_rotate);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.w("mingguo", "onActivityResult resultCode  "+resultCode+" requestCode  "+requestCode+"  file  ");
		if (resultCode == RESULT_OK && requestCode == 1) {
			 Log.w("mingguo", "activity result  width data   "+data);
		}
	}

    	
	private void checkUserNameValid(String username){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=IsExistsLoginName";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mValidAction));
		rpc.addProperty("loginName", username); 
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mValidAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}
	
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100){
				dismissLoadingView();
				if (msg.obj != null){
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						String ret = json.optString("ret");
						if (ret != null){
//							if (ret.equals("0")){
//								showLoadingView();
								checkPhoneVerifyCode(mPhone, mVerifyCode);
//							}else{
//								Toast.makeText(that, "用户名已存在！", Toast.LENGTH_SHORT).show();
//							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if (msg.what == 102){
				dismissLoadingView();
				if (msg.obj != null){
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						String ret = json.optString("ret");
						if (ret != null){
							//if (ret.equals("0")){
							DLIntent nextIntent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.RegisterUserStep2Activity");
								nextIntent.putExtra("phone", mPhone);
								nextIntent.putExtra("user_name", mPhone);
								nextIntent.putExtra("user_password", mPassword);
								startPluginActivity(nextIntent);
//							}else{
//								Toast.makeText(that, getString(R.string.verify_error), Toast.LENGTH_SHORT).show();
//							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
								
			}else if (msg.what == 1000){
				if (mTimeCount >= 0){
					mVerifyCodeButton.setTextColor(Color.parseColor("#b2b2b2"));
					mVerifyCodeButton.setText(mTimeCount +" 秒");
					mTimeCount--;
					mHandler.sendEmptyMessageDelayed(1000, 1000);
				}else{
					mVerifyCodeButton.setTextColor(Color.parseColor("#d43c33"));
					mVerifyCodeButton.setText("获取验证码");
				}
			}
			
		}
		
	};
	
	

	
	

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		super.onStatusError(action, error);
		mHandler.sendEmptyMessage(200);
	}

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "on success  action "+action+"  msg  "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mValidAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mCheckVerifyCodeAction)){
				Message message = mHandler.obtainMessage();
				message.what = 102;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
	}

}