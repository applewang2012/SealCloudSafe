package plugin.safe.cloud.seal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import plugin.safe.cloud.seal.model.CarveCorpInfo;
import plugin.safe.cloud.seal.presenter.AlertDialogInterface;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.shotview.Bimp;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.util.ViewUtil;
import plugin.safe.cloud.seal.widget.MeasureGridView;


public class PoliceCheckShowActivity extends BaseActivity {

	private MeasureGridView noScrollgridview;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private String mGetPoliceCheckInfoAction = "http://tempuri.org/GetPoliceCheckInfo";
	private String mCompletePoliceCheckInfoAction = "http://tempuri.org/CompletePoliceCheckInfo";

	private HoursePresenter mPresenter;
	private int mUploadNum = 0;
	private String mRentNo = "";
	private EditText mFileNameEditView;
	private EditText mSealNameEditView;
	private String mCarveCorpId = null;
	private String mCheckDate = null;
	private CarveCorpInfo mDetailInfo = new CarveCorpInfo();
	private TextView mCorpNameText,mCheckContenText, mNeirongText;
	private ImageView mZhenggaiYes, mZhenggaiNo;
	private Button mZhenggaiButton, mZhenggaiAgainButton;
	private EditText mZhenggaiInput;
	private View mLoadingView;
	private LinearLayout mZhenggaiContent, mZhenggaiNeirongContent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.aty_show_police_check_layout, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("检查详情");
		mCarveCorpId = getIntent().getStringExtra("carve_id");
		mCheckDate = getIntent().getStringExtra("check_date");
		Init();
	}

	private  void Init() {
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		requestGetPoliceCheckInf(CommonUtil.mUserId);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		mCorpNameText = (TextView)findViewById(R.id.id_police_check_danwei_name_show);
		mCheckContenText = (TextView)findViewById(R.id.id_police_check_neirong_show);
		mZhenggaiYes = (ImageView)findViewById(R.id.id_police_check_zhenggai_show_yes_icon);
		mZhenggaiNo = (ImageView)findViewById(R.id.id_police_check_zhenggai_show_no_icon);
		mZhenggaiButton = (Button)findViewById(R.id.id_police_check_zhenggai_button_positive);
		mZhenggaiInput = (EditText)findViewById(R.id.id_police_check_zhenggai_condition_show);
		mZhenggaiContent = (LinearLayout)findViewById(R.id.id_police_check_zhenggai_condition_content);
		mZhenggaiNeirongContent = (LinearLayout)findViewById(R.id.id_police_check_zhenggai_neirong_content);
		mNeirongText = (TextView)findViewById(R.id.id_police_check_zhenggai_neirong_show);
		mZhenggaiButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ViewUtil.showAlertDialog(that, "确认已完成整改", new AlertDialogInterface() {
					@Override
					public void onPostiveButton() {
						requestCompletePoliceZhenggai();
					}

					@Override
					public void onNegativeButton() {

					}
				});

			}
		});
		mZhenggaiAgainButton = (Button)findViewById(R.id.id_police_check_zhenggai_button_negativie);
		mZhenggaiAgainButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	};

	private void setShowView(){
		mCorpNameText.setText(mDetailInfo.getCarveCorp());
		mCheckContenText.setText(mDetailInfo.getCheckContent());
		mNeirongText.setText(mDetailInfo.getCheckZhenggaiNeirong());
		if (mDetailInfo.getCheckStatus().equals("0")){
			mZhenggaiNo.setBackgroundResource(R.drawable.select_selected);
			mZhenggaiYes.setBackgroundResource(R.drawable.select_normal);
			mZhenggaiButton.setVisibility(View.INVISIBLE);
			mZhenggaiAgainButton.setVisibility(View.INVISIBLE);
			mZhenggaiContent.setVisibility(View.INVISIBLE);
			mZhenggaiNeirongContent.setVisibility(View.INVISIBLE);
		}else{
			mZhenggaiNo.setBackgroundResource(R.drawable.select_normal);
			mZhenggaiYes.setBackgroundResource(R.drawable.select_selected);
			mZhenggaiButton.setVisibility(View.VISIBLE);
			mZhenggaiAgainButton.setVisibility(View.VISIBLE);
			mZhenggaiContent.setVisibility(View.VISIBLE);
			mZhenggaiNeirongContent.setVisibility(View.VISIBLE);
		}
	}

	private void requestGetPoliceCheckInf(String checkPersion){
		showLoadingView();
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetPoliceCheckInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetPoliceCheckInfoAction));
		rpc.addProperty("carvecorpId", mCarveCorpId);
		rpc.addProperty("checkedby", CommonUtil.mUserId);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mGetPoliceCheckInfoAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void requestCompletePoliceZhenggai(){
		showLoadingView();
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=CompletePoliceCheckInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mCompletePoliceCheckInfoAction));
		rpc.addProperty("checkId", mDetailInfo.getCheckId());
		rpc.addProperty("completeDesc", mZhenggaiInput.getText().toString());
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mCompletePoliceCheckInfoAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
			ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
			if (imageView != null) {
				RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(that, R.anim.anim_rotate);
				imageView.startAnimation(rotate);
			}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}

	private void parsePoliceCheckDetailList(String value){
		//[{"CheckID":"867c9f19-e01f-4618-a292-358ae496a213","CheckedBy":"52","CompleteDescription":"","CheckedOn":"\/Date(1509354414000)\/","CarveCorpID":"371400000001","CheckedDescription":"不用","CompletedOn":"\/Date(1509354225000)\/","CheckedStatus":"1","Memo":""},
		// {"CheckID":"4c62c77c-402c-4a30-832e-e835867cf6ab","CheckedBy":"52","CompleteDescription":"","CheckedOn":"\/Date(1509353509000)\/","CarveCorpID":"371400000001","CheckedDescription":"erf","CompletedOn":"\/Date(1509353320000)\/","CheckedStatus":"0","Memo":""}]
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					String checkDate = itemJsonObject.optString("CheckedOn");
					if (checkDate != null && checkDate.equals(mCheckDate)){
						mDetailInfo.setCarveCorp(itemJsonObject.optString("CarveCorpID"));
						mDetailInfo.setSealDate(itemJsonObject.optString("CheckedOn"));
						mDetailInfo.setCheckContent(itemJsonObject.optString("CheckedDescription"));
						mDetailInfo.setCheckStatus(itemJsonObject.optString("CheckedStatus"));
						mDetailInfo.setCheckId(itemJsonObject.optString("CheckID"));
						mDetailInfo.setCheckZhenggaiNeirong(itemJsonObject.optString("Memo"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissLoadingView();
			if (msg.what == 100) {
				if (msg.obj != null){
					parsePoliceCheckDetailList((String)msg.obj);
					setShowView();
				}
			}else if (msg.what == 101){

			}
		}
	};

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "select photeo upload  status  success  action "+action+" temp info "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mGetPoliceCheckInfoAction)) {
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mCompletePoliceCheckInfoAction)) {
				Message message = mHandler.obtainMessage();
				message.what = 101;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
	}


	@Override
	public void onStatusError(String action, String error) {
		super.onStatusError(action, error);
		
		
	}

}

