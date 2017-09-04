package safe.cloud.seal;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.UtilTool;

public class WelcomeActivity extends BaseActivity {
	private LinearLayout mLoading_data;
	private Bundle mDataBundle;
	private String mUsername;
	private String mPassword;
	private int mSuccessCount = 0;
	private ImageView mSplashBG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		
		setContentView(R.layout.welcome_main);
		
		SharedPreferences sharedata = getApplicationContext().getSharedPreferences("user_info", 0);
		mUsername = sharedata.getString("user_name", "");
		mPassword = sharedata.getString("user_password", "");
		
		mHandler.sendEmptyMessageDelayed(200, 100);
		
		UtilTool.getCururentLocation(WelcomeActivity.this);
	}
	
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				
				//if (username != null && !username.equals("") && password != null && !password.equals("")){
					Intent intent = new Intent(WelcomeActivity.this, LoginUserActivity.class);
					intent.putExtra("user_name", mUsername);
					intent.putExtra("user_password", mPassword);
					startActivity(intent);
					
//				}else{
//					Intent intent = new Intent(WelcomeActivity.this, RegisterUserActivity.class);
//					startActivity(intent);
//				}
				finish();
				break;
			case 200:
				mHandler.sendEmptyMessageDelayed(100, 3000);
				break;
			case 201:
				
			case 202:
				
			
			default:
				break;
			}
			
		}
		
	};

	
	
	
}
