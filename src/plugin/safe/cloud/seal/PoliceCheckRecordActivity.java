package plugin.safe.cloud.seal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import plugin.safe.cloud.seal.model.CarveCorpInfo;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.shotview.Bimp;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.widget.ExpandableListAdapter;
import plugin.safe.cloud.seal.widget.MeasureGridView;


public class PoliceCheckRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener{

	private MeasureGridView noScrollgridview;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private String mGetPoliceCheckInfoListAction = "http://tempuri.org/GetPoliceCheckInfoList";
	private String mAddSpecialPointFileAction = "http://tempuri.org/UploadSpecialPointFiles";
	private HoursePresenter mPresenter;
	private int mUploadNum = 0;
	private String mRentNo = "";
	private View mLoadingView;

	private TextView mEmptyContent;
	private ExpandableListAdapter mAdapter;
	private ArrayList<ArrayList<CarveCorpInfo>> mDataList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.aty_police_check_record_layout, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("检查记录");
		Button addCheckButton = (Button)findViewById(R.id.id_titlebar_button_add_function);
		addCheckButton.setVisibility(View.VISIBLE);
		addCheckButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startPluginActivity(
						new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.PoliceCheckAddActivity"));
			}
		});
		Init();
	}

	public void Init() {
		initData();
		initAdapter();
	};

	private void initData(){
		mPresenter = new HoursePresenter(getApplicationContext(), this);
//		for (int i = 0; i < 10; i++){
//			CarveCorpInfo info = new CarveCorpInfo();
//			info.setCarveCorp("2017-09-11");
//			mDataList.add(info);
//		}
//		requestGetPoliceCheckInfoList(CommonUtil.mUserId);
	}

	private void requestGetPoliceCheckInfoList(String checkPersion){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetPoliceCheckInfoList";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetPoliceCheckInfoListAction));
		rpc.addProperty("checkedby", checkPersion);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mGetPoliceCheckInfoListAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void initAdapter(){
		mLoadingView = findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mEmptyContent = (TextView)findViewById(R.id.id_police_check_record_empty);
		mEmptyContent.setVisibility(View.INVISIBLE);
		ExpandableListView showList = (ExpandableListView)findViewById(R.id.id_police_check_record_show_list);
		mAdapter = new ExpandableListAdapter(that, mDataList);
		showList.setAdapter(mAdapter);
		showList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
				DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.PoliceCheckShowActivity");
        		intent.putExtra("carve_id", mDataList.get(i).get(i1).getCarveCorp());
				intent.putExtra("check_date", mDataList.get(i).get(i1).getSealDate());
				startPluginActivity(intent);
				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		requestGetPoliceCheckInfoList(CommonUtil.mUserId);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				if (msg.obj != null) {
					parsePoliceCheckRecordList((String)msg.obj);
					if (mDataList.size() == 0){
						mEmptyContent.setVisibility(View.VISIBLE);
					}else{
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};

	private void parsePoliceCheckRecordList(String value){
		//[{"UserID":"52","Memo":"","ID":41,"CarveCorpID":"371400000001"},{"UserID":"52","Memo":"","ID":42,"CarveCorpID":"371400000007"}]
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mDataList.clear();
				Log.w("mingguo", "parse house info "+array.length());
				ArrayList<CarveCorpInfo> allList = new ArrayList<>();
				HashSet<String> typeList = new HashSet<>();
				HashMap<String,ArrayList<CarveCorpInfo>> allMap = new HashMap<>();
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					String carveId = itemJsonObject.optString("CarveCorpID");
					String date = itemJsonObject.optString("CheckedOn");
					String status = itemJsonObject.optString("CheckedStatus");
					CarveCorpInfo info = new CarveCorpInfo();
					info.setCarveCorp(carveId);
					info.setSealDate(date);
					info.setCheckStatus(status);
					typeList.add(carveId);
					allList.add(info);
				}
				if (allList.size() > 0){
					Iterator<String> iterator = typeList.iterator();
					while (iterator.hasNext()){
						String carId = iterator.next();
						ArrayList<CarveCorpInfo> itemList = new ArrayList<>();
						allMap.put(carId, itemList);
					}
					for (int index = 0; index < allList.size(); index++){
						String carveId = allList.get(index).getCarveCorp();
						if (allMap.containsKey(carveId)){
							ArrayList<CarveCorpInfo> carveList = allMap.get(carveId);
							carveList.add(allList.get(index));
						}
					}

					Log.e("mingguo", "alllist size "+allList.size()+" allmap size "+allMap.size());
					Iterator iter = allMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						//Object key = entry.getKey();
						ArrayList<CarveCorpInfo> valList = (ArrayList<CarveCorpInfo>)entry.getValue();
						mDataList.add(valList);
						Log.e("mingguo", " valList "+valList.size());
					}
					Log.e("mingguo", " data size  "+mDataList.size());
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
			if (action.equals(mGetPoliceCheckInfoListAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
//			}else if (action.equals(mAddSpecialPointFileAction)){
//				Message message = mHandler.obtainMessage();
//				message.what = 101;
//				message.obj = templateInfo;
//				mHandler.sendMessage(message);
			}
		}
	}


	@Override
	public void onStatusError(String action, String error) {
		super.onStatusError(action, error);

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent(PoliceCheckRecordActivity.this, PoliceCheckShowActivity.class);
//        intent.putExtra("carve_id", mDataList.get(i).getCarveCorp());
//		startActivity(intent);
	}
}

