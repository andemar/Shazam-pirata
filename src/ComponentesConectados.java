import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class ComponentesConectados {

	//Constantes de Clase Tipografica (T(cc))
	private final static String FULL = "full";
	private final static String HIGH = "high";
	private final static String DEEP = "deep";
	private final static String SHORT = "short";
	private final static String SUP = "sup";
	private final static String SUB = "sub";

	//Constantes de Clases Morfologicas (M(cc))
	private final static String WIDE = "wide";
	private final static String LARGE = "large";
	private final static String SQUARE = "square";
	private final static String TALL = "tall";
	private final static String THIN = "thin";
	private final static String SMALL = "small";


	//Imagen completa
	BufferedImage imagen = null;

	ArrayList<Letra> listaLetras = new ArrayList<Letra>();

	public ComponentesConectados(BufferedImage pImagen) {

		imagen = pImagen;
		//Se encuentran las letras y se guardan en el arraylisy listaLetras
		encontrarLetras();
		//Se calcula el espacio entre las letras.
		espacioEntreLetras();
		//

	}


	public ArrayList<Letra> getListaLetras(){

		return listaLetras;
	}


	public void encontrarLetras(){

		int lIzq = limiteIzquierdo(0);
		int lDer = limiteDerecho(lIzq);
		int lSup = limiteSuperior(lIzq, lDer);
		int lInf = limiteInferior(lIzq, lDer, lSup);
		listaLetras.add(new Letra(lInf, lSup, lIzq, lDer));
		//System.out.println("------------------------------");
		boolean sigue = true;
		int num = 1;
		Letra ultimaLetra = null;
		while(sigue){

			ultimaLetra = listaLetras.get(listaLetras.size()-1);
			if(busquedaPreliminar( ultimaLetra.getDerecho()+1) == false){
				sigue = false;
			}
			else{
				lIzq = limiteIzquierdo(ultimaLetra.getDerecho());
				lDer = limiteDerecho(lIzq);
				lSup = limiteSuperior(lIzq, lDer);
				lInf = limiteInferior(lIzq, lDer, lSup);
				listaLetras.add(new Letra(lInf, lSup, lIzq, lDer));
				//System.out.println("------------------------------");
				//System.out.println("letra " + num +" /izquierdo "+lIzq+" - derecho "+lDer+" - superopr "+lSup+" - inferior "+lInf + " /69lin");
				num++;
			}
		}
		//System.out.println("Encontro todas las letras");
		//System.out.println(listaLetras.size() + " letras encontradas");
	}

	//Metodo que retorna true, si encuentra ALGUN PIXEL NEGRO, despues del lado derecho de la ultima letra.
	//Entra como parametro el lado derecho de la ultima letra encontrada
	private boolean busquedaPreliminar(int derecho) {
		boolean respuesta = false;

		if(derecho >= imagen.getWidth()){

		}else{
			for (int j = derecho; j < imagen.getWidth(); j++) {
				for (int i = 0; i < imagen.getHeight(); i++) {

					if(imagen.getRGB(j, i) == -16777216){
						respuesta = true;
					}
				}
			}
		}
		return respuesta;
	}

	//Metodo que busca el limite izquierdo de una letra, este metodo recibe como parametro la
	//columna desde donde inicia el recorrido (Generalmente es la columna derecha de la letra anterior)
	private int limiteIzquierdo(int indice){
		int limiteizq = 0;
		//El ancho es de 951
		for (int j = indice; j < imagen.getWidth(); j++) {

			for (int i = 0; i < imagen.getHeight(); i++) {
				//System.out.println(j + " j - i " + i);
				if(imagen.getRGB(j, i) == -16777216){
					//System.out.println(i + " alto");
					return j;
				}
			}
		}
		return limiteizq;
	}

	//Metodo que busca el limite derecho de una letra, este metodo recibe como parametro la
	//columna desde donde inicia el recorrido (Generalmente es la columna izquierda de la misma letra)
	//hasta encontrar la ultima coluimna con pixel negro.
	private int limiteDerecho(int lIzq){
		int limiteDer = 0;
		boolean columnaBlanca;

		for (int j = lIzq; j < imagen.getWidth(); j++) {
			columnaBlanca = true;
			for (int i = 0; i < imagen.getHeight(); i++) {
				//System.out.println(j + " j - i " + i);
				if(imagen.getRGB(j, i) == -16777216){
					columnaBlanca = false;
				}
			}
			if(columnaBlanca){
				//System.out.println(j + " izquierda");
				return j;
			}
		}

		return limiteDer;
	}

	//Metodo que busca el limite superior de una letra, este metodo recibe como parametro las
	//columnas izq y der de la letra para conocer sus limites, el recorrido comienza desde la primera fila
	//Hasta encontrar el primer pixel negro.
	private int limiteSuperior(int lIzq, int lDer) {
		int lSup = 0;
		//El recorrido recorre las filas normalmente
		for (int i = 0; i < imagen.getHeight(); i++) {
			//El recorrido inicia desde el lado izquierdo hasta el lado derecho
			for (int j = lIzq; j < lDer; j++) {
				//Si encuentra un pixel negro, retorna la fila donde lo encontro, que seria la fila superior.
				if(imagen.getRGB(j, i) == -16777216){
					//System.out.println(i + " Superior");
					return i;
				}
			}
		}
		return lSup;
	}

	//Metodo que busca el limite inferior de una letra, este metodo recibe como parametro las
	//columnas izq y der y la fila superir de la letra para conocer sus limites, el recorrido 
	//comienza desde la fila superior Hasta encontrar el ultimo pixel negro.
	
	private int limiteInferior(int lIzq, int lDer, int lSup) {
		int respuesta = 0;
		
		for (int i = lSup; i < imagen.getHeight(); i++) {
		
			//El recorrido inicia desde el lado izquierdo hasta el lado derecho
			for (int j = lIzq; j < lDer; j++) {
				//Si encuentra un pixel negro, significa que no se ha llegado al final de la letra.
				
				if(imagen.getRGB(j, i) == -16777216){
					respuesta = i;
					break;
				}
				
			}
			
		}
		return respuesta+1;
	}

	//Se calcula el espacio entre la letra actual y la letra anterior. (La primera letra tiene un valor de cero en su espacio)
	private void espacioEntreLetras(){

		listaLetras.get(0).setEspacio(0);
		Letra anterior = null;
		Letra actual = null;

		for (int i = 1; i < listaLetras.size(); i++) {
			anterior = listaLetras.get(i-1);
			actual = listaLetras.get(i);

			actual.setEspacio(actual.getIzquierdo() - anterior.getDerecho());
			//System.out.println(actual.getEspacio() + " espacio de la letra " + i);
		}
	}

	//Se calcula el espacio entre las letras, normalizado con el numero
	//resultante de resta bl con ul. Las letras que sean mayores o iguales
	//a este valor, se cambia con la media de espacios de las letras.
	//El valor normalizado se calcula con un 70%, si un espacio es mayor a este valor, es que es un espacio
	//entre palabras, y se cambia a espacio de media.
	private int[] espacioEntreLetrasNormalizado(int[] blUl){

		int normalizado = (blUl[1] - blUl[0])*70/100;
		//System.out.println(normalizado + " normalizado " + blUl[1] + " bl - ul " + blUl[0]);
		int[] espacio = new int[listaLetras.size()];
		int media = espacio.length/2;
		//System.out.println(media + " posicion media");
		int[] espacioOrdenado = null;
		for (int i = 0; i < espacio.length; i++) {
			espacio[i] = listaLetras.get(i).getEspacio();
		}

		espacioOrdenado = ordenamientoBurbuja(espacio);
		//System.out.println(espacioOrdenado[media] + " valor de la media");

		for (int i = 0; i < espacio.length; i++) {

			if(espacio[i] > normalizado){
				espacio[i] = espacioOrdenado[media];
			}
		}

		return espacio;
	}

	//Ordenamiento por burbuja, nada del otro siglo.
	private int[] ordenamientoBurbuja(int[] lista){
		int[] listaOrdenada = lista;

		for(int i = 0; i < listaOrdenada.length - 1; i++)
		{
			for(int j = 0; j < listaOrdenada.length - 1; j++)
			{
				if (listaOrdenada[j] > listaOrdenada[j + 1])
				{
					int tmp = listaOrdenada[j+1];
					listaOrdenada[j+1] = listaOrdenada[j];
					listaOrdenada[j] = tmp;
				}
			}
		}
		/*
		System.out.println("------------LISTA ORDENADA-----------");
		for (int i = 0; i < listaOrdenada.length; i++) {
			System.out.println(listaOrdenada[i] + " lista ordenada " + i);
		}
		 */
		return listaOrdenada;
	}

	//El metodo entra como parametro la zona media, pra hacer el calculo de expacio normalizado.
	public double densidadHpV(int[] zonaMedia){
		int cuentaNegros = 0;
		int cuentaColumnas = 0;
		Letra actual = null;
		int izq = 0;
		int der = 0;
		int[] espacio = espacioEntreLetrasNormalizado(zonaMedia);

		for (int i = 0; i < listaLetras.size(); i++) {
			actual = listaLetras.get(i);
			izq = actual.getIzquierdo();
			der = actual.getDerecho();

			cuentaNegros = cuentaNegrosLetra(izq, der, zonaMedia);
			cuentaColumnas = espacio[i] + (der-izq);

			//System.out.println(cuentaNegros + " pixelesNegros - columnas " + cuentaColumnas + " -- letra " + i);
		}
		//System.out.println((cuentaNegros / cuentaColumnas) + " <----- respuesta :)");
		return cuentaNegros/cuentaColumnas;
	}

	//Cuenta el numero de pixeles que tiene una letra.
	private int cuentaNegrosLetra(int lIzq, int lDer, int[] zonaMedia){
		int respuesta = 0;
		int ul = zonaMedia[0];
		int bl = zonaMedia[1];

		for (int j = lIzq; j < lDer; j++) {

			for (int i = ul; i < bl; i++) {
				//System.out.println(j + " j - i " + i);
				if(imagen.getRGB(j, i) == -16777216){
					//System.out.println(i + " alto");
					respuesta++;
				}
			}
		}
		return respuesta;
	}

	
	
	public void asignarTcc(int[] zonaMedia) {
		int ul = zonaMedia[0];
		int bl = zonaMedia[1];
		int to = 0;
		int bo = imagen.getHeight();
		//Encuentra el factor de tolerancia empirico 
		double emp = (Math.abs(to - bo))/8;
		//System.out.println("bl"+bl);
		//System.out.println("emp"+emp);
		for (int i = 0; i < listaLetras.size(); i++) {
			//Factores de la letra parte alta
			//tcc-to
			/*System.out.println("letra "+i+"--tcc "+listaLetras.get(i).getArriba()+"---bcc "
					+listaLetras.get(i).getAbajo()+"--bo "+bo+"--ul "+ul+"--bl"+bl);*/
			int factorAlto1 = Math.abs(listaLetras.get(i).getArriba() - to);
			//tcc-ul
			int factorAlto2 = Math.abs(listaLetras.get(i).getArriba() - ul);
			//tcc-bl
			int factorAlto3 = Math.abs(listaLetras.get(i).getArriba() - bl); 
			//System.out.println("alto"+factorAlto1+"--"+factorAlto2+"--"+factorAlto3+"--");
			//Factores de la letra parte baja
			//bo-bcc
			int factorBajo1 = Math.abs(bo - listaLetras.get(i).getAbajo());
			//bcc-bl
			int factorBajo2 = Math.abs(listaLetras.get(i).getAbajo() - bl);
			//ul-bcc
			int factorBajo3 = Math.abs(ul - listaLetras.get(i).getAbajo());
			//System.out.println("bajo"+factorBajo1+"--"+factorBajo2+"--"+factorBajo3+"--");

			if(factorAlto1<=emp && factorBajo1<=emp) {
				listaLetras.get(i).setTcc(FULL);
			}else if(factorAlto1<=emp && factorBajo2<=emp) {
				listaLetras.get(i).setTcc(HIGH);
			}else if(factorAlto2<=emp && factorBajo1<=emp) {
				listaLetras.get(i).setTcc(DEEP);
			}else if(factorAlto2<=emp && factorBajo2<=emp) {
				listaLetras.get(i).setTcc(SHORT);
			}else if(factorAlto1<=emp && factorBajo3<=emp) {
				listaLetras.get(i).setTcc(SUP);
			}else if(factorAlto3<=emp && factorBajo1<=emp) {
				listaLetras.get(i).setTcc(SUB);
			}
			System.out.println("letra "+i+"Tcc "+listaLetras.get(i).getTcc());
		}
	}

	public void asignarMcc() {
		int	bo = imagen.getHeight();
		for (int i = 0; i < listaLetras.size(); i++) {
			//System.out.println(listaLetras.get(i).getRatio() + " radio de letra " + i);
			if(listaLetras.get(i).getAlto()<=(bo/25)) {
				listaLetras.get(i).setMcc(SMALL);
			}
			else if(listaLetras.get(i).getRatio()>=1.75) {
				listaLetras.get(i).setMcc(WIDE);
			}else if(listaLetras.get(i).getRatio()>=1.25 && listaLetras.get(i).getRatio()<1.75) {
				listaLetras.get(i).setMcc(LARGE);
			}else if(listaLetras.get(i).getRatio()>=0.75 && listaLetras.get(i).getRatio()<1.25) {
				listaLetras.get(i).setMcc(SQUARE);
			}else if(listaLetras.get(i).getRatio()>=0.5 && listaLetras.get(i).getRatio()<0.75) {
				listaLetras.get(i).setMcc(TALL);
			}else if(listaLetras.get(i).getRatio()<0.5) {
				listaLetras.get(i).setMcc(THIN);
			}
			//System.out.println(listaLetras.get(i).getMcc() + " mcc de letra " + i);
			//System.out.println("-------------------------");
		}
	}
	
	//Se calcula el alto promedio de todas las letras, dependiendo la categoria.
	public double getAltoPromedio(int[] zonaMedia) {
		asignarTcc(zonaMedia);
		double respuesta = 0.0;
		int ul = zonaMedia[0];
		int bl = zonaMedia[1];
		int to = 0;
		int bo = imagen.getHeight();
		
		int contadorLetras = 0;
		int contadorAltura = 0;
		
		double full = (Math.abs(bl-ul)/Math.abs(bo-to));
		double high = (Math.abs(bl-ul)/Math.abs(bl-to));
		double shortl = 1.0;
		double deep = (Math.abs(bl-ul)/Math.abs(bo-ul));
		
		for (int i = 0; i < listaLetras.size(); i++) {
			
			if(listaLetras.get(i).getTcc().equals(FULL)  || listaLetras.get(i).getTcc().equals(HIGH) || 
			   listaLetras.get(i).getTcc().equals(SHORT) || listaLetras.get(i).getTcc().equals(DEEP)) {
				
				contadorLetras++;
				
				switch (listaLetras.get(i).getTcc()) {
				case FULL:
					contadorAltura += listaLetras.get(i).getAlto()*full;
					break;
				case HIGH:
					contadorAltura += listaLetras.get(i).getAlto()*high;
					break;
				case SHORT:
					contadorAltura += listaLetras.get(i).getAlto()*shortl;
					break;
				default:
					contadorAltura += listaLetras.get(i).getAlto()*deep;
					break;
				}
				
			}
			
		}
		
		respuesta = contadorAltura/contadorLetras;
		//System.out.println(respuesta + " <-- respuesta");
		return respuesta;
	}
	
	//Calcla el ancho promedio, cuando esta no es de cuadrado y corto.
	public double getAnchoPromedio() {
		asignarMcc();
		double respuesta = 0;
		int contadorLetras = 0;
		int contadorAncho = 0;
		
		for (int i = 0; i < listaLetras.size(); i++) {
			
			if(listaLetras.get(i).getMcc().equals(SQUARE) && listaLetras.get(i).getTcc().equals(SHORT)) {
				
			}else {
				contadorLetras++;
				contadorAncho += listaLetras.get(i).getAncho();
			}	
		}
		
		respuesta = contadorAncho/contadorLetras;
		//System.out.println(respuesta + " ancho");
		return respuesta;
	}

	//Metodo que calcula el espacio promedio normalizado de la imagen
	public double getEspacioPromedio(int[] zonaMedia) {
		double respuesta = 0;
		int espacios = 0;
		int[] espaciosNor = espacioEntreLetrasNormalizado(zonaMedia);
		
		for (int i = 0; i < espaciosNor.length; i++) {
			espacios += espaciosNor[i]; 
		}
		
		respuesta = (espacios/espaciosNor.length);
		
		return respuesta;	
	}
}