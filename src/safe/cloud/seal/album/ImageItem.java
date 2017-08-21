package safe.cloud.seal.album;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import safe.cloud.seal.util.GlobalUtil;


public class ImageItem implements Serializable {
	private String base64String = null;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	//private String imageName;
	public boolean isSelected = false;
	private String typeName;
	private String typeNameId;
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getImageName() {
		if (imagePath != null){
			String splitName = imagePath.substring(imagePath.lastIndexOf("/")+1);
			if (splitName != null && splitName.length() > 0){
				return splitName;
			}
		}
		return System.currentTimeMillis()+"default.jpg";
	}
	public void setImageName(String name) {
		
		//this.imageName = name;
	}
	public Bitmap getBitmap() {		
		if(bitmap == null){
			try {
				bitmap = GlobalUtil.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setTypeName(String name){
		typeName = name;
	}
	
	public String getTypeName(){
		return typeName; 
	}
	public void setTypeNameId(String id){
		typeNameId = id;
	}
	
	public String getTypeNameId(){
		return typeNameId; 
	}
	public String getBitmapBase64(){
		return this.base64String;
	}
	
	public void setBitmapBase64(String base){
		base64String = base;
	}
	
}
