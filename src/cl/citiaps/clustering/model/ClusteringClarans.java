
package cl.citiaps.clustering.model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ClusteringClarans implements Clustering {
	
	private int sampling = 1000000;
	
	public ClusteringClarans(int _sampling){
		sampling = _sampling;
	}
	
	public void execute(List<Data> data, Distance dist){
		
		Map<Integer, Integer> histo = new TreeMap<Integer, Integer>();
		int n_buckets = 100;
		
		// Matriz Completa
//		for(int i = 0; i < data.size(); ++i){
//			for(int j = i+1; j < data.size(); ++j){
//				Data d1 = data.get(i);
//				Data d2 = data.get(j);
//				double d = dist.compute(d1, d2);
////				System.out.println("Dist " + d1.getId() + " X " + d2.getId() + " : " + String.format("%.2f", d));
//				int pos = (int)(d*n_buckets);
//				histo.putIfAbsent(pos, 0);
//				histo.put(pos, histo.get(pos) + 1);
//			}
//		}
		
		// Muestreo
		for(int i = 0; i < sampling; ++i){
			int pos_1 = ThreadLocalRandom.current().nextInt(0, data.size());
			int pos_2 = ThreadLocalRandom.current().nextInt(0, data.size());
			while(pos_2 == pos_1){
				pos_2 = ThreadLocalRandom.current().nextInt(0, data.size());
			}
			Data d1 = data.get(pos_1);
			Data d2 = data.get(pos_2);
			double d = dist.compute(d1, d2);
			int pos = (int)(d*n_buckets);
			histo.putIfAbsent(pos, 0);
			histo.put(pos, histo.get(pos) + 1);
		}
		
		for( Map.Entry<Integer, Integer> pair : histo.entrySet() ){
			double d = pair.getKey();
			d /= n_buckets;
			System.out.println("histo " + String.format("%.2f", d) + "\t" + pair.getValue());
		}
		
	}
	
}
