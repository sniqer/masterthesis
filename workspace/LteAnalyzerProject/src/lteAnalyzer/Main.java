package lteAnalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Main {

	
	@SuppressWarnings("resource")
	public static void main(String[] arg) throws IOException{
		String[][] lteDataMatrix = new String[1000][31];
		String[][] temp = new String[31][1000];
		int bitsPerSINR = 0;
		int bitsPerSINRPerHz = 1;
		int[] tbs1 = new int[1000];
		int[] tbs2 = new int[1000];
		int[] tbssum = new int[1000];
		int[] prb = new int[1000];
		float[] pucch = new float[1000];
		int[] sinr = new int[1000];
		float[][] MbitsperSINR = new float[2][30];
		
		
		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		
		//l�ser cvs file
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/output.csv"));
		while ((line = reader.readLine()) != null) {
			if(i<1000){
				lteDataMatrix[i] = line.split(",");
				i++;
			}
		}
		temp = transpose(lteDataMatrix);
		
		pucch = convStringArr2FloatArr(temp[19]);
		sinr = convStringArr2IntArr(temp[21]);
		tbs1 = convStringArr2IntArr(temp[13]);
		tbs2 = convStringArr2IntArr(temp[14]);
		prb  = convStringArr2IntArr(temp[10]);
		
		tbssum = addtbs1tbs2(tbs1,tbs2);
		removepucchsinr(sinr,pucch);
		MbitsperSINR = extractDataratePerSinr(tbssum,sinr,prb);
		//printArray(outputdata);
		//fibonacciLevel(3005);
		plot(
				MbitsperSINR[bitsPerSINR],
				"Throughput/SINR",
				"SINR [dB]",
				"Throughput, [Mbits/s]");
		plot(
				MbitsperSINR[bitsPerSINRPerHz],
				"Throughput/SINR/BW", "SINR [dB]",
				"Throughput, [Mbits/s/BW]");
	}

//------------------------------------------------------------------------------------------------
	public static int[] convStringArr2IntArr(String[] stringArr){
		int[] out = new int[stringArr.length];
		for(int i=0;i<stringArr.length;i++){
			try{
				out[i] = Integer.parseInt(stringArr[i].trim()); // No more Exception in this line
			} catch(NumberFormatException e) {
				out[i] = 0;
			}
		}
		return out;
	}
	public static float[] convStringArr2FloatArr(String[] stringArr){
		float[] out = new float[stringArr.length];
		for(int i=0;i<stringArr.length;i++){
			try{
				out[i] = Float.parseFloat(stringArr[i].trim()); // No more Exception in this line
				//System.out.println(out[i]);
			} catch(NumberFormatException e) {
				out[i] = 0;
				//System.out.print("feeeel");
				
			}
		}
		return out;
	}

	public static void printArray(String[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	public static void printArray(float[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	public static void printArray(int[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	
	public static void print2dArray(String[][] stringArr){
		for(int i=0;i<stringArr.length;i++){
			for(int j=0;j<stringArr[1].length;j++){
				System.out.print(stringArr[i][j] + " "); 
			}
			System.out.println();
		}
	}
	public static void print2dArray(int[][] intArr){
		for(int i=0;i<intArr.length;i++){
			for(int j=0;j<intArr[1].length;j++){
				System.out.print(intArr[i][j] + " "); 
			}
			System.out.println();
		}
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
	
	public static void removepucchsinr(int[] sinr,float[] pucch){
		
		for (int i=0;i<sinr.length;i++){
			if (pucch[i] != 0.0){
				sinr[i] = 0;
			}
		}
	}
	
	public static float[][] extractDataratePerSinr(int[] tbs,int[] sinr,int prb[]){
		int bitsPerSINR = 0;
		int bitsPerSINRPerHz = 1;
		int bandWidth = 0;
		int tbscol = 0;
		int sinrcol = 1;
		
		
		int count = 0;
		int currentsinr=0;
		int currenttbs = 0;
		int[][] tbssinr = new int [30][2]; //counter och tbs l�ggs i col1 resp 2. index representerar sinr
		//first row we have MBits/sinr, second row we have bits/sinr/Hertz
		float[][]	tbspersinr = new float [2][30];
		
		for(int i=0;i<sinr.length;i++){
			if(tbs[i] != 29296+29296 && tbs[i] != 120 && tbs[i] != 0){
				currenttbs=currenttbs+tbs[i];
			}
			
			//weve found a transportblock increase counter
			if(tbs[i] != 0 && tbs[i] != 120 && tbs[i] != 29296+29296){//�ndrasen!!!
				count=count+1;
			}
			
			//spara medelv�rdet i tbs n�r vi hittar ny sinr
			if (currentsinr != sinr[i] ){
				tbssinr[sinr[i]][0] = tbssinr[sinr[i]][0]+count; //conter
				tbssinr[sinr[i]][1] = tbssinr[sinr[i]][1]+currenttbs; //tbs
				currentsinr = sinr[i];
				count = 0;
				currenttbs = 0;
			}
		}
		
		//tar fram medelv�rdet i tbs f�r varje sinr (1-30)
		for (int j=0;j<tbssinr.length;j++){
			if(tbssinr[j][0] != 0){
				System.out.println(j);
				tbspersinr[bitsPerSINR][j] = ((float)tbssinr[j][1]/tbssinr[j][0]/1000);
				bandWidth = map2Bandwidth(prb,j);
				tbspersinr[bitsPerSINRPerHz][j] = tbspersinr[bitsPerSINR][j]/bandWidth;
			}
		}
		return tbspersinr;
	}
	
	public static void fibonacciLevel(int xp){

		int fib1 = 1,fib2 = 0;
		int fibonacci = 0;
		int level = 1;
		
		while(xp >= fibonacci){
			fibonacci = fib1+fib2;
			fib2=fib1;
			fib1 = fibonacci;
			level++;
			System.out.println(level+" " + fibonacci);
		}
	}

	public static void plot(float[] outputdata,String header,String Xaxis,String Yaxis){
        XYSeries graph = new XYSeries(0);
        XYDataset xyDataset = new XYSeriesCollection(graph);
        for(int k=0;k<outputdata.length;k++){
        	graph.add(k,outputdata[k]); 
        }
        JFreeChart chart = ChartFactory.createXYLineChart(
                header, Xaxis, Yaxis,
                xyDataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame graphFrame = new ChartFrame("XYLine Chart", chart);
        graphFrame.setVisible(true);
        graphFrame.setSize(1000, 700); 
	}
	
	public static int[] addtbs1tbs2(int[] tbs1,int[] tbs2){
		int[] tbssum = new int[tbs1.length];
		for(int i=0;i<tbs1.length;i++){
			if(tbs1[i] != 0&&tbs1[i] != 0){
				tbssum[i]=tbs1[i]+tbs2[i];
			}
			
		}
		return tbssum;
	}

	public static int map2Bandwidth(int[] prb,int list_index){
		int bandWidth = 0;
		int inc = list_index;
		int dec = list_index;
		
		while(true){
			
				if(prb[dec] != 0){
					bandWidth = prb[dec];
				}
				if(prb[dec] != 0){
					bandWidth = prb[dec]*180*1000;
					break;
				} else if (prb[inc] != 0){
					bandWidth = prb[inc]*180*1000;
					break;
				}
				if(dec-1 >= 0){
					dec--;
				}
				if(inc<prb.length){
					inc++;
				}
		}
		return bandWidth;
		
	}

}
