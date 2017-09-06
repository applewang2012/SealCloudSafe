package safe.cloud.seal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.AddSealTraceActivity.GridAdapter;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.model.SealTraceInfo;
import safe.cloud.seal.model.UniversalAdapter;
import safe.cloud.seal.model.UniversalViewHolder;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.shotview.Bimp;
import safe.cloud.seal.shotview.FileUpLoadUtils;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.UtilTool;
import safe.cloud.seal.util.ViewUtil;
import safe.cloud.seal.widget.MeasureGridView;


/**
 * 首页面activity
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:34
 */
public class SealTraceDetailActivity extends BaseActivity {

	private MeasureGridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	
	private  String mGetSpecialPointFileAction = "http://tempuri.org/GetSpecialPointFilesByID";
	private HoursePresenter mPresenter;
	private int mUploadNum = 0;
	private View mLoadingView;
	private TextView mFileNameEditView;
	private TextView mSealNameEditView;
	private String mFildId = null;
	private String mTraceId;
	private UniversalAdapter<SealTraceInfo> mAdapter;
	private List<SealTraceInfo> mDataList = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.aty_seal_trace_detal_, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("印记详情");
		mTraceId = getIntent().getStringExtra("trace_id"); 
		Log.i("mingguo", "trace  detail trace id  "+mTraceId);
		Init();
	}

	public void Init() {
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mFileNameEditView = (TextView)findViewById(R.id.id_add_seal_track_input_file_name);
		mSealNameEditView = (TextView)findViewById(R.id.id_add_seal_track_input_seal_name);
		noScrollgridview = (MeasureGridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new UniversalAdapter<SealTraceInfo>(SealTraceDetailActivity.this, R.layout.aty_item_published_grida, mDataList) {

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
		noScrollgridview.setAdapter(mAdapter);
		
		Button upLoadImage = (Button)findViewById(R.id.id_upload_selected_image);
		upLoadImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ViewUtil.showLoadingView(SealTraceDetailActivity.this, mLoadingView);
		getSpecialPointFileRequest();
	}
	
	
	private void getSpecialPointFileRequest(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSpecialPointFilesByID";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSpecialPointFileAction));
		rpc.addProperty("sfr_file_id", mTraceId);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mGetSpecialPointFileAction, rpc);
		mPresenter.startPresentServiceTask();
	}
	
	


	private static final int TAKE_PICTURE = 0x000001;
	private static final int TAKE_AlBUM = 0x000002;
	
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 5 && resultCode == RESULT_OK) {
					
					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUpLoadUtils.saveBitmap(bm, fileName);
					
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				break;
			case TAKE_AlBUM:
				if (resultCode == RESULT_OK){
					Bimp.tempSelectBitmap.addAll((ArrayList<ImageItem>) data.getExtras().getSerializable("image_shot"));
					adapter.notifyDataSetChanged();
				}
				
				break;
		}
	
	}
	
	private void parseSealTraceInfo(String value){
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mDataList.clear();
				Log.i("house", "parse house info "+array.length());
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					SealTraceInfo  statusInfo = new SealTraceInfo();
					statusInfo.setSealTraceId(itemJsonObject.optString("sfr_file_id"));
					statusInfo.setSealFileName(itemJsonObject.optString("sfr_memo"));
					statusInfo.setSealName(itemJsonObject.optString("sfr_signet_content"));
					statusInfo.setsealis
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
				if (msg.obj != null){
					JSONObject object;
					try {
						object = new JSONObject((String)msg.obj);
						String ret = object.optString("ret");
						if (ret != null && ret.equals("0")){
							mFildId = object.optString("ID");
							if (mFildId != null){
								mUploadNum = 1;
								Log.w("mingguo", "upload file field id  "+mFildId);
								
							}
							
						}else{
							Toast.makeText(getApplicationContext(), "上传图片失败！", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}else if (msg.what == 101){
				
			}
		}
	};

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.i("mingguo", "select photeo upload  status  success  action "+action+" temp info "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mGetSpecialPointFileAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
	}

	@Override
	public void onStatusStart() {
		super.onStatusStart();
	}

	@Override
	public void onStatusError(String action, String error) {
		super.onStatusError(action, error);
		
		
	}

}

