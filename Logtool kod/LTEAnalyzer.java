package MasterThesisPackage;
import org.eclipse.swt.widgets.TabFolder;
import org.jfree.data.xy.XYSeriesCollection;


public class LTEAnalyzer {


	private String[][] lteDataMatrix;
	private int columns;
	private int rows;
	private String[] header;
	
	private GraphHandler graphHandler;
	
	private final int NR_SINR_VALUES = 45;
	private final int NR_CQI_VALUES = 16;
	private final int NR_DL_MCS_VALUES = 28;
	private final int NR_UL_MCS_VALUES = 23;
	
	

	
	double[] dlAvgBpsPerCqi	 		= new double[NR_SINR_VALUES]; 
	double[] ulAvgBpsPerSINR			= new double[NR_SINR_VALUES];
	double[] dlMaxBpsPerCqi	 		= new double[NR_SINR_VALUES];
	double[] ulMaxBpsPerSINR	 		= new double[NR_SINR_VALUES];
	
	double[] dlPrbPerCqi				= new double[NR_SINR_VALUES];
	double[] ulPrbPerSINR			= new double[NR_SINR_VALUES];
	
	double[] dlAvgSpecEffPerCqi		= new double[NR_SINR_VALUES]; 
	double[] ulAvgSpecEffPerSINR		= new double[NR_SINR_VALUES];
	
	double[] dlBlerPerCqi			= new double[NR_CQI_VALUES];
	double[] dlBler1PerCqi			= new double[NR_CQI_VALUES];
	double[] dlBler2PerCqi			= new double[NR_CQI_VALUES];
	double[] ulBlerPerSINR			= new double[NR_SINR_VALUES];
	double[] riPerCqi				= new double[NR_CQI_VALUES]; //only spatial multiplexing in DL
	double[] pmiPerCqi				= new double[NR_CQI_VALUES]; //only spatial multiplexing in DL
	
	double[] dlAvgCqiPerMcs1			= new double[NR_DL_MCS_VALUES];
	double[] dlAvgCqiPerMcs2			= new double[NR_DL_MCS_VALUES]; //not used right now
	double[] ulAvgSinrPerMcs			= new double[NR_UL_MCS_VALUES];
	
	double[] dlAvgBps1PerCqi	 		= new double[NR_SINR_VALUES];
	double[] dlAvgBps2PerCqi	 		= new double[NR_SINR_VALUES];
	double[] dlAvgTotBpsPerCqi 		= new double[NR_SINR_VALUES];

//	double[] dlAvgBpsPerSec   		= new double[rows];
//	double[] ulAvgBpsPerSec			= new double[rows];
	double[][] dlAvgBpsPerSec;
	double[][] ulAvgBpsPerSec;
	
	private double[] timeStampInDigits;
	private int timeConstant = 100; //default is 1 value per 100 milliseconds
	private double[] uLrealThroughputPerMs;
	private double[] dLrealThroughputPerMs;
	
	
	public void initialize(String[][] lteDataMatrix, int columns, int rows, String[] header, TabFolder folderReference){
		this.lteDataMatrix = BasicCalc.transpose(lteDataMatrix);
		this.columns = columns;
		this.rows = rows;
		this.header = header;
		
		graphHandler = new GraphHandler();
		graphHandler.initialize(folderReference);
	}
	
	public void setDataMatrix(String[][] lteDataMatrix){
		this.lteDataMatrix = BasicCalc.transpose(lteDataMatrix);
	}
	
	
	public void createBasicViewGraphs(){
		try{
			calculateGraphData();
			constructBasicGraphs();
		}
		catch(Exception e){
			System.out.println("an Error occurred while creating basiv view Graphs");
			e.printStackTrace();
		}
	}
	
	public double[] getTimeStampsInDigits(){
		return timeStampInDigits;
	}
	
