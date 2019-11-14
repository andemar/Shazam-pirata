import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BinaryDilatation;
import Catalano.Imaging.Filters.BinaryErosion;
import Catalano.Imaging.Filters.BradleyLocalThreshold;
import Catalano.Imaging.Filters.GaussianBlur;
import Catalano.Imaging.Filters.Grayscale;



import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import static marvin.MarvinPluginCollection.*;


public class Shazam2 {
	
	// Lee el directorio que tiene las imagenes a procesar (asume que solo hay imagenes OJO)
	private File data = new File("./data");

	// Recupera la lista de archivos de imagenes
	private File[] archivos = data.listFiles();
	
	//Coordenadas de la esquina superior izq del recuadro
	private int x1,y1;
	
	//Coordenadas de la esquina inferior der del recuadro
	private int x2,y2;
	
	//Imagen ya procesada
	private BufferedImage imagenProcesada = null;
	
	//Valores maximos del hisgrograma
	private int[] maximos = new int[2];
	
	//Clase que nos separara la imagren procesada, por letras.
	private ComponentesConectados cc;
	
	//Variable que contiene los espacios normalizados de las letras.
	private int[] espacios = null;
	
	public Shazam2() {
		
		//Metodo que procesa la imagen, la guarda y la retorna.
		imagenProcesada = prosesamientoImagen();
		
		//System.out.println(imagenProcesada.getHeight() + " alto - ancho " + imagenProcesada.getWidth());
		
		//Valore de Ul de la imagen
		maximos[0] = calcularUl(imagenProcesada);
		
		//Valore de Bl de la imagen
		maximos[1] = calcularBl(imagenProcesada);
		
		//System.out.println(maximos[0] + " ------ " + maximos[1]);
		
		//Se inicializa la clase componentes conectados, la cual encuentra las letras y las inicializa.
		cc = new ComponentesConectados(imagenProcesada);
		
		//Se extraen los espacios de las letras, dando como parametro la resta de Bl con Ul.
		espacios = cc.espacioEntreLetrasNormalizado(maximos[1] - maximos[0]);
		/*
		System.out.println("---------------------------------");
		for (int i = 0; i < espacios.length; i++) {
			System.out.println(espacios[i] + " espacio de la letra " + i + " normalizado");
		}
		*/
	}

	// Secuencia de Proceso de Pruebas
	public BufferedImage prosesamientoImagen()
	{
		BufferedImage imagenRecortada = null;
		FastBitmap impr = null;
		MarvinImage guardar = null;
		// Recorre una a una las imagenes y repite el proceso completo con cada una
		for (int i = 0; i < archivos.length; i++)
		{
			//Con el nombre de los archivos encontrados, se busca en la raiz la imagen.
			FastBitmap ima = new FastBitmap(archivos[i].getName());
			//nombre = archivos[i].getName();
			
			//La convierte en escala de grises
			Grayscale g = new Grayscale();
			g.applyInPlace(ima);
			
			//gris = new MarvinImage(ima.toBufferedImage());
			//MarvinImageIO.saveImage(gris,"./data/gris."+nombre);
			
			//Se aplica el efecto gauseano para elimiar el ruido de la imagen.
			GaussianBlur gb = new GaussianBlur();
			gb.applyInPlace(ima);
			
			//blur = new MarvinImage(ima.toBufferedImage());
			//MarvinImageIO.saveImage(blur,"./data/blur."+nombre);
			
			//Se le aplica el umbral de Bradley
			BradleyLocalThreshold brad = new BradleyLocalThreshold();
			brad.applyInPlace(ima);

			//Realiza erosion a la imagen binaria
			BinaryErosion bie = new BinaryErosion();
			bie.applyInPlace(ima);
			
			//biEro = new MarvinImage(ima.toBufferedImage());
			//MarvinImageIO.saveImage(biEro,"./data/erosion."+nombre);

			//Realiza dilatacion a la erocion.
			BinaryDilatation bid = new BinaryDilatation();
			bid.applyInPlace(ima);
			
			//Metodo que prueba el framework Tess4j
			tess4jOcr(archivos[i].getName(),ima.toBufferedImage());
			
			//biDil = new MarvinImage(ima.toBufferedImage());
			//MarvinImageIO.saveImage(biDil,"./data/dilatacion."+nombre);

			//Metodo que descubre que zona de la imagen contiene una palabra, y guarda los valores de la zona en x1,y1 como primer punto y x2 y2 como punto final.
			regionesDeTexto(ima.toBufferedImage(), 150, archivos[i].getName());
			
			//Metodo que imrprime la penultima imagen, antes de se recortada
			
			impr = new FastBitmap(ima);
			
			imprimir(impr, "imagenPenultima");
			
			//Metodo que recorta la imagen por la region indicada.
			imagenRecortada = recortarRegion(ima);
			
			//Metodos para imprimir y guardar la iamgen recortada
			guardar = new MarvinImage(imagenRecortada);
			impr = new FastBitmap(imagenRecortada);
			imprimir(impr, "Imagen final");
			//MarvinImageIO.saveImage(guardar,"./data/pro."+archivos[i].getName());

		}
		return imagenRecortada;
	}

