package plugin.safe.cloud.seal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import plugin.safe.cloud.seal.model.CarveCorpInfo;
import plugin.safe.cloud.seal.presenter.HoursePresenter;
import plugin.safe.cloud.seal.shotview.Bimp;
import plugin.safe.cloud.seal.util.CommonUtil;
import plugin.safe.cloud.seal.util.UtilTool;


public class PoliceCheckAddActivity extends BaseActivity {

	private View parentView;
	private String mGetCarveCorpIdByUserAction = "http://tempuri.org/GetCarveCorpIdByUser";
	private String mAddPoliceCheckInfoAction = "http://tempuri.org/AddPoliceCheckInfo";
	private HoursePresenter mPresenter;
	private View mLoadingView;
	private String[] mSealCorpList;
	private String mSelectSealCorp;
	private Button corpNameShow;
	private String mChangeStatus = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.aty_add_police_check_layout, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("添加检查");
		Init();
	}

	public void Init() {
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		corpNameShow = (Button) findViewById(R.id.id_police_check_danwei_name_button);
		corpNameShow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showLoadingView();
				requestGetPoliceCheckInfoList(CommonUtil.mUserId);
			}
		});
		final LinearLayout zhenggaiContent = (LinearLayout)findViewById(R.id.id_police_check_zhenggai_neirong_content) ;
		zhenggaiContent.setVisibility(View.GONE);
		final EditText zhenggaiNeirong = (EditText)findViewById(R.id.id_police_check_zhenggai_condition_input);
		LinearLayout changeYes = (LinearLayout)findViewById(R.id.id_police_check_zhenggai_yes);
		final ImageView yesIcon = (ImageView)findViewById(R.id.id_police_check_zhenggai_yes_icon);
		LinearLayout changeNo = (LinearLayout)findViewById(R.id.id_police_check_zhenggai_no);
		final ImageView noIcon = (ImageView)findViewById(R.id.id_police_check_zhenggai_no_icon);
		changeYes.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				yesIcon.setBackgroundResource(R.drawable.select_selected);
				noIcon.setBackgroundResource(R.drawable.select_normal);
				mChangeStatus = "1";
				zhenggaiContent.setVisibility(View.VISIBLE);
			}
		});
		changeNo.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				yesIcon.setBackgroundResource(R.drawable.select_normal);
				noIcon.setBackgroundResource(R.drawable.select_selected);
				mChangeStatus = "0";
				zhenggaiContent.setVisibility(View.GONE);
			}
		});
		final EditText checkContent = (EditText)findViewById(R.id.id_police_check_neirong_input);
		Button negativeButton = (Button)findViewById(R.id.id_police_check_button_negative);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		Button positiveButton = (Button)findViewById(R.id.id_police_check_button_positive);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mSelectSealCorp == null || mSelectSealCorp.equals("")){
					Toast.makeText(that, "请选择单位名称", Toast.LENGTH_LONG).show();
					return;
				}

				if (checkContent.getText().toString() == null || checkContent.getText().toString().equals("")){
					Toast.makeText(that, "检查内容不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				if (!mChangeStatus.equals("0")){
					if (zhenggaiNeirong.getText().toString() == null || zhenggaiNeirong.getText().toString().equals("")){
						Toast.makeText(that, "整改内容不能为空", Toast.LENGTH_LONG).show();
						return;
					}
				}


				showLoadingView();
				requestAddPoliceCheckInfoList(mSelectSealCorp, CommonUtil.mUserId,
						UtilTool.stampToNormalDateTime(System.currentTimeMillis()+""), checkContent.getText().toString(),
								mChangeStatus, zhenggaiNeirong.getText().toString());
			}
		});
	}


	private void requestGetPoliceCheckInfoList(String checkPersion){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=GetCarveCorpIdByUser";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mGetCarveCorpIdByUserAction));
		rpc.addProperty("userID", checkPersion);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mGetCarveCorpIdByUserAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void requestAddPoliceCheckInfoList(String carveId, String checkBy, String checkOn, String desc, String status, String memo){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=AddPoliceCheckInfo";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mAddPoliceCheckInfoAction));
		rpc.addProperty("checkID", "");
		rpc.addProperty("carveId", carveId);
		rpc.addProperty("checkedby", checkBy);
		rpc.addProperty("checkedOn", checkOn); //时间
		rpc.addProperty("desc", desc);
		rpc.addProperty("status", status);
		rpc.addProperty("memo", memo);
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mAddPoliceCheckInfoAction, rpc);
		mPresenter.startPresentServiceTask(true);
	}

	private void parsePoliceCheckInfoList(String value){
		//[{"UserID":"52","Memo":"","ID":41,"CarveCorpID":"371400000001"},{"UserID":"52","Memo":"","ID":42,"CarveCorpID":"371400000007"}]
		try{
			JSONArray array = new JSONArray(value);
			if (array != null){
				mSealCorpList = new String[array.length()];
				Log.w("mingguo", "parse house info "+array.length());
				for (int item = 0; item < array.length(); item++){
					JSONObject itemJsonObject = array.optJSONObject(item);
					CarveCorpInfo info = new CarveCorpInfo();
					info.setCarveCorp(itemJsonObject.optString("CarveCorpID"));
					mSealCorpList[item] = itemJsonObject.optString("RealName");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	private void showAlertDialog(final  String[] items){
		AlertDialog.Builder builder =new AlertDialog.Builder(that, AlertDialog.THEME_HOLO_LIGHT);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mSelectSealCorp = items[which];
				corpNameShow.setText(mSelectSealCorp);
			}
		});
		builder.show();
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
	

	@Override
	public void onRestart() {
		super.onRestart();
	}



	@Override
	public void onDestroy() {
		Bimp.tempSelectBitmap.clear();
		super.onDestroy();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissLoadingView();
			if (msg.what == 100){
                JSONObject obj = null;
                try {
                    obj = new JSONObject((String) msg.obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (obj != null){
					String ret = obj.optString("ret");
					if (ret != null && ret.equals("0")){
						Toast.makeText(that, "添加检查成功！", Toast.LENGTH_LONG).show();
                        finish();
					}
				}
			}else if (msg.what == 101) {
				parsePoliceCheckInfoList((String) msg.obj);
				showAlertDialog(mSealCorpList);
			}
		}
	};

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "select photeo upload  status  success  action "+action+" temp info "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mGetCarveCorpIdByUserAction)){
				Message message = mHandler.obtainMessage();
				message.what = 101;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mAddPoliceCheckInfoAction)){
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

}

