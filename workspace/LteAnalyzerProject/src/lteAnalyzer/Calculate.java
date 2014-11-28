package lteAnalyzer;

public class Calculate extends Main{
	
	//dl
	static final int NOT_A_VALUE = -1337; //empty spaces in a vector is represented by this value
	static final int NR_OF_CQI_VALS = 16; //0 to 15
	static final int NR_OF_MCS_VALS = 29;
	static final int MAX_MCS_VAL = 28;
	static final int MIN_NR_OF_FOUND_XAXIS_VALS = 50;
	
	//ul
	static final int NR_OF_SINR_VALS = 66; //-25 to 40
	static final int NR_OF_UL_MCS_VALS = 23; // 0 to 22
	static final int MAX_UL_MCS_VAL = 22;
	static final int MIN_SINR_VAL = -25;
	
	private int[] SIB;
	
	public static double[][] avgTbsPerSecond(int[] tbs, double[] timeStamps, int[] SIB, int granularity){

		int logFileCounter = 0;
		int averageCounter = 0;
		
		int averageValue = 0;
		int sum = 0;
		int timeStampIndex = 0;
		int currentTbsValue;
		double currentTimeStamp = 0;
		
		double[][] outputArray = new double[2][tbs.length/granularity + 1];

		while(logFileCounter < tbs.length)
		{
			currentTbsValue = tbs[logFileCounter]; 
			if(currentTbsValue != NOT_A_VALUE && SIB[logFileCounter] != -1){
				sum += currentTbsValue;
				++averageCounter;
				
				//check if we have found all values we intend to use for calculating the averageSum
				if(averageCounter == granularity){
					averageValue = sum / granularity;
					currentTimeStamp = timeStamps[logFileCounter];
					
					//put values in output array
					outputArray[1][timeStampIndex] = averageValue;
					outputArray[0][timeStampIndex] = BasicCalc.findCloseValFrInd(timeStamps, logFileCounter);
					++timeStampIndex;
					
					//reset all counters the relates to the granularity loop
					averageCounter = 0;
					sum = 0;
				}
			}
			++logFileCounter;
		}
		return outputArray;
	}
	
	
public double[] avgYValPerXVal(double[] Xvals,double[] Yvals,int minNrFoundXaxisVals, int smallestXValue){
 		
		int value_ind = 0;
		int counter_ind = 1;
		
		int counter = 0;
		double currentVal = 0;
		int x;
		
		int lenOfXAxis = (int) Math.ceil(BasicCalc.getBiggest(Xvals)-smallestXValue+1);
		double[][] tempXvalPerYval = new double[2][lenOfXAxis];
		//tempXvalPerYval = BasicCalc.init(tempXvalPerYval);
		
		double[] YvalsPerXvals = new double[lenOfXAxis];
		//valPerCqi = BasicCalc.init(valPerCqi);
		
		for(int i=0;i < Yvals.length;i++){
				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && Yvals[i] != NOT_A_VALUE ){
					currentVal=currentVal+Yvals[i]; 
					counter++;
				}
			if (SIB[i] != -1 && Xvals[i] != NOT_A_VALUE){
				x = (int) Xvals[i];
				//System.out.println(x-smallestXValue);
				tempXvalPerYval[value_ind][x-smallestXValue] = tempXvalPerYval[value_ind][x-smallestXValue] + currentVal; //accumulated Xvals
				tempXvalPerYval[counter_ind][x-smallestXValue] = tempXvalPerYval[counter_ind][x-smallestXValue] + counter;
				counter=0;
				currentVal=0;
			}
		}
		for(int j=0;j<YvalsPerXvals.length;j++)
			if (tempXvalPerYval[counter_ind][j] >= minNrFoundXaxisVals)
				YvalsPerXvals[j] = tempXvalPerYval[value_ind][j]/tempXvalPerYval[counter_ind][j];
			return YvalsPerXvals;
	}

public double[] maxYValPerXVal(double[] Xvals,double[] Yvals){
		
	double currentVal = 0;
	
	int lenOfXAxis = (int) Math.ceil(BasicCalc.getBiggest(Xvals))+1;
	
	
	double[] yvalPerXval = new double[lenOfXAxis];
	//valPerCqi = BasicCalc.init(valPerCqi);
	
	for(int i=0;i < Yvals.length;i++){
		if(SIB[i] != -1 && Yvals[i] != NOT_A_VALUE ){
			currentVal=Math.max(currentVal, Yvals[i]); 
		}
		
		if (SIB[i] != -1 && Xvals[i] != NOT_A_VALUE){
			//System.out.println(yvalsPerXvals[(int) Xvals[i]]+" "+tempXvalPerYval[value_ind][(int) Xvals[i]]+ " yvalperxval");
			yvalPerXval[(int) Xvals[i]] = Math.max(currentVal,yvalPerXval[(int) Xvals[i]]); 
			currentVal=0;
		}
	}
	return yvalPerXval;
}

public double[] minYValPerXVal(double[] Xvals,double[] Yvals){
	double max = Double.MAX_VALUE;
	double currentVal = max;
	
	int lenOfXAxis = (int) Math.ceil(BasicCalc.getBiggest(Xvals))+1;
	
	double[] yvalPerXval = new double[lenOfXAxis];
	
	for(int j=0;j < yvalPerXval.length;j++){
		yvalPerXval[j] = max;
	}

	//Print.array(Yvals);
	
	for(int i=0;i < Yvals.length;i++){
		if(SIB[i] != -1 && Yvals[i] != NOT_A_VALUE ){
			currentVal=Math.min(currentVal, Yvals[i]); 
			//System.out.println(Yvals[i]);
		}
		
		if (SIB[i] != -1 && Xvals[i] != NOT_A_VALUE){
			yvalPerXval[(int) Xvals[i]] = Math.min(currentVal,yvalPerXval[(int) Xvals[i]]); 
			//System.out.println(yvalPerXval[(int) Xvals[i]]);
//			System.out.println(currentVal);
			currentVal=max;
		}
	}
	for(int k=0;k < yvalPerXval.length;k++){
		if(yvalPerXval[k] == max) yvalPerXval[k] = 0;
	}
	
	//Print.array(yvalPerXval);
	return yvalPerXval;
}

	

/*-------------------------------------------SETTERS!!!-------------------------------------------*/

public void setSIB(int[] SIB){
	this.SIB = SIB;
}


}

