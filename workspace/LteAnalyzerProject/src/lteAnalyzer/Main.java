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

		//String[][] temp = new String[31][1000];
		
		int nrSinrVals = 45;
		int nrDlMcsVals = 28;
		int nrUlMcsVals = 23;
		System.out.println(rows + " " + cols);
		
		int[] dLtbssum 		= new int[rows]; 		//the sum of the Tbs's
		int[] uLtbssum 		= new int[rows]; 		//the sum of the Tbs's
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


		
		


		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		
		//l�ser cvs file
		String[][] lteDataMatrix = new String[rows][cols];
		String[] header = new String[cols];
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/burk.csv"));
		while ((line = reader.readLine()) != null) {
			if(i<rows){
				lteDataMatrix[i] = line.split(",");
				i++;
			} else break;
		}
		reader.close();
		header = lteDataMatrix[0];
		Print.array(header);
		//System.out.println("kommer hit");
		lteDataMatrix = BasicCalc.transpose(lteDataMatrix);
		
		//indexes
		int SIBIndex 		= BasicCalc.findHeaderIndex(header, "bbUeRef",0);
		int uLSINRIndex 	= BasicCalc.findHeaderIndex(header, "sinr",0);//hos razmus puschSinr
		int cqiIndex 		= BasicCalc.findHeaderIndex(header, "cqi",0);
		int dLtbssumIndex 	= BasicCalc.findHeaderIndex(header, "tbs1",0);//hos razmus tbssum
		int dLprbIndex 		= BasicCalc.findHeaderIndex(header, "prb",0);
		int uLprbIndex 		= BasicCalc.findHeaderIndex(header, "prb",1);
		int uLtbsIndex 		= BasicCalc.findHeaderIndex(header, "tbs",2);
		int riIndex			= BasicCalc.findHeaderIndex(header, "ri",0);
		int blerIndex 		= BasicCalc.findHeaderIndex(header, "bler",0);
		
		int ulMcsIndex 		= BasicCalc.findHeaderIndex(header, "mcs",2);
		int dlMcs1Index 	= BasicCalc.findHeaderIndex(header, "mcs1",0);
		int dlMcs2Index 	= BasicCalc.findHeaderIndex(header, "mcs2",0);
		