	public void tess4jOcr (String dir, BufferedImage buIma){
		//Busca el archivo y lo guarda en un file.
		File imageFile = new File(dir);
		//Se crea una instancia de Itesseract
		ITesseract instance = new Tesseract();  // JNA Interface Mapping

		//Se crea una lista de rectangulos.
		List<Rectangle> seg = new ArrayList<Rectangle>();

		try {
			//Se crea un string que contiene las palabras inmersas en la imagen.
			String result = instance.doOCR(imageFile);

			//Se supone que saca los segmentos donde hay letras.
			//instance.getSegmentedRegions(buIma, 1);

			//Se supone x2 que imprime las coordenadas de los segmentos.
			for (int i = 0; i < seg.size(); i++) {
				System.out.println(seg.get(i));
			}

			//Imprime el resultado de las palabras inmersas en la imagen encontradas.
			System.out.println("------OCR-------\n"+ result + "-------------");

			//Si no encuentra imagen, pues explota.
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}

	}

	//Imprime el FastBitmap mediante una ventana.
	public void imprimir (FastBitmap imagen, String mensaje){
		JOptionPane.showMessageDialog(null, imagen.toIcon(),mensaje, JOptionPane.PLAIN_MESSAGE);
	}

	//Metodo que busca las posibles aras de texto en una imagen, recibe la imagen y un umbral (Que no sirve para nada)
	public void regionesDeTexto (BufferedImage procesada, int umbral, String nombre){

		//Se crea un MarvinImage con el BufferedImage recibido.
		MarvinImage image = new MarvinImage(procesada);
		//Clona la imagen, no se para que.
		MarvinImage originalImage = image.clone();
		//Busca las areas mediante el metodo find(imagen, maxWhiteSpace, maxFontLineWidth, minTexWidth, Umbral)
		List<MarvinSegment> segments = findTextRegions(image, 150, 20, 40, umbral); //15 8 30 150 / 10 20 70 200 ---> funciono: 150 20 40 150

		//Recorre la imagen, para pintar las areas encontradas y pintarlas.
		for(MarvinSegment s:segments){
			if(s.height >= 5){
				//Se obtienen el punto inicial
				x1 = s.x1;
				y1 = s.y1;
				
				//Se obtiene el punto final, que son los pixeles desde x1 y y1, no desde 0,0
				x2 = s.x2-s.x1;
				y2 = s.y2-s.y1;
				
				
				//System.out.println(x1 + " <--- X1\n" + y1 + " <--- Y1");
				//System.out.println("---------------");
				//System.out.println(x2 + " <--- X2\n" + y2 + " <--- Y2\n");
				
				
				originalImage.drawRect(x1, y1, x2, y2, Color.red);
				
			}
		}
		//Guarda la imagen.
		//MarvinImageIO.saveImage(originalImage,"./data/pro."+nombre);
	}
	
