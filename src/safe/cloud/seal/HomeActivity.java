package safe.cloud.seal;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import safe.cloud.seal.fragment.MyFragment;
import safe.cloud.seal.fragment.SealStatusFragment;
import safe.cloud.seal.presenter.HoursePresenter;

public class HomeActivity extends BaseActivity {

	private HoursePresenter mPresenter;
	//private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private String mUpdateAction="http://tempuri.org/CheckUpgrade";
	private String mUserInfoAction = "http://tempuri.org/GetUserInfo";
	private String mUserName, mPassword;
	private String mUserInfoString = null;
	private String mCity = null;
	private int mVersionCode = -1;
	private SealStatusFragment mStatusFragment;
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
		//getUserInfo();
	}
	
	private void initView(){
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
				myIcon.setBackgroundResource(R.drawable.home_tab_self_press);
				myText.setTextColor(Color.parseColor("#d43c33"));
			}
		});
	}
	
	
	
	private void hideAllFragments(FragmentTransaction transaction) {
		if (mStatusFragment != null && !mStatusFragment.isHidden()) {
			transaction.hide(mStatusFragment);
		}
		if (mSelfFragment != null && !mSelfFragment.isHidden()) {
			transaction.hide(mSelfFragment);
		}
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
