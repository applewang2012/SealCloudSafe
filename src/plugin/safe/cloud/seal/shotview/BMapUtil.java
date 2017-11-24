package plugin.safe.cloud.seal.shotview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import plugin.safe.cloud.seal.util.LogUtil;


public class BMapUtil {
	
    	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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
	
	
	/** 
	 * 图片按比例大小压缩方法 
	 * 
	 * @param image （根据Bitmap图片压缩） 
	 * @return 
	 */  
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

	public static String createScreenshotDirectory(Context context) {
		File file = new File(Environment.getExternalStorageDirectory()+"/sealimage");
		if (!file.exists()){
			file.mkdir();
		}
		String filename = file.getPath()+"/"+"sealuser"+".jpg";
		LogUtil.w("mingguo", "generate  file name   "+filename);
		return filename;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	
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
	
	/*
	    * 旋转图片 
	    * @param angle 
	    * @param bitmap 
	    * @return Bitmap 
	    */  
	   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
	       //旋转图片 动作   
	       Matrix matrix = new Matrix();;
	       matrix.postRotate(angle);  
	       Log.w("mingguo", "image  rotation  angle = " + angle);
	       // 创建新的图片   
	       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
	               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	       return resizedBitmap;  
	   }
	   
	   private static int QR_WIDTH = 480; // pixel
		private static int QR_HEIGHT = 480; // pixel

		public static Bitmap createQRImage(String url, ImageView imgQrd, int nWidth, int nHeight) {
			QR_WIDTH = nWidth;
			QR_HEIGHT = nHeight;

			return createQRImage(url, imgQrd);
		}

		public static Bitmap createQRImage(String url, ImageView imgQrd) {
			Bitmap bitmap = null;
			try {
				if (url == null || "".equals(url) || url.length() < 1) {
					return null;
				}
				
				Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
				int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
				
				for (int y = 0; y < QR_HEIGHT; y++) {
					for (int x = 0; x < QR_WIDTH; x++) {
						if (bitMatrix.get(x, y)) {
							pixels[y * QR_WIDTH + x] = 0xff000000;
						} else {
							pixels[y * QR_WIDTH + x] = 0xffffffff;
						}
					}
				}
				
				bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
				bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
				imgQrd.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
				return null;
			}
			return bitmap;
		}
		
		
		public static String bitmapToBase64(Bitmap bitmap) {
	    	  
	        String result = null;
	        ByteArrayOutputStream baos = null;
	        try {  
	            if (bitmap != null) {  
	                baos = new ByteArrayOutputStream();
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	      
	                baos.flush();  
	                baos.close();  
	      
	                byte[] bitmapBytes = baos.toByteArray();  
	                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
	            }  
	        } catch (IOException e) {
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                if (baos != null) {  
	                    baos.flush();  
	                    baos.close();  
	                }  
	            } catch (IOException e) {
	                e.printStackTrace();  
	            }  
	        }  
	        return result;  
	    }  
	
}
