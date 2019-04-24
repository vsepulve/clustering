
package cl.citiaps.clustering.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BM25Distance implements Distance {
	
	// Alfabeto con 
	Map<String, Double> idf_map;
	double avg_length;
	double k1;
	double b;
	
	public BM25Distance(List<Data> collection){
		this(collection, 1.5, 0.75);
	}
	
	public BM25Distance(List<Data> collection, double k1, double b){
		idf_map = new HashMap<String, Double>();
		double total = collection.size();
		
		// Primero uso el mapa para contar numero de docs con cada termino
		System.out.println("BM25Distance - Preparing Idf Map");
		avg_length = 0;
		for( Data doc : collection ){
			if( ! (doc instanceof DataMapStringFloat) ){
				continue;
			}
			Map<String, Float> map_doc = ((DataMapStringFloat)doc).getData();
			for( Map.Entry<String, Float> par_doc : map_doc.entrySet() ){
				// Cada termino del documento (aca no importa la frecuencia)
				String term = par_doc.getKey();
				idf_map.putIfAbsent(term, 0.0);
				idf_map.put(term, idf_map.get(term) + 1.0);
			}
			avg_length += map_doc.size();
		}
		avg_length /= total;
		
		// Convierto los valores del mapa a los IDFs
		System.out.println("BM25Distance - Computing Idfs");
		for( Map.Entry<String, Double> par_idf : idf_map.entrySet() ){
			double n = par_idf.getValue();
//			double idf = java.lang.Math.log(
//				(total - n + 0.5) / (n + 0.5)
//			);
			double idf = java.lang.Math.log( total / n );
			par_idf.setValue(idf);
//			System.out.println("BM25Distance - IDF for " + par_idf.getKey() + " : " + idf);
		}
		
	}
	
	public double queryBM25(DataMapStringFloat doc, DataMapStringFloat query){
		Map<String, Float> doc_map = doc.getData();
		double res = 0.0;
		double length = doc_map.size();
		for( Map.Entry<String, Float> pair : query.getData().entrySet() ){
			String term = pair.getKey();
			if( ! doc_map.containsKey(term) ){
				continue;
			}
			double frec = doc_map.get(term);
			double idf = idf_map.get(term);
			res += idf * (frec * (k1 + 1.0)) 
						/ ( frec + k1 * ( 1.0 - b + b * ( length / avg_length ) ) );
		}
		return res;
	}
	
	public double compute(Data o1, Data o2){
		if( ! (o1 instanceof DataMapStringFloat)
			|| ! (o2 instanceof DataMapStringFloat) ){
			System.err.println("BM25Distance.compute - Wrong Data Type");
			return 0.0;
		}
		DataMapStringFloat doc_1 = (DataMapStringFloat)o1;
		DataMapStringFloat doc_2 = (DataMapStringFloat)o2;
		double res = 0;
		res += queryBM25(doc_1, doc_2);
		res += queryBM25(doc_2, doc_1);
		res /= 2;
		return res;
	}
	
}

