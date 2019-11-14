//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Vector;

//import javax.xml.bind.util.ValidationEventCollector;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
//import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

//import com.lowagie.text.List;



public class pruebaIa {

	@SuppressWarnings("unchecked")
	NeuralNetwork<BackPropagation> redNeuronal = new Perceptron(4, 3);
	
	TrainingSet entrenamiento = new TrainingSet(4, 3);
	
	DataSet ds = new DataSet(4, 3);
	
	public pruebaIa() {

		entrenar();
		
	}
	
	private void entrenar(){
		
		double[] calibriEsperado = {1,0,0};
		double[] maindraEsperado = {0,1,0};
		double[] fixedEsperado = {0,0,1};
		double[] courierEsperado = {0,1,1};
		
		
		//Calibri
		DataSetRow calibriUno = new DataSetRow(new double[] {30, 60, 70, 20}, calibriEsperado);
		ds.add(0,calibriUno);
		DataSetRow calibriDos = new DataSetRow(new double[] {32, 66, 76, 23}, calibriEsperado);
		ds.add(0,calibriDos);
		DataSetRow calibriTres = new DataSetRow(new double[] {33, 67, 74, 23}, calibriEsperado);
		ds.add(0,calibriTres);
		DataSetRow calibriCua = new DataSetRow(new double[] {32, 70, 78, 24}, calibriEsperado);
		ds.add(0,calibriCua);
		DataSetRow calibriCin = new DataSetRow(new double[] {30, 61, 70, 18}, calibriEsperado);
		ds.add(0,calibriCin);
		DataSetRow calibriSei = new DataSetRow(new double[] {31, 61, 69, 19}, calibriEsperado);
		ds.add(0,calibriSei);
		DataSetRow calibriSie = new DataSetRow(new double[] {34, 66, 73, 21}, calibriEsperado);
		ds.add(0,calibriSie);
		DataSetRow calibriOch = new DataSetRow(new double[] {33, 67, 73, 22}, calibriEsperado);
		ds.add(0,calibriOch);
		DataSetRow calibriNue = new DataSetRow(new double[] {30, 68, 75, 24}, calibriEsperado);
		ds.add(0,calibriNue);
		DataSetRow calibriDie = new DataSetRow(new double[] {29, 60, 67, 21}, calibriEsperado);
		ds.add(0,calibriDie);
		
		
		
		//Maiandra
		DataSetRow maiandraUno = new DataSetRow(new double[] {30, 55, 78, 16},maindraEsperado);
		ds.add(1,maiandraUno);
		DataSetRow maiandraDos = new DataSetRow(new double[] {30, 67, 82, 17}, maindraEsperado);
		ds.add(1,maiandraDos);
		DataSetRow maiandraTres = new DataSetRow(new double[] {29, 56, 81, 16}, maindraEsperado);
		ds.add(1,maiandraTres);
		DataSetRow maiandraCue = new DataSetRow(new double[] {37, 36, 91, 14}, maindraEsperado);
		ds.add(1,maiandraCue);
		DataSetRow maiandraCin = new DataSetRow(new double[] {32, 61, 77, 15}, maindraEsperado);
		ds.add(1,maiandraCin);
		DataSetRow maiandraSei = new DataSetRow(new double[] {29, 59, 73, 16}, maindraEsperado);
		ds.add(1,maiandraSei);
		DataSetRow maiandraSie = new DataSetRow(new double[] {30, 62, 76, 16}, maindraEsperado);
		ds.add(1,maiandraSie);
		DataSetRow maiandraOch = new DataSetRow(new double[] {29, 63, 76, 15}, maindraEsperado);
		ds.add(1,maiandraOch);
		DataSetRow maiandraNue = new DataSetRow(new double[] {29, 58, 77, 12}, maindraEsperado);
		ds.add(1,maiandraNue);
		
		/*
		//Fixed
		DataSetRow fixedUno = new DataSetRow(new double[] {28, 48, 70, 18}, fixedEsperado);
		ds.add(2,fixedUno);
		DataSetRow fixedDos = new DataSetRow(new double[] {34, 49, 67, 21}, fixedEsperado);
		ds.add(2,fixedDos);
		DataSetRow fixedTres = new DataSetRow(new double[] {17, 51, 74, 23}, fixedEsperado);
		ds.add(2,fixedTres);
		DataSetRow fixedCua = new DataSetRow(new double[] {32, 53, 72, 23}, fixedEsperado);
		ds.add(2,fixedCua);
		*/
		
		//Courier
		DataSetRow CourierUno = new DataSetRow(new double[] {20, 49, 86, 19}, courierEsperado);
		ds.add(2,CourierUno);
		DataSetRow CourierDos = new DataSetRow(new double[] {21, 49, 86, 20}, courierEsperado);
		ds.add(2,CourierDos);
		DataSetRow CourierTres = new DataSetRow(new double[] {16, 43, 75, 17}, courierEsperado);
		ds.add(2,CourierTres);
		DataSetRow CourierCua = new DataSetRow(new double[] {18, 46, 80, 18}, courierEsperado);
		ds.add(2,CourierCua);
		DataSetRow CourierCin = new DataSetRow(new double[] {16, 42, 72, 16}, courierEsperado);
		ds.add(2,CourierCin);
		DataSetRow CourierSei = new DataSetRow(new double[] {17, 43, 75, 15}, courierEsperado);
		ds.add(2,CourierSei);
		DataSetRow CourierSie = new DataSetRow(new double[] {18, 45, 78, 17}, courierEsperado);
		ds.add(2,CourierSie);
		DataSetRow CourierOch = new DataSetRow(new double[] {18, 45, 78, 18}, courierEsperado);
		ds.add(2,CourierOch);
		DataSetRow CourierNue = new DataSetRow(new double[] {19, 46, 80, 18}, courierEsperado);
		ds.add(2,CourierNue);
		DataSetRow CourierDie = new DataSetRow(new double[] {19, 49, 85, 18}, courierEsperado);
		ds.add(2,CourierDie);
		
		
		//Entrenamiento
		BackPropagation bp = new BackPropagation();
		bp.setMaxIterations(1000);
		redNeuronal.learn(ds, bp);
	}
	
	public String getFuente(double Hpv, double altoPromedio, double anchoPromedio, double espacioPromedio){
		
		String fuente = "No se encontro la fuente - Ojala no aparesca esto en la presentacion :)";
		
		double[] valores = {Hpv,altoPromedio,anchoPromedio,espacioPromedio};
		
		//ingreso de prueba
		redNeuronal.setInput(valores);
		redNeuronal.calculate();

		double[] respuesta = redNeuronal.getOutput();

		System.out.println(respuesta[0] + " - " + respuesta[1] + " - " + respuesta[2]);

		if(respuesta[0] == 1.0 && respuesta[1] == 0.0 && respuesta[2] == 0.0){
			fuente = "Calibri";
		}else if(respuesta[0] == 0.0 && respuesta[1] == 1.0 && respuesta[2] == 0.0){
			fuente = "Maiandra";
		}else if(respuesta[0] == 0.0 && respuesta[1] == 0.0 && respuesta[2] == 1.0){
			fuente = "Fixed";
		}else if(respuesta[0] == 0.0 && respuesta[1] == 1.0 && respuesta[2] == 1.0){
			fuente = "Courier New";
		}else{
			
		}
		return fuente;
	}

}
