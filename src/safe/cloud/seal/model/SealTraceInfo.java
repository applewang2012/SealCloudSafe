package safe.cloud.seal.model;

import java.util.ArrayList;
import java.util.List;

public class SealTraceInfo {
	private String traceId ;
	private String sealType;
	private List<String> traceList;
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
	
	
	public void setSealTraceList(List<String> list){
		traceList = list;
	}
	
	public List<String> GetSealTraceList(){
		return traceList;
	}
	
	public void setSealDate(String date){
		sealDate = date;
	}
	
	public String getSealDate(){
		return sealDate;
	}
	
}
