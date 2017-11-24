package plugin.safe.cloud.seal.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.presenter.AlertDialogInterface;



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

	public static void showAlertDialog(Activity aty, String title, final AlertDialogInterface action){
		new AlertDialog.Builder(aty, AlertDialog.THEME_HOLO_LIGHT).setTitle(title)
				.setPositiveButton(aty.getResources().getString(R.string.button_ok),new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						action.onPostiveButton();
					}

				}).setNegativeButton(aty.getResources().getString(R.string.button_cancel),new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						action.onNegativeButton();
					}

		}).show();
	}
}
