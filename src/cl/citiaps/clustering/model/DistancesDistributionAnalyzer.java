
package cl.citiaps.clustering.model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class DistancesDistributionAnalyzer {
	
	private int sampling = 1000000;
	
	public DistancesDistributionAnalyzer(int _sampling){
		sampling = _sampling;
	}
	
	public void execute(List<Data> data, Distance dist){
		
		Map<Integer, Integer> histo = new TreeMap<Integer, Integer>();
		int n_buckets = 100;
		
		List<Double> distancias = new LinkedList<Double>();
		double min = 1000000;
		double max = 0.0;
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
			distancias.add(d);
			if( d > max ){
				max = d;
			}
			if( d < min ){
				min = d;
			}
//			int pos = (int)(d*n_buckets);
//			histo.putIfAbsent(pos, 0);
//			histo.put(pos, histo.get(pos) + 1);
		}
		
		// Normalizacion
		System.out.println("Normalizando en [" + min + ", " + max + "]");
		final double fixed_min = min;
		final double fixed_max = max;
		distancias.replaceAll( d -> (1.0 - (d-fixed_min)/(fixed_max-fixed_min)) );
		
		// Generacion del histograma
		for( double d : distancias ){
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
