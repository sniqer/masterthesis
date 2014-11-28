package lteAnalyzer;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Main {
	@SuppressWarnings("resource")
	static final int NOT_A_VALUE = -1337; //empty spaces in a vector is represented by this value
	public static void main(String[] arg) throws IOException{
		
		
		
		String dummyline = null;
		BufferedReader dummyreader = null;
		int rows = 0;
		int cols = 0;
		
		//l�ser cvs file
		dummyreader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/maximus.csv"));
		cols = dummyreader.readLine().split(",").length;
		
		while ((dummyline = dummyreader.readLine()) != null) {
			rows++;
		}
		dummyreader.close();
		rows--;
		int nrSinrVals = 45;
		int nrDlMcsVals = 28;
		int nrUlMcsVals = 23;
		System.out.println(rows + " " + cols);
		


		
		


		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		System.out.println(cols);
		//l�ser cvs file
		String[][] lteDataMatrix = new String[rows][cols];
		String[] header  = new String[cols];
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/maximus.csv"));
		while ((line = reader.readLine()) != null) {
			if(i == 0){
				header = line.split(",");
				i++;
			}
			else if(i>0 && i<rows+1){ //avoid the header
				//System.out.println(i);
				lteDataMatrix[i-1] = line.split(",");
				System.out.println(lteDataMatrix[i-1].length);
				if (lteDataMatrix[i-1].length != cols)
					System.out.println("fel p� rad  " + i);
				i++;
			} else break;
		}
		reader.close();
		
		Print.array(header);

		
		lteDataMatrix = BasicCalc.transpose(lteDataMatrix);
		System.out.println("f�rdigtransponerad");
		
		//we have to set header here and SIB later
		BasicCalc basicCalc = new BasicCalc();
		basicCalc.setHeader(header);

		
		int SIBIndex 		= BasicCalc.findHeaderIndex("bbUeRef",0);
		int SINRIndex 		= BasicCalc.findHeaderIndex("sinr",0);//hos razmus puschSinr
		int cqiIndex 		= BasicCalc.findHeaderIndex("cqi",0);
		int dlTbs1Index 	= BasicCalc.findHeaderIndex("tbs1",0);
		int dlTbs2Index 	= BasicCalc.findHeaderIndex("tbs2",0);
		int dlTbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2);
		int ulTbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2); //3 hos razmus
		int dLprbIndex 		= BasicCalc.findHeaderIndex("prb",0);
		int uLprbIndex 		= BasicCalc.findHeaderIndex("prb",1);
		int uLtbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2);
		int riIndex			= BasicCalc.findHeaderIndex("ri",0);
		int blerIndex 		= BasicCalc.findHeaderIndex("bler",0);
		
		int ulMcsIndex 		= BasicCalc.findHeaderIndex("mcs",2);
		int dlMcs1Index 	= BasicCalc.findHeaderIndex("mcs1",0);
		int dlMcs2Index 	= BasicCalc.findHeaderIndex("mcs2",0);
		
		int timeStampIndex 	= BasicCalc.findHeaderIndex("timeStamp", 0);
		int ndf1Index		= BasicCalc.findHeaderIndex("ndf1", 0);
		int ndf2Index		= BasicCalc.findHeaderIndex("ndf2", 0);
		int pmiIndex		= BasicCalc.findHeaderIndex("pmi", 0);
		int sfIndex			= BasicCalc.findHeaderIndex("sf", 0);
		



		

		int[] dlTbs1 		= interpretLTEdata(lteDataMatrix[dlTbs1Index]);
		int[] dlTbs2	 	= interpretLTEdata(lteDataMatrix[dlTbs2Index]);
		//dlTbsSum 	= interpretLTEdata(lteDataMatrix[dlTbsSumIndex]);
		int[] dlTbsSum 	= BasicCalc.addArrays(dlTbs1, dlTbs2);
		int[] ulTbsSum	= interpretLTEdata(lteDataMatrix[ulTbsSumIndex]);
		int[] SIB			= interpretLTEdata(lteDataMatrix[SIBIndex]);
		int[] SINR 		= interpretLTEdata(lteDataMatrix[SINRIndex]);
		int[] dlPrb  		= interpretLTEdata(lteDataMatrix[dLprbIndex]);
		int[] ulPrb  		= interpretLTEdata(lteDataMatrix[uLprbIndex]);
		int[] cqi   		= interpretLTEdata(lteDataMatrix[cqiIndex]);
		int[] ri			= interpretLTEdata(lteDataMatrix[riIndex]);
		int[] bler		= interpretLTEdata(lteDataMatrix[blerIndex]);
		
		int[] ulMcs		= interpretLTEdata(lteDataMatrix[ulMcsIndex]);
		int[] dlMcs1		= interpretLTEdata(lteDataMatrix[dlMcs1Index]);
		int[] dlMcs2		= interpretLTEdata(lteDataMatrix[dlMcs2Index]);
		
		int[] sf	= interpretLTEdata(lteDataMatrix[sfIndex]);
		int[] dlMcsSum	= BasicCalc.addArrays(dlMcs1, dlMcs2);

		
		String[] ndf1 = lteDataMatrix[ndf1Index];
		String[] ndf2 = lteDataMatrix[ndf2Index];
		
		String[] timeStamp	= lteDataMatrix[timeStampIndex];
		
		timeStamp	= BasicCalc.discardDate(timeStamp);
		
		double[] timeStampInDigits	= BasicCalc.startTimeFrZero(timeStamp);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHH!!!!");
		
		//setting variable in BasicCalc class

		basicCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		DLCalc dlCalc = new DLCalc();
		dlCalc.setCqi(cqi);
		dlCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		ULCalc ulCalc = new ULCalc();
		ulCalc.setCqi(cqi);
		ulCalc.setSIB(SIB);
		

		Calculate calculate = new Calculate();
		calculate.setSIB(SIB);
		
		/*The data to be plotted, first row is bitsperSINR, second is bitsperSINRperhz
		 * it will go from -15 to 30 dB*/
		float[] dlAvgBps1PerCqi	 		= new float[nrSinrVals];
		float[] dlAvgBps2PerCqi	 		= new float[nrSinrVals];
		float[] dlAvgTotBpsPerCqi 		= new float[nrSinrVals];
		float[] ulAvgBpsPerSINR			= new float[nrSinrVals];
		float[] dlMaxBpsPerCqi	 		= new float[nrSinrVals];
		float[] ulMaxBpsPerSINR	 		= new float[nrSinrVals];

		double[][] dlAvgBpsPerSec		= new double[2][rows];
		double[][] ulAvgBpsPerSec		= new double[2][rows];
		
		float[] dlPrbPerCqi				= new float[nrSinrVals];
		float[] ulPrbPerSINR			= new float[nrSinrVals];
		
		float[] dlAvgSpecEffPerCqi		= new float[nrSinrVals]; 
		float[] ulAvgSpecEffPerSINR		= new float[nrSinrVals];

		float[] blerPerSINR				= new float[nrSinrVals];
		float[] riPerCqi				= new float[nrSinrVals];
		
		float[] dlAvgCqiPerMcs1			= new float[nrUlMcsVals];
		float[] ulAvgSinrPerMcs1		= new float[nrDlMcsVals];
		double[] test					= new double[100];
		double[] test2					= new double[100];
		double[] time					= new double[rows];
		double[] realThroughputPerMs	= new double[rows];
		int smallestXValue;

		//Print.array(BasicCalc.intArr2DoubleArr(dlTbs1));
		dlAvgTotBpsPerCqi = BasicCalc.addArrays(dlAvgBps1PerCqi, dlAvgBps2PerCqi);
		//Print.array(dlTbs1);
		smallestXValue = (int) basicCalc.getSmallest(timeStampInDigits);

		//Print.array(timeStamp);
		System.out.println(smallestXValue);
		//test = calculate.avgYValPerXVal(timeStamp, BasicCalc.intArr2DoubleArr(dlTbs1),1,smallestXValue);
		
		realThroughputPerMs = BasicCalc.tbs2throughput(dlTbs1,ndf1,timeStampInDigits,sf);
		//Print.array(realThroughputPerMs);
		
		test = calculate.avgYValPerXVal(timeStampInDigits, realThroughputPerMs,1,smallestXValue);
		Print.array(test);
	
		
		//Plot.normal(test2, "test2", "Xaxis", "Yaxis", "text");
		Plot.normal(test, "test", "Xaxis", "Yaxis", "text");
		//Plot.normal2(timeStampInDigits, realThroughputPerMs, "erg", "we", "wer", "r");
