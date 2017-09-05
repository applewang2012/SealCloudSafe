package safe.cloud.seal;


import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import cn.cloudwalk.libproject.callback.OnCaptureCallback;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.fragment.AddSealInfoStep1Fragment;
import safe.cloud.seal.fragment.AddSealInfoStep2Fragment;
import safe.cloud.seal.fragment.SealStatusFragment;
import safe.cloud.seal.model.SealStatusInfo;
import safe.cloud.seal.model.UniversalAdapter;
import safe.cloud.seal.model.UniversalViewHolder;
import safe.cloud.seal.presenter.ActionOperationInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;
import safe.cloud.seal.util.ViewUtil;

public class ShowSealTraceActivity extends BaseActivity implements OnItemClickListener{

	private HoursePresenter mPresenter;
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
	private View mLoadingView;
	private UniversalAdapter<SealStatusInfo> mAdapter;
	private List<SealStatusInfo> mDataList = new ArrayList<>();
	private TextView mEmptyContent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_seal_trace_listview_layout); 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("印记上传");
		Button addTrace = (Button)findViewById(R.id.id_titlebar_button_add_function);
		addTrace.setVisibility(View.VISIBLE);
		addTrace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShowSealTraceActivity.this, AddSealTraceActivity.class));
				
			}
		});
		initView();
		
	}
	
	
	
	private void initView(){
		initAdapter();
	}
	
	private void initAdapter(){
		mLoadingView = findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mEmptyContent = (TextView)findViewById(R.id.id_seal_trace_empty_view);
		mEmptyContent.setVisibility(View.VISIBLE);
		ListView showList = (ListView)findViewById(R.id.id_seal_trace_show_list);
		mAdapter = new UniversalAdapter<SealStatusInfo>(ShowSealTraceActivity.this, R.layout.aty_seal_trace_listview_item, mDataList) {

			@Override
			public void convert(UniversalViewHolder holder, SealStatusInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView fileName = (TextView)holderView.findViewById(R.id.id_seal_trace_file_list_name);
				TextView sealName = (TextView)holderView.findViewById(R.id.id_seal_trace_seal_list_name);
				TextView traceDate = (TextView)holderView.findViewById(R.id.id_seal_trace_seal_list_date);
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
	}
	
	private void requestGetSignetsList(){
		Log.w("mingguo", "request signet  list  username   "+CommonUtil.mUserLoginName);
		ViewUtil.showLoadingView(this, mLoadingView);
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSignetsListByApplyer";
//		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSignetsListAction));
//		rpc.addProperty("applyerId", CommonUtil.mUserLoginName);
//		mPresent.readyPresentServiceParams(mContext, url, mGetSignetsListAction, rpc);
//		mPresent.startPresentServiceTask();
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



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	


}
