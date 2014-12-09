package MasterThesisPackage;


public class DLCalc extends Calculate {

	private int[] cqi;
	private int[] SIB;
	
	
	/*Calculates the average of a value and maps it to a cqi value. a place at cqi[i] that doesnt have a value is represented as something
	big negative */
 	public double[] avgValPerCqi(double val[],int minNrFoundXaxisVals){
 		
		int value_ind = 0;
		int counter_ind = 1;
		
		int counter = 0;
		double currentVal = 0;
		double[][] tempValPerCqi = new double[2][NR_OF_CQI_VALS];
		
		double[] valPerCqi = new double[NR_OF_CQI_VALS];
		BasicCalc.init(valPerCqi);
		for(int i=0;i < cqi.length;i++){
				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && val[i] != NOT_A_VALUE ){
					currentVal=currentVal+val[i]; 
					counter++;
				}
			if (SIB[i] != -1 && cqi[i] >= MIN_CQI_VAL && cqi[i] <= MAX_CQI_VAL){
				tempValPerCqi[value_ind][cqi[i]] = tempValPerCqi[value_ind][cqi[i]] + currentVal; //accumulated cqi
				tempValPerCqi[counter_ind][cqi[i]] = tempValPerCqi[counter_ind][cqi[i]] + counter;
				counter=0;
				currentVal=0;
			}
		}
		for(int j=0;j<valPerCqi.length;j++)
			if (tempValPerCqi[counter_ind][j] >= minNrFoundXaxisVals)
				valPerCqi[j] = tempValPerCqi[value_ind][j]/tempValPerCqi[counter_ind][j];
			return valPerCqi;
	}
 	
 	
	
	public double[] maxValPerCqi(int[] val){
		
		double currentMaxVal = 0;
		double[]	maxValPerCqi = new double[NR_OF_CQI_VALS];
		BasicCalc.init(maxValPerCqi);
		for(int i=0;i<cqi.length;i++){
			
			currentMaxVal = Math.max((double) (val[i]), currentMaxVal);

			if (cqi[i] != NOT_A_VALUE && cqi[i] >= MIN_CQI_VAL && cqi[i] <= MAX_CQI_VAL){
				maxValPerCqi[cqi[i]] = Math.max(maxValPerCqi[cqi[i]], currentMaxVal);
				currentMaxVal = 0;
			}
		}
		Print.array(maxValPerCqi);
		return maxValPerCqi;
	}
	
	public double[] maxValPerCqi(double[] val){
		
		double currentMaxVal = 0;
		double[] maxValPerCqi = new double[NR_OF_CQI_VALS];
		
		for(int i=0;i<cqi.length;i++){
			
			currentMaxVal = Math.max((double) (val[i]), currentMaxVal);

			if (cqi[i] != NOT_A_VALUE && >= MIN_CQI_VAL && cqi[i] <= MAX_CQI_VAL){
				maxValPerCqi[cqi[i]] = Math.max(maxValPerCqi[cqi[i]], currentMaxVal);
				currentMaxVal = 0;
			}
		}
		return maxValPerCqi;
	}
	
	public double[] avgValPerMcs(double[] val,int[] mcs){
		
		//indexes
		int counter_ind = 0;
		int currentCqi_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		double currentCqi = -1; //dummy default value
		int currentMcs = -1; //dummy default value
		
		double[][] tempCqiPerMcs = new double [NR_OF_DL_MCS_VALS][2];
		double[] cqiPerMcs =  new double[NR_OF_DL_MCS_VALS];
		BasicCalc.init(cqiPerMcs);
		for(int i=0;i<val.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && val[i] != NOT_A_VALUE){
				currentCqi=currentCqi+val[i]; 
				counter++;
			}

			if (currentMcs != mcs[i] && mcs[i] >= MIN_DL_MCS_VAL && mcs[i] <= MAX_DL_MCS_VAL){
				tempCqiPerMcs[mcs[i]][counter_ind] = tempCqiPerMcs[mcs[i]][counter_ind]+counter; //accumulated Counter
				tempCqiPerMcs[mcs[i]][currentCqi_ind] = tempCqiPerMcs[mcs[i]][currentCqi_ind]+currentCqi; //accumulated Prb//System.out.println(currentMcs + " " +tempCqiPerMcs[mcs[i]-1][counter_ind] + " " +  tempCqiPerMcs[mcs[i]][currentCqi_ind]);
				//reset values
				currentCqi = 0;
				counter = 0;
				currentMcs = mcs[i];
			}
		}
		for (int j=0;j<tempCqiPerMcs.length;j++) 
			if(tempCqiPerMcs[j][counter_ind] >= 0) //dummy value, we thrash Cqi values that we havn't got enough of.
				cqiPerMcs[j] = ((float) tempCqiPerMcs[j][currentCqi_ind]/tempCqiPerMcs[j][counter_ind]);//divide by thousand, we get Mbits/s/Cqi
		return cqiPerMcs;
	}
	
	
	
/*-------------------------------------------SETTERS!!!-------------------------------------------*/

	public void setCqi(int[] cqi){
		this.cqi = cqi;
	}
	
	public void setSIB(int[] SIB){
		this.SIB = SIB;
	}

}
