package safe.cloud.seal;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import safe.cloud.seal.shotview.Bimp;
import safe.cloud.seal.shotview.ViewPagerFixed;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
    // 返回按钮
   // private Button back_bt;
	// 发送按钮
	//private Button send_bt;
	//删除按钮
	//private Button del_bt;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	//private int position;
	//当前的位置
	private int location = 0;
	
	private ArrayList<String> mImageStringList = new ArrayList<>();
	private ArrayList<View> listViews = new ArrayList<>();
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	
	private Context mContext;

	RelativeLayout photo_relativeLayout;
	private TextView mSelectedViewNum;
	private int mTotalNum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		//PublicWay.activityList.add(this);
		mContext = this;
		//back_bt = (Button) findViewById(R.id.gallery_back);
		//send_bt = (Button) findViewById(R.id.send_button);
		//del_bt = (Button)findViewById(R.id.gallery_del);
		//back_bt.setOnClickListener(new BackListener());
		//send_bt.setOnClickListener(new GallerySendListener());
		//del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		//position = intent.getIntExtra("position", 0);
		//mTotalNum = intent.getIntExtra("listSize", 0);
		mImageStringList =  getIntent().getStringArrayListExtra("imagelist");
		//bundle.get("imagelist");
		if (mImageStringList != null){
			mTotalNum = mImageStringList.size();
			for (int i = 0; i < mImageStringList.size(); i++){
				Log.i("mingguo", "mImageStringList.get(i) "+mImageStringList.get(i));
				initListViewsByMemoryBitmap(i);
			}
		}
		if (mTotalNum == 0){
			mTotalNum = Bimp.tempSelectBitmap.size();
		}
		mSelectedViewNum = (TextView) findViewById(R.id.id_show_seleted_num);
		mSelectedViewNum.setText((location+1) + "/"+mTotalNum);
		//isShowOkBt();
		// 为发送按钮设置文字
		FrameLayout back = (FrameLayout)findViewById(R.id.id_titlebar_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
//		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//			initListViews( Bimp.tempSelectBitmap.get(i).getBitmap() );
//		}
		
		if (listViews.size() == 0){
			for (int index = 0; index < Bimp.tempSelectBitmap.size(); index++){
				Bitmap bitmap = Bimp.tempSelectBitmap.get(index).getBitmap();
				ImageView image = new ImageView(mContext);
				image.setBackgroundColor(0xff000000);
				image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				image.setImageBitmap(bitmap);
				listViews.add(image);
			}
		}
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
		int id = intent.getIntExtra("position", 0);
		pager.setCurrentItem(0);
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			mSelectedViewNum.setText((location+1) + "/"+mTotalNum);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
//	private void initListViews(Bitmap bm) {
//		if (listViews == null)
//			listViews = new ArrayList<View>();
//		PhotoView img = new PhotoView(this);
//		img.setBackgroundColor(0xff000000);
//		img.setImageBitmap(bm);
//		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT));
//		listViews.add(img);
//	}
	
	private void initListViewsByMemoryBitmap(int index) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		
		ImageView image = new ImageView(mContext);
		image.setBackgroundColor(0xff000000);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ImageLoader.getInstance().displayImage(mImageStringList.get(index), image);
		listViews.add(image);
}
	
	// 返回按钮添加的监听器
//	private class BackListener implements OnClickListener {
//
//		public void onClick(View v) {
//			intent.setClass(GalleryActivity.this, ImageFile.class);
//			startActivity(intent);
//		}
//	}
	
//	// 删除按钮添加的监听器
//	private class DelListener implements OnClickListener {
//
//		public void onClick(View v) {
//			if (listViews.size() == 1) {
//				Bimp.tempSelectBitmap.clear();
//				Bimp.max = 0;
//				//send_bt.setText(R.string.finish+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
//				Intent intent = new Intent("data.broadcast.action");  
//                sendBroadcast(intent);  
//				finish();
//			} else {
//				Bimp.tempSelectBitmap.remove(location);
//				Bimp.max--;
//				pager.removeAllViews();
//				listViews.remove(location);
//				adapter.setListViews(listViews);
//				//send_bt.setText(R.string.finish +"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
//				adapter.notifyDataSetChanged();
//				mSelectedViewNum.setText((location+1) + "/"+Bimp.tempSelectBitmap.size());
//			}
//		}
//	}

	// 完成按钮的监听
	private class GalleryClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}
	}

//	public void isShowOkBt() {
//		if (Bimp.tempSelectBitmap.size() > 0) {
//			send_bt.setText(R.string.finish +"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
//			send_bt.setPressed(true);
//			send_bt.setClickable(true);
//			send_bt.setTextColor(Color.WHITE);
//		} else {
//			send_bt.setPressed(false);
//			send_bt.setClickable(false);
//			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
//		}
//	}

	/**
	 * 监听返回按钮
	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(position==1){
//				this.finish();
//				intent.setClass(GalleryActivity.this, AlbumActivity.class);
//				startActivity(intent);
//			}else if(position==2){
//				this.finish();
//				intent.setClass(GalleryActivity.this, ShowAllPhoto.class);
//				startActivity(intent);
//			}
//		}
//		return true;
//	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);
				
			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
