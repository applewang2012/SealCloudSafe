package safe.cloud.seal;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.cloudwalk.libproject.util.Util;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.presenter.HoursePresenter;
import safe.cloud.seal.shotview.BMapUtil;
import safe.cloud.seal.shotview.Bimp;
import safe.cloud.seal.shotview.FileUpLoadUtils;
import safe.cloud.seal.shotview.PublicWay;
import safe.cloud.seal.util.CommonUtil;
import safe.cloud.seal.util.UtilTool;
import safe.cloud.seal.widget.MeasureGridView;


/**
 * 首页面activity
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:48:34
 */
public class AddSealTraceActivity extends BaseActivity {

	private MeasureGridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private  String mAddSpecialPointAction = "http://tempuri.org/UploadSpecialPoint";
	private  String mAddSpecialPointFileAction = "http://tempuri.org/UploadSpecialPointFiles";
	private HoursePresenter mPresenter;
	private int mUploadNum = 0;
	private String mRentNo = "";
	private View mLoadingView;
	private EditText mFileNameEditView;
	private EditText mSealNameEditView;
	private String mFildId = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bimp.tempSelectBitmap.clear();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView tilebar = (TextView)findViewById(R.id.id_titlebar);
		tilebar.setText("添加印迹");
		Init();
	}

	public void Init() {
		mRentNo = getIntent().getStringExtra("rentNo");
		mPresenter = new HoursePresenter(getApplicationContext(), this);
		pop = new PopupWindow(AddSealTraceActivity.this);
		mLoadingView = (View)findViewById(R.id.id_data_loading);
		mLoadingView.setVisibility(View.INVISIBLE);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		mFileNameEditView = (EditText)findViewById(R.id.id_add_seal_track_input_file_name);
		mSealNameEditView = (EditText)findViewById(R.id.id_add_seal_track_input_seal_name);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AddSealTraceActivity.this,
						MoreAlbumActivity.class);
				
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
				startActivityForResult(intent, TAKE_AlBUM);
				//finish();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		noScrollgridview = (MeasureGridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size() && arg2 < PublicWay.num) {
					Log.w("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(AddSealTraceActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else if (arg2 != PublicWay.num){
					Intent intent = new Intent(AddSealTraceActivity.this, GalleryActivity.class);
					startActivity(intent);
				}
			}
		});
		
		Button upLoadImage = (Button)findViewById(R.id.id_upload_selected_image);
		upLoadImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mFileNameEditView.getText().toString() == null || mFileNameEditView.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "文件名字不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (mSealNameEditView.getText().toString() == null || mSealNameEditView.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "印章名字不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				mUploadNum = 0;
				startAddSpecialPointImage(mUploadNum);
			}
		});
	}
	
	private void startAddSpecialPointImage(int num){
		if (Bimp.tempSelectBitmap.size() < 1){
			Toast.makeText(getApplicationContext(), "请先选择图片", Toast.LENGTH_SHORT).show();
		}else{
			showLoadingView();
			addSpecialPointRequest(num);
			
		}
	}
	
	private void showLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	private void dismissLoadingView(){
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void addSpecialPointRequest(int num){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=UploadSpecialPoint";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mAddSpecialPointAction));
		rpc.addProperty("signetId", "");
		rpc.addProperty("content", mSealNameEditView.getText().toString());
		//rpc.addProperty("data", BMapUtil.bitmapToBase64(Bimp.tempSelectBitmap.get(num-1).getBitmap()));
		rpc.addProperty("type", "0");
		rpc.addProperty("lon", UtilTool.getCururentLocation(AddSealTraceActivity.this).get(1));
		rpc.addProperty("lat", UtilTool.getCururentLocation(AddSealTraceActivity.this).get(0));
		rpc.addProperty("uploadedBy", CommonUtil.mUserLoginName);
		rpc.addProperty("memo", mFileNameEditView.getText().toString());
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mAddSpecialPointAction, rpc);
		mPresenter.startPresentServiceTask();
		Log.w("mingguo", "add seal trace  special point request lati  "+UtilTool.getCururentLocation(AddSealTraceActivity.this).get(0)+" long "+UtilTool.getCururentLocation(AddSealTraceActivity.this).get(1));
	}
	
	private void addSpecialPointFileRequest(int num){
		String url = CommonUtil.mUserHost+"SignetService.asmx?op=UploadSpecialPointFiles";
		SoapObject rpc = new SoapObject(CommonUtil.NAMESPACE, CommonUtil.getSoapName(mAddSpecialPointFileAction));
		rpc.addProperty("fileId", mFildId);
		rpc.addProperty("data", BMapUtil.bitmapToBase64(Bimp.tempSelectBitmap.get(num-1).getBitmap()));
		rpc.addProperty("type", "0");
		rpc.addProperty("uploadedBy", CommonUtil.mUserLoginName);
		rpc.addProperty("lon", UtilTool.getCururentLocation(AddSealTraceActivity.this).get(1));
		rpc.addProperty("lat", UtilTool.getCururentLocation(AddSealTraceActivity.this).get(0));
		rpc.addProperty("memo", "");
		mPresenter.readyPresentServiceParams(getApplicationContext(), url, mAddSpecialPointFileAction, rpc);
		mPresenter.startPresentServiceTask();
		Log.w("mingguo", "add seal trace  special point request username  "+CommonUtil.mUserLoginName+" bitmap  width   "+Bimp.tempSelectBitmap.get(num-1).getBitmap().getWidth()
				+" bitmap  height   "+Bimp.tempSelectBitmap.get(num-1).getBitmap().getHeight());
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 6){
				return 6;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.aty_item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_seal_icon_image));
				if (position == PublicWay.num) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}
	
	@Override
	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;
	private static final int TAKE_AlBUM = 0x000002;
	
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 5 && resultCode == RESULT_OK) {
					
					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUpLoadUtils.saveBitmap(bm, fileName);
					
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				break;
			case TAKE_AlBUM:
				if (resultCode == RESULT_OK){
					Bimp.tempSelectBitmap.addAll((ArrayList<ImageItem>) data.getExtras().getSerializable("image_shot"));
					adapter.notifyDataSetChanged();
				}
				
				break;
		}
	
	}
	
	
	@Override
	protected void onDestroy() {
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
				if (msg.obj != null){
					JSONObject object;
					try {
						object = new JSONObject((String)msg.obj);
						String ret = object.optString("ret");
						if (ret != null && ret.equals("0")){
							mFildId = object.optString("ID");
							if (mFildId != null){
								mUploadNum = 1;
								Log.w("mingguo", "upload file field id  "+mFildId);
								addSpecialPointFileRequest(mUploadNum);
							}
							
						}else{
							Toast.makeText(getApplicationContext(), "上传图片失败！", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}else if (msg.what == 101){
				if (msg.obj != null){
					JSONObject object;
					try {
						object = new JSONObject((String)msg.obj);
						String ret = object.optString("ret");
						if (ret != null && ret.equals("0")){
							mUploadNum++;
							if (mUploadNum <= Bimp.tempSelectBitmap.size()){
								showLoadingView();
								addSpecialPointFileRequest(mUploadNum);
							}else if (mUploadNum > Bimp.tempSelectBitmap.size()){
								Log.e("mingguo", "  upload Bimp.tempSelectBitmap.size()  "+Bimp.tempSelectBitmap.size());
								Toast.makeText(getApplicationContext(), "上传图片完成！", Toast.LENGTH_SHORT).show();
								AddSealTraceActivity.this.finish();
							}
						}
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	@Override
	public void onStatusSuccess(String action, String templateInfo) {
		Log.w("mingguo", "select photeo upload  status  success  action "+action+" temp info "+templateInfo);
		if (action != null && templateInfo != null){
			if (action.equals(mAddSpecialPointAction)){
				Message message = mHandler.obtainMessage();
				message.what = 100;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}else if (action.equals(mAddSpecialPointFileAction)){
				Message message = mHandler.obtainMessage();
				message.what = 101;
				message.obj = templateInfo;
				mHandler.sendMessage(message);
			}
		}
	}

	@Override
	public void onStatusStart() {
		super.onStatusStart();
	}

	@Override
	public void onStatusError(String action, String error) {
		super.onStatusError(action, error);
		
		
	}

}