	private void constructBasicGraphs(){
		System.out.println("starting to construct the actual graphs");
		//downlink and uplink bits per second over time
//		graphHandler.addSeriesOverTimeToDataset(timeStampInDigits, dlAvgBpsPerSec, "Average");
		graphHandler.addSeriesOverTimeToDataset(dlAvgBpsPerSec[0], dlAvgBpsPerSec[1], "Average");
		graphHandler.addLineChartToComposite("DL Average Bits per second / timestamp", "time", "throughput [kbits/s]");
//		graphHandler.addSeriesOverTimeToDataset(timeStampInDigits, ulAvgBpsPerSec, "Average 2");
		graphHandler.addSeriesOverTimeToDataset(ulAvgBpsPerSec[0], ulAvgBpsPerSec[1], "Average");
		graphHandler.addLineChartToComposite("UL Average Bits per second / timestamp", "time", "throughput [kbits/s]");
		graphHandler.addCompositeToTab("dl and ul bps/time"); //verkar inte kunna skriva ut &
		
		//downlink bits per second over CQI
		graphHandler.addDLSeriesToDataset(dlAvgBps1PerCqi, "Average bps1");
		graphHandler.addDLSeriesToDataset(dlAvgBps2PerCqi, "Average bps2");
		graphHandler.addDLSeriesToDataset(dlAvgTotBpsPerCqi, "Average total_bps");
		graphHandler.addDLSeriesToDataset(dlMaxBpsPerCqi, "Max");
		graphHandler.addLineChartToComposite("DL Bits per second / CQI", "CQI", "throughput [kbits/s]");
		
		//uplink bits per second over SINR
		graphHandler.addULSeriesToDataset(ulAvgBpsPerSINR, "Average");
		graphHandler.addULSeriesToDataset(ulMaxBpsPerSINR, "Max");
		graphHandler.addLineChartToComposite("ul Bits per second / SINR", "SINR", "throughput [kbits/s]");
		graphHandler.addCompositeToTab("dl and ul bps/SINR and CQI");
		
		//downlink and uplink prb per cqi and SINR
		graphHandler.addDLSeriesToDataset(dlPrbPerCqi, "Average prb");
		graphHandler.addLineChartToComposite("dl prb / CQI", "CQI", "dl prb");
		graphHandler.addULSeriesToDataset(ulPrbPerSINR, "Average prb");
		graphHandler.addLineChartToComposite("ul prb / SINR", "SINR", "ul prb");
		graphHandler.addCompositeToTab("dl and ul prb/SINR and CQI");

		
		//downlink and uplink spectral efficiency per cqi and SINR
		graphHandler.addDLSeriesToDataset(dlAvgSpecEffPerCqi, "Average spectral efficiency");
		graphHandler.addLineChartToComposite("dl spectral efficiency / CQI", "CQI", "spectral efficiency [bits/s/Hz]");
		graphHandler.addULSeriesToDataset(ulAvgSpecEffPerSINR, "Average spectral efficiency");
		graphHandler.addLineChartToComposite("ul spectral efficiency / SINR", "SINR", "spectral efficiency [bits/s/Hz]");
		graphHandler.addCompositeToTab("dl and ul specEff/SINR and CQI");
		
		//downlink and uplink block error rate per cqi and sinr
		graphHandler.addDLSeriesToDataset(dlBler1PerCqi, "Average bler1");
		graphHandler.addDLSeriesToDataset(dlBler2PerCqi, "Average bler2");
		graphHandler.addDLSeriesToDataset(dlBlerPerCqi, "Average bler");
		graphHandler.addLineChartToComposite("dl block error rate / CQI", "CQI", "block error rate [%]");
		graphHandler.addULSeriesToDataset(ulBlerPerSINR, "Average bler");
		graphHandler.addLineChartToComposite("ul block error rate / SINR", "SINR", "block error rate [%]");
		graphHandler.addCompositeToTab("dl and ul bler/SINR and CQI");


		graphHandler.addDLSeriesToDataset(riPerCqi, "Average RI");
		graphHandler.addDLSeriesToDataset(pmiPerCqi, "Average PMI");
		graphHandler.addLineChartToComposite("dl rank indicatpr / cqi", "CQI", "rank indicator");
		graphHandler.addCompositeToTab("RI and PMI/CQI");

		graphHandler.addDLSeriesToDataset(dlAvgCqiPerMcs1, "Average cqi");
		graphHandler.addLineChartToComposite("dl cqi / mcs1", "mcs", "CQI");
		graphHandler.addDLSeriesToDataset(ulAvgSinrPerMcs, "Average SINR");
		graphHandler.addLineChartToComposite("ul SINR / mcs", "mcs", "SINR");
		graphHandler.addCompositeToTab("dl and ul SINR and CQI / mcs");
	}
	
	
	public void recalculateThroughput(){
		int dlTbs1Index 	= findHeaderIndex("tbs1",0);
		int dlTbs2Index 	= findHeaderIndex("tbs2",0);
		int ulTbsIndex 		= findHeaderIndex("tbs",3);
		int ndf1Index		= findHeaderIndex("ndf1", 0);
		int ndf2Index		= findHeaderIndex("ndf2", 0);
		int ndfIndex		= findHeaderIndex("ndf", 2);
		
		int[] dlTbs1 		= interpretLTEdata(dlTbs1Index);
		int[] dlTbs2	 	= interpretLTEdata(dlTbs2Index);
		int[] ulTbs			= interpretLTEdata(ulTbsIndex);

		String[] ndf1 = lteDataMatrix[ndf1Index];
		String[] ndf2 = lteDataMatrix[ndf2Index];
		String[] ndf  = lteDataMatrix[ndfIndex];
		
		double[] dLrealThroughput1PerMs = BasicCalc.tbs2throughput(dlTbs1,ndf1,timeStampInDigits);
		double[] dLrealThroughput2PerMs = BasicCalc.tbs2throughput(dlTbs2,ndf2,timeStampInDigits);
		
		uLrealThroughputPerMs = BasicCalc.tbs2throughput(ulTbs,ndf,timeStampInDigits);
		dLrealThroughputPerMs =  BasicCalc.addArrays(dLrealThroughput1PerMs, dLrealThroughput2PerMs);
	
		dLrealThroughputPerMs = BasicCalc.multiplyArray(dLrealThroughputPerMs, 0.001);
		uLrealThroughputPerMs = BasicCalc.multiplyArray(uLrealThroughputPerMs, 0.001);
		
		dlAvgBpsPerSec = getValuesOverTime(dLrealThroughputPerMs);
		ulAvgBpsPerSec = getValuesOverTime(uLrealThroughputPerMs);
	}
	
