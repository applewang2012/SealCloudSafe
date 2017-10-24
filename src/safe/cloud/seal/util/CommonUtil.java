package safe.cloud.seal.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

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
    public static String SEAL_DOWNLOAD_DIR = "sealdownload";
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
    
    public static String getDefaultDownloadPath(String downloadUrl){
    	String path = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 存在获取外部文件路径
            File root = Environment.getExternalStorageDirectory();
            File base = new File(root.getPath() + "/"+SEAL_DOWNLOAD_DIR);
            if (!base.exists()){
            	base.mkdir();
            }
        	
        	File[] files = base.listFiles();
        	for (int i = 0; i < files.length; i++) {
        			String filename = files[i].getName();
        			if (filename != null && downloadUrl != null){
        				if (downloadUrl.endsWith(filename)){
        					path = base.getPath()+File.separator+filename;
        					break;
        				}
        			}
        	}
        	if (path == null){
        		path = base.getPath();
        	}
        } else {
            // 不存在获取内部存
            return null;
        }
        Log.w("mingguo", "common util get default download path  "+path);
       return path;
    }
    
    
    /* 安装apk */    
    public static void installApk(Context context, String fileName) {    
        Intent intent = new Intent();    
        intent.setAction(Intent.ACTION_VIEW);    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setDataAndType(Uri.parse("file://" + fileName),"application/vnd.android.package-archive");    
        context.startActivity(intent);    
    }    
        
    /* 卸载apk */    
    public static void uninstallApk(Context context, String packageName) {    
        Uri uri = Uri.parse("package:" + packageName);    
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);    
        context.startActivity(intent);    
    }  
    
}
