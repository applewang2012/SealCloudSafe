package plugin.safe.cloud.seal.model;

public class SealTraceInfo {
	private String traceId ;
	private String sealType;
	private String traceImage;
	private String sealDate;
	private String filename;
	private String sealname;

	public void setSealName(String no){
		sealname = no;
	}
	
	public String getSealName(){
		return sealname;
	}
	
	public void setSealFileName(String status){
		filename = status;
	}
	
	public String getSealFileName(){
		return filename;
	}
	
	public void setSealTraceId(String id){
		traceId = id;
	}
	
	public String getSealTraceId(){
		return traceId;
	}
	
	
	public void setSealTraceImage(String list){
		traceImage = list;
	}
	
	public String GetSealTraceImage(){
		return traceImage;
	}
	
	public void setSealDate(String date){
		sealDate = date;
	}
	
	public String getSealDate(){
		return sealDate;
	}
	
}
