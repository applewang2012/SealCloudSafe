package safe.cloud.seal.model;

public class SealInfoModel {
	private String selectId ;
	private String selectName;
	private String[] allValueId;
	private String[] allValue;

	public void setSelectedId(String id){
		selectId = id;
	}
	
	public String getSelectedId(){
		return selectId;
	}
	
	public void setSelectedName(String name){
		selectName = name;
	}
	
	public String getSelectedName(){
		return selectName;
	}
	
	public String [] getHouseAllContent() {
		return allValue;
	}

	public void setHouseAllContent(String [] value) {
		this.allValue = value;
	}
	
	public String [] getHouseAllId() {
		return allValueId;
	}

	public void setHouseAllId(String [] value) {
		this.allValueId = value;
	}
	
}
