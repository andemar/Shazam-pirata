
public class Letra {
	
	//Propiedades de la letra
	private int limiteInferior;
	private int limiteSuperior;
	private int limiteIzquierdo;
	private int limiteDerecho;
	private String Tcc;
	private String Mcc;
	
	//Es el espacio entre el limite izquierdo de esta letra menos el limite derecho de la letra anterior.
	private int espacio;
	
	//constyructyor
	public Letra(int pInferior, int pSuperior, int pIzquierdo, int pDerecho) {
		
		limiteInferior = pInferior;
		limiteSuperior = pSuperior;
		limiteIzquierdo = pIzquierdo;
		limiteDerecho = pDerecho;
		
	}
	

	public void setEspacio(int pEspacio){
		espacio = pEspacio;
	}
	
	//Retorna el limite derecho
	public int getDerecho(){
		return limiteDerecho;
	}
	
	//Retorna el limite izquierdo
	public int getIzquierdo(){
		return limiteIzquierdo;
	}
	
	//Retorna el pixel mas alto
	public int getArriba() {
		return limiteSuperior;
	}
	
	//Retorna el pixel mas bajo
	public int getAbajo() {
		return limiteInferior;
	}
	
	//retorna el alto
	public int getAlto (){
		
		return limiteInferior-limiteSuperior;
	}
	//retorna el ancho
	public int getAncho(){
		return limiteDerecho-limiteIzquierdo;
	}
	//retorna el ratio
	public double getRatio(){
		//System.out.println(getAncho() + " ancho");
		//System.out.println(getAlto() + " alto");
		return (double)getAncho()/getAlto();
	}
	
	public int getEspacio(){
		return espacio;
	}
	//Asigna Tcc
	public void setTcc(String pTcc) {
		Tcc = pTcc;
	}
	//Asigna Mcc
	public void setMcc(String pMcc) {
		Mcc = pMcc;
	}
	//da Tcc
	public String getTcc() {
		return Tcc;
	}
	
	//da Mcc
	public String getMcc() {
		return Mcc;
	}
}
