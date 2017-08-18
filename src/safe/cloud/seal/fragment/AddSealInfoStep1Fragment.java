package safe.cloud.seal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import safe.cloud.seal.R;
import safe.cloud.seal.model.SealInfoModel;
import safe.cloud.seal.presenter.ActionOperationInterface;
import safe.cloud.seal.presenter.DataStatusInterface;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.GlobalUtil;
import safe.cloud.seal.widget.CircleFlowIndicator;
import safe.cloud.seal.widget.ViewFlow;

public class AddSealInfoStep1Fragment extends Fragment implements DataStatusInterface{
	

	
	private Context mContext;
	private View mRootView;
	
	private HoursePresenter mPresent;
	private List<String> mTitleList = new ArrayList<>();
	private Map<Integer, String[]> mContentMap = new HashMap<>();
	private List<String> mContentList = new ArrayList<>();
	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private ActionOperationInterface mAction;
	private String mAllRegisterAction = "http://tempuri.org/GetAllRegisters";
	private String mGetSignetIDAction = "http://tempuri.org/GetSignetID";
	private String mGetAllCarveCorpsAction = "http://tempuri.org/GetAllCarveCorps";
	private String mGetGeneralCodeAction = "http://tempuri.org/GetGeneralCode";
	private String mGetSealGuigeAction = "http://tempuri.org/GetSignetSpecification";
	private String mSendVerifyCodeAction = "http://tempuri.org/SendIdentifyCodeMsg";
	private String mCheckVerifyCodeAction = "http://tempuri.org/ValidateIdentifyCode";
	private String mGetDefaultSignContentAction = "http://tempuri.org/GetDefaultSignContent";
	private String mGetCorpInfoAction = "http://tempuri.org/GetCorporationInfo";
	private Map<String, SealInfoModel> mSelectorInfo = new HashMap<>();
	private View mLoadingView;
	private TextView mSelectQuyuText, mSealNumberText, mSelectSealType;
	private final String TYPE_ST = "ST", TYPE_SM = "SM", TYPE_UG = "UG", TYPE_KL = "KL", TYPE_DL = "DL";
	private TextView mSelectSealGuige;
	private TextView mSelectKezhiType;
	private String mCurrentType;
	private TextView mSelectDengjiType;
	private TextView mSelectZhizuoType;
	private TextView mSelectChaizhiType;
	private TextView mZhizhangDanweiText;
	private Button mVerifyCodeButton;
	private String mPhone, mVerifyCode;;
	private int mTimeCount = -1;
	private TextView mSealNeirongText;
	private EditText mSealUseDanweiEdit;
	
	
	public  void setFragmentActionListener(ActionOperationInterface action) {
		mAction = action;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mPresent = new HoursePresenter(mContext, AddSealInfoStep1Fragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("fragmenttest", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fgt_apply_for_seal_info_layout, container, false);
		initTitleBar();
		initView();
		initData();
		return mRootView;
	}
	
	private void initTitleBar(){
		
//		View titlebarView = (View)mRootView.findViewById(R.id.id_common_title_bar);
//		TextView titleText = (TextView) titlebarView.findViewById(R.id.id_titlebar);
//		titleText.setText("印章状态");
//		titleText.setTextColor(Color.parseColor("#ffffff"));
//		Button backButton = (Button)titlebarView.findViewById(R.id.id_titlebar_back);
//		backButton.setVisibility(View.INVISIBLE);
//		View titleBarBg = (View)mRootView.findViewById(R.id.id_titlebar_background);
	}
	
	
	
	private void initView(){
		mLoadingView = mRootView.findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
			
		mSelectQuyuText = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_quyu_content);
		FrameLayout quyuContent = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_select_quyu);
		quyuContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getAllRegister();
			}
		});
		
		FrameLayout zhizhangLayout = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_zhizhang_danwei);
		mZhizhangDanweiText = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_zhizhang_danwei_content);
		zhizhangLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mSealNumberText = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_number_content);
		
		FrameLayout sealType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_type);
		mSelectSealType = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_type_content);
		sealType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (mSelectorInfo.get("getAllRegister") == null || mSelectorInfo.get("getAllRegister").getSelectedId() == null){
//					GlobalUtil.shortToast(mContext, "请首先选择所属区域", getResources().getDrawable(R.drawable.ic_dialog_no));
//				}else{
					mCurrentType = TYPE_ST;
					mSelectSealGuige.setText("");
					mSelectorInfo.remove("GetSignetSpecification");
					showLoadingView();
					requestGetCorInfo(mSealUseDanweiEdit.getEditableText().toString());
					
//				}
			}
		});
		
		mSealUseDanweiEdit = (EditText)mRootView.findViewById(R.id.id_aty_apply_seal_input_seal_use_danwei);
