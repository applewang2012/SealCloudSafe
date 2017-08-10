package safe.cloud.seal.util;

import android.graphics.Canvas;

/**
 * @author shenxiaolei
 *
 */
public class CommonUtil {

    /**
     * 记录播放位置
     */
    public static int playPosition=-1;
    
    private static  Canvas canvas;
    
    public static String DOWLOAD_URL = null; //"http://acj2.pc6.com/pc6_soure/2017-6/com.dybag_25.apk";
    public static String GURADTS_DOWNLOAD_DIR = "guardtsdownload";
    public static final String NAMESPACE = "http://tempuri.org/";
    
    public static final String UPDATE_VERSION_HOST = "http://www.guardts.com/";
    
    public static String mRegisterRealName = null;
    public static String mRegisterIdcard = null;
    public static String mUserLoginName = null;
    public static String mUserHost = "http://byw3131840001.my3w.com/";
    public static String mUserArea;
    public static double mCurrentLati, mCurrentLongi;
    public static int mScanCodeRequestCode = 1;
    public static int mSelectCityRequestCode = 2;
    
    
    public static String getSoapName(String action){
		if (action == null || action.equals("")){
			return null;
		}
		int index = action.lastIndexOf("/");
		return action.substring(index+1);
	}
    
    private static double dEARTH_RADIUS = 6378.137;  // 地球半径
    private static double Rad(double d)
    {
        return (d * Math.PI / 180.0);
    }
    
}
