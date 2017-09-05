package safe.cloud.seal.util;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

public class UtilTool {

	private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",  
            "8", "9", "a", "b", "c", "d", "e", "f"};  
	
	private UtilTool() {}
	
	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/** 
     * ת���ֽ�����Ϊ16�����ִ� 
     * @param b �ֽ����� 
     * @return 16�����ִ� 
     */  
    public static String byteArrayToHexString(byte[] b) {  
        StringBuilder resultSb = new StringBuilder();  
        for (byte aB : b) {  
            System.out.println(aB);  
            resultSb.append(byteToHexString(aB));  
        }  
        return resultSb.toString();  
    }  
  
    /** 
     * ת��byte��16���� 
     * @param b Ҫת����byte 
     * @return 16���Ƹ�ʽ 
     */  
    private static String byteToHexString(byte b) {  
        int n = b;  
        if (n < 0) {  
            n = 256 + n;  
        }  
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }  
  
    /** 
     * MD5���� 
     * @param origin ԭʼ�ַ� 
     * @return ����MD5����֮��Ľ�� 
     */  
    public static String MD5Encode(String origin) {  
        System.out.println(origin);  
        String resultString = null;  
        try {  
            resultString = origin;  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return resultString;  
  
    }  
    
    public static Map<String,String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:

                    if("xml".equals(nodeName)==false){
                        //实例化student对象
                        xml.put(nodeName,parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("Simon","----"+e.toString());
        }
        return null;

    }
    
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static String stampToNormalDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static String stampToNormalDateTime(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static String generateOrderNo(){
    	String orderNo = null;
    	String timeStamp = stampToDate(System.currentTimeMillis()+"");
        String randomValue = String.valueOf(Math.random()).replace("0.", "").substring(0, 8);
        orderNo = timeStamp+randomValue;
        return orderNo;
    }
    
    public static String getStringToUtf8(String xml) {  
        // A StringBuffer Object  
        StringBuffer sb = new StringBuffer();  
        sb.append(xml);  
        String xmlUTF8="";  
        try {  
//        xmString = new String(sb.toString().getBytes("UTF-8"));  
//        xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");  
//        System.out.println("utf-8 编码：" + xmlUTF8) ;  
        	xmlUTF8 = new String(sb.toString().getBytes(),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {  
        // TODO Auto-generated catch block  
        e.printStackTrace();  
        }  
        // return to String Formed  
        return xmlUTF8;  
	}
    
    public static String extractNumberFromString(String value) {
    	String a="love23next234csdn3423javaeye";
    	String regEx="[^0-9]";   
    	Pattern p = Pattern.compile(regEx);   
    	Matcher m = p.matcher(value);   
    	return m.replaceAll("").trim();
    }
    
    public static List<String> getCururentLocation(Activity activity){
    	double latitude=0.0;  
    	double longitude =0.0; 
    	List<String> locationXY = new ArrayList<>();
    	LocationManager locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);  
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
            if(location != null){  
            	locationXY.clear();
                latitude = location.getLatitude();  
                longitude = location.getLongitude();  
                locationXY.add(latitude+"");
                locationXY.add(longitude+"");
                }  
        }else{  
            LocationListener locationListener = new LocationListener() {  
                  
                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
                @Override  
                public void onStatusChanged(String provider, int status, Bundle extras) {  
                      
                }  
                  
                // Provider被enable时触发此函数，比如GPS被打开  
                @Override  
                public void onProviderEnabled(String provider) {  
                      
                }  
                  
                // Provider被disable时触发此函数，比如GPS被关闭   
                @Override  
                public void onProviderDisabled(String provider) {  
                      
                }  
                  
                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
                @Override  
                public void onLocationChanged(Location location) {  
                    if (location != null) {     
                        Log.e("mingguo", "Location changed : Lat: "    
                        + location.getLatitude() + " Lng: "    
                        + location.getLongitude());     
                    }  
                }  
            };  
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100000, 0,locationListener);     
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
            if(location != null){     
            	locationXY.clear();
                latitude = location.getLatitude(); //经度     
                longitude = location.getLongitude(); //纬度  
                locationXY.add(latitude+"");
                locationXY.add(longitude+"");
            }     
        }  
        locationXY.add(latitude+"");
        locationXY.add(longitude+"");
        return locationXY;
    }
    
}
