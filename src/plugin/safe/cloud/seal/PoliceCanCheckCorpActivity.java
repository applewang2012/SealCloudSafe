package plugin.safe.cloud.seal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.internal.DLIntent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import plugin.safe.cloud.seal.model.CarveCorpInfo;
import plugin.safe.cloud.seal.model.UniversalAdapter;
import plugin.safe.cloud.seal.model.UniversalViewHolder;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.shotview.Bimp;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.widget.MeasureGridView;

public class PoliceCanCheckCorpActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	private MeasureGridView noScrollgridview;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private String mGetCarveCorpIdByUserAction = "http://tempuri.org/GetCarveCorpIdByUser";
	private HoursePresenter mPresenter;
	private int mUploadNum = 0;
	private String mRentNo = "";
	private View mLoadingView;
	private EditText mFileNameEditView;
	private EditText mSealNameEditView;
	private String mFildId = null;
	private TextView mEmptyContent;
	private UniversalAdapter mAdapter;
	private List<CarveCorpInfo> mDataList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.aty_police_can_check_corp_layout, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("民警检查");
		Init();
	}

	public void Init() {
		initData();
		initAdapter();
	};

	private void initData(){
//		for (int i = 0; i < 10; i++){
//			CarveCorpInfo info = new CarveCorpInfo();
//			info.setCarveCorp("天津市河西区安云印刻章厂");
//			mDataList.add(info);
//		}
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		requestGetPoliceCheckInfoList(CommonUtil.mUserId);
	}

	private void requestGetPoliceCheckInfoList(String checkPersion){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetCarveCorpIdByUser";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetCarveCorpIdByUserAction));
		rpc.addProperty("userID", checkPersion);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mGetCarveCorpIdByUserAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void initAdapter(){
		mLoadingView = findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mEmptyContent = (TextView)findViewById(R.id.id_police_can_check_record_empty);
		mEmptyContent.setVisibility(View.INVISIBLE);
		ListView showList = (ListView)findViewById(R.id.id_police_can_check_record_show_list);
		mAdapter = new UniversalAdapter<CarveCorpInfo>(getApplicationContext(), R.layout.item_police_check_show_expandable_layout, mDataList) {

			@Override
			public void convert(final UniversalViewHolder holder, CarveCorpInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView name = (TextView)holderView.findViewById(R.id.id_item_police_check_name);
				name.setText(info.getCarveCorp());
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100) {
				if (msg.obj != null){
					parsePoliceCheckInfo((String)msg.obj);
					if (mDataList.size() == 0){
						mEmptyContent.setVisibility(View.VISIBLE);
					}else{
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};

	private void parsePoliceCheckInfo(String value){
		//[{"UserID":"52","Memo":"","ID":41,"CarveCorpID":"371400000001"},{"UserID":"52","Memo":"","ID":42,"CarveCorpID":"371400000007"}]
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mDataList.clear();
				Log.w("mingguo", "parse house info "+array.length());
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					CarveCorpInfo info = new CarveCorpInfo();
					info.setCarveCorp(itemJsonObject.optString("CarveCorpID"));
					mDataList.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "select photeo upload  status  success  action "+action+" temp info "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mGetCarveCorpIdByUserAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
	}


	@Override
	public void onStatusError(String action, String error) {
		super.onStatusError(action, error);
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			startPluginActivity(
					new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.PoliceCheckRecordActivity"));
	}
}

