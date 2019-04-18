
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
		String output_file = null;
		
		if( args.length == 2 ){
			data_file_sells = args[0];
			output_file = args[1];
		}
		else{
			System.out.println("Modo de Uso:");
			System.out.println(">java [-cp \"libs.jar:.\"] cl.citiaps.clustering.ClusteringTest data_sells.csv output.csv");
			return;
		}
		
		System.out.println("Preparando Mapa de Ventas \"" + data_file_sells + "\"");
		
		// Mapa de Ventas
		Map<String, Set<Integer>> client_sells_map = new TreeMap<String, Set<Integer>>();
		Map<Integer, Integer> sells_total_map = new TreeMap<Integer, Integer>();
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
				if( rutcli.equals("9994800") ){
					System.out.println("Agregando " + rutcli + " " + compra + " " + sku + " " + costo + " " + " " + neto);
				}
				client_sells_map.putIfAbsent(rutcli, new TreeSet<Integer>());
				client_sells_map.get(rutcli).add(compra);
				sells_total_map.putIfAbsent(compra, 0);
				sells_total_map.put(compra, sells_total_map.get(compra) + neto);
				if( rutcli.equals("9994800") ){
					System.out.println("Resultado compra " + compra + " : " + sells_total_map.get(compra));
				}
			}
			lector.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
}

