package safe.cloud.seal.fragment;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import safe.cloud.seal.R;
import safe.cloud.seal.ScanQrcodeActivity;
import safe.cloud.seal.model.SealStatusInfo;
import safe.cloud.seal.model.UniversalAdapter;
import safe.cloud.seal.model.UniversalViewHolder;
import safe.cloud.seal.presenter.DataStatusInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.widget.CircleFlowIndicator;
import safe.cloud.seal.widget.ImagePagerAdapter;
import safe.cloud.seal.widget.ViewFlow;

public class SealStatusFragment extends Fragment implements DataStatusInterface, OnItemClickListener{
	

	
	private Context mContext;
	private View mRootView;
	
	private HoursePresenter mPresent;
	private List<String> mTitleList = new ArrayList<>();
	private Map<Integer, String[]> mContentMap = new HashMap<>();
	private List<String> mContentList = new ArrayList<>();
	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private String mPhoneNumber;
	private List<SealStatusInfo> mDataList = new ArrayList<>();
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mPresent = new HoursePresenter(mContext, SealStatusFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("fragmenttest", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fg_signal_status_layout, container, false);
		if (getArguments() != null) {  
	        mPhoneNumber = getArguments().getString("phone");
	        Log.i("mingguo", "framgent user phone  "+mPhoneNumber);
		}
		initTitleBar();
		
		initData();
		initAdapter();
		initBanner();
		
		return mRootView;
	}
	
	private void initData(){
		for (int i = 0; i < 5; i++){
			SealStatusInfo info = new SealStatusInfo();
			info.setSealCorp("天津有限公司");
			info.setSealDate("申请时间：2017/08/05");
			info.setSealType("财务专用章");
			info.setSealStatus("未通过");
			mDataList.add(info);
		}
		
	}
	private void initTitleBar(){
		
		View titlebarView = (View)mRootView.findViewById(R.id.id_common_title_bar);
		TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);
		titleText.setText("印章状态");
		titleText.setTextColor(Color.parseColor("#ffffff"));
		Button backButton = (Button)titlebarView.findViewById(R.id.id_titlebar_back);
		backButton.setVisibility(View.INVISIBLE);
		
		ImageView scanLogin = (ImageView)titlebarView.findViewById(R.id.id_titlebar_scan_login);
		scanLogin.setVisibility(View.VISIBLE);
		scanLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(), ScanQrcodeActivity.class), 1);
			}
		});
		
	}
	
	private void initBanner() {
		List<Integer> localImage = new ArrayList<>();
		localImage.add(R.drawable.fg_sign_status_banner_view1);
		localImage.add(R.drawable.fg_sign_status_banner_view2);
		localImage.add(R.drawable.fg_sign_status_banner_view3);
		mViewFlow = (ViewFlow) mRootView.findViewById(R.id.id_fragment_surround_life_viewflow);
		mFlowIndicator = (CircleFlowIndicator) mRootView.findViewById(R.id.id_fragment_surround_life_viewflow_indicator);
		mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(), localImage,
				null, null).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(localImage.size()); // 实际图片张数，
														// 我的ImageAdapter实际图片张数为3
		mFlowIndicator.setIndicatorCount(localImage.size());
		
		mViewFlow.setFlowIndicator(mFlowIndicator);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(localImage.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mFlowIndicator.requestLayout();
		mFlowIndicator.invalidate();
	}
	
	private void initAdapter(){
		ListView showList = (ListView)mRootView.findViewById(R.id.id_seal_status_fragment_list);
		UniversalAdapter mAdapter = new UniversalAdapter<SealStatusInfo>(mContext, R.layout.fgt_status_listview_item, mDataList) {

			@Override
			public void convert(UniversalViewHolder holder, SealStatusInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView sealType = (TextView)holderView.findViewById(R.id.id_seal_status_item_type);
				TextView sealStatus = (TextView)holderView.findViewById(R.id.id_seal_status_item_status);
				TextView sealCorp = (TextView)holderView.findViewById(R.id.id_seal_status_item_corporation);
				TextView sealDate = (TextView)holderView.findViewById(R.id.id_seal_status_item_apply_date);
				sealType.setText(info.getSealType());
				sealStatus.setText(info.getSealStaus());
				sealCorp.setText(info.getSealCorp());
				sealDate.setText(info.getSealDate());
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
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
	             Log.i("alertdialog"," dialog interface ");  
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
		mPresent.startPresentServiceTask();
	}
	
	
//
//	private void showLoadingView(){
//		if (mLoadingView != null) {
//			mLoadingView.setVisibility(View.VISIBLE);
//        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
//        	if (imageView != null) {
//        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);
//        		imageView.startAnimation(rotate);
//        	}
//		}
//	}
//	private void dismissLoadingView(){
//		if (mLoadingView != null) {
//			mLoadingView.setVisibility(View.INVISIBLE);
//		}
//	}
	
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
							Log.i("mingguo", "scan  result  sid " + sid+"  phone  "+mPhoneNumber);
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
			// TODO Auto-generated method stub
			super.handleMessage(msg);

		}
	};
	
	public static HashMap<String,String> parseUserInfo(String value) {
		HashMap<String,String> userInfo = null;
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				Log.i("house", "parse house info "+array.length());
				//for (int item = 0; item < array.length(); item++){
					
					JSONObject itemJsonObject = array.optJSONObject(0);
					userInfo = new HashMap<>();
					userInfo.put("NickName", itemJsonObject.optString("NickName"));
					userInfo.put("LoginName", itemJsonObject.optString("LoginName"));
					userInfo.put("Address", itemJsonObject.optString("Address"));
					userInfo.put("IDCard", itemJsonObject.optString("IDCard"));
					userInfo.put("Phone", itemJsonObject.optString("Phone"));
					
			}
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return userInfo;
		}
	}
	
	

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
		Log.e("mingguo", "on status  start ");
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "on status error  "+error);
	}

	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
//		if (mDataList.get(position).getNearDetailUrl()== null || mDataList.get(position).getNearDetailUrl().equals("")){
//			GlobalUtil.shortToast(mContext, "抱歉，未获取到详细信息！", mContext.getResources().getDrawable(R.drawable.ic_dialog_no));
//			return;
//		}else{
//			Intent loadIntent = new Intent(mContext, LoadUrlTestActivity.class);
//			loadIntent.putExtra("url", mDataList.get(position).getNearDetailUrl());
//			loadIntent.putExtra("tab_name", mDataList.get(position).getNearName());
//			startActivity(loadIntent);
//		}
	}
	
}
