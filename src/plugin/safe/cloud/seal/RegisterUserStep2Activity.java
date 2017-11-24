package plugin.safe.cloud.seal;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.internal.DLIntent;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Intent;
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
import plugin.seal.common.Common;
import plugin.seal.common.InterfaceManager;
import plugin.seal.common.ShieldSdkInterface;


public class RegisterUserStep2Activity extends BaseActivity{

	private TextView mTitleBar;
	private HoursePresenter mPresenter;
	private String mPhone;
	private String mPassword, mPasswordIndentify;
	private String mValidIDcardAction = "http://tempuri.org/IsExistsIDCard";
	private boolean mUsernameValid;
	private View mLoadingView;
	private String mRealName, mIdCard;
	private String mRegisterAction = "http://tempuri.org/AddUserInfo";
	private String mIdentifyStyle = "1";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_register_user_step2_layout);
		ActivityController.addActivity(that);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		mTitleBar = (TextView)findViewById(R.id.id_titlebar);
		mTitleBar.setText("实名认证");
		mPhone = getIntent().getStringExtra("phone");
		mPassword = getIntent().getStringExtra("user_password");
		requestLiveIdentifyStyle();
		initView();
		//initHandler();
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w("mingguo","register step2 onResume  ");
		dismissLoadingView();
		if (Common.SHIELD_IDENTIFY_STATUS == 0){
			showLoadingView();
			registerUserName();
		}else if (Common.SHIELD_IDENTIFY_STATUS == 1){
			
		}
	}

	private void requestLiveIdentifyStyle(){
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("http://39.106.19.37/seal/live/style").build();

		Call call = client.newCall(request);
//请求加入调度
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {

			}


			@Override
			public void onResponse(Response arg0) throws IOException {
				// TODO Auto-generated method stub
				String htmlStr =  arg0.body().string();
				Log.w("mingguo","register step2 onResponse  "+htmlStr);
				if (htmlStr != null){
					try {
						JSONObject object = new JSONObject(htmlStr);
						mIdentifyStyle = object.optString("ret");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void checkUserIDcardValid(String idcard){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=IsExistsIDCard";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mValidIDcardAction));
		rpc.addProperty("idcard", idcard); 
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mValidIDcardAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}


	private void initView(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		
		final EditText realName = (EditText)findViewById(R.id.id_register_step2_real_username);
		final EditText idCard = (EditText)findViewById(R.id.id_register_step2_idcard);
		
		Button registerButton = (Button)findViewById(R.id.id_register_user_step2_next);
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRealName = realName.getEditableText().toString();
				mIdCard = idCard.getEditableText().toString();
				if (mRealName == null || mRealName.equals("")){
					Toast.makeText(that, getString(R.string.surface_name_not_null), Toast.LENGTH_SHORT).show();
					return;
				}
				if (mIdCard == null || mIdCard.equals("")){
					Toast.makeText(that,getString(R.string.id_card_not_null) , Toast.LENGTH_SHORT).show();
					return;
				}else if (mIdCard.length() < 18){
					Toast.makeText(that,getString(R.string.id_card_input_error) , Toast.LENGTH_SHORT).show();
					return;
				}
				showLoadingView();
				checkUserIDcardValid(mIdCard);
			}
		});
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
		dismissLoadingView();
		if (resultCode == RESULT_OK && requestCode == 1) {
//			 Log.w("mingguo", "activity result  width data   "+data);
//			 mSubHandler.sendEmptyMessage(1000);
			if (data != null){
				Bundle bundle = data.getExtras();
				if (bundle != null){
					String result = bundle.getString("result");
					if (result != null){
						showLoadingView();
						registerUserName();
						//Toast.makeText(that, "register2 onActivityResult  "+result, Toast.LENGTH_LONG).show();
					}
				}
			}

		}else{
			Toast.makeText(that, "实名认证失败，请重试！", Toast.LENGTH_SHORT).show();
		}
	}

	private void registerUserName(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=AddUserInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mRegisterAction));
		rpc.addProperty("loginName", mPhone);
		rpc.addProperty("password", mPassword);
		rpc.addProperty("userType", "0");
		rpc.addProperty("realName", mRealName);
		rpc.addProperty("title", "title");
		rpc.addProperty("sex", "male");
		rpc.addProperty("phone", mPhone);
		rpc.addProperty("fax", "fax");
		rpc.addProperty("email", "email");
		rpc.addProperty("idcard", mIdCard);
		rpc.addProperty("nickName", "nick");
		rpc.addProperty("address", "address");
		rpc.addProperty("status", "0"); //

		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mRegisterAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}
	
    	
	
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissLoadingView();
			if (msg.what == 100){
				if (msg.obj != null){
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						String ret = json.optString("ret");
						if (ret != null){
//							if (ret.equals("0")){
//								showLoadingView();
								/*AuthenticationUtil.startLive(that, LiveStartActivity.class, liveLevel, liveList, liveCount);*/
//							}else{
//								Toast.makeText(that, "该身份证号已被注册！", Toast.LENGTH_SHORT).show();
//							}

//
							showLoadingView();
							mIdentifyStyle = "1";
							
//							DLIntent intent = new DLIntent("cn.cloudwalk.libproject", "cn.cloudwalk.libproject.CloudwalkActivity");
//							intent.putExtra("real_name", mRealName);
//							intent.putExtra("real_idcard", mIdCard);
//							startPluginActivityForResult(intent, 1);
							
							ShieldSdkInterface shieldInterface = (ShieldSdkInterface)InterfaceManager.getInstance().obtainInterface(Common.PLUGIN_SHIEKD_SDK);
							shieldInterface.startShildIdentify(mRealName, mIdCard);
//							if (mIdentifyStyle.equals("0")){
//								Intent intent = new Intent(that, CloudwalkActivity.class);
//								intent.putExtra("real_name", mRealName);
//								intent.putExtra("real_idcard", mIdCard);
//								startActivityForResult(intent ,1);
//							}else{
//								Intent intent = new Intent(that, IdentifyDetectActivity.class);
//								intent.putExtra("real_name", mRealName);
//								intent.putExtra("real_idcard", mIdCard);
//								startActivityForResult(intent ,1);
//							}

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}else if (msg.what == 301){
				ActivityController.finishAll();
				Toast.makeText(that, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
				DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.RegisterUserFinishActivity");
				intent.putExtra("user_name", mPhone);
				intent.putExtra("user_password", mPassword);
				startPluginActivity(intent);
				finish();
			}else if (msg.what == 302){
				Toast.makeText(that, "登录失败,请重试！", Toast.LENGTH_SHORT).show();
			}else if (msg.what == 500){
				JSONObject object = null;
				try {
					object = new JSONObject((String)msg.obj);
					if (object != null){
						String status_code = object.optString("status_code");
						if (status_code != null){
							if (status_code.equals("100")){
								Toast.makeText(that, mRealName + " 身份认证成功 ", Toast.LENGTH_SHORT).show();
								registerUserName();
							}else {
//								if (status_code.equals("111")){
//									String compareError = object.optString("error_code");
//									Toast.makeText(that, status_code +"  "+compareError, Toast.LENGTH_SHORT).show();
//								}else{
									String compareError = object.optString("Msg");
									Toast.makeText(that, status_code +"  "+compareError, Toast.LENGTH_SHORT).show();
//								}
								
							}
						}
							
					}else{
							Toast.makeText(that, mRealName + " 身份认证失败  " , Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	};
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}



	
	

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
			if (action.equals(mValidIDcardAction)) {
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mRegisterAction)){
				if (templateInfo.equals("true")){
					mHandler.sendEmptyMessage(301);
				}else{
					mHandler.sendEmptyMessage(302);
				}
			}
		}
	}

}
