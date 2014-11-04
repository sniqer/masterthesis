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
	
	public static void main(String[] arg) throws IOException{
		
		
		
		String dummyline = null;
		BufferedReader dummyreader = null;
		int rows = 0;
		int cols = 0;
		
		//l�ser cvs file
		dummyreader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/burk.csv"));
		cols = dummyreader.readLine().split(",").length;
		
		while ((dummyline = dummyreader.readLine()) != null) {
			rows++;
		}
		dummyreader.close();

		int nrSinrVals = 45;
		int nrDlMcsVals = 28;
		int nrUlMcsVals = 23;
		System.out.println(rows + " " + cols);
		
		int[] dlTbsSum 		= new int[rows]; 		//the sum of the Tbs's
		int[] ulTbsSum 		= new int[rows]; 		//the sum of the Tbs's
		int[] dlTbs1		= new int[rows];		//left antenna tbs
		int[] dlTbs2		= new int[rows];		//left antenna tbs
		int[] dlPrb 		= new int[rows];		//the physical resource blocks
		int[] ulPrb 		= new int[rows];		//the physical resource blocks
		int[] SINR	 		= new int[rows];		//the signal-to-interference-ratio in the channel, over pucch (i think)
		
		int[] ulMcs			= new int[rows];		//b�rk
		int[] dlMcs1		= new int[rows];		//b�rk
		int[] dlMcs2		= new int[rows];		//b�rk
		int[] dlMcsSum		= new int[rows];		//b�rk
		
		int[] SIB			= new int[rows];		//System Information Block, this block we dont want to look at at the moment, delete the data in the sib rows
		int[] cqi			= new int[rows];		//Channel Quality Index
		int[] ri			= new int[rows];
		int[] HARQ			= new int[rows];		//HARQ ack and nacks
		int[] bler			= new int[rows];
		double[] timeStamp	= new double[rows];		//time stamps
		String[] ndf1		= new String[rows];
		String[] ndf2		= new String[rows];


		
		


		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		
		//l�ser cvs file
		String[][] lteDataMatrix = new String[rows][cols];
		String[] header  = new String[cols];
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/burk.csv"));
		while ((line = reader.readLine()) != null) {
			if(i<rows){
				lteDataMatrix[i] = line.split(",");
				i++;
			} else break;
		}
		reader.close();
		header = lteDataMatrix[0];
		lteDataMatrix = BasicCalc.transpose(lteDataMatrix);
		
		//setting variable in BasicCalc class
		BasicCalc basicCalc = new BasicCalc();
		basicCalc.setHeader(header);
		

		
		
		int SIBIndex 		= BasicCalc.findHeaderIndex("bbUeRef",0);
		int SINRIndex 		= BasicCalc.findHeaderIndex("sinr",0);//hos razmus puschSinr
		int cqiIndex 		= BasicCalc.findHeaderIndex("cqi",0);
		int dlTbs1Index 	= BasicCalc.findHeaderIndex("tbs1",0);
		int dlTbs2Index 	= BasicCalc.findHeaderIndex("tbs2",0);
		//int dlTbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2);
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
		


		
		//Print.array(lteDataMatrix[SIBIndex]);
		dlTbs1 		= interpretLTEdata(lteDataMatrix[dlTbs1Index]);
		dlTbs2	 	= interpretLTEdata(lteDataMatrix[dlTbs2Index]);
		//dlTbsSum 	= interpretLTEdata(lteDataMatrix[dlTbsSumIndex]);
		dlTbsSum 	= BasicCalc.addArrays(dlTbs1, dlTbs2);
		ulTbsSum	= interpretLTEdata(lteDataMatrix[ulTbsSumIndex]);
		SIB			= interpretLTEdata(lteDataMatrix[SIBIndex]);
		SINR 		= interpretLTEdata(lteDataMatrix[SINRIndex]);
		dlPrb  		= interpretLTEdata(lteDataMatrix[dLprbIndex]);
		ulPrb  		= interpretLTEdata(lteDataMatrix[uLprbIndex]);
		cqi   		= interpretLTEdata(lteDataMatrix[cqiIndex]);
		ri			= interpretLTEdata(lteDataMatrix[riIndex]);
		bler		= interpretLTEdata(lteDataMatrix[blerIndex]);
		
		ulMcs		= interpretLTEdata(lteDataMatrix[ulMcsIndex]);
		dlMcs1		= interpretLTEdata(lteDataMatrix[dlMcs1Index]);
		dlMcs2		= interpretLTEdata(lteDataMatrix[dlMcs2Index]);
		dlMcsSum	= BasicCalc.addArrays(dlMcs1, dlMcs2);
		
		ndf1 = lteDataMatrix[ndf1Index];
		ndf2 = lteDataMatrix[ndf2Index];
		
		timeStamp	= BasicCalc.timeConverter(lteDataMatrix[timeStampIndex]);
		//Print.array(timeStamp,30);
		
		//setting variable in DLCalc class
		DLCalc dlCalc = new DLCalc();
		dlCalc.setCqi(cqi);
		dlCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		ULCalc ulCalc = new ULCalc();
		ulCalc.setCqi(cqi);
		ulCalc.setSIB(SIB);
		
		
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
		
		
		
		
		dlAvgBps1PerCqi = BasicCalc.tbs2Mbps(
				dlCalc.avgValPerCqi(
						BasicCalc.tbs2throughput(
								dlTbs1,ndf1)
						,50));
		
		dlAvgBps2PerCqi = BasicCalc.tbs2Mbps(
				dlCalc.avgValPerCqi(
						BasicCalc.tbs2throughput(
								dlTbs2,ndf2)
						,50));
		
		dlAvgTotBpsPerCqi = BasicCalc.addArrays(dlAvgBps1PerCqi, dlAvgBps2PerCqi);
		dlMaxBpsPerCqi = BasicCalc.tbs2Mbps(dlCalc.maxValPerCqi(dlTbsSum));
		ulAvgBpsPerSINR = ulCalc.avgValPerSINR(ulTbsSum,100);
		ulMaxBpsPerSINR = BasicCalc.tbs2Mbps(ulCalc.maxValPerSINR(ulTbsSum));
		
		dlAvgBpsPerSec = Calculate.avgTbsPerSecond(dlTbsSum, timeStamp, SIB, 500);
		ulAvgBpsPerSec = Calculate.avgTbsPerSecond(ulTbsSum, timeStamp, SIB, 500);
		
		dlPrbPerCqi = dlCalc.avgValPerCqi(dlPrb,300);
		ulPrbPerSINR = ulCalc.avgValPerSINR(ulPrb,100);
		
		dlAvgSpecEffPerCqi = BasicCalc.spectralEfficiencyPerSINR(BasicCalc.prefixChanger(dlAvgTotBpsPerCqi, 1000000), BasicCalc.prb2hz(dlPrbPerCqi));
		ulAvgSpecEffPerSINR = BasicCalc.spectralEfficiencyPerSINR(BasicCalc.prefixChanger(ulAvgBpsPerSINR,1000000), BasicCalc.prb2hz(ulPrbPerSINR));
		
		blerPerSINR = dlCalc.avgValPerCqi(bler,30);
		riPerCqi = dlCalc.avgValPerCqi(ri,1);

		dlAvgCqiPerMcs1 = dlCalc.avgValPerMcs(cqi,dlMcs1);
		//ulAvgSinrPerMcs = ULCalc.avgSINRPerMcs(ulMcs, SINR , SIB);
		
		//Vad fattas, dlbler1PerCqi, dlbler2PerCqi, ulBlerPerSINR, dlAvgCqiPerMcs2, ulAvgSinrPerMcs, pmiPerCQI / pmiPerSINR
		
		/*PLOTTING SECTION*/
		Plot.overTime(dlAvgBpsPerSec[0], dlAvgBpsPerSec[1], "dlAvgBpsPerSec", "Xaxis", "Yaxis", "text");
		Plot.normal(dlAvgBps1PerCqi, "dlAvgBps1PerCqi", "Xaxis", "Yaxis", "text");
		Plot.normal(dlAvgBps2PerCqi, "dlAvgBps2PerCqi", "Xaxis", "Yaxis", "text");
		Plot.normal(dlAvgTotBpsPerCqi, "dlAvgTotBpsPerCqi", "Xaxis", "Yaxis", "text");
		Plot.normal(dlMaxBpsPerCqi, "dlMaxBpsPerCqi", "Xaxis", "Yaxis", "text");
		

		Plot.overTime(ulAvgBpsPerSec[0], ulAvgBpsPerSec[1], "ulAvgBpsPerSec", "Xaxis", "Yaxis", "text");
		Plot.normal(ulAvgBpsPerSINR, "ulAvgBpsPerSINR", "Xaxis", "Yaxis", "text");
		Plot.normal(ulMaxBpsPerSINR, "dlMaxBpsPerCqi", "Xaxis", "Yaxis", "text");
		
//		
//		Plot.normal(dlPrbPerCqi, "dlPrbPerCqi", "Xaxis", "Yaxis", "text");
//		Plot.normal(ulPrbPerSINR, "ulPrbPerSINR", "Xaxis", "Yaxis", "text");
//		Plot.normal(dlAvgSpecEffPerCqi, "dlAvgSpecEffPerCqi", "Xaxis", "Yaxis", "text");
//		Plot.normal(ulAvgSpecEffPerSINR, "ulAvgSpecEffPerSINR", "Xaxis", "Yaxis", "text");
//		Plot.normal(blerPerSINR, "blerPerSINR", "Xaxis", "Yaxis", "text");
//		Plot.normal(riPerCqi, "riPerCqi", "Xaxis", "Yaxis", "text");
//		Plot.normal(dlAvgCqiPerMcs1, "dlAvgCqiPerMcs1", "Xaxis", "Yaxis", "text");



		
//		timePerSecond = Calculate.avgTbsPerSecond(dLtbssum, timeStamp, SIB, 50);
//		Plot.overTime(timePerSecond[0],timePerSecond[1], "header", "Xaxis", "Yaxis", "text");
	//printArray(sinr);

	}

//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

	
	public static int[] interpretLTEdata(String[] lteData){
		int[] out = new int[lteData.length];
		for(int i=0;i<lteData.length;i++){
			
			if (lteData[i].contains("64QAM")){
				out[i] = 64;
			} else if (lteData[i].contains("16QAM")){
				out[i] = 16;
			} else if (lteData[i].contains("QPSK")){
				out[i] = 4;
			} else if (lteData[i].contains("SIB") ){
				out[i] = -1; 
				
			} else if (lteData[i].contains("Y") ){
				out[i] = 1;
			} else if (lteData[i].contains("N  ") ){
				out[i] = 0;
			} else if (lteData[i].contains("%") ){
				out[i]= Integer.parseInt(lteData[i].replace("%", "").trim());
			}
				
			else {
				try{
					out[i] = Integer.parseInt(lteData[i].trim()); // parse the string in ltedata[i] to an integer
				} catch(Exception floatError) {
					try{
						out[i] = (int) Float.parseFloat(lteData[i].trim());
					} catch(Exception notAnumberError) {
						out[i] = -1337;
					}
				}
			}
		}
		return out;
	}

}
