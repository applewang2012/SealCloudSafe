package safe.cloud.seal.model;

import com.google.zxing.common.BitArray;

import android.graphics.Bitmap;

public class SealUploadFileType {
	private String fileName ;
	private String fileId;
	private int imageId, imageId2;
	private Bitmap imageBitmap;
	private Bitmap imageBitmap2;
	private String base64String;
	public String imagePath;
	private String base64String2;
	private String imagePath2;
	
	public void setFileType(String name){
		fileName = name;
	}
	
	public String getFileType(){
		return fileName;
	}
	
	public void setFileId(String id){
		fileId = id;
	}
	
	public String getFileId(){
		return fileId;
	}
	
	public void setImageId(int id){
		imageId = id;
	}
	
	public int getImaged(){
		return imageId;
	}
	
	public void setImageId2(int id2){
		imageId2 = id2;
	}
	
	public int getImaged2(){
		return imageId2;
	}
	
	public void setImageBitmap(Bitmap bitmap){
		imageBitmap = bitmap;
	}
	
	public Bitmap getImageBitmap(){
		return imageBitmap;
	}
	
	public void setImage2Bitmap(Bitmap bitmap){
		imageBitmap2 = bitmap;
	}
	
	public Bitmap getImage2Bitmap(){
		return imageBitmap2;
	}
	
	public void setBitmapBase64(String base){
		base64String = base;
	}
	
	public String getBitmapBase64(){
		return base64String;
	}
	
	public void setImagePath(String path){
		imagePath = path;
	}
	
	
	public String getImagePath(){
		return imagePath;
	}
	
	public void setBitmap2Base64(String base){
		base64String2 = base;
	}
	
	public String getBitmap2Base64(){
		return base64String2;
	}
	
	public void setImage2Path(String path){
		imagePath2 = path;
	}
	
	
	public String getImage2Path(){
		return imagePath2;
	}
	
	
}
