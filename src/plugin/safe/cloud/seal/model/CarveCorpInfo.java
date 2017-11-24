package plugin.safe.cloud.seal.model;

public class CarveCorpInfo {
	private String sealStatus ;
	private String sealType;
	private String sealCorp;
	private String sealDate;
	private String showStatus;
	private String sealNo;
	private String checkContent;
	private String checkStatus;
	private String checkId;
	private String zhenggaiNeirong;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSealNo(String no){
		sealNo = no;
	}
	
	public String getSealNo(){
		return sealNo;
	}
	
	public void setShowSealStatus(String status){
		showStatus = status;
	}
	
	public String getShowSealStaus(){
		return showStatus;
	}
	
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
	
	public void setCarveCorp(String cp){
		sealCorp = cp;
	}
	
	public String getCarveCorp(){
		return sealCorp;
	}
	
	public void setSealDate(String date){
		sealDate = date;
	}
	
	public String getSealDate(){
		return sealDate;
	}

	public void setCheckContent(String ct){
		checkContent = ct;
	}

	public String getCheckContent(){
		return checkContent;
	}
	public void setCheckStatus(String st){
		checkStatus = st;
	}

	public String getCheckStatus(){
		return checkStatus;
	}

	public void setCheckId(String id){
		checkId = id;
	}

	public String getCheckId(){
		return checkId;
	}
	public void setCheckZhenggaiNeirong(String neirong){
		zhenggaiNeirong = neirong;
	}

	public String getCheckZhenggaiNeirong(){
		return zhenggaiNeirong;
	}
}
