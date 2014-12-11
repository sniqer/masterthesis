package lteAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


public class Main {
	@SuppressWarnings("resource")
	static final int NOT_A_VALUE = -1337; //empty spaces in a vector is represented by this value
	public static void main(String[] arg) throws IOException{
		
		
		
		String dummyline = null;
		BufferedReader dummyreader = null;
		int rows = 0;
		int cols = 0;
		
		//l�ser cvs file
		dummyreader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/bler10.csv"));
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
		
		Path path = FileSystems.getDefault().getPath("C:/Users/epauned/documents/logtool", "bler10.csv");
		try (Stream<String> lines = Files.lines(path)) {
			  long numOfLines = lines.count();
			  System.out.println("rows2!!!: "+numOfLines);
			}


		
		


		
		String line = null;
		BufferedReader reader = null;
		int i = 0;
		System.out.println(cols);
		//l�ser cvs file
		String[][] lteDataMatrix = new String[rows][cols];
		String[] header  = new String[cols];
		reader = new BufferedReader(new FileReader("C:/Users/epauned/documents/logtool/bler10.csv"));
		while ((line = reader.readLine()) != null) {
			if(i == 0){
				header = line.split(",");
				i++;
			}
			else if(i>0 && i<rows+1){ //avoid the header
				//System.out.println(i);
				lteDataMatrix[i-1] = line.split(",");
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
		int SINRIndex 		= BasicCalc.findHeaderIndex("puschSinr",0);//hos razmus puschSinr
		int cqiIndex 		= BasicCalc.findHeaderIndex("cqi",0);
		int dlTbs1Index 	= BasicCalc.findHeaderIndex("tbs1",0);
		int dlTbs2Index 	= BasicCalc.findHeaderIndex("tbs2",0);
		int dlTbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2);
		int ulTbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",3); //3 hos razmus
		int dLprbIndex 		= BasicCalc.findHeaderIndex("prb",0);
		int uLprbIndex 		= BasicCalc.findHeaderIndex("prb",1);
		int uLtbsSumIndex 	= BasicCalc.findHeaderIndex("tbs",2);
		int riIndex			= BasicCalc.findHeaderIndex("ri",0);
		int blerIndex 		= BasicCalc.findHeaderIndex("bler",0);
		int ulBlerIndex 		= BasicCalc.findHeaderIndex("bler",2);
		
		int ulMcsIndex 		= BasicCalc.findHeaderIndex("mcs",2);
		int dlMcs1Index 	= BasicCalc.findHeaderIndex("mcs1",0);
		int dlMcs2Index 	= BasicCalc.findHeaderIndex("mcs2",0);
		
		int timeStampIndex 	= BasicCalc.findHeaderIndex("timeStamp", 0);
		int ndf1Index		= BasicCalc.findHeaderIndex("ndf1", 0);
		int ndf2Index		= BasicCalc.findHeaderIndex("ndf2", 0);
		int ndfIndex		= BasicCalc.findHeaderIndex("ndf", 2);
		int pmiIndex		= BasicCalc.findHeaderIndex("pmi", 0);
		int sfIndex			= BasicCalc.findHeaderIndex("sf", 0);
		



		

		int[] dlTbs1 		= interpretLTEdata(lteDataMatrix[dlTbs1Index]);
		int[] dlTbs2	 	= interpretLTEdata(lteDataMatrix[dlTbs2Index]);
		//dlTbsSum 	= interpretLTEdata(lteDataMatrix[dlTbsSumIndex]);
		int[] dlTbsSum 	= BasicCalc.addArrays(dlTbs1, dlTbs2);
		int[] ulTbsSum	= interpretLTEdata(lteDataMatrix[ulTbsSumIndex]);
		int[] SIB			= interpretLTEdata(lteDataMatrix[SIBIndex]);
		int[] SINR 		= interpretLTEdata(lteDataMatrix[SINRIndex]);
//		int[] dlPrb  		= interpretLTEdata(lteDataMatrix[dLprbIndex]);
//		int[] ulPrb  		= interpretLTEdata(lteDataMatrix[uLprbIndex]);
//		int[] cqi   		= interpretLTEdata(lteDataMatrix[cqiIndex]);
//		int[] ri			= interpretLTEdata(lteDataMatrix[riIndex]);
		int[] bler		= interpretLTEdata(lteDataMatrix[ulBlerIndex]);
//		
//		int[] ulMcs		= interpretLTEdata(lteDataMatrix[ulMcsIndex]);
		int[] dlMcs1		= interpretLTEdata(lteDataMatrix[dlMcs1Index]);
		int[] dlMcs2		= interpretLTEdata(lteDataMatrix[dlMcs2Index]);
		
//		int[] sf	= interpretLTEdata(lteDataMatrix[sfIndex]);
		int[] dlMcsSum	= BasicCalc.addArrays(dlMcs1, dlMcs2);

		
		String[] ndf1 = lteDataMatrix[ndf1Index];
		String[] ndf2 = lteDataMatrix[ndf2Index];
		String[] ndf = lteDataMatrix[ndfIndex];
		
		String[] timeStamp	= lteDataMatrix[timeStampIndex];
		
		timeStamp	= BasicCalc.discardDate(timeStamp);
		
		double[] timeStampInDigits	= BasicCalc.startTimeFrZero(timeStamp);
		timeStampInDigits = basicCalc.multiplyArray(timeStampInDigits, 1000);
		basicCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		DLCalc dlCalc = new DLCalc();
		dlCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		ULCalc ulCalc = new ULCalc();
		ulCalc.setSIB(SIB);
		
		Calculate calculate = new Calculate();
		calculate.setSIB(SIB);
		basicCalc.removePotentialErrorsInSINR(SINR);
		int smallestXValue = (int) basicCalc.getSmallest(BasicCalc.intArr2DoubleArr(bler));
		System.out.println(smallestXValue);
		double[] realThroughputPerMs = BasicCalc.tbs2throughput(ulTbsSum,ndf,timeStampInDigits);
		
		double[] test  = calculate.avgYValPerXVal(timeStampInDigits,BasicCalc.intArr2DoubleArr(ulTbsSum),1,0);
	
		System.out.println(test.length);

		Plot.normal(test,smallestXValue, "test", "Xaxis", "Yaxis", "text");


	}

//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

	
	public static int[] interpretLTEdata(String[] lteData){
		int[] out = new int[lteData.length];
		for(int i=0;i<lteData.length;i++){
			if (lteData[i].contains("64QAM")){
				out[i] = 6;
			} else if (lteData[i].contains("16QAM")){
				out[i] = 4;
			} else if (lteData[i].contains("QPSK")){
				out[i] = 2;
			} else if (lteData[i].contains("SIB") || lteData[i].contains("*")){
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
