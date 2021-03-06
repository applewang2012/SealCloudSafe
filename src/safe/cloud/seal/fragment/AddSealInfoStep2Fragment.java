package safe.cloud.seal.fragment;

import java.io.File;
import java.security.spec.ECPrivateKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.google.zxing.oned.rss.FinderPattern;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Network;
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
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.AlbumActivity;
import safe.cloud.seal.LoginUserActivity;
import safe.cloud.seal.R;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.model.SealInfoModel;
import safe.cloud.seal.model.SealStatusInfo;
import safe.cloud.seal.model.SealUploadFileType;
import safe.cloud.seal.model.UniversalAdapter;
import safe.cloud.seal.model.UniversalViewHolder;
import safe.cloud.seal.presenter.DataStatusInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
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
	private View mLoadingView;
	private String mUploadFileAction = "http://tempuri.org/AddSignetFile";
	private String mGetGeneralCodeAction = "http://tempuri.org/GetGeneralCode";
	private List<SealUploadFileType> mDataList = new ArrayList<>();
	private UniversalAdapter mAdapter;
	private int mUploadNum = 0;
	private List<ImageItem> mUploadList = new ArrayList<>();
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
		requestCommonData("CE");
		return mRootView;
	}
	
	private void initTitleBar(){

		
	}
	
	private void requestUploadSignFile(String signId, String type, String file, String data, String demo){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=AddSignetFile";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mUploadFileAction));
		rpc.addProperty("signetId",signId);
		rpc.addProperty("type",type);
		rpc.addProperty("file",file);
		rpc.addProperty("data",data);
		rpc.addProperty("demo",demo);
		mPresent.readyPresentServiceParams(mContext, url, mUploadFileAction, rpc);
		mPresent.startPresentServiceTask();
		
	}
	
	private void initView(){
		
		pop = new PopupWindow(mContext);
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		mLoadingView = mRootView.findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
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
		initAdapter();
		
		Button submitFile = (Button)mRootView.findViewById(R.id.id_aty_apply_seal_submit_file_button);
		submitFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				addUploadFileList();
				
			}
		});
		