//		test = calculate.avgYValPerXVal(timeStamp, BasicCalc.intArr2DoubleArr(dlTbs1),1,smallestXValue);
//		Plot.normal(test, "test", "Xaxis", "Yaxis", "text");

	}

//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

	
	public static int[] interpretLTEdata(String[] lteData){
		int[] out = new int[lteData.length];
		int hej = 0;
		for(int i=0;i<lteData.length;i++){
			if (lteData[i].contains("64QAM")){
				out[i] = 6;
			} else if (lteData[i].contains("16QAM")){
				out[i] = 4;
			} else if (lteData[i].contains("QPSK")){
				out[i] = 2;
			} else if (lteData[i].contains("SIB") || lteData[i].contains("*")){
				//System.out.println(lteData[i]);
				hej++;
				out[i] = -1; 
			} else if (lteData[i].contains("%") ){
				out[i]= Integer.parseInt(lteData[i].replace("%", "").trim());
			}
				
			else {
				try{
					out[i] = Integer.parseInt(lteData[i].trim()); 
					
				} catch(Exception floatError) {
					try{
						out[i] = (int) Float.parseFloat(lteData[i].trim());
					} catch(Exception notAnumberError) {
						out[i] = NOT_A_VALUE;
					}
				}
			}
		}
		return out;
	}

}
