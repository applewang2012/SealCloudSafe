package safe.cloud.seal.model;

public class SealStatusInfo {
	private String sealStatus ;
	private String sealType;
	private String sealCorp;
	private String sealDate;

	public void setSealStatus(String status){
		sealStatus = status;
	}
	
	public String getSealStaus(){
		return sealStatus;
	}
	
	public void setSealType(String type){
		sealType = type;
	}
	
	public String getSealType(){
		return sealType;
	}
	
	public void setSealCorp(String cp){
		sealCorp = cp;
	}
	
	public String getSealCorp(){
		return sealCorp;
	}
	
	public void setSealDate(String date){
		sealDate = date;
	}
	
	public String getSealDate(){
		return sealDate;
	}
	
}
