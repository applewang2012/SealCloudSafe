package safe.cloud.seal;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;

public class RegisterUserFinishActivity extends BaseActivity{

	private TextView mTitleBar;
	private HoursePresenter mPresenter;
	private String mUserName;
	private String mPassword, mPasswordIndentify;
	private String mValidAction = "http://tempuri.org/ValidateLoginName";
	private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private boolean mUsernameValid;
	private View mLoadingView;
	private String mPhone;
	private String mRealName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_register_user_finish_layout); 
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		mTitleBar = (TextView)findViewById(R.id.id_titlebar);
		mTitleBar.setText("注册完成");
		Button back = (Button)findViewById(R.id.id_titlebar_back);
		back.setVisibility(View.INVISIBLE);
		mUserName = getIntent().getStringExtra("user_name");
		mPassword = getIntent().getStringExtra("user_password");
		initView();
	}
	

	private void initView(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		
		Button interButton = (Button)findViewById(R.id.id_register_user_enter_app);
		interButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLoadingView();
				loginUser();
			}
		});
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

	private void loginUser(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=ValidateLogin";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mLoginAction));
		rpc.addProperty("username", mUserName);
		rpc.addProperty("password", mPassword);
		rpc.addProperty("userType", "0");
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mLoginAction, rpc);
		mPresenter.startPresentServiceTask();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100){
				dismissLoadingView();
				SharedPreferences sharedata = getApplicationContext().getSharedPreferences("user_info", 0);
				SharedPreferences.Editor editor = sharedata.edit();
			    editor.putString("user_name", mUserName);
			    editor.putString("user_password", mPassword);
			    editor.commit();
				GlobalUtil.shortToast(getApplication(), getString(R.string.login_success), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_yes));
				Intent intent = new Intent(RegisterUserFinishActivity.this, HomeActivity.class);
				intent.putExtra("user_name", mUserName);
				intent.putExtra("user_password", mPassword);
				intent.putExtra("user_phone", mPhone);
				intent.putExtra("user_realname", mRealName);
				startActivity(intent);
				finish();
			}else if (msg.what == 101){
				dismissLoadingView();
				GlobalUtil.shortToast(getApplication(), getString(R.string.login_failed), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
			}else if (msg.what == 110){
//				dismissLoadingView();
//				showSelectAlertDialog("请选择所在区域", parseCommonService((String)msg.obj));
			}
		}
	};
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		// TODO Auto-generated method stub
		super.onStatusSuccess(action, templateInfo);
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
			}
		}
	}
	
	
}
