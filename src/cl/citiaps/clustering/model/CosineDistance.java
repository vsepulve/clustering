
package cl.citiaps.clustering.model;

import java.util.Map;

public class CosineDistance implements Distance {
	
	public CosineDistance(){
	}
	
	public double compute(Data o1, Data o2){
		if( ! (o1 instanceof DataMapStringFloat)
			|| ! (o2 instanceof DataMapStringFloat) ){
			System.err.println("SpecialMapsDistance.compute - Wrong Data Type");
			return 0.0;
		}
		Map<String, Float> map_1 = ((DataMapStringFloat)o1).getData();
		Map<String, Float> map_2 = ((DataMapStringFloat)o2).getData();
		double res = 0.0;
		double sum_1 = 0.0;
		double sum_2 = 0.0;
		for( Map.Entry<String, Float> pair : map_1.entrySet() ){
			String key = pair.getKey();
			double val = pair.getValue();
			sum_1 += (val * val);
			if( map_2.containsKey(key) ){
				res += (val * map_2.get(key));
			}
		}
		for( Map.Entry<String, Float> pair : map_2.entrySet() ){
			double val = pair.getValue();
			sum_2 += (val * val);
		}
		double sum = java.lang.Math.sqrt(sum_1 * sum_2);
//		System.out.println("CosineDistance.compute - res: " + (1.0 - res/sum) + " (1.0 - " + res + " / (" + sum_1 + " * " + sum_2 + ")^1/2)");
		return (1.0 - res/sum);
	}
	
}

