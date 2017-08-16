package safe.cloud.seal;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import safe.cloud.seal.fragment.SealStatusFragment;
import safe.cloud.seal.presenter.HoursePresenter;

public class HomeActivity extends BaseActivity {

	private HoursePresenter mPresenter;
	//private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private String mUpdateAction="http://tempuri.org/CheckUpgrade";
	private String mUserInfoAction = "http://tempuri.org/GetUserInfo";;
	private String mUserName, mPassword;
	private String mUserInfoString = null;
	private String mCity = null;
	private int mVersionCode = -1;
	private SealStatusFragment mStatusFragment;
	private String mPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_home_layout); 
		
		mUserName = getIntent().getStringExtra("user_name");
		mPassword = getIntent().getStringExtra("user_password");
		mPhone = getIntent().getStringExtra("user_phone");
		initView();
//		getUserInfo();
		
	}
	
	private void initView(){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		if (mStatusFragment == null){
			mStatusFragment = new SealStatusFragment();
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
	}
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.i("mingguo", "on success  action "+action+"  msg  "+templateInfo);
//		if (action != null && templateInfo != null){}
//			if (action.equals(mUserInfoAction)){
//				Message message = mHandler.obtainMessage();
//				message.what = 100;
//				message.obj = templateInfo;
//				mHandler.sendMessage(message);
//			}else if (action.equals(mUpdateAction)){
//				Message message = mHandler.obtainMessage();
//				message.what = 200;
//				message.obj = templateInfo;
//				mHandler.sendMessageDelayed(message, 500);
//			}
	}
	
	


}