//		Print.array(header);
//		Print.array(lteDataMatrix[0]);
		System.out.println(dLtbssumIndex);
		System.out.println(SIBIndex);

		
		//Print.array(lteDataMatrix[SIBIndex]);
		dLtbssum 	= interpretLTEdata(lteDataMatrix[dLtbssumIndex]);
		uLtbssum	= interpretLTEdata(lteDataMatrix[uLtbsIndex]);
		SIB			= interpretLTEdata(lteDataMatrix[SIBIndex]);
		SINR 		= interpretLTEdata(lteDataMatrix[uLSINRIndex]);
		dlPrb  		= interpretLTEdata(lteDataMatrix[dLprbIndex]);
		ulPrb  		= interpretLTEdata(lteDataMatrix[uLprbIndex]);
		cqi   		= interpretLTEdata(lteDataMatrix[cqiIndex]);
		ri			= interpretLTEdata(lteDataMatrix[riIndex]);
		bler		= interpretLTEdata(lteDataMatrix[blerIndex]);
		
		ulMcs		= interpretLTEdata(lteDataMatrix[ulMcsIndex]);
		dlMcs1		= interpretLTEdata(lteDataMatrix[dlMcs1Index]);
		dlMcs2		= interpretLTEdata(lteDataMatrix[dlMcs2Index]);
		dlMcsSum	= BasicCalc.addArrays(dlMcs1, dlMcs2);
		
		
		/*The data to be plotted, first row is bitsperSINR, second is bitsperSINRperhz
		 * it will go from -15 to 30 dB*/
		float[] dlAvgBpsPerCqi	 		= new float[nrSinrVals]; 
		float[] ulAvgBpsPerSINR			= new float[nrSinrVals];
		float[] dlMaxBpsPerCqi	 		= new float[nrSinrVals];
		float[] ulMaxBpsPerSINR	 		= new float[nrSinrVals];
		
		float[] dlPrbPerCqi				= new float[nrSinrVals];
		float[] ulPrbPerSINR			= new float[nrSinrVals];
		
		float[] dlAvgSpecEffPerCqi		= new float[nrSinrVals]; 
		float[] ulAvgSpecEffPerSINR		= new float[nrSinrVals];

		float[] blerPerSINR				= new float[nrSinrVals];
		float[] riPerCqi				= new float[nrSinrVals];
		
		float[] dlAvgCqiPerMcs1			= new float[nrUlMcsVals];
		float[] ulAvgSinrPerMcs			= new float[nrDlMcsVals];
		
		
		
		dlAvgBpsPerCqi = BasicCalc.tbs2Mbps(DLCalc.avgValPerCqi(dLtbssum, cqi, SIB,300)); 
		dlMaxBpsPerCqi = BasicCalc.tbs2Mbps(DLCalc.maxValPerCqi(dLtbssum, cqi, SIB));
		ulAvgBpsPerSINR = BasicCalc.tbs2Mbps(ULCalc.avgValPerSINR(uLtbssum, SINR, SIB,100)); 
		ulMaxBpsPerSINR = BasicCalc.tbs2Mbps(ULCalc.maxValPerSINR(uLtbssum, SINR, SIB));
		
		dlPrbPerCqi = DLCalc.avgValPerCqi(dlPrb, cqi, SIB,300);
		ulPrbPerSINR = ULCalc.avgValPerSINR(ulPrb, SINR, SIB,100);
		
		dlAvgSpecEffPerCqi = BasicCalc.spectralEfficiencyPerSINR(BasicCalc.prefixChanger(dlAvgBpsPerCqi, 1000000), BasicCalc.prb2hz(dlPrbPerCqi));
		ulAvgSpecEffPerSINR = BasicCalc.spectralEfficiencyPerSINR(BasicCalc.prefixChanger(ulAvgBpsPerSINR,1000000), BasicCalc.prb2hz(ulPrbPerSINR));
		
		blerPerSINR = DLCalc.avgValPerCqi(bler, cqi, SIB,30);
		riPerCqi = DLCalc.avgValPerCqi(ri, cqi, SIB,1);

		dlAvgCqiPerMcs1 = DLCalc.avgCqiPerMcs(dlMcs1, cqi , SIB);
		ulAvgSinrPerMcs = ULCalc.avgSINRPerMcs(ulMcs, SINR , SIB);
		
		
		

		
		
		Plot.normal(
				dlPrbPerCqi,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		Plot.normal(
				ulPrbPerSINR,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		
		Plot.normal(
				dlAvgSpecEffPerCqi,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		Plot.normal(
				ulAvgSpecEffPerSINR,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");

		
		Plot.normal(
				blerPerSINR,
				"Throughput, [Mbits/s]/Cqi",
				"Cqi",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		Plot.normal(
				riPerCqi,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		Plot.normal(
				dlAvgCqiPerMcs1,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");
		Plot.normal(
				ulAvgSinrPerMcs,
				"UL Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]",
				"inte logaritmik");


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
				
			//Harq dl ack and nack bits.
			} else if (lteData[i].contains("A A") ){
				out[i] = 2;
			} else if (lteData[i].contains("A N") ){
				out[i] = 1;
			} else if (lteData[i].contains("N A") ){
				out[i] = 1;
			} else if (lteData[i].contains("N N") ){
				out[i] = 0;
			
			//Harq ul ack and nack bits.
			} else if (lteData[i].contains("ACK") ){
				out[i] = 1;
			
			} else if (lteData[i].contains("NACK") ){
				out[i] = 0;
			} else if (lteData[i].contains("%") ){
				//bl�����rk
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
