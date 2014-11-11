package lteAnalyzer;

public class ULCalc extends Calculate{
	
	private int[] SINR;
	private int[] SIB;
	
	
	public float[] avgValPerSINR(int[] val,int minNrFoundXaxisVals){
		
		int value_ind = 0;
		int counter_ind = 1;
		
		//int currentsinr = -1000; //dummydefault value
		int counter = 0;
		int currentVal = 0;
		
		float[][] tempValPerSINR = new float[2][NR_OF_SINR_VALS];
		//tempValPerSINR = BasicCalc.init(tempValPerSINR);
		
		float[] valPerSINR = new float[NR_OF_SINR_VALS];
		//valPerSINR = BasicCalc.init(valPerSINR);
		
		for(int i=0;i<SINR.length;i++){
			

				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && val[i] != NOT_A_VALUE ){
					currentVal=currentVal+val[i]; 
					counter++;
				}
			
			if (SINR[i] != NOT_A_VALUE && SIB[i] != -1){
				//System.out.println( " currentSinr: " + currentsinr+ " currentVal " + val[i]);
				tempValPerSINR[value_ind][SINR[i]-MIN_SINR_VAL] = tempValPerSINR[value_ind][SINR[i]-MIN_SINR_VAL] + currentVal;
				tempValPerSINR[counter_ind][SINR[i]-MIN_SINR_VAL] = tempValPerSINR[counter_ind][SINR[i]-MIN_SINR_VAL] + counter;
				
				//reset values
				//currentsinr = sinr[i];
				counter=0;
				currentVal=0;
				
			}
		}
		for(int j=0;j<valPerSINR.length;j++){
			if (tempValPerSINR[counter_ind][j] > minNrFoundXaxisVals)
				valPerSINR[j] = tempValPerSINR[value_ind][j]/tempValPerSINR[counter_ind][j];
		}
			Print.array2D(tempValPerSINR);
			//Print.array(valPerSINR);
			return valPerSINR;
	}
	
	
	
	//beh�vs f�rmodligen inte
	public float[] sinrGenerator(int[] puschPwr, int[] puschNoiseIntPwr){
		float[] sinr = new float[puschPwr.length];
		for (int i=0; i<puschPwr.length;i++){
			sinr[i] = (int) puschPwr[i]/BasicCalc.findCloseValFrInd(puschNoiseIntPwr,i);
		}
		return sinr;
	}
	

	public float[] maxValPerSINR(int[] val){
		//indexes
		
		//values from the inputarrays
		float currentMaxVal = 0;
		
		float[]	maxValPerSINR = new float[NR_OF_SINR_VALS];
		//maxValPerSINR = BasicCalc.init(maxValPerSINR);
		
		
		for(int i=0;i<SINR.length;i++){
			
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			currentMaxVal = Math.max((float) (val[i]), currentMaxVal);
			

			if (SINR[i] != NOT_A_VALUE && SIB[i] != -1){
				//System.out.println( " currentMaxTBS: " + currentMaxTbs);
				maxValPerSINR[SINR[i]-MIN_SINR_VAL] = Math.max(maxValPerSINR[SINR[i]-MIN_SINR_VAL], currentMaxVal); //accumulated tbs

				//reset values
				currentMaxVal = 0;
			}
		}
		Print.array(maxValPerSINR);
		return maxValPerSINR;
	}
	
	

	
	
	public float[] avgSINRPerMcs(int[] mcs,int[] sinr, int[] SIB){
		
		//indexes
		int counter_ind = 0;
		int currentSinr_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		int currentSinr = 0;
		int currentMcs = 0;
		
		int[][] tempSinrPerMcs = null;
		float[]	sinrPerMcs = null;
		
		tempSinrPerMcs = new int [24][2];
		sinrPerMcs = new float[24];


		
		//tempSinrPerMcs = BasicCalc.init(tempSinrPerMcs);
		//sinrPerMcs = BasicCalc.init(sinrPerMcs);
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && sinr[i] != -1337){
				currentSinr=currentSinr+sinr[i]; 
				counter++;
			}

			if (currentMcs != mcs[i] && mcs[i] >= 0 && mcs[i] <= MAX_MCS_VAL && SIB[i] != -1 ){
				//System.out.println(mcs[i]);
				//System.out.println( " mcs: " + mcs[i] + " current sinr: " + currentSinr + " counter: " + counter);
				tempSinrPerMcs[mcs[i]][counter_ind] = tempSinrPerMcs[mcs[i]][counter_ind]+counter; //accumulated Counter
				tempSinrPerMcs[mcs[i]][currentSinr_ind] = tempSinrPerMcs[mcs[i]][currentSinr_ind]+currentSinr; //accumulated Prb
				//System.out.println(currentMcs + " " +tempSinrPerMcs[mcs[i]-1][counter_ind] + " " +  tempSinrPerMcs[mcs[i]][currentSinr_ind]);
				//reset values
				currentSinr = 0;
				counter = 0;
				currentMcs = mcs[i];
			}
		}
		for (int j=0;j<tempSinrPerMcs.length;j++){ 
			
			if(tempSinrPerMcs[j][counter_ind] != 0){ //dummy value, we thrash sinr values that we havn't got enough of.
				sinrPerMcs[j] = ((float) tempSinrPerMcs[j][currentSinr_ind]/tempSinrPerMcs[j][counter_ind]);//divide by thousand, we get Mbits/s/SINR
			}
		}
		Print.array(sinrPerMcs);
		return sinrPerMcs;
	}
	
	/*-------------------------------------------SETTERS!!!-------------------------------------------*/

	public  void setCqi(int[] SINR){
		this.SINR = SINR;
	}
	
	public void setSIB(int[] SIB){
		this.SIB = SIB;
	}

}
