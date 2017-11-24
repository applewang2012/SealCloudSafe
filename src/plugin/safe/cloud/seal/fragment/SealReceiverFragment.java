package plugin.safe.cloud.seal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.model.SealStatusInfo;
import plugin.safe.cloud.seal.model.UniversalAdapter;
import plugin.safe.cloud.seal.model.UniversalViewHolder;
import plugin.safe.cloud.seal.presenter.DataStatusInterface;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.util.UtilTool;
import plugin.safe.cloud.seal.util.ViewUtil;


public class SealReceiverFragment extends Fragment implements DataStatusInterface, OnItemClickListener {
	

	
	private Context mContext;
	private View mRootView;
	
	private HoursePresenter mPresent;
	private List<String> mTitleList = new ArrayList<>();
	private Map<Integer, String[]> mContentMap = new HashMap<>();
	private List<String> mContentList = new ArrayList<>();
	private String mPhoneNumber;
	private List<SealStatusInfo> mDataList = new ArrayList<>();
	private String mAcceptSignetAction = "http://tempuri.org/AcceptSignets";
	private String mGetCarveCopAction = "http://tempuri.org/GetCarveCorpIdByUser";
	private String mGetAcceptSignetAction = "http://tempuri.org/GetAcceptSignetList";
	private UniversalAdapter mAdapter;
	private View mLoadingView;
	private TextView mEmptyContent;
	private String mCarveCorpId;
	
	public SealReceiverFragment(Context ctx) {
		mContext = ctx;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPresent = new HoursePresenter(mContext, SealReceiverFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.w("fragmenttest", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fg_signal_receiver_layout, container, false);
		if (getArguments() != null) {
	        mPhoneNumber = getArguments().getString("phone");
	        Log.w("mingguo", "framgent user phone  "+mPhoneNumber);
		}
		initTitleBar();
		initAdapter();
		getmGetCarveCopRequest();
		return mRootView;
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	private void initData(){
//		for (int i = 0; i < 5; i++){
//			SealStatusInfo info = new SealStatusInfo();
//			info.setSealCorp("天津有限公司");
//			info.setSealDate("申请时间：2017/08/05");
//			info.setSealType("财务专用章");
//			info.setSealStatus("未通过");
//			mDataList.add(info);
//		}
//		requestGetSignetsList();
		
	}
	private void initTitleBar(){
		
		View titlebarView = (View)mRootView.findViewById(R.id.id_common_title_bar);
		TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);
		titleText.setText("接收印章");
		titleText.setTextColor(Color.parseColor("#ffffff"));
		Button backButton = (Button)titlebarView.findViewById(R.id.id_titlebar_back);
		backButton.setVisibility(View.INVISIBLE);
		
	}
	
	
	private void initAdapter(){
		mLoadingView = mRootView.findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mEmptyContent = (TextView)mRootView.findViewById(R.id.id_seal_receiver_fragment_empty);
		mEmptyContent.setVisibility(View.INVISIBLE);
		ListView showList = (ListView)mRootView.findViewById(R.id.id_seal_receiver_fragment_list);
		mAdapter = new UniversalAdapter<SealStatusInfo>(mContext, R.layout.fgt_seal_receiver_listview_item, mDataList) {

			@Override
			public void convert(final UniversalViewHolder holder, SealStatusInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView sealType = (TextView)holderView.findViewById(R.id.id_seal_receiver_item_type);
				Button sealStatus = (Button)holderView.findViewById(R.id.id_seal_receiver_item_button);
				TextView sealCorp = (TextView)holderView.findViewById(R.id.id_seal_receiver_item_corporation);
				TextView sealDate = (TextView)holderView.findViewById(R.id.id_seal_receiver_item_apply_date);
				sealType.setText(info.getSealType());
				sealStatus.setText("接收");
				sealCorp.setText(info.getSealCorp());
				sealDate.setText("申请时间："+info.getSealDate());
				sealStatus.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						requestAcceptSignetsButton(holder.getPosition());
					}
				});
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
	}
	
	private void requestAcceptSignetsButton(int index){
		ViewUtil.showLoadingView(mContext, mLoadingView);
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=AcceptSignets";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mAcceptSignetAction));
		rpc.addProperty("loginName", CommonUtil.mUserLoginName);
		rpc.addProperty("signetIds", mDataList.get(index).getSealNo());
		mPresent.readyPresentServiceParams(mContext, url, mAcceptSignetAction, rpc);
		mPresent.startPresentServiceTask(true);
	}

	private void requestGetAcceptSignetsList(String carcopId){
		ViewUtil.showLoadingView(mContext, mLoadingView);
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetAcceptSignetList";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetAcceptSignetAction));
		rpc.addProperty("carvecorpId", carcopId);
		mPresent.readyPresentServiceParams(mContext, url, mGetAcceptSignetAction, rpc);
		mPresent.startPresentServiceTask(true);
	}

	private void getmGetCarveCopRequest(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetCarveCorpIdByUser";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetCarveCopAction));
		rpc.addProperty("userID", CommonUtil.mUserId);
		mPresent.readyPresentServiceParams(mContext, url, mGetCarveCopAction, rpc);
		mPresent.startPresentServiceTask(true);
	}
	