	//Metodo que recorta la region donde se haya el texto
	private BufferedImage recortarRegion(FastBitmap regionesDeTexto) {
		//Convierte el fastbitmap en vufferimage
		BufferedImage imagen = regionesDeTexto.toBufferedImage();
		//Buffer que contendra la imagen recortada, x y y para el tamaño de la imagen, 2. para que se ajuste a los datos ingresados. (sin franja negra)
		BufferedImage temp = new BufferedImage(x2, y2, 1);
		
		// i para la imagen recortada
		int i2 = -1;
		
		//Recorta la imagen y la pasa a temp.
		for (int i = x1; i < (x1+x2); i++) {
			i2++;
			// j para la imagen recortada - Se inicia en el for i, para que inicie en 0 cada horizontal.
			int j2 = -1;
			for (int j = y1; j < (y1+y2); j++) {
				j2++;
				temp.setRGB(i2, j2, imagen.getRGB(i, j));
			}
		}
		//Retorna la imagen ya recortada.
		return temp;
	}

	
	public int calcularUl(BufferedImage imagen){
		//Los negros horizontales de la imagen
		int negrosHorizontal;
		//Fila actual
		int fila1 = 0;
		//Fila siguiente
		int fila2 = 0;
		//El maximo valor de diferencia
		int maximo = 0;
		//Pues la respuesta, que mas....
		int respuesta = 0;
		//Valor de diferencia entre la fila 1 y fila2
		int resta = 0;
		//Es el limite de Ul
		int limite = 0 +(Math.abs( imagen.getHeight() - 0 ))/2;
		
		for (int i = 0; i < limite ; i++) {
			negrosHorizontal = 0;
			for (int j = 0; j < imagen.getWidth(); j++) {
				//Ese numero todo estrano, es el color nigga
				if(imagen.getRGB(j, i) == -16777216){
					negrosHorizontal++;
				}
			}
			//El comienza a contar del inicio hasta la mitad.
			if(i == 0 ){
				fila1 = negrosHorizontal;
			}
			else{
				//Calcula la diferencia y despues cambia fila 1 por fila 2.
				fila2 = negrosHorizontal;
				resta = fila2 - fila1;
				fila1 = fila2;
				//condiciona la diferencia, si es mayor, lo guarda y la respuesta se iguala a la fila.
				if(resta > maximo){
					maximo = resta;
					respuesta = i;
				}
			}
		}
		return respuesta;
	}

	
	public int calcularBl(BufferedImage imagen){
		
		//Los negros horizontales de la imagen
		int negrosHorizontal;
		//Fila actual
		int fila1 = 0;
		//Fila siguiente
		int fila2 = 0;
		//El maximo valor de diferencia
		int maximo = 0;
		//Pues la respuesta, que mas....
		int respuesta = 0;
		//Valor de diferencia entre la fila 1 y fila2
		int resta = 0;
		//Es el limite de Ul
		int limite = 0 +(Math.abs( imagen.getHeight() - 0 ))/2;
		
		for (int i = limite; i < imagen.getHeight(); i++) {
			negrosHorizontal = 0;
			for (int j = 0; j < imagen.getWidth(); j++) {
				//Ese numero todo estrano, es el color nigga
				if(imagen.getRGB(j, i) == -16777216){
					negrosHorizontal++;
				}
			}
			//El comienza a contar de la mitad hacia abajo
			if(i == limite ){
				fila1 = negrosHorizontal;
			}
			else{
				//Calcula la diferencia y despues cambia fila 1 por fila 2.
				fila2 = negrosHorizontal;
				resta = fila2 - fila1;
				fila1 = fila2;
				//condiciona la diferencia, si es mayor, lo guarda y la respuesta se iguala a la fila.
				if(resta > maximo){
					maximo = resta;
					respuesta = i;
				}
			}
		}
		return respuesta;
		
	}
	
	public static void main(String arg[])
	{
		Shazam2 o = new Shazam2();
	}
}