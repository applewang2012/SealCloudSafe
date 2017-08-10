package safe.cloud.seal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.R;
import safe.cloud.seal.widget.NewToast;


/**
 * Some common global utilities.
 */
public final class GlobalUtil {

	// This class should not be instantiated, hence the private constructor
	private GlobalUtil() {
	}


	/**
	 * Get version code for this application.
	 * 
	 * @param context
	 *            The context to use. Usually your
	 *            {@link android.app.Application} or
	 *            {@link android.app.Activity} object.
	 * @return the version code or -1 if package not found
	 */
	public static int getVersionCode(Context context) {
		int versionCode;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			versionCode = -1;
		}

		return versionCode;
	}

	/**
	 * Get version code for the application package name.
	 * 
	 * @param context
	 *            The context to use. Usually your
	 *            {@link android.app.Application} or
	 *            {@link android.app.Activity} object.
	 * @param packageName
	 *            application package name
	 * @return the version code or -1 if package not found
	 */
	public static int getVersionCode(Context context, String packageName) {
		int versionCode;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (Exception e) {
			versionCode = -1;
		}

		return versionCode;
	}

	/**
	 * Get version name for this application.
	 * 
	 * @param context
	 *            The context to use. Usually your
	 *            {@link android.app.Application} or
	 *            {@link android.app.Activity} object.
	 * @return the version name or empty string if package not found
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			//versionName = StringUtil.EMPTY_STRING;
		}

		return versionName;
	}
	
	/**
	 * Get version name for this application.
	 * 
	 * @param context
	 *            The context to use. Usually your
	 *            {@link android.app.Application} or
	 *            {@link android.app.Activity} object.
	 * @return the version name or empty string if package not found
	 */
	public static String getVersionName(Context context, String packageName) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (Exception e) {
			//versionName = StringUtil.EMPTY_STRING;
		}

		return versionName;
	}
	
	public static String getApplicationName(Context ctx) { 
		PackageManager packageManager = null; 
		ApplicationInfo applicationInfo = null; 
		try { 
		packageManager = ctx.getPackageManager(); 
			applicationInfo = packageManager.getApplicationInfo(getPackageName(ctx), 0); 
		} catch (PackageManager.NameNotFoundException e) { 
			applicationInfo = null; 
		} 
		String applicationName = 
		(String) packageManager.getApplicationLabel(applicationInfo); 
		return applicationName; 
		} 
	
	public static String getPackageName(Context context){
		return context.getPackageName();
	}

	public static void startActivity(Context context, Class<?> class1) {
		Intent intent = new Intent();
		intent.setClass(context, class1);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, Class<?> class1, int flags) {
		Intent intent = new Intent();
		intent.setClass(context, class1);
		intent.addFlags(flags);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, Class<?> class1,
			Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, class1);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void shortToast(Context context, int resId) {
		// Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
		newtoast(context, context.getString(resId), null, Toast.LENGTH_SHORT);
	}

	public static void shortToast(Context context, int resId, Drawable icon) {
		newtoast(context, context.getString(resId), icon, Toast.LENGTH_SHORT);
	}

	public static void shortToast(Context context, CharSequence text,
			Drawable icon) {
		newtoast(context, text, icon, Toast.LENGTH_SHORT);
	}

	public static void shortToast(Context context, CharSequence text) {
		newtoast(context, text, null, Toast.LENGTH_SHORT);
	}

	public static void longToast(Context context, int resId) {
		newtoast(context, context.getString(resId), null, Toast.LENGTH_LONG);
	}

	public static void longToast(Context context, int resId, Drawable icon) {
		newtoast(context, context.getString(resId), icon, Toast.LENGTH_LONG);
	}

	public static void longToast(Context context, CharSequence text) {
		newtoast(context, text, null, Toast.LENGTH_LONG);
	}

	/**
	 * Make a toast.
	 * 
	 * @param context
	 *            The context to use. Usually your
	 *            {@link android.app.Application} or
	 *            {@link android.app.Activity} object.
	 * @param text
	 *            The text to show. Can be formatted text.
	 * @param icon
	 *            The icon image to show.
	 * @param duration
	 *            How long to display the message. Either {@link #LENGTH_SHORT}
	 *            or {@link #LENGTH_LONG}
	 * 
	 */
	/*
	public static void pritoast(Context context, CharSequence text, Drawable icon,
			int duration) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = inflate.inflate(R.layout.toast, null);
		((TextView) v.findViewById(android.R.id.message)).setText(text);
		if (icon != null) {
			ImageView imageView = (ImageView) v.findViewById(R.id.icon_image);
			imageView.setImageDrawable(icon);
			imageView.setVisibility(View.VISIBLE);
		}

		NewToast toast = new NewToast(context);
		toast.setDuration(duration);
		toast.setContentView(v);
		toast.show();
	}
	*/
	public static void newtoast(Context context, CharSequence text,
			Drawable icon, int duration) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = inflate.inflate(R.layout.toast, null);
		((TextView) v.findViewById(android.R.id.message)).setText(text);
		if (icon != null) {
			ImageView imageView = (ImageView) v.findViewById(R.id.icon_image);
			imageView.setImageDrawable(icon);
			imageView.setVisibility(View.VISIBLE);
		}
		switch (duration) {
		case Toast.LENGTH_SHORT:
			duration = 1000;
			break;
		case Toast.LENGTH_LONG:
			duration = 3000;
			break;
		}
		NewToast toast = new NewToast(context);
		toast.setContentView(v);
		toast.setDuration(duration);
		toast.show();

	}

	public static void safeShowDialog(Activity activity, int id) {
		try {
			activity.showDialog(id);
		} catch (Throwable e) {
		}
	}

	public static void safeDismissDialog(Activity activity, int id) {
		try {
			activity.dismissDialog(id);
		} catch (Throwable e) {
		}
	}
	
	public static String createScreenshotDirectory(Context context) {
		File file = new File(Environment.getExternalStorageDirectory()+"/signetimage");
		if (!file.exists()){
			file.mkdir();
		}
		String filename = file.getPath()+"/"+"signetsuser"+".jpg";
		Log.i("mingguo", "phote generate  file name   "+filename);
		return filename;
	}
	
	public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
	
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
	       //旋转图片 动作   
	       Matrix matrix = new Matrix();;  
	       matrix.postRotate(angle);  
	       Log.i("mingguo", "image  rotation  angle = " + angle);  
	       // 创建新的图片   
	       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
	               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	       return resizedBitmap;  
	 }
	
	public static Bitmap compressScale(Bitmap image) {  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	  
	    // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出  
	    if (baos.toByteArray().length / 1024 > 1024) {  
	        baos.reset();// 重置baos即清空baos  
	        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中  
	    }  
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    // 开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
	    // float hh = 800f;// 这里设置高度为800f  
	    // float ww = 480f;// 这里设置宽度为480f  
	    float hh = 512f;  
	    float ww = 512f;  
	    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	    int be = 1;// be=1表示不缩放  
	    if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放  
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放  
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be; // 设置缩放比例  
	    // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565  
	  
	    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	  
	    return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩  
	  
	    //return bitmap;  
	}  
	
	/** 
	 * 质量压缩方法 
	 * 
	 * @param image 
	 * @return 
	 */  
	public static Bitmap compressImage(Bitmap image) {  
	  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	    int options = 90;  
	  
	    while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩  
	        baos.reset(); // 重置baos即清空baos  
	        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中  
	        options -= 10;// 每次都减少10  
	    }  
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片  
	    return bitmap;  
	} 

	public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
	}
}
