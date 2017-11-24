package plugin.safe.cloud.seal;


import com.ryg.dynamicload.internal.DLIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import plugin.safe.cloud.seal.util.UtilTool;


public class WelcomeActivity extends BaseActivity {
	private LinearLayout mLoading_data;
	private Bundle mDataBundle;
	private String mUsername;
	private String mPassword;
	private int mSuccessCount = 0;
	private ImageView mSplashBG;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.welcome_main);
		
		SharedPreferences sharedata = getApplicationContext().getSharedPreferences("user_info", 0);
		mUsername = sharedata.getString("user_name", "");
		mPassword = sharedata.getString("user_password", "");
		
		mHandler.sendEmptyMessageDelayed(200, 100);
		
		UtilTool.getCururentLocation(that);
	}
	
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				
				//if (username != null && !username.equals("") && password != null && !password.equals("")){
				DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.LoginUserActivity");
					intent.putExtra("user_name", mUsername);
					intent.putExtra("user_password", mPassword);
					//DLPluginManager.getInstance(that).startPluginActivity(that, intent);
					//startPluginActivityForResult(intent, 0);
					startPluginActivity(intent);
					
//				}else{
//					Intent intent = new Intent(that, RegisterUserActivity.class);
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("mingguo", "onActivity Result ");
	}

	
	
	
	
}
