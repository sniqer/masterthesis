package lteAnalyzer;


/*functions that calculates average values and max values Per CQI. Also CQI per MCS. 
 * All functions are for downlink*/
public class DLCalc {
	static final int NOT_A_VALUE = -1337; //empty spaces in a vector is represented by this value
	static final int NR_OF_CQI_VALS = 16; //0 to 15
	static final int NR_OF_MCS_VALS = 29;
	static final int MAX_MCS_VAL = 28;
	static final int MIN_NR_OF_FOUND_XAXIS_VALS = 50;
	
	/*Calculates the average of a value and maps it to a cqi value. a place at cqi[i] that doesnt have a value is represented as something
	big negative */
 	public static float[] avgValPerCqi(int[] val, int[] cqi, int[] SIB,int minNrFoundXaxisVals){
 		
		int value_ind = 0;
		int counter_ind = 1;
		
		int currentCqi = -1000; //dummydefault value
		int counter = 0;
		int currentVal = 0;
		
		float[][] tempValPerCqi = new float[2][NR_OF_CQI_VALS];
		tempValPerCqi = BasicCalc.init(tempValPerCqi);
		
		float[] valPerCqi = new float[NR_OF_CQI_VALS];
		valPerCqi = BasicCalc.init(valPerCqi);
		
		for(int i=0;i < cqi.length;i++){
				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && val[i] != -1337 ){
					currentVal=currentVal+val[i]; 
					counter++;
				}
			if (cqi[i] >= 0 && cqi[i] <= 15 && SIB[i] != -1){
				System.out.println( " currentCqi: " + currentCqi+ " currentVal " + val[i]);
				tempValPerCqi[value_ind][cqi[i]] = tempValPerCqi[value_ind][cqi[i]] + currentVal; //accumulated cqi
				tempValPerCqi[counter_ind][cqi[i]] = tempValPerCqi[counter_ind][cqi[i]] + counter;
				//reset values
				currentCqi = cqi[i];
				counter=0;
				currentVal=0;
			}
		}
		for(int j=0;j<valPerCqi.length;j++){
			if (tempValPerCqi[counter_ind][j] >= minNrFoundXaxisVals)
				valPerCqi[j] = tempValPerCqi[value_ind][j]/tempValPerCqi[counter_ind][j];
		}
			Print.array2D(tempValPerCqi);
			//Print.array(valPerCqi);
			return valPerCqi;
	}
 	
 	
	
	public static float[] maxValPerCqi(int[] tbs,int[] cqi,int[] SIB){
		//indexes
		
		//values from the inputarrays
		int currentCqi = 0;
		float currentMaxTbs = 0;
		
		float[]	maxValPerCqi = new float[NR_OF_CQI_VALS];
		maxValPerCqi = BasicCalc.init(maxValPerCqi);
		
		
		for(int i=0;i<cqi.length;i++){
			
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			currentMaxTbs = Math.max((float) (tbs[i]), currentMaxTbs);
			

			if (currentCqi != cqi[i] && cqi[i] != NOT_A_VALUE){
				//System.out.println( " currentMaxTBS: " + currentMaxTbs);
				maxValPerCqi[cqi[i]] = Math.max(maxValPerCqi[cqi[i]], currentMaxTbs); //accumulated tbs

				//reset values
				currentCqi = cqi[i];
				currentMaxTbs = 0;
			}
		}
		Print.array(maxValPerCqi);
		return maxValPerCqi;
	}
	
	

	
	
	public static float[] avgCqiPerMcs(int[] mcs,int[] cqi, int[] SIB){
		
		//indexes
		int counter_ind = 0;
		int currentCqi_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		int currentCqi = -1; //dummy default value
		int currentMcs = -1; //dummy default value
		
		int[][] tempCqiPerMcs = null;
		float[]	cqiPerMcs = null;
		
		tempCqiPerMcs = new int [NR_OF_MCS_VALS][2];
		cqiPerMcs = new float[NR_OF_MCS_VALS];
		
		
		tempCqiPerMcs = BasicCalc.init(tempCqiPerMcs);
		cqiPerMcs = BasicCalc.init(cqiPerMcs);
		
		
		for(int i=0;i<cqi.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && cqi[i] != -1337){
				currentCqi=currentCqi+cqi[i]; 
				counter++;
			}

			if (currentMcs != mcs[i] && mcs[i] >= 0 && mcs[i] <= MAX_MCS_VAL && SIB[i] != -1 ){
				//System.out.println(mcs[i]);
				//System.out.println( " mcs: " + mcs[i] + " current cqi: " + currentCqi + " counter: " + counter);
				tempCqiPerMcs[mcs[i]][counter_ind] = tempCqiPerMcs[mcs[i]][counter_ind]+counter; //accumulated Counter
				tempCqiPerMcs[mcs[i]][currentCqi_ind] = tempCqiPerMcs[mcs[i]][currentCqi_ind]+currentCqi; //accumulated Prb
				//System.out.println(currentMcs + " " +tempCqiPerMcs[mcs[i]-1][counter_ind] + " " +  tempCqiPerMcs[mcs[i]][currentCqi_ind]);
				//reset values
				currentCqi = 0;
				counter = 0;
				currentMcs = mcs[i];
			}
		}
		for (int j=0;j<tempCqiPerMcs.length;j++) 
			if(tempCqiPerMcs[j][counter_ind] >= 0) //dummy value, we thrash Cqi values that we havn't got enough of.
				cqiPerMcs[j] = ((float) tempCqiPerMcs[j][currentCqi_ind]/tempCqiPerMcs[j][counter_ind]);//divide by thousand, we get Mbits/s/Cqi
		Print.array(cqiPerMcs);
		return cqiPerMcs;
	}
}