	private void showScanLoginDialog(final String sid){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle("扫描登录")
		  
	     .setMessage("您确认要登录印章治安管理信息系统") 
	  
	     .setPositiveButton(getString(R.string.button_ok),new DialogInterface.OnClickListener() {
	         @Override
	  
	         public void onClick(DialogInterface dialog, int which) {
	        	 requestQRcodeLogin(sid, mPhoneNumber);
	         }  
	  
	     }).setNegativeButton(getString(R.string.button_cancel),new DialogInterface.OnClickListener() {
	  
	         @Override
	  
	         public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�
	             Log.w("alertdialog"," dialog interface ");
	         }  
	  
	     }).show();
	}
	
	private void requestQRcodeLogin(String sid, String phone){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=QRLogin";
		String soapaction = "http://tempuri.org/QRLogin";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(soapaction));
		rpc.addProperty("sid", sid);
		rpc.addProperty("phone", phone);
		mPresent.readyPresentServiceParams(mContext, url, soapaction, rpc);
		mPresent.startPresentServiceTask(true);
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK){
			if (data != null){
				try {
					String scanResult = data.getExtras().getString("result");
					Log.e("mingguo", "scan  result  " + scanResult);
					if (!TextUtils.isEmpty(scanResult)) {
						JSONObject obj = new JSONObject(scanResult);
						String type = obj.optString("type");
						if (type != null && type.equalsIgnoreCase("login")){
							String sid = obj.optString("sid");
							Log.w("mingguo", "scan  result  sid " + sid+"  phone  "+mPhoneNumber);
							showScanLoginDialog(sid);
							
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			ViewUtil.dismissLoadingView(mLoadingView);
			if (msg.what == 100){
				if (msg.obj != null) {
					try {
						JSONArray array = new JSONArray((String)msg.obj);
						if (array != null) {
							JSONObject itemJsonObject = array.optJSONObject(0);
							mCarveCorpId = itemJsonObject.optString("CarveCorpID");
							requestGetAcceptSignetsList(mCarveCorpId);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if (msg.what == 101){
				if (parseSealRecevierList((String)msg.obj) > 0){
						mEmptyContent.setVisibility(View.INVISIBLE);
						mAdapter.notifyDataSetChanged();
					}else{
						mEmptyContent.setVisibility(View.VISIBLE);
					}
					Log.e("mingguo", "date size  "+mDataList.size());
			}else if (msg.what == 102){
				try {
					JSONObject object = new JSONObject((String) msg.obj);
					String ret = object.optString("ret");
					if (ret != null && ret.equals("0")){
						requestGetAcceptSignetsList(mCarveCorpId);
					}else{
						Toast.makeText(mContext,"印章接收失败，请重试！", Toast.LENGTH_SHORT).show();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	};


	
	private  int parseSealRecevierList(String value) {
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mDataList.clear();
				Log.w("house", "parse house info "+array.length());
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					SealStatusInfo  statusInfo = new SealStatusInfo();
					statusInfo.setSealCorp(itemJsonObject.optString("co_corp_name"));
					statusInfo.setSealType(itemJsonObject.optString("RegCategory"));
					statusInfo.setShowSealStatus(itemJsonObject.optString("Status"));
					statusInfo.setSealStatus(itemJsonObject.optString("se_status"));
					statusInfo.setSealNo(itemJsonObject.optString("se_signet_id"));
					statusInfo.setSealDate(UtilTool.stampToNormalDate(UtilTool.extractNumberFromString(itemJsonObject.optString("sr_apply_date"))));
					//UtilTool.stampToNormalDate(s)
					mDataList.add(statusInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDataList.size();
	}
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.e("mingguo", "add seal info  action  "+action+"  success "+templateInfo);
		if (templateInfo != null){
			if (action.equals(mGetCarveCopAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 100;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mGetAcceptSignetAction)){
					Message msgMessage = mHandler.obtainMessage();
					msgMessage.what = 101;
					msgMessage.obj = templateInfo;
					msgMessage.sendToTarget();
			}else if (action.equals(mAcceptSignetAction)){
				//{"ret":"0","msg":"successed"}
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 102;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}
		}
		
		
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "on status error  "+error);
	}

	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
//		if (mDataList.get(position).getSealNo() != null || !mDataList.get(position).getSealNo().equals("")){
//			if (mDataList.get(position).getSealStaus().equals("8")){
//				Intent intent = new Intent(getActivity(), EditSealInfoActivity.class);
//				intent.putExtra("seal_no", mDataList.get(position).getSealNo());
//				startActivity(intent);
//			}else{
//				Intent intent = new Intent(getActivity(), ShowSealInfoActivity.class);
//				intent.putExtra("seal_no", mDataList.get(position).getSealNo());
//				startActivity(intent);
//			}
//
//		}else{
//			Intent loadIntent = new Intent(mContext, LoadUrlTestActivity.class);
//			loadIntent.putExtra("url", mDataList.get(position).getNearDetailUrl());
//			loadIntent.putExtra("tab_name", mDataList.get(position).getNearName());
//			startActivity(loadIntent);
//		}
	}

	@Override
	public void onStatusStart(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	
}
