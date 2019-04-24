
package cl.citiaps.clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
 
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import cl.citiaps.clustering.model.*;

public class ClusteringTest {
	
	private ClusteringTest() {}
	
	public static void main(String[] args) {
		
		int min_compras = 5;
		int min_productos = 5;
		int sampling = 1000000;
		int data_type = 1;
		
		String data_sells_file = null;
		String data_products_file = null;
		
		if( args.length == 4 ){
			data_sells_file = args[0];
			data_products_file = args[1];
			sampling = new Integer(args[2]);
			data_type = new Integer(args[3]);
		}
		else{
			System.out.println("Modo de Uso:");
			System.out.println(">java [-cp \"libs.jar:.\"] cl.citiaps.clustering.ClusteringTest data_sells.csv data_products.csv MAX_SAMPLING DATA_TYPE");
			return;
		}
		
		System.out.println("Cargando Productos de \"" + data_products_file + "\"");
		
		Map<String, Product> products_map = new HashMap<String, Product>();
		try {
			Reader lector = new FileReader(data_products_file);
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(lector);
			for (CSVRecord record : records) {
				String sku = record.get(0);
				String codjer = record.get(1);
				String nombre = record.get(2);
				String linea = record.get(3);
				products_map.put(sku, new Product(sku, nombre, linea, codjer));
			}
			lector.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Preparando Mapa de Ventas \"" + data_sells_file + "\"");
		
		// Datos para primera version de clustering:
		// Usuario (RUT) -> Mapa de productos (SKU -> Cantidad)
		// La cantidad por producto puede ser la cantidad promedio de cada producto POR venta
		// Para eso se necesita un mapa temporal de total de compras por cliente
		// Esto es para ser tratados como Conjuntos
		Map<String, Set<Integer>> client_sells_map = new TreeMap<String, Set<Integer>>();
		Map<String, Map<String, Float>> clients_data_map = new HashMap<String, Map<String, Float>>();
		try {
			Reader lector = new FileReader(data_sells_file);
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(lector);
			for (CSVRecord record : records) {
				String rutcli = record.get(0);
				String cencos = record.get(1);
				String sku = record.get(5);
				int compra = 0;
				int cantidad = 0;
				int costo = 0;
				int neto = 0;
				try{
					compra = new Integer(record.get(2));
					cantidad = new Integer(record.get(4));
					costo = new Integer(record.get(7));
					neto = new Integer(record.get(8));
				}
				catch(Exception e){
					compra = 0;
				}
				if( compra == 0 ){
					continue;
				}
				
				client_sells_map.putIfAbsent(rutcli, new TreeSet<Integer>());
				client_sells_map.get(rutcli).add(compra);
				
				clients_data_map.putIfAbsent(rutcli, new TreeMap<String, Float>());
				Map<String, Float> products = clients_data_map.get(rutcli);
				
				if( data_type == 1 ){
					// Version 1: Sku directo
					products.putIfAbsent(sku, 0.0f);
					products.put(sku, products.get(sku) + cantidad);
				}
				else if(data_type == 2){
					// Version 2: Codjer
					if( ! products_map.containsKey(sku) ){
						continue;
					}
					String key = products_map.get(sku).getCodjer();
					if( key == null || key.length() < 1 ){
						continue;
					}
					products.putIfAbsent(key, 0.0f);
					products.put(sku, products.get(key) + cantidad);
				}
				else if(data_type == 3){
					// Version 3: Descripciones (texto)
					if( ! products_map.containsKey(sku) ){
						continue;
					}
					Product prod = products_map.get(sku);
					if( prod.getNombre() == null ){
						continue;
					}
					String terms[] = prod.getNombre().split(" ");
					boolean first = true;
					for(String term : terms){
						if( term == null || term.length() < 1 ){
							continue;
						}
						// Omito terminos con numeros o simbolos
						if( ! term.matches("[A-Za-z]+") ){
							continue;
						}
//						System.out.println("Agregando " + term + " (first? " + first + ")");
						products.putIfAbsent(term, 0.0f);
//						products.put(term, products.get(term) + 1);
						// Ajuste de peso para potenciar el PRIMER termino de cada descripcion
						if(first){
							products.put(term, products.get(term) + 3);
							first = false;
						}
						else{
							products.put(term, products.get(term) + 1);
						}
					}
				}
				else{
					System.err.println("Unknown Data Type " + data_type);
					break;
				}
				
				
			}
			lector.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Normalizacion de Cantidades por compra
		for( Map.Entry<String, Set<Integer>> client_pair : client_sells_map.entrySet() ){
			String rut = client_pair.getKey();
			Set<Integer> sells = client_pair.getValue();
//			System.out.println("Compras de cliente " + rut + " (" + sells.size() + ")");
			Map<String, Float> products = clients_data_map.get(rut);
			for( Map.Entry<String, Float> prod : products.entrySet() ){
				prod.setValue( prod.getValue() / sells.size());
//				System.out.println(prod.getKey() + " : " + prod.getValue());
			}
		}
		
		// Vuelco los datos en una lista
		List<Data> data = new ArrayList<Data>();
		for( Map.Entry<String, Map<String, Float>> data_pair : clients_data_map.entrySet() ){
			String rut = data_pair.getKey();
			Map<String, Float> products = data_pair.getValue();
			// Filtrar clientes con muy pocas compras
			if( client_sells_map.get(rut).size() < min_compras || products.size() < min_productos ){
				continue;
			}
			data.add(new DataMapStringFloat(rut, products));
		}
		
//		Distance dist = new SpecialMapsDistance();
//		Distance dist = new JaccardDistance();
		Distance dist = new CosineDistance();
//		Distance dist = new BM25Distance(data);
		
		// Antes de realizar un verdadero clustering revisare las distribuciones de distancia
		DistancesDistributionAnalyzer distribution = new DistancesDistributionAnalyzer(sampling);
		distribution.execute(data, dist);
		
		
		
		
	}
}

