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
		int size = 300;
		String[][] lteDataMatrix = new String[size][22];
		String[] header = new String[22];
		//String[][] temp = new String[31][1000];
		
		int nrSinrVals = 45;
		
		int[] dLtbs1 		= new int[size];		//transport block size antenna 1
		int[] dLtbs2 		= new int[size];		//transport block size antenna 2
		int[] dLtbssum 		= new int[size]; 		//the sum of the Tbs's
		int[] dlPrb 		= new int[size];		//the physical resource blocks
		int[] uLtbs 		= new int[size];		//transport block size antenna
		int[] SIB			= new int[size];		//System Information Block, this block we dont want to look at at the moment, delete the data in the sib rows
		int[] cqi			= new int[size];		//Channel Quality Index
		int[] ri			= new int[size];
		int[] HARQ			= new int[size];		//HARQ ack and nacks

		int[] uLtbssum 		= new int[size]; 		//the sum of the Tbs's
		int[] ulPrb 		= new int[size];		//the physical resource blocks
		int[] pucch 		= new int[size];		//the physical control channel
		int[] pusch 		= new int[size];		//the physical shared channel
		
		int[] dlSINR 		= new int[size];		//the signal-to-interference-ratio in the channel, over pusch (i think)
		int[] ulSINR 		= new int[size];		//the signal-to-interference-ratio in the channel, over pucch (i think)
		int[] frameNr 		= new int[size];		//frame number
		
		int[] subFrame 		= new int[size];		//subframe number
		

		
		
		/*The data to be plotted, first row is bitsperSINR, second is bitsperSINRperhz
		 * it will go from -15 to 30 dB*/
		float[] dlAvgBpsPerSINR	 		= new float[nrSinrVals]; 
		float[] dlMaxBpsPerSINR	 		= new float[nrSinrVals];
		float[] dlAvgBpsPerHzPerSINR	= new float[nrSinrVals]; 
		float[] dlMaxBpsPerHzPerSINR	= new float[nrSinrVals];
		float[] cqiPerSINR				= new float[nrSinrVals];
		float[] riPerSINR				= new float[nrSinrVals];
		float[] dlPrbPerSINR			= new float[nrSinrVals];
		float[] ulPrbPerSINR			= new float[nrSinrVals];
		float[] blerPerSINR				= new float[nrSinrVals];
		float[] ulAvgBpsPerSINR			= new float[nrSinrVals];
		
		

		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		
		//läser cvs file
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/output666.csv"));
		while ((line = reader.readLine()) != null) {
			if(i<size){
				lteDataMatrix[i] = line.split(",");
				i++;
			}
		}
		header = lteDataMatrix[0];
		lteDataMatrix = transpose(lteDataMatrix);
		
		//indexes
		int SIBIndex 		= findIndex(header, "bbUeRef",0);
		int dLSINRIndex 	= findIndex(header, "puschSinr",0);
		int uLSINRIndex 	= findIndex(header, "pucchSinr",0);
		int dLtbssumIndex 	= findIndex(header, "tbsSum",0);
		int dLprbIndex 		= findIndex(header, "prb",0);
		int uLprbIndex 		= findIndex(header, "prb",1);
		int uLtbsIndex 		= findIndex(header, "tbs",0);
		int cqiIndex 		= findIndex(header, "cqi",0);
		int riIndex			= findIndex(header, "ri",0);
		int HARQIndex 		= findIndex(header, "harq",0);
		System.out.println(uLprbIndex);
//		Print.array(header);
//		Print.array(lteDataMatrix[0]);


		
		//Print.array(lteDataMatrix[SIBIndex]);
		SIB			= interpretLTEdata(lteDataMatrix[SIBIndex]);
		dlSINR 		= interpretLTEdata(lteDataMatrix[dLSINRIndex]);
		ulSINR 		= interpretLTEdata(lteDataMatrix[uLSINRIndex]);
		HARQ		= interpretLTEdata(lteDataMatrix[HARQIndex]);
		dlPrb  		= interpretLTEdata(lteDataMatrix[dLprbIndex]);
		dLtbssum 	= interpretLTEdata(lteDataMatrix[dLtbssumIndex]);
		ulPrb  		= interpretLTEdata(lteDataMatrix[uLprbIndex]);
		uLtbs 		= interpretLTEdata(lteDataMatrix[uLtbsIndex]);
		cqi   		= interpretLTEdata(lteDataMatrix[cqiIndex]);
		ri			= interpretLTEdata(lteDataMatrix[riIndex]);
		
		//dlAvgBpsPerSINR = Calculate.avgBpsPerSINR(dLtbssum, dlSINR, SIB);
		ulAvgBpsPerSINR = Calculate.avgBpsPerSINR(uLtbssum, ulSINR, SIB);
		//dlMaxBpsPerSINR = Calculate.maxBpsPerSINR(dLtbssum, dlSINR, SIB);
		//dlAvgBpsPerHzPerSINR = Calculate.avgBpsPerHzPerSINR(dLtbssum,dlPrb, dlSINR, SIB);
		//dlMaxBpsPerHzPerSINR = Calculate.maxBpsPerHzPerSINR(dLtbssum,dlPrb, dlSINR, SIB);
		//cqiPerSINR = Calculate.cqiPerSINR(cqi, dlSINR);
		//riPerSINR = Calculate.riPerSINR(ri, dlSINR);
		//blerPerSINR	= Calculate.blerPerSINR(HARQ, dlSINR);
		//ulPrbPerSINR = Calculate.avgPrbPerSINR(ulPrb, ulSINR, SIB);
		//dlPrbPerSINR = Calculate.avgPrbPerSINR(dlPrb, dlSINR, SIB);
		//Print.array(ulSINR);

		
