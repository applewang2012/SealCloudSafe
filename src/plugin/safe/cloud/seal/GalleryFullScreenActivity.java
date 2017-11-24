package plugin.safe.cloud.seal;

import java.util.ArrayList;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import plugin.safe.cloud.seal.shotview.ViewPagerFixed;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:53
 */
public class GalleryFullScreenActivity extends DLBasePluginActivity {
	private Intent intent;
	private TextView positionTextView;
	//当前的位置
	private int location = 0;
	
	private ArrayList<String> mImageStringList = new ArrayList<>();
	private ArrayList<View> listViews = new ArrayList<>();
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	
	private Context mContext;

	RelativeLayout photo_relativeLayout;
	private TextView mSelectedViewNum;
	private int mTotalNum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		mContext = this;
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		//position = intent.getIntExtra("position", 0);
		//mTotalNum = intent.getIntExtra("listSize", 0);
		
		mImageStringList =  getIntent().getStringArrayListExtra("imagelist");
		
		if (mImageStringList != null){
			mTotalNum = mImageStringList.size();
			for (int i = 0; i < mImageStringList.size(); i++){
				Log.w("mingguo", "mImageStringList.get(i) "+mImageStringList.get(i));
				initListViewsByMemoryBitmap(i);
			}
		}
//		if (mTotalNum == 0){
//			mTotalNum = Bimp.tempSelectBitmap.size();
//		}
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
		
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
		int id = intent.getIntExtra("selected_position", 0);
		Log.w("mingguo", "full screen click image position  "+id);
		pager.setCurrentItem(id);
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
	
	
	private void initListViewsByMemoryBitmap(int index) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		
		ImageView image = new ImageView(mContext);
		image.setBackgroundColor(0xff000000);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		//ImageLoader.getInstance().displayImage(mImageStringList.get(index), image);
		Picasso.with(mContext).load(mImageStringList.get(index)).into(image);
		listViews.add(image);
	}
	
	// 完成按钮的监听
	private class GalleryClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}
	}

	
	
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
				listViews.get(arg1 % size).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				
			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
