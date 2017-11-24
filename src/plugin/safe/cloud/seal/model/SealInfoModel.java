package plugin.safe.cloud.seal.model;

public class SealInfoModel {
	private String selectId ;
	private String selectName;
	private String[] allValueId;
	private String[] allValue;
	private String[] allImageUrl;

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
	
	public String[] getSealAllContent() {
		return allValue;
	}

	public void setSealAllContent(String[] value) {
		this.allValue = value;
	}
	
	public String[] getSealAllId() {
		return allValueId;
	}

	public void setSealAllId(String[] value) {
		this.allValueId = value;
	}
	
	public String[] getSealImageUrl() {
		return allImageUrl;
	}

	public void setSealImageUrl(String[] value) {
		this.allImageUrl = value;
	}
	
}
