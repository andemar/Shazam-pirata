import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

public class Shazam {
	
	//
	private ProcesamientoDeImagenes procesado;
	
	//Clase que nos separara la imagren procesada, por letras.
	private ComponentesConectados cc;
	
	//Es la imagen ya procesada de la clase procesamiento de imagenes.
	private BufferedImage imagenProcesada;
	
	//Es la variable que contendra el Hpv (Perfil horizontal)
	private double Hpv;
	
	//Es el alto promedio de las letras
	private double altoPromedio;
	
	//Es el ancho promedio de las letras
	private double anchoPromedio;
	
	//Es el espacio promedio de las letras
	private double espacioPromedio;
	
	//Atributo que ejecuta IA
	private pruebaIa ia;
	
	public Shazam() {
		
		procesado = new ProcesamientoDeImagenes();
		
		//Metodo que procesa la imagen, la guarda y la retorna.
		imagenProcesada = procesado.getImgenProcesada();
		
		//Se inicializa la clase componentes conectados, la cual encuentra las letras y las inicializa.
		cc = new ComponentesConectados(imagenProcesada);
		
		//Densidad de pixeles
		Hpv = cc.densidadHpV(procesado.getZonaMedia());
		System.out.println(Hpv + " densidad");
		//Asignar el tcc a cada letra (Categorizacion)
		altoPromedio = cc.getAltoPromedio(procesado.getZonaMedia());
		System.out.println(altoPromedio + " altoPromedio");
		//Asignar el mcc a cada letra (Categorizacion)
		anchoPromedio = cc.getAnchoPromedio();
		System.out.println(anchoPromedio + " anchoPromedio");
		//Asignar el espacio normalizado promedio de cada letra.
		espacioPromedio = cc.getEspacioPromedio(procesado.getZonaMedia());
		System.out.println(espacioPromedio + " espacioPromedio");
		
		ia = new pruebaIa();
		String nombreFuente = ia.getFuente(Hpv, altoPromedio, anchoPromedio, espacioPromedio);
		
		JOptionPane.showMessageDialog(null, "El tipo de fuente es: " + nombreFuente , "Fuente", JOptionPane.PLAIN_MESSAGE);
		
		/*
		System.out.println("---------------------------------");
		for (int i = 0; i < espacios.length; i++) {
			System.out.println(espacios[i] + " espacio de la letra " + i + " normalizado");
		}
		*/
	}
	
	
	
	public static void main(String arg[])
	{
		Shazam o = new Shazam();
	}
}