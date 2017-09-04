package safe.cloud.seal.model;

import com.google.zxing.common.BitArray;

import android.graphics.Bitmap;

public class SealUploadFileType {
	private String fileName ;
	private String fileTypeId;
	private int imageId, imageId2;
	private Bitmap imageBitmap;
	private Bitmap imageBitmap2;
	private String base64String;
	public String imagePath;
	private String base64String2;
	private String imagePath2;
	private String imageUrl;
	private String imageUrl2;
	private String fileId2;
	private String fileId;
	
	public void setFileType(String name){
		fileName = name;
	}
	
	public String getFileType(){
		return fileName;
	}
	
	public void setFileID(String id){
		fileId = id;
	}
	
	public String getFileID(){
		return fileId;
	}
	
	public void setFileID2(String id){
		fileId2 = id;
	}
	
	public String getFileID2(){
		return fileId2;
	}
	
	public void setFileTypeId(String id){
		fileTypeId = id;
	}
	
	public String getFileTypeId(){
		return fileTypeId;
	}
	
	public void setImageUrl(String url){
		imageUrl = url;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	
	public void setImageUrl2(String url){
		imageUrl2 = url;
	}
	
	public String getImageUrl2(){
		return imageUrl2;
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
