package plugin.safe.cloud.seal.fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.presenter.DataStatusInterface;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.util.CommonUtil;

public class MyFragment extends Fragment implements DataStatusInterface {
	

	
	private Context mContext;
	private View mRootView;
	private TextView mUserNickname;
	//private TextView mUserId;
	private View mLoadingView;
	private TextView mUserAddress;
	private HoursePresenter mPresent;
	private FrameLayout mSealTraceUpload;
	private FrameLayout mApplySeal;
	private FrameLayout mPassword;
	private FrameLayout mLogout;
	//private String mUsername;
	private FrameLayout mAboutUs;
	private String mPhone;
	private String mRealName;
	private Button mImageAvator;
	private String phone;
	private String realName;
	private String idCard;

	public MyFragment(Context ctx) {
		mContext = ctx;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPresent = new HoursePresenter(mContext, MyFragment.this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.w("fragmenttest", "homefragment onCreateView ");
		mRootView = inflater.inflate(R.layout.fgt_my_fragment, container, false);
		if (getArguments() != null) {  
	        mPhone = getArguments().getString("phone");
	        mRealName = getArguments().getString("realname");
		}
		initView();
//		initData();
		return mRootView;
	}
	
	private void initView(){
		
		TextView phone = (TextView)mRootView.findViewById(R.id.id_my_fragment_phone);
		TextView name = (TextView)mRootView.findViewById(R.id.id_my_fragment_name);
		phone.setText(mPhone);
		name.setText(mRealName);
		mImageAvator = (Button) mRootView.findViewById(R.id.img_avator);// 头像
//		mUserAddress = (TextView)mRootView.findViewById(R.id.id_user_address);
//		mLoadingView = (View)mRootView.findViewById(R.id.id_data_loading);
//		showLoadingView();
		mSealTraceUpload = (FrameLayout)mRootView.findViewById(R.id.id_my_seal_upload);
		mApplySeal = (FrameLayout)mRootView.findViewById(R.id.id_my_apply_for_seal);
		mPassword = (FrameLayout)mRootView.findViewById(R.id.id_my_seal_modify_password);
		mLogout = (FrameLayout)mRootView.findViewById(R.id.id_my_seal_exit_user);
		mAboutUs = (FrameLayout)mRootView.findViewById(R.id.id_my_seal_about_us);
		mPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(),new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.ModifyPasswordActivity"));
			
			}
		});
		
		mLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logoutUserDialog(0);
				
			}
		});
		mApplySeal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(),new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.ApplyForSealActivity"));
			}
		});
		mSealTraceUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(),new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.ShowSealTraceActivity"));
			}
		});
		mImageAvator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
					//if (realName != null && mPhone != null && idCard != null) {
						DLIntent intent = new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.PersonalInfoActivity");
						intent.putExtra("RealName", CommonUtil.mRegisterRealName);
						intent.putExtra("Phone", mPhone);
						intent.putExtra("IDCard", CommonUtil.mRegisterIdcard);
						DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(),intent);
					//}

			}
		});
		
		mAboutUs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(), new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.AboutUsActivity"));
				
			}
		});

	}
	
	private void initData(){
		getUserInfo();
	}
	
	private void getUserInfo(){
		String url = CommonUtil.mUserHost+"services.asmx?op=GetUserInfo";
		String soapaction = "http://tempuri.org/GetUserInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(soapaction));
		rpc.addProperty("username", CommonUtil.mUserLoginName);
		mPresent.readyPresentServiceParams(mContext, url, soapaction, rpc);
		mPresent.startPresentServiceTask(true);
		
	}

	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	private Handler mHandler = new Handler(){

		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HashMap<String,String> infoModel = parseUserInfo((String)msg.obj);
			dismissLoadingView();
			if (infoModel != null){
				mUserNickname.setText(CommonUtil.mRegisterRealName);
				mUserAddress.setText(infoModel.get("Phone"));
				phone = infoModel.get("Phone");
				mUserAddress.setText(phone);// 显示手机号
				realName = infoModel.get("RealName");
				mUserNickname.setText(realName);// 显示姓名
				idCard = infoModel.get("IDCard");
			}
		}
	};
	
	public static HashMap<String,String> parseUserInfo(String value) {
		HashMap<String,String> userInfo = null;
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				Log.w("house", "parse house info "+array.length());
				//for (int item = 0; item < array.length(); item++){
					
					JSONObject itemJsonObject = array.optJSONObject(0);
					userInfo = new HashMap<>();
					userInfo.put("NickName", itemJsonObject.optString("NickName"));
					userInfo.put("LoginName", itemJsonObject.optString("LoginName"));
					userInfo.put("Address", itemJsonObject.optString("Address"));
					userInfo.put("IDCard", itemJsonObject.optString("IDCard"));
					userInfo.put("Phone", itemJsonObject.optString("Phone"));
					//CommonUtil.mRegisterName = itemJsonObject.optString("RealName");
					//CommonUtil.mRegisterIdcard = itemJsonObject.optString("IDCard");
			}
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return userInfo;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void logoutUserDialog(final int position){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle(getString(R.string.user_logout))

	     .setMessage(getString(R.string.user_logout_remind))

	     .setPositiveButton(getString(R.string.button_ok),new DialogInterface.OnClickListener() {
	         @Override

	         public void onClick(DialogInterface dialog, int which) {
					SharedPreferences sharedata = mContext.getSharedPreferences("user_info", 0);
					SharedPreferences.Editor editor = sharedata.edit();
				    editor.putString("user_name", "");
				    editor.putString("user_password", "");
				    editor.putString("user_idcard", "");
				    editor.commit();
				    DLPluginManager.getInstance(getActivity()).startPluginActivity(getActivity(),new DLIntent("plugin.safe.cloud.seal", "plugin.safe.cloud.seal.LoginUserActivity"));
				    getActivity().finish();
	         }

	     }).setNegativeButton(getString(R.string.button_cancel),new DialogInterface.OnClickListener() {//��ӷ��ذ�ť

	         @Override

	         public void onClick(DialogInterface dialog, int which) {
	             Log.w("alertdialog"," dialog interface ");
	         }

	     }).show();
	}
//	
//	private void changeUserAreaDialog(){
//		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle(getString(R.string.user_logout)) 
//		  
//	     .setMessage(getString(R.string.user_change_area_title))//������ʾ������  
//	  
//	     .setPositiveButton(getString(R.string.button_ok),new DialogInterface.OnClickListener() {
//	         @Override  
//	  
//	         public void onClick(DialogInterface dialog, int which) {
//	        	 SharedPreferences sharedata = mContext.getSharedPreferences("user_info", 0);
//					SharedPreferences.Editor editor = sharedata.edit();
//				    editor.putString("user_name", "");
//				    editor.putString("user_password", "");
//				    editor.putString("area", "");
//				    editor.putString("host", "");
//				    editor.commit();
//				    Intent intent = new Intent(mContext, LoginUserActivity.class);
//		            startActivity(intent);    
//		            MyFragment.this.getActivity().finish();
//	         }  
//	  
//	     }).setNegativeButton(getString(R.string.button_cancel),new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
//	  
//	         @Override  
//	  
//	         public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�  
//	             Log.w("alertdialog"," �뱣�����ݣ�");  
//	         }  
//	  
//	     }).show();
//	}

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		// TODO Auto-generated method stub
		Log.e("mingguo", "success "+templateInfo);
		Message msgMessage = mHandler.obtainMessage();
		msgMessage.obj = templateInfo;
		msgMessage.sendToTarget();
	}

	@Override
	public void onStatusError(String action, String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusStart(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
