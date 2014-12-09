package MasterThesisPackage;

public class ULCalc extends Calculate{
	
	private int[] SINR;
	private int[] SIB;
	
	
	public double[] avgValPerSINR(double[] val,int minNrFoundXaxisVals){
		
		int value_ind = 0;
		int counter_ind = 1;
		
		//int currentsinr = -1000; //dummydefault value
		int counter = 0;
		double currentVal = 0;
		
		double[][] tempValPerSINR = new double[2][NR_OF_SINR_VALS];
		//tempValPerSINR = BasicCalc.init(tempValPerSINR);
		
		double[] valPerSINR = new double[NR_OF_SINR_VALS];
		BasicCalc.init(valPerSINR);
		//valPerSINR = BasicCalc.init(valPerSINR);
		
		for(int i=0;i<SINR.length;i++){
			

				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && val[i] != -1337 ){
					currentVal=currentVal+val[i]; 
					counter++;
				}
			
			if (SINR[i] != NOT_A_VALUE && SIB[i] != -1 && SINR[i] >= -25 && SINR[i] <= 40){
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
			return valPerSINR;
	}
	

	public double[] maxValPerSINR(double[] val){
		
		double currentMaxVal = 0;
		double[]	maxValPerSINR = new double[NR_OF_SINR_VALS];
		BasicCalc.init(maxValPerSINR);
		//maxValPerSINR = BasicCalc.init(maxValPerSINR);
		
		
		for(int i=0;i<SINR.length;i++){
			
			currentMaxVal = Math.max((double) (val[i]), currentMaxVal);

			if (SINR[i] != NOT_A_VALUE && SINR[i] >=-25 && SINR[i] <=40){
				maxValPerSINR[SINR[i]-MIN_SINR_VAL] = Math.max(maxValPerSINR[SINR[i]-MIN_SINR_VAL], currentMaxVal); //accumulated tbs
				currentMaxVal = 0;
			}
		}
		return maxValPerSINR;
	}
	
	

	
	public double[] avgValPerMcs(double[] val,int[] mcs){
		
		//indexes
		int counter_ind = 0;
		int currentSinr_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		double currentSinr = 0;
		int currentMcs = 0;
		
		
		double[][] tempSinrPerMcs = new double [NR_OF_UL_MCS_VALS][2];
		double[] sinrPerMcs = new double[NR_OF_UL_MCS_VALS];
		BasicCalc.init(sinrPerMcs);
		
		//tempSinrPerMcs = BasicCalc.init(tempSinrPerMcs);
		//sinrPerMcs = BasicCalc.init(sinrPerMcs);
		
		
		for(int i=0;i<SINR.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && mcs[i] != NOT_A_VALUE){
				currentSinr=currentSinr+val[i]; 
				counter++;
			}

			if (currentMcs != mcs[i] && mcs[i] >= 0 && mcs[i] <= MAX_UL_MCS_VAL && SIB[i] != -1 ){
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
//		Print.array(sinrPerMcs);
		return sinrPerMcs;
	}
	
	/*-------------------------------------------SETTERS!!!-------------------------------------------*/

	public  void setSINR(int[] SINR){
		this.SINR = SINR;
	}
	
	public void setSIB(int[] SIB){
		this.SIB = SIB;
	}
}