//		mSealUseDanweiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus){
//					mSelectSealType.setText("");
//				}
//			}
//		});
		
		mSealUseDanweiEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("mingguo", "use danwei  onTextChanged  ");
				mSelectSealType.setText("");
				mSelectSealGuige.setText("");
				mSealNeirongText.setText("");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
		
		mSealNeirongText = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_seal_neirong_content);
		
		FrameLayout guigeType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_select_guige);
		mSelectSealGuige = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_select_guige_content);
		guigeType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mSelectorInfo.get("GetGeneralCode"+TYPE_ST) == null || mSelectorInfo.get("GetGeneralCode"+TYPE_ST).getSelectedId() == null){
					GlobalUtil.shortToast(mContext, "请首先选择印章类型", getResources().getDrawable(R.drawable.ic_dialog_no));
				}else{
					getSealGuigeByType(mSelectorInfo.get("GetGeneralCode"+TYPE_ST).getSelectedId());
				}
			}
		});
		
		FrameLayout kezhiType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_kezhi_type);
		mSelectKezhiType = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_kezhi_type_content);
		kezhiType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentType = TYPE_KL;
				getSealCommonCode(mSelectKezhiType, TYPE_KL);
				
			}
		});
		
		FrameLayout dengjiType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_dengji_type);
		mSelectDengjiType = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_dengji_type_content);
		dengjiType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentType = TYPE_DL;
				getSealCommonCode(mSelectDengjiType, TYPE_DL);
				
			}
		});
		
		FrameLayout zhizuoType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_zhizuo_type);
		mSelectZhizuoType = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_zhizuo_type_content);
		zhizuoType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentType = TYPE_UG;
				getSealCommonCode(mSelectZhizuoType, TYPE_UG);
				
			}
		});
		
		FrameLayout chaizhiType = (FrameLayout)mRootView.findViewById(R.id.id_aty_apply_seal_chaizhi_type);
		mSelectChaizhiType = (TextView)mRootView.findViewById(R.id.id_aty_apply_seal_chaizhi_type_content);
		chaizhiType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentType = TYPE_SM;
				getSealCommonCode(mSelectChaizhiType, TYPE_SM);
				
			}
		});
		
		final EditText phone = (EditText)mRootView.findViewById(R.id.id_aty_apply_seal_input_phone_number);
		final EditText inputVerify = (EditText)mRootView.findViewById(R.id.id_aty_apply_seal_input_phone_yanzhengma);
		final EditText inputName = (EditText)mRootView.findViewById(R.id.id_aty_apply_seal_input_idname);
		final EditText inputIdcard = (EditText)mRootView.findViewById(R.id.id_aty_apply_seal_input_idcard_number);
		mVerifyCodeButton = (Button)mRootView.findViewById(R.id.id_aty_apply_seal_get_phone_verifycode);
		mVerifyCodeButton.setOnClickListener(new OnClickListener() {
			


			@Override
			public void onClick(View v) {
				mPhone = phone.getEditableText().toString();
				if (mPhone == null || mPhone.equals("")){
					GlobalUtil.shortToast(mContext, getString(R.string.phone_not_null), mContext.getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}else if (mPhone.length() < 11){
					GlobalUtil.shortToast(mContext, getString(R.string.phone_input_error), mContext.getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				Log.w("mingguo", "register step 1  phone  "+mPhone+" time count  "+mTimeCount);
				if (mTimeCount < 0){
					mTimeCount = 60;
					sendPhoneVerifyCode(mPhone);
					mHandler.sendEmptyMessage(1000);
				}else{
					return;
				}
				
			}
		});
		
		
		Button actionNext = (Button)mRootView.findViewById(R.id.id_aty_apply_seal_next_step_button);
		actionNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAction.onNextFragment();
				return;
				/*if (!checkApplySealValid()){
					return;
				}
				mVerifyCode = inputVerify.getEditableText().toString();
				mPhone = phone.getEditableText().toString();
				String name = inputName.getEditableText().toString();
				String idcard = inputIdcard.getEditableText().toString();
				if (name == null || name.equals("")){
					GlobalUtil.shortToast(mContext, "申请人姓名不能为空", getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				if (idcard == null || idcard.equals("")){
					GlobalUtil.shortToast(mContext, "申请人身份证号不能为空", getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}else  if (idcard.length() < 18){
					GlobalUtil.shortToast(mContext, "申请人身份证号输入有误", getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				
				if (mPhone == null || mPhone.equals("")){
					GlobalUtil.shortToast(mContext, getString(R.string.phone_not_null), mContext.getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}else if (mPhone.length() < 11){
					GlobalUtil.shortToast(mContext, getString(R.string.phone_input_error), mContext.getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				if (mVerifyCode == null || mVerifyCode.equals("")){
					GlobalUtil.shortToast(mContext, "手机验证码不能为空", getResources().getDrawable(R.drawable.ic_dialog_no));
					return;
				}
				showLoadingView();
				checkPhoneVerifyCode(mPhone, mVerifyCode);*/
			}
		});
	}
	
	private boolean checkApplySealValid(){
		if (mSelectQuyuText == null || mSelectQuyuText.length() < 2){
			GlobalUtil.shortToast(mContext, "所属区域不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mZhizhangDanweiText == null || mZhizhangDanweiText.length() < 2){
			GlobalUtil.shortToast(mContext, "制章单位不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSealUseDanweiEdit.getEditableText().toString() == null || mSealUseDanweiEdit.getEditableText().toString().length() < 2){
			GlobalUtil.shortToast(mContext, "使用单位不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSelectSealType == null || mSelectSealType.length() < 2){
			GlobalUtil.shortToast(mContext, "印章类型不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSelectSealGuige == null || mSelectSealGuige.length() < 2){
			GlobalUtil.shortToast(mContext, "印章规格不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSealNeirongText == null || mSealNeirongText.length() < 2){
			GlobalUtil.shortToast(mContext, "使用单位输入有误！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSelectKezhiType == null || mSelectKezhiType.length() < 2){
			GlobalUtil.shortToast(mContext, "刻制类型不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSelectDengjiType == null || mSelectDengjiType.length() < 2){
			GlobalUtil.shortToast(mContext, "登记类型不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		if (mSelectZhizuoType == null || mSelectZhizuoType.length() < 2){
			GlobalUtil.shortToast(mContext, "制作等级不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}if (mSelectChaizhiType == null || mSelectChaizhiType.length() < 2){
			GlobalUtil.shortToast(mContext, "章体材质不能为空！", getResources().getDrawable(R.drawable.ic_dialog_no));
			return false;
		}
		return true;
	}
	
	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initData(){
		
	}
	
	private void getAllRegister(){
		if (!mSelectorInfo.containsKey("getAllRegister")){
			showLoadingView();
			SealInfoModel infoModel = new SealInfoModel();
			mSelectorInfo.put("getAllRegister", infoModel);
			requestAllRegister();
		}else{
			showAlertDialog(mSelectQuyuText, "getAllRegister", mSelectorInfo.get("getAllRegister").getHouseAllContent());
		}
	}
	
	private void requestAllRegister(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetAllRegisters";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mAllRegisterAction));
		mPresent.readyPresentServiceParams(mContext, url, mAllRegisterAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void requestGetCorInfo(String corpName){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetCorporationInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetCorpInfoAction));
		rpc.addProperty("corpName",corpName);
		mPresent.readyPresentServiceParams(mContext, url, mGetCorpInfoAction, rpc);
		mPresent.startPresentServiceTask();
		
		SealInfoModel infoModel = new SealInfoModel();
		infoModel.setSelectedName(corpName);
		mSelectorInfo.put("GetCorporationInfo", infoModel);
	}
	
	private void requestSignetId(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSignetID";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSignetIDAction));
		rpc.addProperty("regDeptId", mSelectorInfo.get("getAllRegister").getSelectedId());
		mPresent.readyPresentServiceParams(mContext, url, mGetSignetIDAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void requestDefaultSignetContent(){
		if (mSelectorInfo.get("GetCorporationInfo") != null && mSelectorInfo.get("GetCorporationInfo").getSelectedId() != null){
			if (mSelectorInfo.get("GetGeneralCode"+TYPE_ST)!= null && mSelectorInfo.get("GetGeneralCode"+TYPE_ST).getSelectedId() != null){
				String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetDefaultSignContent";
				SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetDefaultSignContentAction));
				rpc.addProperty("corpId", mSelectorInfo.get("GetCorporationInfo").getSelectedId());
				rpc.addProperty("signetTypeId", mSelectorInfo.get("GetGeneralCode"+TYPE_ST).getSelectedId());
				rpc.addProperty("signetType", mSelectorInfo.get("GetGeneralCode"+TYPE_ST).getSelectedName());
				mPresent.readyPresentServiceParams(mContext, url, mGetDefaultSignContentAction, rpc);
				mPresent.startPresentServiceTask();
			}
		}
	}
	
	private void getAllCarveCorps(){
		
		//if (!mSelectorInfo.containsKey("getCarveCorps")){
			showLoadingView();
			SealInfoModel infoModel = new SealInfoModel();
			mSelectorInfo.put("getCarveCorps", infoModel);
			requestGetAllCarveCorps();
//		}else{
//			showAlertDialog(mZhizhangDanweiText, "getCarveCorps", mSelectorInfo.get("getCarveCorps").getHouseAllContent());
//		}
	}
	
	private void requestGetAllCarveCorps(){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetAllCarveCorps";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetAllCarveCorpsAction));
		Log.i("mingguo", "add seal info get adll carvecorps  id  "+mSelectorInfo.get("getAllRegister").getSelectedId());
		rpc.addProperty("regDeptId", mSelectorInfo.get("getAllRegister").getSelectedId());
		mPresent.readyPresentServiceParams(mContext, url, mGetAllCarveCorpsAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void getSealCommonCode(TextView commonView, String typeId){
		if (!mSelectorInfo.containsKey("GetGeneralCode"+typeId)){
			showLoadingView();
			SealInfoModel infoModel = new SealInfoModel();
			mSelectorInfo.put("GetGeneralCode"+typeId, infoModel);
			requestCommonData(typeId);
		}else{
			showAlertDialog(commonView, "GetGeneralCode"+typeId, mSelectorInfo.get("GetGeneralCode"+typeId).getHouseAllContent());
		}
	}
	
	private void sendPhoneVerifyCode(String phone){
		String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=SendIdentifyCodeMsg";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mSendVerifyCodeAction));
		rpc.addProperty("phone", phone); 
		mPresent.readyPresentServiceParams(mContext, url, mSendVerifyCodeAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void checkPhoneVerifyCode(String phone, String code){
		String url = "http://www.guardts.com/COMMONSERVICE/COMMONSERVICES.ASMX?op=ValidateIdentifyCode";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mCheckVerifyCodeAction));
		rpc.addProperty("phone", phone); 
		rpc.addProperty("number", code); 
		mPresent.readyPresentServiceParams(mContext, url, mCheckVerifyCodeAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void getSealGuigeByType(String typeId){
//		if (!mSelectorInfo.containsKey("GetSignetSpecification")){
			showLoadingView();
			SealInfoModel infoModel = new SealInfoModel();
			mSelectorInfo.put("GetSignetSpecification", infoModel);
			requestSealGuige(typeId);
//		}else{
//			showAlertDialog(mSelectSealGuige, "GetSignetSpecification", mSelectorInfo.get("GetSignetSpecification").getHouseAllContent());
//		}
	}
	
	private void requestCommonData(String typeId){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetGeneralCode";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetGeneralCodeAction));
		Log.i("mingguo", "add seal info get common data  typeid   "+typeId);
		rpc.addProperty("typeId", typeId);
		mPresent.readyPresentServiceParams(mContext, url, mGetGeneralCodeAction, rpc);
		mPresent.startPresentServiceTask();
	}
	
	private void requestSealGuige(String typeId){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetSignetSpecification";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetSealGuigeAction));
		Log.i("mingguo", "add seal info get common data  typeid   "+typeId);
		rpc.addProperty("typeId", typeId);
		mPresent.readyPresentServiceParams(mContext, url, mGetSealGuigeAction, rpc);
		mPresent.startPresentServiceTask();
	}
	

	private void showAlertDialog(final TextView text, final String tag, final String[] items) {  
		  AlertDialog.Builder builder =new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
		  builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mSelectorInfo.get(tag).setSelectedId(mSelectorInfo.get(tag).getHouseAllId()[which]);
				mSelectorInfo.get(tag).setSelectedName(items[which]);
				text.setText(items[which]);
				if (tag != null ){
					if (tag.equals("getAllRegister")){
						requestSignetId();
					}else if (tag.equals("GetGeneralCode"+TYPE_ST)){
						requestDefaultSignetContent();
					}
				}
				
			}
		});
		builder.show();
}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.obj != null){
				if (msg.what == 200){
					dismissLoadingView();
					mSelectorInfo.get("getAllRegister").setHouseAllContent(parseAllRegisterReturn((String)msg.obj).get(1));
					mSelectorInfo.get("getAllRegister").setHouseAllId(parseAllRegisterReturn((String)msg.obj).get(0));
					showAlertDialog(mSelectQuyuText,"getAllRegister" , parseAllRegisterReturn((String)msg.obj).get(1));
				}else if (msg.what == 201){
					dismissLoadingView();
					mSealNumberText.setText((String)msg.obj);
					getAllCarveCorps();
				}else if (msg.what == 202){
					dismissLoadingView();
					mZhizhangDanweiText.setText(parseSealKezhiDanwei((String)msg.obj));
				}else if (msg.what == 203){
					dismissLoadingView();
					if (mCurrentType == TYPE_ST){
						mSelectorInfo.get("GetGeneralCode"+TYPE_ST).setHouseAllContent(parseSealTypeSTReturn((String)msg.obj).get(1));
						mSelectorInfo.get("GetGeneralCode"+TYPE_ST).setHouseAllId(parseSealTypeSTReturn((String)msg.obj).get(0));
						showAlertDialog(mSelectSealType,"GetGeneralCode"+TYPE_ST , parseSealTypeSTReturn((String)msg.obj).get(1));
					}else if (mCurrentType == TYPE_KL){
						mSelectorInfo.get("GetGeneralCode"+TYPE_KL).setHouseAllContent(parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
						mSelectorInfo.get("GetGeneralCode"+TYPE_KL).setHouseAllId(parseSealReturn((String)msg.obj,"gc_name", "gc_id").get(0));
						showAlertDialog(mSelectKezhiType,"GetGeneralCode"+TYPE_KL, parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
					}else if (mCurrentType == TYPE_DL){
						mSelectorInfo.get("GetGeneralCode"+TYPE_DL).setHouseAllContent(parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
						mSelectorInfo.get("GetGeneralCode"+TYPE_DL).setHouseAllId(parseSealReturn((String)msg.obj,"gc_name", "gc_id").get(0));
						showAlertDialog(mSelectDengjiType,"GetGeneralCode"+TYPE_DL, parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
					}else if (mCurrentType == TYPE_UG){
						mSelectorInfo.get("GetGeneralCode"+TYPE_UG).setHouseAllContent(parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
						mSelectorInfo.get("GetGeneralCode"+TYPE_UG).setHouseAllId(parseSealReturn((String)msg.obj,"gc_name", "gc_id").get(0));
						showAlertDialog(mSelectZhizuoType,"GetGeneralCode"+TYPE_UG, parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
					}else if (mCurrentType == TYPE_SM){
						mSelectorInfo.get("GetGeneralCode"+TYPE_SM).setHouseAllContent(parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
						mSelectorInfo.get("GetGeneralCode"+TYPE_SM).setHouseAllId(parseSealReturn((String)msg.obj,"gc_name", "gc_id").get(0));
						showAlertDialog(mSelectChaizhiType,"GetGeneralCode"+TYPE_SM, parseSealReturn((String)msg.obj, "gc_name", "gc_id").get(1));
					}
					
				}else if (msg.what == 204){
					dismissLoadingView();
					mSelectorInfo.get("GetSignetSpecification").setHouseAllContent(parseSealReturn((String)msg.obj, "ss_specification", "ss_signet_type").get(1));
					mSelectorInfo.get("GetSignetSpecification").setHouseAllId(parseSealReturn((String)msg.obj,"ss_specification", "ss_signet_type").get(0));
					showAlertDialog(mSelectSealGuige,"GetSignetSpecification" , parseSealReturn((String)msg.obj, "ss_specification", "ss_signet_type").get(1));
				}else if (msg.what == 205){
					dismissLoadingView();
					String corpId = parseSealShiyongDanwei((String)msg.obj);
					if (corpId != null){
						mSelectorInfo.get("GetCorporationInfo").setSelectedId(corpId);
						getSealCommonCode(mSelectSealType, TYPE_ST);
						//requestDefaultSignetContent();
					}else{
						GlobalUtil.shortToast(mContext, "使用单位输入有误，请重新输入！", getResources().getDrawable(R.drawable.ic_dialog_no));
						mSealNeirongText.setText("");
					}
				}else if (msg.what == 206){
					mSealNeirongText.setText((String)msg.obj);
				}else if (msg.what == 1000){
					Log.i("mingguo", "time count  "+mTimeCount);
					
				}else if (msg.what == 1002){
					dismissLoadingView();
					if (msg.obj != null){
						JSONObject json;
						try {
							json = new JSONObject((String)msg.obj);
							String ret = json.optString("ret");
							if (ret != null){
								if (ret.equals("0")){
									mAction.onNextFragment();
								}else{
									GlobalUtil.shortToast(mContext, getString(R.string.verify_error), getResources().getDrawable(R.drawable.ic_dialog_no));
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				if (msg.what == 1000){
					if (mTimeCount >= 0){
						mVerifyCodeButton.setTextColor(Color.parseColor("#b2b2b2"));
						mVerifyCodeButton.setText(mTimeCount +" 秒");
						mTimeCount--;
						mHandler.sendEmptyMessageDelayed(1000, 1000);
					}else{
						mVerifyCodeButton.setTextColor(Color.parseColor("#ef1619"));
						mVerifyCodeButton.setText("获取验证码");
					}
				}
				
			}
		}
			
	};
	
	public static List<String[]> parseAllRegisterReturn(String value) {
		String [] name = null;
		String [] nameId;
		List<String[]> list = new ArrayList<>();
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				name = new String[array.length()];
				nameId = new String[array.length()];
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					name[item] = itemJsonObject.optString("rd_reg_dept_name");
					nameId[item] = itemJsonObject.optString("rd_reg_dept_id");
				}
				list.add(nameId);
				list.add(name);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}
	
	public static List<String[]> parseSealTypeSTReturn(String value) {
		String [] name = null;
		String [] nameId;
		List<String[]> list = new ArrayList<>();
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				name = new String[array.length()];
				nameId = new String[array.length()];
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					name[item] = itemJsonObject.optString("gc_name");
					nameId[item] = itemJsonObject.optString("gc_id");
				}
				list.add(nameId);
				list.add(name);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}
	
	private String parseSealKezhiDanwei(String value){
		String kezhiDanwei = null;
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
//				name = new String[array.length()];
//				nameId = new String[array.length()];
//				for (int item = 0; item < array.length(); item++){
				if (array.length() > 0){
					JSONObject itemJsonObject = array.optJSONObject(0);
					kezhiDanwei = itemJsonObject.optString("cac_corp_name");
				}

//				}
//				list.add(nameId);
//				list.add(name);
			}
			
			return kezhiDanwei;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String parseSealShiyongDanwei(String value){
		String kezhiDanwei = null;
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
//				name = new String[array.length()];
//				nameId = new String[array.length()];
//				for (int item = 0; item < array.length(); item++){
				if (array.length() > 0){
					JSONObject itemJsonObject = array.optJSONObject(0);
					kezhiDanwei = itemJsonObject.optString("co_corp_id");
				}

//				}
//				list.add(nameId);
//				list.add(name);
			}
			
			return kezhiDanwei;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String[]> parseSealReturn(String value, String dataname, String datanameId) {
		String [] name = null;
		String [] nameId;
		List<String[]> list = new ArrayList<>();
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				name = new String[array.length()];
				nameId = new String[array.length()];
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					name[item] = itemJsonObject.optString(dataname);
					nameId[item] = itemJsonObject.optString(datanameId);
				}
				list.add(nameId);
				list.add(name);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}
	

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "add seal info  action  "+action+"  success "+templateInfo);
		if (action != null){
			if (action.equals(mAllRegisterAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 200;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mGetSignetIDAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 201;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mGetAllCarveCorpsAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 202;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mGetGeneralCodeAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 203;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mGetSealGuigeAction)){
				Message msgMessage = mHandler.obtainMessage();
				msgMessage.what = 204;
				msgMessage.obj = templateInfo;
				msgMessage.sendToTarget();
			}else if (action.equals(mCheckVerifyCodeAction)){
				Message message = mHandler.obtainMessage();
				message.what = 1002;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mGetCorpInfoAction)){
				Message message = mHandler.obtainMessage();
				message.what = 205;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mGetDefaultSignContentAction)){
				Message message = mHandler.obtainMessage();
				message.what = 206;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
		
	}

	@Override
	public void onStatusStart() {
		// TODO Auto-generated method stub
		Log.e("mingguo", "add seal info  action  on status start  ");
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "add seal info  action  "+action+"  error "+error);
	}

	
	
	
	
}
