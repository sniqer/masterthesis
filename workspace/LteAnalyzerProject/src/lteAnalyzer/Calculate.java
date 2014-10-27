package lteAnalyzer;

public class Calculate {
	
	
	public static float[] avgValPerSINR(int[] val, int[] sinr, int[] SIB){
		
		int value_ind = 0;
		int counter_ind = 1;
		
		int currentsinr = -1000; //dummydefault value
		int counter = 0;
		int currentVal = 0;
		
		float[][] tempValPerSINR = new float[2][63];
		tempValPerSINR = BasicCalc.init(tempValPerSINR);
		
		float[] valPerSINR = new float[63];
		valPerSINR = BasicCalc.init(valPerSINR);
		
		for(int i=0;i<sinr.length;i++){
			

				//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
				if(SIB[i] != -1 && val[i] != -1337 ){
					currentVal=currentVal+val[i]; 
					counter++;
				}
			
			if (sinr[i] != -1337&& sinr[i] != -2 && SIB[i] != -1){
				System.out.println( " currentSinr: " + currentsinr+ " currentVal " + val[i]);
				tempValPerSINR[value_ind][sinr[i]] = tempValPerSINR[value_ind][sinr[i]] + currentVal; //accumulated cqi
				tempValPerSINR[counter_ind][sinr[i]] = tempValPerSINR[counter_ind][sinr[i]] + counter;
				
				//reset values
				currentsinr = sinr[i];
				counter=0;
				currentVal=0;
				
			}
		}
		for(int j=0;j<valPerSINR.length;j++){
			if (tempValPerSINR[counter_ind][j] > 1)
				valPerSINR[j] = tempValPerSINR[value_ind][j]/tempValPerSINR[counter_ind][j];
		}
			Print.array2D(tempValPerSINR);
			//Print.array(valPerSINR);
			return valPerSINR;
	}
	
	
	
	
	public static float[] maxBpsPerHzPerSINR(int[] tbs,int[] prb,int[] sinr,int[] SIB){
		
		//values from the inputarrays
		int currentsinr = 0;
		float currentTbs = 0;
		float currentPrb = 0;
		
		float[]	maxBpsPerSINR = new float[45];
		maxBpsPerSINR = BasicCalc.init(maxBpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			
			if(SIB[i] != -1 && tbs[i] != -1337){
				//System.out.println( "foundTBS: " + tbs[i] + " foundPRB: " + findCloseValFrInd(prb,i));
				currentTbs=currentTbs+tbs[i]; 
				currentPrb= BasicCalc.findCloseValFrInd(prb,i)+currentPrb;
			}
			

			if (currentsinr != sinr[i] && sinr[i] != -1337){
				//System.out.println( " currentMaxTBS: " + currentMaxTbs);
				maxBpsPerSINR[sinr[i]+15] = Math.max(maxBpsPerSINR[sinr[i]+15], currentTbs/currentPrb/180); //accumulated tbs

				//reset values
				currentsinr = sinr[i];
				currentTbs = 0;
				currentPrb = 0;
			}
		}
		Print.array(maxBpsPerSINR);
		return maxBpsPerSINR;
	}
	
	//ber�knar SINR med hj�lp av signalstyrkan och bruset.
	public static float[] sinrGenerator(int[] puschPwr, int[] puschNoiseIntPwr){
		float[] sinr = new float[puschPwr.length];
		for (int i=0; i<puschPwr.length;i++){
			sinr[i] = (int) puschPwr[i]/BasicCalc.findCloseValFrInd(puschNoiseIntPwr,i);
		}
		return sinr;
	}
	


	
	

	
	public static float[] maxValPerSINR(int[] tbs,int[] sinr,int[] SIB){
		//indexes
		
		//values from the inputarrays
		int currentsinr = 0;
		float currentMaxTbs = 0;
		
		float[]	maxBpsPerSINR = new float[45];
		maxBpsPerSINR = BasicCalc.init(maxBpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			currentMaxTbs = Math.max((float) (tbs[i]), currentMaxTbs);
			

			if (currentsinr != sinr[i] && sinr[i] != -1337 && sinr[i] != -2){
				//System.out.println( " currentMaxTBS: " + currentMaxTbs);
				maxBpsPerSINR[sinr[i]] = Math.max(maxBpsPerSINR[sinr[i]], currentMaxTbs); //accumulated tbs

				//reset values
				currentsinr = sinr[i];
				currentMaxTbs = 0;
			}
		}
		Print.array(maxBpsPerSINR);
		return maxBpsPerSINR;
	}
	
	

	
	
	public static float[] avgSINRPerMcs(int[] mcs,int[] sinr, int[] SIB, String ULorDL){
		
		//indexes
		int counter_ind = 0;
		int currentSinr_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		int currentSinr = 0;
		int currentMcs = 0;
		
		int[][] tempSinrPerMcs = null;
		float[]	sinrPerMcs = null;
		
		if(ULorDL == "DL"){
			tempSinrPerMcs = new int [29][2];
			sinrPerMcs = new float[29];
		}
		
		if(ULorDL == "UL"){
			tempSinrPerMcs = new int [24][2];
			sinrPerMcs = new float[24];
		}

		
		tempSinrPerMcs = BasicCalc.init(tempSinrPerMcs);
		sinrPerMcs = BasicCalc.init(sinrPerMcs);
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && sinr[i] != -1337){
				currentSinr=currentSinr+sinr[i]; 
				counter++;
			}

			if (currentMcs != mcs[i] && mcs[i] > 0 && mcs[i] <= 28 && SIB[i] != -1 ){
				//System.out.println(mcs[i]);
				System.out.println( " mcs: " + mcs[i] + " current sinr: " + currentSinr + " counter: " + counter);
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
}