//		zhizhaoImage1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_zhizhao1);
//		zhizhaoImage2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_zhizhao2);
//		farenId1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_faren_shenfenzheng);
//		farenId2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_faren_shenfenzheng2);
//		jingbanrenId1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng);
//		jingbanrenId2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_jingbanren_shenfenzheng2);
//		danweijieshaoxin1 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin1);
//		danweijieshaoxin2 = (ImageView)mRootView.findViewById(R.id.id_seal_upload_add_danwei_jieshaoxin2);
//		zhizhaoImage1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1000;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		zhizhaoImage2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1001;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		farenId1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1002;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity() ,R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		farenId2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1003;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		jingbanrenId1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1004;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		jingbanrenId2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1005;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
//		danweijieshaoxin1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1006;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//			}
//		});
//		danweijieshaoxin2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mSelectPhotoFlag  = 1007;
//				ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.activity_translate_in));
//				pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
//				
//			}
//		});
	}
	
	private void addUploadFileList(){
		mUploadList.clear();
		boolean allowUpload = true;
		for (int index = 0; index < mDataList.size(); index++){
			int checkFile = 0;
			if (mDataList.get(index).getBitmapBase64() != null ){
				ImageItem item = new ImageItem();
				item.setImagePath(mDataList.get(index).getImagePath());
				item.setBitmap(mDataList.get(index).getImageBitmap());
				item.setTypeName(mDataList.get(index).getFileType());
				item.setTypeNameId(mDataList.get(index).getFileId());
				item.setBitmapBase64(mDataList.get(index).getBitmapBase64());
				mUploadList.add(item);
				checkFile ++;
			}
			if (mDataList.get(index).getBitmap2Base64() != null){
				ImageItem item2 = new ImageItem();
				item2.setImagePath(mDataList.get(index).getImage2Path());
				item2.setBitmap(mDataList.get(index).getImage2Bitmap());
				item2.setTypeName(mDataList.get(index).getFileType());
				item2.setTypeNameId(mDataList.get(index).getFileId());
				item2.setBitmapBase64(mDataList.get(index).getBitmap2Base64());
				mUploadList.add(item2);
				checkFile ++;
			}
			if (checkFile == 0){
				GlobalUtil.shortToast(mContext, mDataList.get(index).getFileType()+" 未添加文件 ！", getResources().getDrawable(R.drawable.ic_dialog_no));
				allowUpload = false;
				break;
			}
		}
		if (mUploadList.size() > 0 && allowUpload){
			showUploadDialog();
			
		}
	}
	
	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void startUploadFile(int startIndex){
		Log.e("mingguo", "need to upload num  "+mUploadList.size());
		for (int index = 0; index < mUploadList.size(); index++){
			if (startIndex == index){
				Log.e("mingguo", "signet id  "+CommonUtil.mSignetNumberId+"  upload  name   "+mUploadList.get(index).getTypeNameId()+"  path "+mUploadList.get(index).getImageName());
				requestUploadSignFile(CommonUtil.mSignetNumberId, mUploadList.get(index).getTypeNameId(), 
						mUploadList.get(index).getImageName(), mUploadList.get(index).getBitmapBase64(), "demo");
			}
		}
		mUploadNum++;
		if (mUploadNum == mUploadList.size()){
			GlobalUtil.shortToast(mContext, "上传文件成功！", getResources().getDrawable(R.drawable.ic_dialog_no));
			getActivity().finish();
		}
	}

	private void initAdapter(){
		ListView showList = (ListView)mRootView.findViewById(R.id.id_seal_upload_file_list);
		mAdapter = new UniversalAdapter<SealUploadFileType>(mContext, R.layout.fgt_apply_seal_upload_file_item, mDataList) {

			@Override
			public void convert(final UniversalViewHolder holder, final SealUploadFileType info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView typeName = (TextView)holderView.findViewById(R.id.id_seal_upload_add_file_name);
				ImageView addFile1 = (ImageView)holderView.findViewById(R.id.id_seal_upload_add_file1);
				ImageView addFile2 = (ImageView)holderView.findViewById(R.id.id_seal_upload_add_file2);
				typeName.setText(info.getFileType());
				if (info.getImageBitmap() != null){
					addFile1.setImageBitmap(info.getImageBitmap());
				}
				
				if (info.getImage2Bitmap() != null){
					Log.e("mingguo", "info.getImageBitmap2()  "+info.getImage2Bitmap());
					addFile2.setImageBitmap(info.getImage2Bitmap());
				}
				addFile1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//mSelectPhotoFlag  = 1000*(holder.getPosition()+1)+1;
						//info.setImageId(1000*(holder.getPosition()+1)+1);
						mSelectPhotoFlag = info.getImaged();
						Log.w("mingguo", "select  flag  "+mSelectPhotoFlag);
						ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
						pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
						
					}
				});
				addFile2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//info.setImageId(1000*(holder.getPosition()+1)+2);
						mSelectPhotoFlag = info.getImaged2();
						Log.w("mingguo", "select  flag2  "+mSelectPhotoFlag);
						ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
						pop.showAtLocation(getActivity().findViewById(R.id.id_add_seal_content), Gravity.BOTTOM, 0, 0);
						
					}
				});
			}
		};
		showList.setAdapter(mAdapter);
		
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
   			 	String base64Image = android.util.Base64.encodeToString(GlobalUtil.Bitmap2Bytes(newBitmap), android.util.Base64.NO_WRAP);
   			 	ImageItem item = new ImageItem();
   			 	item.setBitmap(newBitmap);
   			 	item.setBitmapBase64(base64Image);
   			 	item.setImagePath((String)msg.obj);
   			 	Message message = mHandler.obtainMessage();
   			 	message.what  = msg.what;
   			 	message.obj = item;
   			 	mHandler.sendMessage(message);
            }
        };
    }
	
	private void requestCommonData(String typeId){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetGeneralCode";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetGeneralCodeAction));
		rpc.addProperty("typeId", typeId);
		mPresent.readyPresentServiceParams(mContext, url, mGetGeneralCodeAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
//	public class ImageItem{
//		private String path;
//		private Bitmap bitmap;
//		private String base64;
//	}
	
	private void parseGetUploadFileType(String value) {
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					SealUploadFileType fileInfo = new SealUploadFileType();
					fileInfo.setFileType(itemJsonObject.optString("gc_name"));
					fileInfo.setFileId(itemJsonObject.optString("gc_id"));
					fileInfo.setImageId(item*2);
					fileInfo.setImageId2(item*2+1);
					mDataList.add(fileInfo);
				}
				Log.i("mingguo", "add seal step  data size  "+mDataList.size());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void showUploadDialog(){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle(getString(R.string.upload_file_title)) 
		  
	    .setMessage(getString(R.string.upload_file_content))  
	 
	    .setPositiveButton(getString(R.string.button_ok),new DialogInterface.OnClickListener() {
	        @Override  
	 
	        public void onClick(DialogInterface dialog, int which) {
	        	showLoadingView();
				startUploadFile(0);
	        }  
	 
	    }).setNegativeButton(getString(R.string.button_cancel),new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
	 
	        @Override  
	 
	        public void onClick(DialogInterface dialog, int which) {
	            Log.i("alertdialog"," dialog interface ");  
	        }  
	 
	    }).show();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == mSelectPhotoFlag){
//				if (mSelectPhotoFlag == 1000){
//					zhizhaoImage1.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag  == 1001){
//					Bitmap bitmap = (Bitmap)msg.obj;
//					zhizhaoImage2.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1002){
//					farenId1.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1003){
//					farenId2.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1004){
//					jingbanrenId1.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1005){
//					jingbanrenId2.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1006){
//					danweijieshaoxin1.setImageBitmap((Bitmap)msg.obj);
//				}else if (mSelectPhotoFlag == 1007){
//					danweijieshaoxin2.setImageBitmap((Bitmap)msg.obj);
//				}
				int position = mSelectPhotoFlag/2;
				int imagePositon = mSelectPhotoFlag % 2;
				Log.i("mingguo", "position  "+position+"  iamge position   "+imagePositon+"  bitmap  "+msg.obj);
				ImageItem item = (ImageItem)msg.obj;
				if (imagePositon == 0){
					mDataList.get(position).setImageBitmap(item.getBitmap());
					mDataList.get(position).setImagePath(item.getImagePath());
					mDataList.get(position).setBitmapBase64(item.getBitmapBase64());
				}else if (imagePositon == 1){
					mDataList.get(position).setImage2Bitmap(item.getBitmap());
					mDataList.get(position).setImage2Path(item.getImagePath());
					mDataList.get(position).setBitmap2Base64(item.getBitmapBase64());
				}
				
				mAdapter.notifyDataSetChanged();
			}else if (msg.what == 100){
				parseGetUploadFileType((String)msg.obj);
				mAdapter.notifyDataSetChanged();
			}else if (msg.what == 120){
				String value = (String)msg.obj;
				if (value != null){
					dismissLoadingView();
					JSONObject object;
					try {
						object = new JSONObject(value);
						String ret = object.optString("ret");
						if (ret.equals("0")){
							startUploadFile(mUploadNum);
						}else {
							GlobalUtil.shortToast(mContext, "文件上传失败！", getResources().getDrawable(R.drawable.ic_dialog_no));
						}
				}catch (Exception e) {
						
					}
				}
				
			}
		}
	};
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "action   "+action + "  success "+templateInfo);
		if (action != null){
			if (action.equals(mGetGeneralCodeAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 100;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mUploadFileAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 120;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}
		}
		
	}

	@Override
	public void onStatusStart() {
		
		
	}

	@Override
	public void onStatusError(String action, String error) {
		
		
	}

	
	
	
	
}
