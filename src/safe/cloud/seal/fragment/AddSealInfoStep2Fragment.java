package safe.cloud.seal.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import safe.cloud.seal.AlbumActivity;
import safe.cloud.seal.R;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.presenter.DataStatusInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.GlobalUtil;
import safe.cloud.seal.widget.CircleFlowIndicator;
import safe.cloud.seal.widget.ViewFlow;

public class AddSealInfoStep2Fragment extends Fragment implements DataStatusInterface{
	

	
	private Context mContext;
	private View mRootView;
	
	private HoursePresenter mPresent;
	private List<String> mTitleList = new ArrayList<>();
	private Map<Integer, String[]> mContentMap = new HashMap<>();
	private List<String> mContentList = new ArrayList<>();
	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mPresent = new HoursePresenter(mContext, AddSealInfoStep2Fragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("fragmenttest", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fgt_apply_for_seal_upload_file_layout, container, false);
		initTitleBar();
		initHandler();
		initView();
		return mRootView;
	}
	
	private void initTitleBar(){
		
//		View titlebarView = (View)mRootView.findViewById(R.id.id_common_title_bar);
//		TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);
//		titleText.setText("印章状态");
//		titleText.setTextColor(Color.parseColor("#ffffff"));
//		Button backButton = (Button)titlebarView.findViewById(R.id.id_titlebar_back);
//		backButton.setVisibility(View.INVISIBLE);
//		View titleBarBg = (View)mRootView.findViewById(R.id.id_titlebar_background);
		
		
	}
	
private void initView(){
		
		pop = new PopupWindow(mContext);
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
				Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mPhotoFilePath = GlobalUtil.createScreenshotDirectory(mContext, System.currentTimeMillis()+"");
				File out = new File(mPhotoFilePath);
				Uri uri = Uri.fromFile(out);
				getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(getPhoto, TAKE_PICTURE);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AlbumActivity.class);
				startActivityForResult(intent, SELECT_PICTURE);
				getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
				//finish();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
				mSelectPhotoFlag = 0;
			}
		});
		
		
		zhizhaoImage1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_zhizhao1);
		zhizhaoImage2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_zhizhao2);
		farenId1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_faren_shenfenzheng);
		farenId2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_faren_shenfenzheng2);
		jingbanrenId1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng);
		jingbanrenId2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng2);
		danweijieshaoxin1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin1);
		danweijieshaoxin2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin2);
		zhizhaoImage1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1000;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
			}
		});
		zhizhaoImage2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1001;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
				
			}
		});
		farenId1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1002;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity() ,R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
			}
		});
		farenId2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1003;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
				
			}
		});
		jingbanrenId1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1004;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
			}
		});
		jingbanrenId2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1005;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
				
			}
		});
		danweijieshaoxin1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1006;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
			}
		});
		danweijieshaoxin2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectPhotoFlag  = 1007;
				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
				
			}
		});
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == Activity.RESULT_OK){
				Message subMessage = mSubHandler.obtainMessage();
				subMessage.what = mSelectPhotoFlag;
				subMessage.obj = mPhotoFilePath;
				mSubHandler.sendMessage(subMessage);
				//mSubHandler.sendEmptyMessage(mSelectPhotoFlag);
			}
			break;
		case SELECT_PICTURE:
			if (resultCode == Activity.RESULT_OK){
				if (data != null){
					ImageItem item = (ImageItem)data.getSerializableExtra("image_shot");
					Log.w("mingguo", "  on activity  result image  path   "+item.getImagePath());
					Message subMessage = mSubHandler.obtainMessage();
					subMessage.what = mSelectPhotoFlag;
					subMessage.obj = item.getImagePath();
					mSubHandler.sendMessage(subMessage);
				}
			}
			break;
		}
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
	public void onStatusSuccess(String action, String templateInfo) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "success "+templateInfo);
		Message msgMessage = mHandler.obtainMessage();
		msgMessage.obj = templateInfo;
		msgMessage.sendToTarget();
	}

	@Override
	public void onStatusStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
