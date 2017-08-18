package safe.cloud.seal.presenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import safe.cloud.seal.util.CommonUtil;

public class DataModel {


	private HoursePresenter mPresenter;
	private String mUrl;
	private String mSoapAction;
	private SoapObject mSoapObject;
	private String[] mDistrictID;
	private String[] mStreetID;
	private HashMap<String, String> mPostData;
	private Context mContext;
	
	public DataModel(HoursePresenter presenter){
		mPresenter = presenter;
	}
	
	public void setAsyncTaskReady(Context ctx, String url, String action, SoapObject object){
		mContext = ctx;
		mUrl = url;
		mSoapAction = action;
		mSoapObject = object;
	}
	
	public void setHttpTaskReady(Context ctx, String url, HashMap<String, String> postdata){
		mContext = ctx;
		mUrl = url;
		mPostData = postdata;
	}
	
	
	public void startDataRequestTask(){
		if (isNetworkAvailable(mContext)){
			new ServiceAsyncTask().execute();
		}else{
			mPresenter.notifyDataRequestError(mUrl, null);
		}
		
	}
	
	public void startHttpRequestTask(){
		if (isNetworkAvailable(mContext)){
			//new HttpAsyncTask().execute();
		}else{
			mPresenter.notifyDataRequestError(mUrl, null);
		}
	}
	
	private class ServiceAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				 Element[] header = new Element[1]; 
	                header[0] = new Element().createElement(CommonUtil.NAMESPACE, "MySoapHeader"); 
	                
	                Element userName = new Element().createElement(CommonUtil.NAMESPACE, "UserID"); 
	                userName.addChild(Node.TEXT, "admin"); 
	                header[0].addChild(Node.ELEMENT, userName); 
	                
	                Element pass = new Element().createElement(CommonUtil.NAMESPACE, "PassWord"); 
	                pass.addChild(Node.TEXT, "Pa$$w0rd780419"); 
	                header[0].addChild(Node.ELEMENT, pass); 
	                
//	                header[0].setAttribute(CommonUtil.NAMESPACE, "UserID", "admin");
//	                header[0].setAttribute(CommonUtil.NAMESPACE, "PassWord", "Pa$$w0rd780419");
	                
				Log.e("house", "services  async taks  do inbackground ");
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.headerOut = header; 
				envelope.bodyOut = mSoapObject;
				envelope.dotNet= true;
				envelope.setOutputSoapObject(mSoapObject);
				HttpTransportSE transport = new HttpTransportSE(mUrl,30000);
				transport.call(mSoapAction, envelope);
				SoapObject valueObject = null;
				if(envelope.getResponse()!=null){
					valueObject = (SoapObject)envelope.bodyIn;				
				}
				String resultString = valueObject.getProperty(0).toString();
				mPresenter.notifyDataRequestSuccess(mSoapAction, resultString);
			} catch (Exception e) {
				mPresenter.notifyDataRequestError(mSoapAction, "error from exception "+e);
			}
			
			return null;
		}
		
	}
	
	
	public static void copyAssetFileToFiles(Context context, String filename) throws IOException {
		InputStream is = context.getAssets().open(filename);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();

		File of = new File(context.getFilesDir() + "/" + filename);
		of.createNewFile();
		FileOutputStream os = new FileOutputStream(of);
		os.write(buffer);
		os.close();
	}
	
//	public static HttpClient getNewHttpClient() {
//		   try {
//		       KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		       trustStore.load(null, null);
//		 
//		       SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
//		       sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//		 
//		       HttpParams params = new BasicHttpParams();
//		       HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//		       HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//		 
//		       SchemeRegistry registry = new SchemeRegistry();
//		       registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//		       registry.register(new Scheme("https", sf, 443));
//		 
//		       ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//		 
//		       return new DefaultHttpClient(ccm, params);
//		   } catch (Exception e) {
//		       return new DefaultHttpClient();
//		   }
//	}
	
	private class RelationServiceAsyncTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				String url = CommonUtil.mUserHost+"services.asmx?op=GetHouseInfo";
				String action = "http://tempuri.org/GetHouseInfo";
				SoapObject object = new SoapObject("http://tempuri.org/", getSoapName(mSoapAction));
				
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = object;
				envelope.dotNet= true;
				envelope.setOutputSoapObject(object);
				HttpTransportSE transport = new HttpTransportSE(url,30000);
				transport.call(action, envelope);
				SoapObject valueObject = null;
				if(envelope.getResponse()!=null){
					valueObject = (SoapObject)envelope.bodyIn;				
				}
				String resultString = valueObject.getProperty(0).toString();
				
				mPresenter.notifyDataRequestSuccess(mSoapAction, resultString);
			} catch (Exception e) {
				
			}
			return null;
		}
		
	}
	
	
	
	private void jsonStreetData(String data){
		JSONArray dataArray;
		try {
			dataArray = new JSONArray(data);
			String[] streetList = new String[dataArray.length()];
			for (int item = 0; item < dataArray.length(); item++){
				JSONObject districtObject = dataArray.optJSONObject(item);
				streetList[item] = districtObject.optString("LSName");
				mStreetID[item] = districtObject.optString("LDID");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getSoapName(String action){
		if (action == null || action.equals("")){
			return null;
		}
		int index = action.lastIndexOf("/");
		return action.substring(index+1);
	}
	
	private String startRequestData(String url, String action, SoapObject object ){
		try {
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = object;
			envelope.dotNet= true;
			envelope.setOutputSoapObject(object);
			HttpTransportSE transport = new HttpTransportSE(url,30000);
			transport.call(action, envelope);
			SoapObject valueObject = null;
			if(envelope.getResponse()!=null){
				valueObject = (SoapObject)envelope.bodyIn;				
			}
			return valueObject.getProperty(0).toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();  
            if (info != null && info.isConnected())   
            {  
                // ��ǰ���������ӵ�  
                if (info.getState() == NetworkInfo.State.CONNECTED)   
                {  
                    // ��ǰ�����ӵ��������? 
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
	
	
	
}
