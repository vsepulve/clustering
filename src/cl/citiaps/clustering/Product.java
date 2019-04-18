package cl.citiaps.clustering;

public class Product {
	
	private String sku;
	private String nombre;
	private String linea;
	private String codjer;
	private Integer precio;
	private Integer cantidad;
	
	// Componentes del puntaje
	// Notar que con esto, podŕia ser razonable separa esto en 2 clases
//	private float score_internal;
//	private float score_client;
//	private float score_global;
	
	public Product() {
		sku = null;
		nombre = null;
		linea = null;
		codjer = null;
		precio = 0;
		cantidad = 0;
	}
	
	public Product(Product original) {
		sku = original.getSku();
		nombre = original.getNombre();
		linea = original.getLinea();
		codjer = original.getCodjer();
		precio = original.getPrecio();
		cantidad = original.getCantidad();
	}
	
	public Product(String _sku, String _nombre, String _linea, String _codjer) {
		sku = _sku;
		nombre = _nombre;
		linea = _linea;
		codjer = _codjer;
		precio = 0;
		cantidad = 0;
	}
	
	public void setSku(String _sku){
		sku = _sku;
	}
	
	public String getSku(){
		return sku;
	}
	
	public void setNombre(String _nombre){
		nombre = _nombre;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void setLinea(String _linea){
		linea = _linea;
	}
	
	public String getLinea(){
		return linea;
	}
	
	public void setCodjer(String _codjer){
		codjer = _codjer;
	}
	
	public String getCodjer(){
		return codjer;
	}
	
	public void setPrecio(Integer _precio){
		precio = _precio;
	}
	
	public Integer getPrecio(){
		return precio;
	}
	
	public void setCantidad(Integer _cantidad){
		cantidad = _cantidad;
	}
	
	public Integer getCantidad(){
		return cantidad;
	}
	
	public String toString(){
		return "(" + sku + ", " + nombre + ", " + precio + ", " + cantidad + ")";
	}
	
}