	public void calculateTimeStampData(){
		int timeStampIndex 	= findHeaderIndex("timeStamp", 0);
		String[] timeStamp	= lteDataMatrix[timeStampIndex];
		
		timeStamp	= BasicCalc.discardDate(timeStamp);
		timeStampInDigits	= BasicCalc.startTimeFrZero(timeStamp);
	}
	
	private void calculateGraphData(){

		//setting variable in BasicCalc class
		BasicCalc basicCalc = new BasicCalc();
		basicCalc.setHeader(header);
		
		System.out.println("finds indexes for all headers");
		int SIBIndex 		= findHeaderIndex("bbUeRef",0);
		int SINRIndex 		= findHeaderIndex("puschSinr",0);//hos razmus puschSinr
		int cqiIndex 		= findHeaderIndex("cqi",0);
		int dlTbsSumIndex 	= findHeaderIndex("tbsSum",0);//hos mig tbssum
		int dlTbs1Index 	= findHeaderIndex("tbs1",0);//hos mig tbssum
		int dlTbs2Index 	= findHeaderIndex("tbs2",0);//hos mig tbssum
		
		int dlPrbIndex 		= findHeaderIndex("prb",0);
		int ulPrbIndex 		= findHeaderIndex("prb",1);
		int ulTbsIndex 		= findHeaderIndex("tbs",3);
		int riIndex			= findHeaderIndex("ri",0);
		int dlBler1Index 	= findHeaderIndex("bler1",0);
		int dlBler2Index 	= findHeaderIndex("bler2",0);
		int ulBlerIndex 	= findHeaderIndex("bler",2);
		
		int ulMcsIndex 		= findHeaderIndex("mcs",2);
		int dlMcs1Index 	= findHeaderIndex("mcs1",0);
		int dlMcs2Index 	= findHeaderIndex("mcs2",0);
		
		int timeStampIndex 	= findHeaderIndex("timeStamp", 0);
		int ndf1Index		= findHeaderIndex("ndf1", 0);
		int ndf2Index		= findHeaderIndex("ndf2", 0);
		int ndfIndex		= findHeaderIndex("ndf", 2);
		int pmiIndex		= findHeaderIndex("pmi", 0);

		System.out.println("constructing the datavectors from indexes in lteDataMatrix");
		int[] dlTbs1 		= interpretLTEdata(dlTbs1Index);
		int[] dlTbs2	 	= interpretLTEdata(dlTbs2Index);
		//dlTbsSum 	= interpretLTEdata(dlTbsSumIndex]);
		int[] ulTbs			= interpretLTEdata(ulTbsIndex);
		int[] SIB			= interpretLTEdata(SIBIndex);
		int[] SINR 			= interpretLTEdata(SINRIndex);
		int[] dlPrb  		= interpretLTEdata(dlPrbIndex);
		int[] ulPrb  		= interpretLTEdata(ulPrbIndex);
		int[] cqi   		= interpretLTEdata(cqiIndex);
		int[] ri			= interpretLTEdata(riIndex);
		int[] dlBler1		= interpretLTEdata(dlBler1Index);
		int[] dlBler2		= interpretLTEdata(dlBler2Index);
		int[] ulBler		= interpretLTEdata(ulBlerIndex);
		
		int[] ulMcs			= interpretLTEdata(ulMcsIndex);
		int[] dlMcs1		= interpretLTEdata(dlMcs1Index);
		int[] dlMcs2		= interpretLTEdata(dlMcs2Index);
		int[] pmi			= interpretLTEdata(pmiIndex);
		
//		int[] sf	= interpretLTEdata(sfIndex);
		int[] dlMcsSum	= BasicCalc.addArrays(dlMcs1, dlMcs2);
		int[] dlBler	= BasicCalc.addArrays(dlBler1, dlBler2);
		int[] dlTbsSum 	= BasicCalc.addArrays(dlTbs1, dlTbs2);
		
		String[] ndf1 = lteDataMatrix[ndf1Index];
		String[] ndf2 = lteDataMatrix[ndf2Index];
		String[] ndf  = lteDataMatrix[ndfIndex];
		
		String[] timeStamp	= lteDataMatrix[timeStampIndex];

		//dlAvgBpsPerSINR = Calculate.avgBpsPerSINR(dLtbssum, dlSINR, SIB);
		System.out.println("Rows: " + rows);
		System.out.println("Columns: " + columns);
		
		

		basicCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		DLCalc dlCalc = new DLCalc();
		dlCalc.setCqi(cqi);
		dlCalc.setSIB(SIB);
		
		//setting variable in DLCalc class
		ULCalc ulCalc = new ULCalc();
		ulCalc.setSINR(SINR);
		ulCalc.setSIB(SIB);
		
		Calculate calculate = new Calculate();
		calculate.setSIB(SIB);
		
		timeStamp	= BasicCalc.discardDate(timeStamp);
		timeStampInDigits	= BasicCalc.startTimeFrZero(timeStamp);
		double[] dLrealThroughput1PerMs = BasicCalc.tbs2throughput(dlTbs1,ndf1,timeStampInDigits);
		double[] dLrealThroughput2PerMs = BasicCalc.tbs2throughput(dlTbs2,ndf2,timeStampInDigits);
		
		//flik 1 graf 2 throughput p� y [Mbits/s], time p� x
		uLrealThroughputPerMs = BasicCalc.tbs2throughput(ulTbs,ndf,timeStampInDigits);
		
		//flik 1 graf 1 throughput p� y [Mbits/s], time p� x
		dLrealThroughputPerMs =  basicCalc.addArrays(dLrealThroughput1PerMs, dLrealThroughput2PerMs);
		timeStampInDigits = basicCalc.multiplyArray(timeStampInDigits, 100);
		
		
		dLrealThroughputPerMs = BasicCalc.multiplyArray(dLrealThroughputPerMs, 0.001);
		uLrealThroughputPerMs = BasicCalc.multiplyArray(uLrealThroughputPerMs, 0.001);
		
		
		System.out.println("uplinks throughput");
		
		dlAvgBpsPerSec = getValuesOverTime(dLrealThroughputPerMs);
		ulAvgBpsPerSec = getValuesOverTime(uLrealThroughputPerMs);
		
		//flik 2, graf1 throughput p� y CQI p� x
		dlAvgBpsPerCqi = dlCalc.avgValPerCqi(dLrealThroughputPerMs, 5); // 5 is a dummy value
		dlAvgBps1PerCqi = dlCalc.avgValPerCqi(dLrealThroughput1PerMs, 5);
		dlAvgBps2PerCqi = dlCalc.avgValPerCqi(dLrealThroughput2PerMs, 5);
		dlAvgTotBpsPerCqi = BasicCalc.addArrays(dlAvgBps1PerCqi, dlAvgBps2PerCqi);
		dlMaxBpsPerCqi = dlCalc.maxValPerCqi(dLrealThroughputPerMs);
		
		//flik 2, graf2 throughput p� y SINR p� x axeln
		ulAvgBpsPerSINR = ulCalc.avgValPerSINR(uLrealThroughputPerMs, 5); // 5 is a dummy value
		ulMaxBpsPerSINR = ulCalc.maxValPerSINR(uLrealThroughputPerMs);
		
		//flik 3, graf 1 prb p� y, cqi p� x
		dlPrbPerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(dlPrb),300);
		//flik 3, graf 2 prb p� y, sinr p� x
		ulPrbPerSINR = ulCalc.avgValPerSINR(basicCalc.intArr2DoubleArr(ulPrb),100);
		
