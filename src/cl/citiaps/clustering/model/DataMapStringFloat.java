
package cl.citiaps.clustering.model;

import java.util.Map;

public class DataMapStringFloat implements Data {
	private String id;
	private Map<String, Float> data;
	
	public DataMapStringFloat(String _id, Map<String, Float> _data){
		id = _id;
		data = _data;
	}
	
	public void setData(Map<String, Float> _data){
		data = _data;
	}
	public Map<String, Float> getData(){
		return data;
	}
	
	public void setId(String _id){
		id = _id;
	}
	public String getId(){
		return id;
	}
}
