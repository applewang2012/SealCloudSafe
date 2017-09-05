package safe.cloud.seal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import safe.cloud.seal.album.AlbumGridViewAdapter;
import safe.cloud.seal.album.AlbumHelper;
import safe.cloud.seal.album.ImageBucket;
import safe.cloud.seal.album.ImageItem;
import safe.cloud.seal.shotview.Bimp;
import safe.cloud.seal.shotview.PublicWay;

/**
 * 这个是进入相册显示所有图片的界面
 * 
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:47:15
 */
public class MoreAlbumActivity extends Activity {
	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private Button mSelectedButton;
	/*
	 * // 返回按钮 private Button back;
	 */
	/*
	 * // 取消按钮 private Button cancel;
	 */
	private Intent intent;
	// 预览按钮
	// private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	private TextView mSelectedViewNum;
	
	
	public  int max = 0;
	
	public  ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表
	
	public  List<Activity> activityList = new ArrayList<Activity>();
	
	public  int num = 5;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		//PublicWay.activityList.add(this);
		mContext = this;
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		num = PublicWay.num - Bimp.tempSelectBitmap.size();
		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
		init();
		initListener();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (tempSelectBitmap.size() > 0) {
//				intent.putExtra("position", "1");
//				intent.setClass(AlbumActivity.this, GalleryActivity.class);
//				startActivity(intent);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
//			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, SelectPhotoActivity.class);
//			startActivity(intent);
//			finish();
			Intent resultIntent = new Intent();
//			Bundle bundle = new Bundle();
//			bundle.putString("result", resultString);
//			bundle.putse
			resultIntent.putExtra("image_shot", tempSelectBitmap);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}

	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
		}
	}

	// 取消按钮的监听
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
//			tempSelectBitmap.clear();
//			intent.setClass(mContext, SelectPhotoActivity.class);
//			startActivity(intent);
		}
	}

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		/*
		 * back = (Button) findViewById(R.id.back); cancel = (Button)
		 * findViewById(R.id.cancel); cancel.setOnClickListener(new
		 * CancelListener()); back.setOnClickListener(new BackListener());
		 */
		// preview = (Button) findViewById(R.id.preview);
		// preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList, tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);
		mSelectedViewNum = (TextView) findViewById(R.id.id_show_seleted_num);
		gridView.setEmptyView(tv);
		mSelectedButton = (Button) findViewById(R.id.id_selected_image_button);
		mSelectedViewNum.setText(tempSelectBitmap.size() + "/" + num);
	}

	private void initListener() {

		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
				if (tempSelectBitmap.size() >= num) {
					toggleButton.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if (!removeOneData(dataList.get(position))) {
						Toast.makeText(MoreAlbumActivity.this, "超过最大选择数量", 200).show();
					}
					return;
				}
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					tempSelectBitmap.add(dataList.get(position));
					mSelectedViewNum.setText(tempSelectBitmap.size() + "/" + num);
				} else {
					tempSelectBitmap.remove(dataList.get(position));
					chooseBt.setVisibility(View.GONE);
					mSelectedViewNum.setText(tempSelectBitmap.size() + "/" + num);
				}
				isShowOkBt();
			}
		});

		mSelectedButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (tempSelectBitmap.contains(imageItem)) {
			tempSelectBitmap.remove(imageItem);
			mSelectedViewNum.setText("(" + tempSelectBitmap.size() + "/" + num);
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (tempSelectBitmap.size() > 0) {
			mSelectedViewNum.setText(tempSelectBitmap.size() + "/" + num);
			// preview.setPressed(true);
			// okButton.setPressed(true);
			// preview.setClickable(true);
			// okButton.setClickable(true);
			// okButton.setTextColor(Color.WHITE);
			// preview.setTextColor(Color.WHITE);
		} else {
			mSelectedViewNum.setText(tempSelectBitmap.size() + "/" + num);
			// preview.setPressed(false);
			// preview.setClickable(false);
			// okButton.setPressed(false);
			// okButton.setClickable(false);
			// okButton.setTextColor(Color.parseColor("#E1E0DE"));
			// preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
//		}
//		return false;
//
//	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
		
	}
	
	
}
