package safe.cloud.seal;

import static com.camera.authenticationlibrary.AuthenticationUtil.publicFilePath;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.camera.authenticationlibrary.AuthenticationUtil;
import com.camera.authenticationlibrary.ReciveImg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
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
import cn.cloudwalk.FaceInterface;
import cn.cloudwalk.libproject.LiveStartActivity;
import cn.cloudwalk.libproject.util.Base64Util;
import cn.cloudwalk.libproject.util.FileUtil;
import cn.cloudwalk.libproject.util.ImgUtil;
import safe.cloud.seal.model.ActivityController;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;

public class RegisterUserStep2Activity extends BaseActivity{

	private TextView mTitleBar;
	private HoursePresenter mPresenter;
	private String mPhone;
	private String mPassword, mPasswordIndentify;
	private String mValidIDcardAction = "http://tempuri.org/IsExistsIDCard";
	private boolean mUsernameValid;
	private View mLoadingView;
	private String mRealName, mIdCard;
	private String mPhotoFilePath;
	private HandlerThread myHandlerThread ;
	private Handler mSubHandler;
	private String mLiveFaceToString, mSelfPhotoToString;
	private String mIdentifyAction = "http://tempuri.org/IdentifyValidateLive";
	private String mRegisterAction = "http://tempuri.org/AddUserInfo";
	public static int liveLevel = FaceInterface.LevelType.LEVEL_STANDARD;
	private ArrayList<Integer> liveList;
	private int liveCount = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_register_user_step2_layout); 
		ActivityController.addActivity(RegisterUserStep2Activity.this);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		mTitleBar = (TextView)findViewById(R.id.id_titlebar);
		mTitleBar.setText("实名认证");
		mPhone = getIntent().getStringExtra("phone");
		mPassword = getIntent().getStringExtra("user_password");
		initData();
		initView();
		initHandler();
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	private void checkUserIDcardValid(String idcard){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=IsExistsIDCard";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mValidIDcardAction));
		rpc.addProperty("idcard", idcard); 
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mValidIDcardAction, rpc);
		mPresenter.startPresentServiceTask();
	}

	private void initData(){
		publicFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator).append("cloudwalk").toString();
        FileUtil.mkDir(publicFilePath);

        liveList = new ArrayList<Integer>();
        liveList.add(FaceInterface.LivessType.LIVESS_MOUTH);
        liveList.add(FaceInterface.LivessType.LIVESS_HEAD_UP);
        liveList.add(FaceInterface.LivessType.LIVESS_HEAD_DOWN);
        liveList.add(FaceInterface.LivessType.LIVESS_HEAD_LEFT);
        liveList.add(FaceInterface.LivessType.LIVESS_HEAD_RIGHT);
        liveList.add(FaceInterface.LivessType.LIVESS_EYE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("iw_sendok");
        registerReceiver(broadcastReceiver, intentFilter);
        
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               
//            }
//        });
	}
	
	

	private void initView(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		
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
					GlobalUtil.shortToast(RegisterUserStep2Activity.this, getString(R.string.surface_name_not_null), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				if (mIdCard == null || mIdCard.equals("")){
					GlobalUtil.shortToast(RegisterUserStep2Activity.this,getString(R.string.id_card_not_null) , getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}else if (mIdCard.length() < 18){
					GlobalUtil.shortToast(RegisterUserStep2Activity.this,getString(R.string.id_card_input_error) , getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				showLoadingView();
				checkUserIDcardValid(mIdCard);
			}
		});
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	showLoadingView();
        	mSubHandler.sendEmptyMessage(5000);
        	
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                	mSubHandler.sendEmptyMessage(5000);
//                    
//                }
//            }).start();

        }
    };

    public String getBase64Pic() {
        Bitmap face1Bitmap = BitmapFactory.decodeFile(publicFilePath + "/" + "bestface.jpg");//filePath;
        byte[] imgAData = ImgUtil.bitmapToByte(face1Bitmap, Bitmap.CompressFormat.JPEG, 70);

        return Base64Util.encode(imgAData);
    }
	
	private void initHandler(){
    	//创建一个线程,线程名字：handler-thread
        myHandlerThread = new HandlerThread( "handler-thread") ;
        myHandlerThread.start();
        
        mSubHandler = new Handler(myHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1000){
                	int degree = GlobalUtil.readPictureDegree(mPhotoFilePath);
	                Bitmap rotationBitmap = GlobalUtil.rotaingImageView(degree, BitmapFactory.decodeFile(mPhotoFilePath, null));
	   			 	Log.w("mingguo", "onActivityResult  before compress image  "+rotationBitmap.getWidth()+" height  "+rotationBitmap.getHeight()+"  byte  ");
	   			 	Bitmap newBitmap = GlobalUtil.compressScale(rotationBitmap);
	   			 	Log.w("mingguo", "onActivityResult  compress image  "+newBitmap.getWidth()+" height  "+newBitmap.getHeight()+"  byte  ");
	   			 	mSelfPhotoToString = android.util.Base64.encodeToString(GlobalUtil.Bitmap2Bytes(newBitmap), android.util.Base64.NO_WRAP);
                }else if (msg.what == 5000){
                	String renzhengResult = ReciveImg.getRenzhengResult(RegisterUserStep2Activity.this, mIdCard, mRealName ,getBase64Pic());
                	Log.w("mingguo", "register step2  renzheng result  "+renzhengResult);
                    Message message = mHandler.obtainMessage();
                    message.what = 500;
                    message.obj = renzhengResult;
                    mHandler.sendMessage(message);
                }
                
            }
        };
        
    }
	
	private void identifyUserInfo(String faceStr, String screenshotStr){
		if (faceStr == null || screenshotStr == null){
			return;
		}
		Log.i("mingguo", "register interface  faceStr  "+faceStr.length()+"  screenshot   "+screenshotStr.length());
		Log.i("mingguo", "register interface  mIdCard  "+mIdCard+"  mRealName  "+mRealName);
		String identifyUrl = "http://www.guardts.com/ValidateService/IdentifyValidateService.asmx?op=IdentifyValidateLive";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mIdentifyAction));
		rpc.addProperty("idcard", mIdCard);
		rpc.addProperty("name", mRealName);
		rpc.addProperty("base64Str", faceStr);
		rpc.addProperty("picBase64Str", screenshotStr);
		mPresenter.readyPresentServiceParams(getApplicationContext(), identifyUrl, mIdentifyAction, rpc);
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
		Log.i("mingguo", "onActivityResult resultCode  "+resultCode+" requestCode  "+requestCode+"  file  "+mPhotoFilePath);
		if (resultCode == RESULT_OK && requestCode == 1) {
			 Log.w("mingguo", "activity result  width data   "+data);
			 mSubHandler.sendEmptyMessage(1000);
		}else{
			GlobalUtil.shortToast(RegisterUserStep2Activity.this, "头像采集失败", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
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
		mPresenter.startPresentServiceTask();
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
								//showLoadingView();
								AuthenticationUtil.startLive(RegisterUserStep2Activity.this, LiveStartActivity.class, liveLevel, liveList, liveCount);
//							}else{
//								GlobalUtil.shortToast(RegisterUserStep2Activity.this, "该身份证号已被注册！", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
//							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if (msg.what == 200){
				
			}else if (msg.what == 102){
				dismissLoadingView();
				try {
					JSONObject object = new JSONObject((String)msg.obj);
					if (object != null){
						String compareResult = object.optString("compareresult");
						if (compareResult == null || compareResult.equals("")){
							GlobalUtil.shortToast(RegisterUserStep2Activity.this, mRealName + " 身份认证失败 ", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
						}else{
							if (compareResult.equals("0")){
									GlobalUtil.shortToast(RegisterUserStep2Activity.this, mRealName + " 身份认证成功 ", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_yes));
									registerUserName();
									return;
								
							}else{
								GlobalUtil.shortToast(RegisterUserStep2Activity.this, mRealName + " 身份认证失败  "+compareResult , getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (msg.what == 301){
				ActivityController.finishAll();
				GlobalUtil.shortToast(RegisterUserStep2Activity.this, getString(R.string.register_success), getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_yes));
				Intent intent = new Intent(RegisterUserStep2Activity.this, RegisterUserFinishActivity.class);
				intent.putExtra("user_name", mPhone);
				intent.putExtra("user_password", mPassword);
				startActivity(intent);
				finish();
			}else if (msg.what == 302){
				GlobalUtil.shortToast(RegisterUserStep2Activity.this, "登录失败,请重试！", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
			}else if (msg.what == 500){
				dismissLoadingView();
				JSONObject object = null;
				try {
					object = new JSONObject((String)msg.obj);
					if (object != null){
						String status_code = object.optString("status_code");
						if (status_code != null){
							if (status_code.equals("200")){
								GlobalUtil.shortToast(RegisterUserStep2Activity.this, mRealName + " 身份认证成功 ", getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_yes));
								//registerUserName();
							}else {
								if (status_code.equals("111")){
									String compareError = object.optString("error_code");
									GlobalUtil.shortToast(RegisterUserStep2Activity.this, status_code +"  "+compareError, getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
								}else{
									String compareError = object.optString("msg");
									GlobalUtil.shortToast(RegisterUserStep2Activity.this, status_code +"  "+compareError, getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
								}
								
							}
						}
							
					}else{
							GlobalUtil.shortToast(RegisterUserStep2Activity.this, mRealName + " 身份认证失败  " , getApplicationContext().getResources().getDrawable(R.drawable.ic_dialog_no));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	};
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
	}



	@Override
	public void onStatusStart() {
		
		
	}
	
	

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		super.onStatusError(action, error);
		mHandler.sendEmptyMessage(200);
	}

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.i("mingguo", "on success  action "+action+"  msg  "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mValidIDcardAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mIdentifyAction)){
				Message message = mHandler.obtainMessage();
				message.what = 102;
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
