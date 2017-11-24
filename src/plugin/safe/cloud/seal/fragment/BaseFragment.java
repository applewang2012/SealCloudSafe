package plugin.safe.cloud.seal.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.presenter.DataStatusInterface;

/**
 * Created by Zhou on 2017/11/9.
 */

public class BaseFragment extends Fragment implements DataStatusInterface {


    View mFragmentLoadingView ;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 800){
                dismissLoadingView(mFragmentLoadingView);
               Toast.makeText(getActivity().getApplicationContext(), "网络异常，请检查网络！"+msg.obj, Toast.LENGTH_SHORT).show();
            }else if (msg.what == 1000){
                Activity parentActivity = (Activity)msg.obj;
                mFragmentLoadingView = parentActivity.findViewById(R.id.id_data_loading);
                showLoadingView(parentActivity, mFragmentLoadingView);
            }else if (msg.what == 900){
                dismissLoadingView(mFragmentLoadingView);
            }

        }
    };

    @Override
    public void onStatusSuccess(String action, String templateInfo) {
        // TODO Auto-generated method stub
        Message msg = mHandler.obtainMessage();
        msg.what = 900;
        mHandler.sendMessage(msg);
        if (getActivity().isFinishing()){
            templateInfo = null;
        }
    }

    @Override
    public void onStatusStart(Activity activity) {
        // TODO Auto-generated method stub
        Message msg = mHandler.obtainMessage();
        msg.what = 1000;
        msg.obj = activity;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onStatusError(String action, String error) {
        // TODO Auto-generated method stub
        Message msg = mHandler.obtainMessage();
        msg.what = 800;
        msg.obj = action + " " + error;
        mHandler.sendMessage(msg);
    }

    private void showLoadingView(Context ctx, View loadingView){
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) loadingView.findViewById(R.id.id_progressbar_img);
            if (imageView != null) {
                RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(ctx, R.anim.anim_rotate);
                imageView.startAnimation(rotate);
            }
        }
    }
    private void dismissLoadingView(View loadingView){
        if (loadingView != null) {
            loadingView.setVisibility(View.INVISIBLE);
        }
    }


}
