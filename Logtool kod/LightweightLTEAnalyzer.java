package MasterThesisPackage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.jfree.data.xy.XYSeriesCollection;


public class LightweightLTEAnalyzer {

	private int rows;
	private String[] header;
	
	private GraphHandler graphHandler;
	
	private int timeConstant = 100; //default is 1 value per 100 milliseconds
	private String filePath;
	
	
	
	public void initialize(String filePath, TabFolder folderReference){
		setFilePath(filePath);
		
		graphHandler = new GraphHandler();
		graphHandler.initialize(folderReference);
	}

	public double[] getTimeStampsInDigits(){
		int timeStampIndex 	= findHeaderIndex("timeStamp", 0);
		String[] timeStamp	= getDataColumn(timeStampIndex);
		
		timeStamp	= BasicCalc.discardDate(timeStamp);
		return BasicCalc.startTimeFrZero(timeStamp);
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
		
		System.out.println("Initializes lightweightAnalyzer values...");
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));

			header = reader.readLine().split(", ");
			reader.close();
			
			//the reader in countLines is a bit faster than just using readline for all rows
			rows = countLines(filePath) -1;
			
			if(rows == 0)
				System.out.println("ERROR: opens the view with empty data matrix");
		}
		
		catch(Exception e){
			e.printStackTrace();
			MessageBox dialog = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("ERROR");
			dialog.setMessage("There occurred an error while reading the file:\n" + filePath );
			dialog.open();
		}
		System.out.println("------------Finished Initializing Analyzer Values----------------\n");
	}
	
	private int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n' || c[i] == '\r') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public double[] calculateDLThroughput(){
		int dlTbs1Index 	= findHeaderIndex("tbs1",0);
		int dlTbs2Index 	= findHeaderIndex("tbs2",0);
		int ndf1Index		= findHeaderIndex("ndf1", 0);
		int ndf2Index		= findHeaderIndex("ndf2", 0);
		
		int[] dlTbs1 		= interpretLTEdata(getDataColumn(dlTbs1Index), dlTbs1Index);
		int[] dlTbs2	 	= interpretLTEdata(getDataColumn(dlTbs2Index),dlTbs2Index);

		String[] ndf1 = getDataColumn(ndf1Index);
		String[] ndf2 = getDataColumn(ndf2Index);

		double[] timeStampInDigits = getTimeStampsInDigits();
		double[] dLrealThroughput1PerMs = BasicCalc.tbs2throughput(dlTbs1,ndf1,timeStampInDigits);
		double[] dLrealThroughput2PerMs = BasicCalc.tbs2throughput(dlTbs2,ndf2,timeStampInDigits);
		
		double[] dLrealThroughputPerMs =  BasicCalc.addArrays(dLrealThroughput1PerMs, dLrealThroughput2PerMs);
	
		return dLrealThroughputPerMs = BasicCalc.multiplyArray(dLrealThroughputPerMs, 0.001);
	}
	
	public double[] calculateULThroughput(){
		int ulTbsIndex 		= findHeaderIndex("tbs",3);
		int ndfIndex		= findHeaderIndex("ndf", 2);
		int[] ulTbs			= interpretLTEdata(getDataColumn(ulTbsIndex),ulTbsIndex);
		String[] ndf  = getDataColumn(ndfIndex);
		
		double[] timeStampInDigits = getTimeStampsInDigits();
		double[] uLrealThroughputPerMs = BasicCalc.tbs2throughput(ulTbs,ndf,timeStampInDigits);
	
		return uLrealThroughputPerMs = BasicCalc.multiplyArray(uLrealThroughputPerMs, 0.001);
	}
	
	public String[] getDataColumn(int columnIndex){
		String[] output = new String[rows];
		 
		System.out.println("Loading file...");
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));

			for(int i = 0; i < rows; i++)
				output[i] = reader.readLine().split(", ")[columnIndex];
			
			reader.close();
			
			if(rows == 0)
				System.out.println("ERROR: opens the view with empty data matrix");
		}
		
		catch(Exception e){
			e.printStackTrace();
			MessageBox dialog = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("ERROR");
			dialog.setMessage("There occurred an error while reading the file:\n" + filePath );
			dialog.open();
		}
		System.out.println("------------Finished Loading Data from file----------------\n");
		return output;
	}

	
	
	
	public double[][] getValuesOverTime(double[] values, double[] timeStampInDigits){
		int newLength = (int)Math.ceil(timeStampInDigits.length / timeConstant);
		System.out.println("time constant = " + timeConstant);
		double[][] output = new double[2][newLength];
		
		boolean isSet;
		double sum;
		int position;
		
		for(int i = 0; i < newLength; i++){
			sum = 0;
			isSet = false;
			
			for(int k = 0; k < timeConstant; k++){
				position = i*timeConstant + k;
				if(values[position] != -1337)
					sum += values[position];
				if(!isSet)	
					if(timeStampInDigits[position] != -1337){
						output[0][i] = timeStampInDigits[position];
						isSet = true;
					}
			} 
			output[1][i] = sum/timeConstant;
		}
		System.out.println("new length of output = "+ output[0].length);
		return output;
	}

	
	public int[] interpretLTEdata(String[] lteData, int headerIndex){		
		int[] out = new int[lteData.length];
		
		//dl and ul harq index
		//Paul added this 9/12
		if(header[headerIndex].contains("harq")){
			out = interpretHARQ(lteData);
		} else {
		for(int i=0;i<lteData.length;i++){
			
			if (lteData[i].contains("64QAM")){
				out[i] = 6;
			} else if (lteData[i].contains("16QAM")){
				out[i] = 4;
			} else if (lteData[i].contains("QPSK")){
				out[i] = 2;
			} else if (lteData[i].contains("SIB") ){
				out[i] = -1; 
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
		}
		return out;
	}
	
	//Paul added this 9/12/14
	public int[] interpretHARQ(String[] lteData){
		int[] out = new int[lteData.length];
		for(int i=0;i<lteData.length;i++){
			if (lteData[i].contains("ACK")) out[i] = 1;
			if (lteData[i].contains("NACK")) out[i] = 0;
			if (lteData[i].contains("A  ")) out[i] = 1;
			if (lteData[i].contains("  A")) out[i] = 1;
			if (lteData[i].contains("N  ")) out[i] = 0;
			if (lteData[i].contains("  N")) out[i] = 0;
			if (lteData[i].contains("N A")) out[i] = 1;
			if (lteData[i].contains("A N")) out[i] = 1;
			if (lteData[i].contains("A A")) out[i] = 2;
			if (lteData[i].contains("N N")) out[i] = 0;
			else out[i] = -1337;
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


	public int findHeaderIndex(String headerName,int doublett){
		int doublettcounter = 0;
		for(int i=0;i<header.length;i++){
			
			if(header[i].contains(headerName)){
				
				if (doublett==doublettcounter){
					return i;
				} else {
					doublettcounter++;
				}
			}
		}
		System.out.println("hittade inte " + headerName);
		return -1;
	}


	public XYSeriesCollection [] getGraphData() {
		return graphHandler.getTables();
	}


	public void setTimeConstant(int newValue) {
		timeConstant = newValue;
		
	}
	
}