		//flik 4 graf1 spectral efficiency p� y bits/s/Hz, cqi p� x
		double[] ulSpecEff = basicCalc.bitwiseArrayDiv(
				BasicCalc.multiplyArray(uLrealThroughputPerMs, 1000),BasicCalc.prb2hz(ulPrb));
		double[] dlSpecEff = basicCalc.bitwiseArrayDiv(
				BasicCalc.multiplyArray(uLrealThroughputPerMs, 1000),BasicCalc.prb2hz(dlPrb));
		dlAvgSpecEffPerCqi =  dlCalc.avgValPerCqi(dlSpecEff, 5);
		//flik 4 graf2 spectral efficiency p� y bits/s/Hz, sinr p� x
		ulAvgSpecEffPerSINR = ulCalc.avgValPerSINR(ulSpecEff, 5);
		
		
		dlBlerPerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(dlBler),1);
		dlBler1PerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(dlBler1),1);
		dlBler2PerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(dlBler2),1);
		ulBlerPerSINR = ulCalc.avgValPerSINR(basicCalc.intArr2DoubleArr(ulBler), 1);
		
		riPerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(ri),1);
		pmiPerCqi = dlCalc.avgValPerCqi(basicCalc.intArr2DoubleArr(pmi),20);
		dlAvgCqiPerMcs1 = dlCalc.avgValPerMcs(basicCalc.intArr2DoubleArr(cqi),dlMcs1);
		ulAvgSinrPerMcs = ulCalc.avgValPerMcs(basicCalc.intArr2DoubleArr(SINR),ulMcs);
	}
	
	/**
	 * this is throughput average that's split over timeConstant number of values
	 * @return
	 */
	public double[][] getULThroughput(){
		return ulAvgBpsPerSec;
	}
	
	/**
	 * this is throughput average that's split over timeConstant number of values
	 * @return
	 */
	public double[][] getDLThroughput(){
		return dlAvgBpsPerSec;
	}
	
	public double[] getTimeDifference(){
		return timeStampInDigits;
	}

	//TODO create and save thoughput over time in array
	 /** 
	 * this is throughput over 1 millisecond
	 * @return
	 */
	public double[] getULRealThroughput(){
		return uLrealThroughputPerMs;
	}
	
	/**
	 * this is throughput over 1 millisecond
	 * @return
	 */
	public double[] getDLRealThroughput(){
		return dLrealThroughputPerMs;
	}
	
	public double[][] getValuesOverTime(double[] values){
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

	
	public int[] interpretLTEdata(int index){
		String[] lteData = lteDataMatrix[index];
		int[] out = new int[lteData.length];
		//dl and ul harq index
		//Paul added this 9/12
		if(header[index].contains("harq")){
			out = interpretHARQ(index);
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
				//bl�����rk
				out[i]= Integer.parseInt(lteData[i].replace("%", "").trim());
				System.out.println("bler �r: " + out[i]);
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
	//Paul added this 9/12/12
	public int[] interpretHARQ(int index){
		String[] lteData = lteDataMatrix[index];
		
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
		// TODO Auto-generated method stub
		return graphHandler.getTables();
	}

	public double[] getTimeDiffArray(int xIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTimeConstant(int newValue) {
		timeConstant = newValue;
		
	}
	
}
