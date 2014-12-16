package lteAnalyzer;

public class Calculate extends Main{
	
	static final int NOT_A_VALUE = -1337; //empty spaces in a vector is represented by this value
	static final int MIN_NR_OF_FOUND_XAXIS_VALS = 0;
	//dl

	static final int MIN_CQI_VAL = 0;
	static final int MAX_CQI_VAL = 15;
	static final int NR_OF_CQI_VALS = 16; //0 to 15
	
	static final int MIN_DL_MCS_VAL = 0;
	static final int MAX_DL_MCS_VAL = 28;
	static final int NR_OF_DL_MCS_VALS = 29; // 0 to 22
	

	
	//ul
	static final int MAX_UL_MCS_VAL = 24;
	static final int MIN_UL_MCS_VAL = 0;
	static final int NR_OF_UL_MCS_VALS = 25; // 0 to 24
	
	static final int MIN_SINR_VAL = -25;
	static final int MAX_SINR_VAL = 45;
	static final int NR_OF_SINR_VALS = 71; //-25 to 45
	
	private int[] SIB;
	
	
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
				if(SIB[i] != -1 && Yvals[i] != NOT_A_VALUE){
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

