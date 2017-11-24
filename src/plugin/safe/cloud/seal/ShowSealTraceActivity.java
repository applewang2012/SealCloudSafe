package plugin.safe.cloud.seal;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.internal.DLIntent;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import plugin.safe.cloud.seal.fragment.AddSealInfoStep1Fragment;
import plugin.safe.cloud.seal.fragment.AddSealInfoStep2Fragment;
import plugin.safe.cloud.seal.model.SealTraceInfo;
import plugin.safe.cloud.seal.model.UniversalAdapter;
import plugin.safe.cloud.seal.model.UniversalViewHolder;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.util.UtilTool;
import plugin.safe.cloud.seal.util.ViewUtil;

public class ShowSealTraceActivity extends BaseActivity implements OnItemClickListener {

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
	private UniversalAdapter<SealTraceInfo> mAdapter;
	private List<SealTraceInfo> mDataList = new ArrayList<>();
	private TextView mEmptyContent;
	private String mGetSpecialPointAction = "http://tempuri.org/GetSpecialPointByUser";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_seal_trace_listview_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("印迹上传");
		Button addTrace = (Button)findViewById(R.id.id_titlebar_button_add_function);
		addTrace.setVisibility(View.VISIBLE);
		addTrace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startPluginActivity(
						new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.AddSealTraceActivity"));
			}
		});
		initView();
		
	}
	
	
	
	private void initView(){
		initAdapter();
	}
	
	private void initAdapter(){
		mPresenter = new HoursePresenter(that, this);
		
		mLoadingView = findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mEmptyContent = (TextView)findViewById(R.id.id_seal_trace_empty_view);
		mEmptyContent.setVisibility(View.INVISIBLE);
		ListView showList = (ListView)findViewById(R.id.id_seal_trace_show_list);
		mAdapter = new UniversalAdapter<SealTraceInfo>(that, R.layout.aty_seal_trace_listview_item, mDataList) {

			@Override
			public void convert(UniversalViewHolder holder, SealTraceInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView fileName = (TextView)holderView.findViewById(R.id.id_seal_trace_file_list_name);
				TextView sealName = (TextView)holderView.findViewById(R.id.id_seal_trace_seal_list_name);
				TextView traceDate = (TextView)holderView.findViewById(R.id.id_seal_trace_seal_list_date);
				fileName.setText(info.getSealFileName());
				sealName.setText(info.getSealName());
				traceDate.setText("申请时间："+info.getSealDate());
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
		requestGetSignetsList();
	}
	
	private void requestGetSignetsList(){
		Log.w("mingguo", "request signet  list  username   "+ CommonUtil.mUserLoginName);
		ViewUtil.showLoadingView(that, mLoadingView);
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSpecialPointByUser";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSpecialPointAction));
		rpc.addProperty("userId", CommonUtil.mUserLoginName);
		mPresenter.readyPresentServiceParams(that, url, mGetSpecialPointAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}
	
	private void parseSealTraceInfo(String value){
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mDataList.clear();
				Log.w("house", "parse house info "+array.length());
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					SealTraceInfo  statusInfo = new SealTraceInfo();
					statusInfo.setSealTraceId(itemJsonObject.optString("sfr_file_id"));
					statusInfo.setSealFileName(itemJsonObject.optString("sfr_memo"));
					statusInfo.setSealName(itemJsonObject.optString("sfr_signet_content"));
					statusInfo.setSealDate(UtilTool.stampToNormalDate(UtilTool.extractNumberFromString(itemJsonObject.optString("sfr_upload_date"))));
					mDataList.add(statusInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			ViewUtil.dismissLoadingView(mLoadingView);
			if (msg.what == 100){
					parseSealTraceInfo((String)msg.obj);
					if (mDataList.size() > 0){
						mEmptyContent.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
					}else{
						mEmptyContent.setVisibility(View.VISIBLE);
					}
					
			}
		}
	};
	
	
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "on success  action "+action+"  msg  "+templateInfo);
		if (action != null && templateInfo != null){}
			if (action.equals(mGetSpecialPointAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
//			else if (action.equals(mUpdateAction)){
//				Message message = mHandler.obtainMessage();
//				message.what = 200;
//				message.obj = templateInfo;
//				mHandler.sendMessageDelayed(message, 500);
//			}
	}

	


	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		super.onStatusError(action, error);
		Log.e("mingguo", "on success  action "+action+"  error  "+error);
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.SealTraceDetailActivity");
		intent.putExtra("trace_id", mDataList.get(arg2).getSealTraceId());
		intent.putExtra("seal_name", mDataList.get(arg2).getSealName());
		intent.putExtra("file_name", mDataList.get(arg2).getSealFileName());
		startPluginActivity(intent);
	}
	
	


}
