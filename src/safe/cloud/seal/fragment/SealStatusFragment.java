package safe.cloud.seal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.R;
import safe.cloud.seal.presenter.DataStatusInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;
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
		initTitleBar();
		initAdapter();
		
		initBanner();
		initSearchData();
		
		return mRootView;
	}
	
	private void initTitleBar(){
		
		View titlebarView = (View)mRootView.findViewById(R.id.id_common_title_bar);
		TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);
		titleText.setText("印章状态");
		titleText.setTextColor(Color.parseColor("#ffffff"));
		Button backButton = (Button)titlebarView.findViewById(R.id.id_titlebar_back);
		backButton.setVisibility(View.INVISIBLE);
		View titleBarBg = (View)mRootView.findViewById(R.id.id_titlebar_background);
		
		
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
	

	
	
	
	
	public  class TitleViewHolder {
        public TextView textView;
    }
	
	public  class ContentViewHolder {
        public Button button;
    }
	
	
	private void initSearchData() {
		
	}
	
	private int getContentCount(){
		int num = 0;
		for (int i =0; i < mContentMap.size(); i++){
			int childCount = mContentMap.get(i).length;
			num = num + childCount;
		}
		return num;
	}
	
	
	
	
//	private void getUserInfo(){
//		String url = CommonUtil.mUserHost+"services.asmx?op=GetUserInfo";
//		String soapaction = "http://tempuri.org/GetUserInfo";
//		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(soapaction));
//		rpc.addProperty("username", mUsername);
//		mPresent.readyPresentServiceParams(mContext, url, soapaction, rpc);
//		mPresent.startPresentServiceTask();
//		
//	}
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
	public void onDestroy() {
		super.onDestroy();
	}

	


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HashMap<String,String> infoModel = parseUserInfo((String)msg.obj);
//			dismissLoadingView();
//			if (infoModel != null){
//				mUserContainer.setVisibility(View.VISIBLE);
//				
//				mUserAddress.setText(infoModel.get("Phone"));
//				mUserId.setText(infoModel.get("LoginName"));
//			}
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
		
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		
	}

	
	
	private void initAdapter(){
//		mSurroundListview = (ListView)mRootView.findViewById(R.id.id_fragment_surround_life_listview);
//		mAdapter = new UniversalAdapter<SurroundInfo>(mContext, R.layout.surround_fragment_list_item, mDataList) {
//
//			@Override
//			public void convert(UniversalViewHolder holder, SurroundInfo info) {
//				View holderView = holder.getConvertView();
//				TextView surroundname = (TextView)holderView.findViewById(R.id.id_surround_fragment_item_hot_name);
//				TextView surroundaddress = (TextView)holderView.findViewById(R.id.id_surround_fragment_item_hot_address);
//				TextView surroundphone = (TextView)holderView.findViewById(R.id.id_surround_fragment_item_hot_phone);
//				surroundname.setText(info.getNearName());
//				surroundaddress.setText(info.getNearAddress());
//				surroundphone.setText(info.getNearPhone());
//			}
//		};
//		mSurroundListview.setAdapter(mAdapter);
//		mSurroundListview.setOnItemClickListener(this);
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