//		uLMbitsperSINR[0] = uplinkData[0];
//		uLMbitsperSINR[1] = uplinkData[2];
//		
//		uLMbitsperHzperSINR[0] = uplinkData[1];
//		uLMbitsperHzperSINR[1] = uplinkData[3];


//		logPlot(
//				dLMbitsperSINR,
//				"DL Throughput/SINR",
//				"SINR [dB]",
//				"Throughput, [Mbits/s]");
//		logPlot(
//				dLMbitsperHzperSINR,
//				"DL Throughput/SINR/BW", 
//				"SINR [dB]",
//				"Spectral efficiency, [bits/s/Hz]");
		
//		logPlot(
//				uLMbitsperSINR,
//				"UL Throughput/SINR",
//				"SINR [dB]",
//				"Throughput, [Mbits/s]");
//		logPlot(
//				dLMbitsperHzperSINR,
//				"UL Throughput/SINR/BW", 
//				"SINR [dB]",
//				"Spectral efficiency, [bits/s/Hz]");
//		
		//printArray(chCap);

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
			
			} else if (lteData[i].contains("NAK") ){
				out[i] = 0;
			}
				
			else {
				try{
					out[i] = Integer.parseInt(lteData[i].trim()); // parse the string in ltedata[i] to an integer
				} catch(Exception e) {
					out[i] = -1337;
				}
			}
		}
		return out;
	}


	
	


	
	public static String[][] transpose(String[][] stringArr){
		String[][] output = new String[stringArr[1].length][stringArr.length];
		for(int i=0;i<stringArr.length;i++){
			for(int j=0;j<stringArr[1].length;j++){
				output[j][i] = stringArr[i][j];
			}
		}
		return output;
	}
	
	//this function removes data that we dont want to look at from that index in correctvals
	public static void removeTrashData(int[] correctVals,int[] errorVals){
		
		for (int i=0;i<correctVals.length;i++){
			if (errorVals[i] != 0.0){
				correctVals[i] = 0;
			}
		}
	}
	
	


	

	

	public static void logPlot(float[][] outputdata,String header,String Xaxis,String Yaxis){

		XYSeries avgVal 	= new XYSeries("average");
		XYSeries peakVal 	= new XYSeries("peak");
		
		XYDataset xyDataset = new XYSeriesCollection();
		((XYSeriesCollection) xyDataset).addSeries(avgVal);
		((XYSeriesCollection) xyDataset).addSeries(peakVal);

		//print2dArray(outputdata);
        for(int k=0;k<outputdata[0].length;k++){
        	if(outputdata[0][k] != 0){
        		avgVal.add(k-15,outputdata[0][k]); //average data values is on row 0
        	}        	
        	if(outputdata[1][k] != 0){
        		peakVal.add(k-15,outputdata[1][k]); //average data values is on row 1
        	}
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                header, 
                Xaxis, 
                Yaxis,
                xyDataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        final XYPlot plot = chart.getXYPlot();
        ChartFrame graphFrame = new ChartFrame("XYLine Chart", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(700, 500); 
        final NumberAxis domainAxis = new NumberAxis(Xaxis);
        final NumberAxis rangeAxis = new LogarithmicAxis(Yaxis);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
	}


	
	public static float[] maxKbps(int bandwidth,int fromSINR,int toSINR){
		float[] maximum = new float[45];
		for(int i=fromSINR;i<toSINR;i++){
			maximum[i-fromSINR] =  (float) (bandwidth*(Math.log(Math.pow(10, (float) i/10)+1)/0.6931));
		}
		return maximum;
	}
	public static float[] maxKbpspHz(int fromSINR,int toSINR){
		float[] maxbpspHz = new float[45];
		for(int i=fromSINR;i<toSINR;i++){
			maxbpspHz[i-fromSINR] =  (float) (Math.log(Math.pow(10, (float) i/10)+1)/0.6931);
		}
		return maxbpspHz;
	}


	
	public static int findIndex(String[] header, String headerName,int doublett){
		int doublettcounter = 0;
		for(int i=0;i<header.length;i++){
			
			if(header[i].contains(headerName)){
				
				if (doublett==doublettcounter){
					return i;
				} else {
					System.out.println("koomer hit");
					doublettcounter++;
				}
			}
		}
		return -1;
	}
}
