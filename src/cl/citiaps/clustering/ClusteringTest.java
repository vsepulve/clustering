
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

public class ClusteringTest {
	
	private ClusteringTest() {}

	public static void main(String[] args) {
		
		String data_file_sells = null;
//		String output_file = null;
		
		if( args.length == 1 ){
			data_file_sells = args[0];
//			output_file = args[1];
		}
		else{
			System.out.println("Modo de Uso:");
			System.out.println(">java [-cp \"libs.jar:.\"] cl.citiaps.clustering.ClusteringTest data_sells.csv");
			return;
		}
		
		System.out.println("Preparando Mapa de Ventas \"" + data_file_sells + "\"");
		
		// Datos para primera version de clustering:
		// Usuario (RUT) -> Mapa de productos (SKU -> Cantidad)
		// La cantidad por producto puede ser la cantidad promedio de cada producto POR venta
		// Para eso se necesita un mapa temporal de total de compras por cliente
		// Esto es para ser tratados como Conjuntos
		Map<String, Set<Integer>> client_sells_map = new TreeMap<String, Set<Integer>>();
		Map<String, Map<String, Float>> clients_data_map = new HashMap<String, Map<String, Float>>();
		
		// Mapa de Ventas
		try {
			Reader lector = new FileReader(data_file_sells);
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
				products.putIfAbsent(sku, 0.0f);
				products.put(sku, products.get(sku) + cantidad);
				
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
			System.out.println("Compras de cliente " + rut + " (" + sells.size() + ")");
			Map<String, Float> products = clients_data_map.get(rut);
			for( Map.Entry<String, Float> prod : products.entrySet() ){
				prod.setValue( prod.getValue() / sells.size());
				System.out.println(prod.getKey() + " : " + prod.getValue());
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}

