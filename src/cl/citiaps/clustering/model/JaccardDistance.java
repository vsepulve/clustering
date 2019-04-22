
package cl.citiaps.clustering.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JaccardDistance implements Distance {
	
	public JaccardDistance(){
	}
	
	public double compute(Data o1, Data o2){
		if( ! (o1 instanceof DataMapStringFloat)
			|| ! (o2 instanceof DataMapStringFloat) ){
			System.err.println("JaccardDistance.compute - Wrong Data Type");
			return 0.0;
		}
		Map<String, Float> map_1 = ((DataMapStringFloat)o1).getData();
		Map<String, Float> map_2 = ((DataMapStringFloat)o2).getData();
		int inter_size = 0;
		Set<String> keys = new TreeSet<String>();
		for( Map.Entry<String, Float> pair : map_1.entrySet() ){
			String key = pair.getKey();
			if( map_2.containsKey(key) ){
				inter_size++;
			}
			keys.add(key);
		}
		for( Map.Entry<String, Float> pair : map_2.entrySet() ){
			String key = pair.getKey();
			keys.add(key);
		}
		double res = inter_size;
		res /= keys.size();
		return res;
	}
	
}

