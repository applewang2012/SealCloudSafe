package safe.cloud.seal;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.fragment.AddSealInfoStep1Fragment;
import safe.cloud.seal.fragment.AddSealInfoStep2Fragment;
import safe.cloud.seal.fragment.SealStatusFragment;
import safe.cloud.seal.presenter.ActionOperationInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.GlobalUtil;

public class ApplyForSealActivity extends BaseActivity {

	private HoursePresenter mPresenter;
	//private String mLoginAction = "http://tempuri.org/ValidateLogin";
	private String mUpdateAction="http://tempuri.org/CheckUpgrade";
	private String mUserInfoAction = "http://tempuri.org/GetUserInfo";;
	private String mUserName, mPassword;
	private String mUserInfoString = null;
	private String mCity = null;
	private int mVersionCode = -1;
	private SealStatusFragment mStatusFragment;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private final int TAKE_PICTURE = 1, SELECT_PICTURE = 2;
	private HandlerThread myHandlerThread ;
	private Handler mSubHandler;
	private String mPhotoFilePath;
	private int mSelectPhotoFlag = 0;
	private ImageView zhizhaoImage1, zhizhaoImage2;
	private ImageView farenId1;
	private ImageView farenId2;
	private ImageView jingbanrenId1;
	private ImageView jingbanrenId2;
	private ImageView danweijieshaoxin1;
	private ImageView danweijieshaoxin2;

	private AddSealInfoStep1Fragment mAddInfoFragment ;
	private AddSealInfoStep2Fragment mUploadInfoFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_add_seal_layout); 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("申请印章");
		
		mUserName = getIntent().getStringExtra("user_name");
		mPassword = getIntent().getStringExtra("user_password");
		initHandler();
		initView();
