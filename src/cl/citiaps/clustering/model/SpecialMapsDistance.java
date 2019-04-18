
package cl.citiaps.clustering.model;

import java.util.Map;

public class SpecialMapsDistance implements Distance {
	
	public SpecialMapsDistance(){
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
		double sum_max = 0.0;
		int n = 0;
		// Primero de 1 hacia 2, INCLUYENDO
		for( Map.Entry<String, Float> pair : map_1.entrySet() ){
			double val_1 = pair.getValue();
			double val_2 = 0;
			if( map_2.containsKey(pair.getKey()) ){
				val_2 = map_2.get(pair.getKey());
			}
			if( val_1 > val_2 ){
				sum_max += val_1;
				res += (val_1 - val_2);
			}
			else{
				sum_max += val_2;
				res += (val_2 - val_1);
			}
			++n;
		}
		// Luego de 2 hacia 1, SOLO EXCLUYENDO
		for( Map.Entry<String, Float> pair : map_2.entrySet() ){
			double val_2 = 0;
			if( ! map_1.containsKey(pair.getKey()) ){
				sum_max += val_2;
				res += val_2;
				++n;
			}
		}
		res /= sum_max;
//		System.out.println("SpecialMapsDistance.compute - res: " + String.format("%.2f", res) + " (sum_max: " + String.format("%.2f", sum_max) + " de " + n + " llaves)");
		return res;
	}
	
}

