package safe.cloud.seal.util;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import safe.cloud.seal.R;

public class ViewUtil {

	public static void showLoadingView(Context context, View loadingView){
		if (loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
        	ImageView imageView = (ImageView) loadingView.findViewById(R.id.id_progressbar_img);
        	if (imageView != null) {
        		RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        		imageView.startAnimation(rotate);
        	}
		}
	}
	public static void dismissLoadingView(View loadingView){
		if (loadingView != null) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}
}