//		getUserInfo();
		
	}
	
	
	private void hideAllFragments(FragmentTransaction transaction) {
		if (mAddInfoFragment != null && !mAddInfoFragment.isHidden()) {
			transaction.hide(mAddInfoFragment);
		}
		if (mUploadInfoFragment != null && !mUploadInfoFragment.isHidden()) {
			transaction.hide(mUploadInfoFragment);
		}
		
	}
	
	private void initView(){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		if (mAddInfoFragment == null){
			mAddInfoFragment = new AddSealInfoStep1Fragment();
			fragmentTransaction.add(R.id.id_add_seal_content, mAddInfoFragment);
			fragmentTransaction.commitAllowingStateLoss();
			mAddInfoFragment.setFragmentActionListener(new ActionOperationInterface() {
				
				@Override
				public void onNextFragment() {
					FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
					if (mUploadInfoFragment == null){
						hideAllFragments(fragmentTransaction);
						mUploadInfoFragment = new AddSealInfoStep2Fragment();
						fragmentTransaction.add(R.id.id_add_seal_content, mUploadInfoFragment);
						fragmentTransaction.commitAllowingStateLoss();
					}else{
						fragmentTransaction.show(mUploadInfoFragment);
						fragmentTransaction.commitAllowingStateLoss();
					}
				}
			});
		}else{
			fragmentTransaction.show(mAddInfoFragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
		
		
//		pop = new PopupWindow(ApplyForSealActivity.this);
//		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
//
//		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//		
//		pop.setWidth(LayoutParams.MATCH_PARENT);
//		pop.setHeight(LayoutParams.WRAP_CONTENT);
//		pop.setBackgroundDrawable(new BitmapDrawable());
//		pop.setFocusable(true);
//		pop.setOutsideTouchable(true);
//		pop.setContentView(view);
//		
//		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
//		Button bt1 = (Button) view
//				.findViewById(R.id.item_popupwindows_camera);
//		Button bt2 = (Button) view
//				.findViewById(R.id.item_popupwindows_Photo);
//		Button bt3 = (Button) view
//				.findViewById(R.id.item_popupwindows_cancel);
//		parent.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				pop.dismiss();
//				ll_popup.clearAnimation();
//			}
//		});
//		bt1.setOnClickListener(new OnClickListener() {
//			
//
//			public void onClick(View v) {
//				Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				mPhotoFilePath = GlobalUtil.createScreenshotDirectory(ApplyForSealActivity.this, System.currentTimeMillis()+"");
//				File out = new File(mPhotoFilePath);
//				Uri uri = Uri.fromFile(out);
//				getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//				startActivityForResult(getPhoto, TAKE_PICTURE);
//				pop.dismiss();
//				ll_popup.clearAnimation();
//			}
//		});
//		bt2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(ApplyForSealActivity.this,
//						AlbumActivity.class);
//				startActivityForResult(intent, SELECT_PICTURE);
//				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//				pop.dismiss();
//				ll_popup.clearAnimation();
//				//finish();
//			}
//		});
//		bt3.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				pop.dismiss();
//				ll_popup.clearAnimation();
//				mSelectPhotoFlag = 0;
//			}
//		});
//		
//		
//		zhizhaoImage1 = (ImageView)findViewById(R.id.id_seal_upload_add_zhizhao1);
//		zhizhaoImage2 = (ImageView)findViewById(R.id.id_seal_upload_add_zhizhao2);
//		farenId1 = (ImageView)findViewById(R.id.id_seal_upload_add_faren_shenfenzheng);
//		farenId2 = (ImageView)findViewById(R.id.id_seal_upload_add_faren_shenfenzheng2);
//		jingbanrenId1 = (ImageView)findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng);
//		jingbanrenId2 = (ImageView)findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng2);
//		danweijieshaoxin1 = (ImageView)findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin1);
//		danweijieshaoxin2 = (ImageView)findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin2);
//		zhizhaoImage1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1000;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		zhizhaoImage2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1001;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		farenId1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1002;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		farenId2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1003;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		jingbanrenId1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1004;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		jingbanrenId2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1005;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		danweijieshaoxin1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1006;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		danweijieshaoxin2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1007;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(ApplyForSealActivity.this,R.anim.activity_translate_in));
//				pop.showAtLocation(getLayoutInflater().inflate(R.layout.aty_apply_for_seal_upload_file_layout, null), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
	}
	
	private void initHandler(){
    	//创建一个线程,线程名字：handler-thread
        myHandlerThread = new HandlerThread( "handler-thread") ;
        myHandlerThread.start();
        
        mSubHandler = new Handler(myHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int degree = GlobalUtil.readPictureDegree((String)msg.obj);
                Bitmap rotationBitmap = GlobalUtil.rotaingImageView(degree, BitmapFactory.decodeFile((String)msg.obj, null));
   			 	Log.w("mingguo", "onActivityResult  before compress image  "+rotationBitmap.getWidth()+" height  "+rotationBitmap.getHeight()+"  byte  ");
   			 	Bitmap newBitmap = GlobalUtil.compressScale(rotationBitmap);
   			 	Log.w("mingguo", "onActivityResult  compress image  "+newBitmap.getWidth()+" height  "+newBitmap.getHeight()+"  byte  ");
   			 	//mSelfPhotoToString = android.util.Base64.encodeToString(GlobalUtil.Bitmap2Bytes(newBitmap), android.util.Base64.NO_WRAP);
   			 	Message message = mHandler.obtainMessage();
   			 	message.what  = msg.what;
   			 	message.obj = newBitmap;
   			 	mHandler.sendMessage(message);
            }
        };
        
    }
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == mSelectPhotoFlag){
				if (mSelectPhotoFlag == 1000){
					zhizhaoImage1.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag  == 1001){
					Bitmap bitmap = (Bitmap)msg.obj;
					zhizhaoImage2.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1002){
					farenId1.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1003){
					farenId2.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1004){
					jingbanrenId1.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1005){
					jingbanrenId2.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1006){
					danweijieshaoxin1.setImageBitmap((Bitmap)msg.obj);
				}else if (mSelectPhotoFlag == 1007){
					danweijieshaoxin2.setImageBitmap((Bitmap)msg.obj);
				}
			}
		}
	};
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == RESULT_OK){
				Message subMessage = mSubHandler.obtainMessage();
				subMessage.what = mSelectPhotoFlag;
				subMessage.obj = mPhotoFilePath;
				mSubHandler.sendMessage(subMessage);
				//mSubHandler.sendEmptyMessage(mSelectPhotoFlag);
			}
			break;
		case SELECT_PICTURE:
			if (resultCode == RESULT_OK){
				if (data != null){
					ImageItem item = (ImageItem)data.getSerializableExtra("image_shot");
					Log.w("mingguo", "  on activity  result image  path   "+item.getImagePath());
//					Message msg = mHandler.obtainMessage();
//					msg.what = mSelectPhotoFlag;
//					msg.obj = item.getBitmap();
//					mHandler.sendMessage(msg);
					Message subMessage = mSubHandler.obtainMessage();
					subMessage.what = mSelectPhotoFlag;
					subMessage.obj = item.getImagePath();
					mSubHandler.sendMessage(subMessage);
				}
			}
			break;
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
