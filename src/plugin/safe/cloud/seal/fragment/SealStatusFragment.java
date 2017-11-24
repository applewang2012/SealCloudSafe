package plugin.safe.cloud.seal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;

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
import android.widget.Toast;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.ScanQrcodeActivity;
import plugin.safe.cloud.seal.model.SealStatusInfo;
import plugin.safe.cloud.seal.model.UniversalAdapter;
import plugin.safe.cloud.seal.model.UniversalViewHolder;
import plugin.safe.cloud.seal.presenter.DataStatusInterface;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.util.UtilTool;
import plugin.safe.cloud.seal.util.ViewUtil;
import plugin.safe.cloud.seal.widget.CircleFlowIndicator;
import plugin.safe.cloud.seal.widget.ImagePagerAdapter;
import plugin.safe.cloud.seal.widget.ViewFlow;


public class SealStatusFragment extends Fragment implements DataStatusInterface, OnItemClickListener {
	

	
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
	private String mGetSignetsListAction = "http://tempuri.org/GetSignetsListByApplyer";
	private UniversalAdapter mAdapter;
	private View mLoadingView;
	private TextView mEmptyContent;
	
	public SealStatusFragment(Context ctx) {
		mContext = ctx;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.w("mingguo", "homefragment onCreateView ");
		mPresent = new HoursePresenter(mContext, SealStatusFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.w("mingguo", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fg_signal_status_layout, container, false);
		if (getArguments() != null) {
	        mPhoneNumber = getArguments().getString("phone");
	        Log.w("mingguo", "framgent user phone  "+mPhoneNumber);
		}
		initTitleBar();
		initAdapter();
		initBanner();
		
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
        requestGetSignetsList();

    }

    private void initTitleBar() {

        View titlebarView = (View) mRootView.findViewById(R.id.id_common_title_bar);

        TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);

        titleText.setText("印章状态");
        titleText.setTextColor(Color.parseColor("#ffffff"));
        Button backButton = (Button) titlebarView.findViewById(R.id.id_titlebar_back);
        backButton.setVisibility(View.INVISIBLE);

        ImageView scanLogin = (ImageView) titlebarView.findViewById(R.id.id_titlebar_scan_login);
        if (!TextUtils.isEmpty(CommonUtil.mUserType)) {
            if ("0".equals(CommonUtil.mUserType)) {
                scanLogin.setVisibility(View.GONE);
            } else if ("2".equals(CommonUtil.mUserType)) {

                scanLogin.setVisibility(View.VISIBLE);
            }
        }
        scanLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	Toast.makeText(getActivity(), "扫描二维码", Toast.LENGTH_SHORT).show();
                DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.ScanQrcodeActivity");
                //((DLBasePluginActivity)mContext).startPluginActivityForResult(intent, 1);
                DLPluginManager.getInstance(getActivity()).startPluginActivityForResult(getActivity(), intent, 1);
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

    private void initAdapter() {
        mLoadingView = mRootView.findViewById(R.id.id_data_loading);
        mLoadingView.setVisibility(View.INVISIBLE);
        mEmptyContent = (TextView) mRootView.findViewById(R.id.id_seal_status_fragment_empty);
        mEmptyContent.setVisibility(View.INVISIBLE);
        ListView showList = (ListView) mRootView.findViewById(R.id.id_seal_status_fragment_list);
        mAdapter = new UniversalAdapter<SealStatusInfo>(mContext, R.layout.fgt_status_listview_item, mDataList) {

			@Override
			public void convert(UniversalViewHolder holder, SealStatusInfo info) {
				// TODO Auto-generated method stub
				View holderView = holder.getConvertView();
				TextView sealType = (TextView)holderView.findViewById(R.id.id_seal_status_item_type);
				TextView sealStatus = (TextView)holderView.findViewById(R.id.id_seal_status_item_status);
				TextView sealCorp = (TextView)holderView.findViewById(R.id.id_seal_status_item_corporation);
				TextView sealDate = (TextView)holderView.findViewById(R.id.id_seal_status_item_apply_date);
				sealType.setText(info.getSealType());
				sealStatus.setText(info.getShowSealStaus());
				sealCorp.setText(info.getSealCorp());
				sealDate.setText("申请时间："+info.getSealDate());
				if ("0".equals(info.getSealStaus())){
					//sealStatus.setText("审核中");
					sealStatus.setTextColor(Color.parseColor("#5686cd"));
				}else if ("8".equals(info.getSealStaus())){
					//sealStatus.setText("未通过");
					sealStatus.setTextColor(Color.parseColor("#d43c33"));
				}
			}
		};
		showList.setAdapter(mAdapter);
		showList.setOnItemClickListener(this);
	}
	
	private void requestGetSignetsList(){
		Log.w("mingguo", "request signet  list  username   "+ CommonUtil.mUserLoginName);
		ViewUtil.showLoadingView(mContext, mLoadingView);
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSignetsListByApplyer";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSignetsListAction));
		rpc.addProperty("applyerId", CommonUtil.mUserLoginName);
		mPresent.readyPresentServiceParams(mContext, url, mGetSignetsListAction, rpc);
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
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.e("mingguo", "onActivity result  request code  "+requestCode+"  data  "+data);
//		if (requestCode == 1 && resultCode == Activity.RESULT_OK){
//			if (data != null){
//				try {
//					String scanResult = data.getExtras().getString("result");
//					Log.e("mingguo", "scan  result  " + scanResult);
//					Toast.makeText(mContext, "scan result  "+scanResult, Toast.LENGTH_SHORT).show();
//					if (!TextUtils.isEmpty(scanResult)) {
//						JSONObject obj = new JSONObject(scanResult);
//						String type = obj.optString("type");
//						if (type != null && type.equalsIgnoreCase("login")){
//							String sid = obj.optString("sid");
//							Log.w("mingguo", "scan  result  sid " + sid+"  phone  "+mPhoneNumber);
//							showScanLoginDialog(sid);
//							
//						}
//					}
//
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 100){
				ViewUtil.dismissLoadingView(mLoadingView);
				if (msg.obj != null){
					if (parseSealStausInfo((String)msg.obj) > 0){
						mEmptyContent.setVisibility(View.INVISIBLE);
						mAdapter.notifyDataSetChanged();
					}else{
						mEmptyContent.setVisibility(View.VISIBLE);
					}
					
				}
			}

		}
	};
	
	private  int parseSealStausInfo(String value) {
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
			if (action != null && action.equals(mGetSignetsListAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 100;
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
		
		if (mDataList.get(position).getSealNo() != null || !mDataList.get(position).getSealNo().equals("")){
			if (mDataList.get(position).getSealStaus().equals("8")){
				DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.EditSealInfoActivity");
				intent.putExtra("seal_no", mDataList.get(position).getSealNo());
				((DLBasePluginActivity)getActivity()).startPluginActivity(intent);
			}else{
				DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.ShowSealInfoActivity");
				intent.putExtra("seal_no", mDataList.get(position).getSealNo());
				((DLBasePluginActivity)getActivity()).startPluginActivity(intent);
			}
			
		}else{
//			Intent loadIntent = new Intent(mContext, LoadUrlTestActivity.class);
//			loadIntent.putExtra("url", mDataList.get(position).getNearDetailUrl());
//			loadIntent.putExtra("tab_name", mDataList.get(position).getNearName());
//			startActivity(loadIntent);
		}
	}

	@Override
	public void onStatusStart(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	
}
