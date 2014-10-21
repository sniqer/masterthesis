package lteAnalyzer;

public class Calculate {
	public static float[][] traceData(int[] tbs,int[] sinr,int prb[], int SIB[], int[] cqi, int[] HARQ){
		
		//indexes to output array
		int avgMbitsperSINR = 0;
		int avgBitsperSINRperHz = 1;
		int peakMbitsperSINR = 2;
		int peakBitsperSINRperHz = 3;
		int avgCqi = 4;
		int avgBler = 5;
		
		//indexes
		int counter_ind = 0;
		int acc_tbs_ind = 1;
		int max_tbs_ind = 3;
		int acc_prb_ind = 2;
		int max_prb_ind = 4;
		int avg_cqi_ind = 5;
		int cqi_counter_ind = 6;
		int acc_HARQ_ind	= 7;
		int HARQ_counter_ind = 8;
		
		//values from the inputarrays
		int counter = 0;
		int cqiCounter = 0;
		int currentCqi = 0;
		int currentsinr = 0;
		int currentTbs = 0;
		int currentBandWidth = 0;
		int maxTbs = 0;
		int maxPrb = 0;
		int currentHARQ = 0;
		int HARQCounter = 0;
		
		/*row1 : counter
		  row2 : average data rate per SINR
		  row3 : peak data rate per SINR
		  row4 : average data rate per bandwidth per SINR
		  row5 : peak data rate per bandwidth per SINR
		  row6 : average cqi per SINR
		  row7 : cqiCounter.
		 */
		int[][] TBS_SINR_BW = new int [45][7]; 
		TBS_SINR_BW = init(TBS_SINR_BW);
		
		/*row1 : average data rate per SINR
		  row2 : peak data rate per SINR
		  row3 : average data rate per bandwidth per SINR
		  row4 : peak data rate per bandwidffth per SINR
		  row5 : average cqi per SINR
		 */
		float[][]	dataperSinr = new float [5][45];
		dataperSinr = init(dataperSinr);
		
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			if(SIB[i] != -1 && tbs[i] != 0){
				currentTbs=currentTbs+tbs[i]; 
				currentBandWidth = findCloseValFrInd(prb, i)+currentBandWidth;
				
				maxTbs = Math.max(maxTbs, tbs[i]);
				//maxPrb = Math.max(maxPrb, prb[i]);
				//System.out.println(" foundBW: " + closeValFrInd(prb, i)+ " foundTBS: " + tbs[i] + " foundTBS: " + maxTbs);
				counter++;
			}
			
			//Yay!!! we've found HARQ ack and nack bits
			if(HARQ[i] != 0){
				currentHARQ = currentHARQ + HARQ[i]-1;
				HARQCounter++;
			}
			

			//acumulate the legit bandwidth and tbs and put it in TBS_SINR_BW on the sinr index.
			if (currentsinr != sinr[i] && sinr[i] != 0){
				 
				//System.out.println(" currentBW: " + currentBandWidth + " currentTBS: " + currentTbs + " maxTbs: " + maxTbs + " sinr " + sinr[i]);
				TBS_SINR_BW[sinr[i]+15][counter_ind] = TBS_SINR_BW[sinr[i]+15][0]+counter; //accumulated Counter
				TBS_SINR_BW[sinr[i]+15][acc_tbs_ind] = TBS_SINR_BW[sinr[i]+15][1]+currentTbs; //accumulated tbs
				TBS_SINR_BW[sinr[i]+15][acc_prb_ind] = TBS_SINR_BW[sinr[i]+15][2]+currentBandWidth; //accumulated allocated bandwidth
				TBS_SINR_BW[sinr[i]+15][max_tbs_ind] = Math.max(TBS_SINR_BW[sinr[i]+15][3], maxTbs); //maximum tbs
				TBS_SINR_BW[sinr[i]+15][max_prb_ind] = Math.max(TBS_SINR_BW[sinr[i]+15][4], maxPrb);
				
				currentCqi = findCloseValFrInd(cqi, i) + currentCqi;
				cqiCounter++;
				TBS_SINR_BW[sinr[i]+15][avg_cqi_ind] = currentCqi;
				TBS_SINR_BW[sinr[i]+15][cqi_counter_ind] = cqiCounter;

				TBS_SINR_BW[sinr[i]+15][acc_HARQ_ind]= TBS_SINR_BW[sinr[i]+15][acc_HARQ_ind] + currentHARQ; 
				TBS_SINR_BW[sinr[i]+15][HARQ_counter_ind]= TBS_SINR_BW[sinr[i]+15][acc_HARQ_ind] + HARQCounter; 

				//reset the accumulated values
				currentBandWidth = 0;
				currentsinr = sinr[i];
				counter = 0;
				currentTbs = 0;
				maxTbs = 0;
				HARQCounter=0;
				currentHARQ=0;
			}
		}
		
		//calculate the average throughput/SINR and throughput/SINR/Hz
		for (int j=0;j<TBS_SINR_BW.length;j++){ 
			
			if(TBS_SINR_BW[j][counter_ind] != 0 && TBS_SINR_BW[j][cqi_counter_ind] != 0){ //counter index mustn't be 0, division by zero error
				dataperSinr[avgMbitsperSINR][j] = ((float) TBS_SINR_BW[j][acc_tbs_ind]/TBS_SINR_BW[j][counter_ind]/1000);//divide by thousand, we get Mbits/s/SINR
				dataperSinr[peakMbitsperSINR][j] = (float) TBS_SINR_BW[j][max_tbs_ind]/1000;  //divide by thousand, we get Mbits/s/SINR
				
				//the division by 180 comes from one tbs is 1 ms long, multiply it by 1000 and divide by 180 000 to get bits/s/Hz/SINR
				dataperSinr[avgBitsperSINRperHz][j] = (float) TBS_SINR_BW[j][acc_tbs_ind]/TBS_SINR_BW[j][acc_prb_ind]/180; //average bits/s/Hz/SINR
				dataperSinr[peakBitsperSINRperHz][j] = (float) TBS_SINR_BW[j][max_tbs_ind]*TBS_SINR_BW[j][counter_ind]/TBS_SINR_BW[j][acc_prb_ind]/180; //average bits/s/Hz/SINR
				dataperSinr[avgCqi][j] = (float) TBS_SINR_BW[j][avg_cqi_ind]/TBS_SINR_BW[j][cqi_counter_ind];
			}
		}
		Print.array2D(dataperSinr);
		return dataperSinr;
	}
	
	
	public static float[] avgBpsPerSINR(int[] tbs,int[] sinr,int[] SIB){
		
		//indexes
		int counter_ind = 0;
		int currentTbs_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		int currentsinr = 0;
		int currentTbs = 0;
		
		int[][] tempTbsSINR = new int [45][2]; 
		tempTbsSINR = init(tempTbsSINR);
		
		float[]	bpsPerSINR = new float [45];
		bpsPerSINR = init(bpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			if(SIB[i] != -1 && tbs[i] != -1337){
				currentTbs=currentTbs+tbs[i]; 
				counter++;
			}
			

			//acumulate the legit bandwidth and tbs and put it in TBS_SINR_BW on the sinr index.
			if (currentsinr != sinr[i] && sinr[i] != -1337){
				 
				//System.out.println( " currentTBS: " + currentTbs);
				tempTbsSINR[sinr[i]+15][counter_ind] = tempTbsSINR[sinr[i]+15][0]+counter; //accumulated Counter
				tempTbsSINR[sinr[i]+15][currentTbs_ind] = tempTbsSINR[sinr[i]+15][1]+currentTbs; //accumulated tbs

				//reset values
				currentsinr = sinr[i];
				counter = 0;
				currentTbs = 0;
			}
		}
		
		//calculate the average throughput/SINR and throughput/SINR/Hz
		for (int j=0;j<tempTbsSINR.length;j++){ 
			//System.out.println("komemr hit");
			if(tempTbsSINR[j][counter_ind] != 0){ //counter index mustn't be 0, division by zero error
				bpsPerSINR[j] = ((float) tempTbsSINR[j][currentTbs_ind]/tempTbsSINR[j][counter_ind]/1000);//divide by thousand, we get Mbits/s/SINR
			}
		}
		Print.array(bpsPerSINR);
		return bpsPerSINR;
	}
	
	public static float[] maxBpsPerSINR(int[] tbs,int[] sinr,int[] SIB){
		
		//indexes
		
		//values from the inputarrays
		int currentsinr = 0;
		float currentMaxTbs = 0;
		
		float[]	maxBpsPerSINR = new float[45];
		maxBpsPerSINR = init(maxBpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			currentMaxTbs = Math.max((float) (tbs[i]/1000), currentMaxTbs);
			

			if (currentsinr != sinr[i] && sinr[i] != -1337){
				//System.out.println( " currentMaxTBS: " + currentMaxTbs);
				maxBpsPerSINR[sinr[i]+15] = Math.max(maxBpsPerSINR[sinr[i]+15], currentMaxTbs); //accumulated tbs

				//reset values
				currentsinr = sinr[i];
				currentMaxTbs = 0;
			}
		}
		Print.array(maxBpsPerSINR);
		return maxBpsPerSINR;
	}
	
	
	
	public static float[] avgBpsPerHzPerSINR(int[] tbs,int[] prb,int[] sinr,int[] SIB){
		
		//indexes
		int currentTbs_ind = 0;
		int currentPrb_ind = 1;

		
		//values from the inputarrays
		
		int currentsinr = 0;
		int currentTbs = 0;
		int currentPrb = 0;
		
		int[][] tempTbsSINR = new int [45][2]; 
		tempTbsSINR = init(tempTbsSINR);
		
		float[]	bpsPerHzPerSINR = new float [45];
		bpsPerHzPerSINR = init(bpsPerHzPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit tbs data, accumulate counter, tbs, bW and see if we have peak data rate.
			if(SIB[i] != -1 && tbs[i] != -1337){
				//System.out.println( "foundTBS: " + tbs[i] + " foundPRB: " + findCloseValFrInd(prb,i));
				currentTbs=currentTbs+tbs[i]; 
				currentPrb=findCloseValFrInd(prb,i)+currentPrb;
			}
			

			//acumulate the legit bandwidth and tbs and put it in TBS_SINR_BW on the sinr index.
			if (currentsinr != sinr[i] && sinr[i] != -1337){
				 
				//System.out.println( " currentTBS: " + currentTbs + " currentPRB: " + currentPrb);
				tempTbsSINR[sinr[i]+15][currentTbs_ind] = tempTbsSINR[sinr[i]+15][currentTbs_ind]+currentTbs; //accumulated Counter
				tempTbsSINR[sinr[i]+15][currentPrb_ind] = tempTbsSINR[sinr[i]+15][currentPrb_ind]+currentPrb; //accumulated tbs

				//reset values
				currentsinr = sinr[i];
				currentPrb = 0;
				currentTbs = 0;
			}
		}
		
		//calculate the average throughput/SINR and throughput/SINR/Hz
		for (int j=0;j<tempTbsSINR.length;j++){ 
			//System.out.println("kommer hit");
			if(tempTbsSINR[j][currentPrb_ind] != 0 && tempTbsSINR[j][currentTbs_ind] != 0){ //index mustn't be 0, division by zero error
				//System.out.println( " currentTBS: " +  tempTbsSINR[j][currentTbs_ind]  + " currentPRB: " +  tempTbsSINR[j][currentPrb_ind] );
				bpsPerHzPerSINR[j] = ((float) tempTbsSINR[j][currentTbs_ind]/tempTbsSINR[j][currentPrb_ind]/180);//divide by thousand, we get Mbits/s/SINR
			}
		}
		Print.array(bpsPerHzPerSINR);
		return bpsPerHzPerSINR;
	}
	
	
	public static float[] maxBpsPerHzPerSINR(int[] tbs,int[] prb,int[] sinr,int[] SIB){
		
		//values from the inputarrays
		int currentsinr = 0;
		float currentTbs = 0;
		float currentPrb = 0;
		
		float[]	maxBpsPerSINR = new float[45];
		maxBpsPerSINR = init(maxBpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			
			if(SIB[i] != -1 && tbs[i] != -1337){
				//System.out.println( "foundTBS: " + tbs[i] + " foundPRB: " + findCloseValFrInd(prb,i));
				currentTbs=currentTbs+tbs[i]; 
				currentPrb=findCloseValFrInd(prb,i)+currentPrb;
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
	
	public static float[] cqiPerSINR(int[] cqi,int[] sinr){
		int currentsinr = -1000; //dummydefault value
		int currentCqi_ind = 0;
		int cqiCounter_ind = 1;
		
		float[][] tempCqiPerSINR = new float[2][45];
		tempCqiPerSINR = init(tempCqiPerSINR);
		
		float[] cqiPerSINR = new float[45];
		cqiPerSINR = init(cqiPerSINR);
		
		
		
		for(int i=0;i<sinr.length;i++){
			if (currentsinr != sinr[i] && sinr[i] != -1337 && cqi[i] != -1337){
				//System.out.println( " currentCqi: " + tempCqiPerSINR[currentCqi_ind][sinr[i]+15]+ " counter: " + tempCqiPerSINR[cqiCounter_ind][sinr[i]+15]);
				tempCqiPerSINR[currentCqi_ind][sinr[i]+15] = tempCqiPerSINR[currentCqi_ind][sinr[i]+15] + cqi[i]; //accumulated cqi
				tempCqiPerSINR[cqiCounter_ind][sinr[i]+15] = tempCqiPerSINR[cqiCounter_ind][sinr[i]+15] + 1;
				
				//reset values
				currentsinr = sinr[i];
			}
		}
		for(int j=0;j<cqiPerSINR.length;j++){
			if (tempCqiPerSINR[currentCqi_ind][j] != 0 && tempCqiPerSINR[cqiCounter_ind][j] != 0 )
				cqiPerSINR[j] = tempCqiPerSINR[currentCqi_ind][j]/tempCqiPerSINR[cqiCounter_ind][j];
		}
			Print.array(cqiPerSINR);
			return cqiPerSINR;//maxBpsPerSINR;
		}
	
	public static float[] riPerSINR(int[] ri,int[] sinr){
		int currentsinr = -1000; //dummydefault value
		int currentRi_ind = 0;
		int riCounter_ind = 1;
		
		float[][] tempRiPerSINR = new float[2][45];
		tempRiPerSINR = init(tempRiPerSINR);
		
		float[] riPerSINR = new float[45];
		riPerSINR = init(riPerSINR);
		
		for(int i=0;i<sinr.length;i++){
			if (currentsinr != sinr[i] && sinr[i] != -1337 && ri[i] != -1337){
				//System.out.println( " currentCqi: " + tempCqiPerSINR[currentCqi_ind][sinr[i]+15]+ " counter: " + tempCqiPerSINR[cqiCounter_ind][sinr[i]+15]);
				tempRiPerSINR[currentRi_ind][sinr[i]+15] = tempRiPerSINR[currentRi_ind][sinr[i]+15] + ri[i]; //accumulated cqi
				tempRiPerSINR[riCounter_ind][sinr[i]+15] = tempRiPerSINR[riCounter_ind][sinr[i]+15] + 1;
				
				//reset values
				currentsinr = sinr[i];
			}
		}
		for(int j=0;j<riPerSINR.length;j++){
			if (tempRiPerSINR[currentRi_ind][j] != 0 && tempRiPerSINR[currentRi_ind][j] != 0 )
				riPerSINR[j] = tempRiPerSINR[currentRi_ind][j]/tempRiPerSINR[riCounter_ind][j];
		}
			Print.array(riPerSINR);
			return riPerSINR;
		}
	
	public static float[] blerPerSINR(int[] acksNnacks,int[] sinr){
		int currentsinr = -1000; //dummydefault value
		int currentAcks = 0;
		int ackCounter = 0;
		int currentBler_ind = 0;
		int blerCounter_ind = 1;
		
		float[][] tempBler = new float[2][45];
		tempBler = init(tempBler);
		
		float[] bler = new float[45];
		bler = init(bler);
		Print.array(acksNnacks);
		//accumulate values
		for(int i=0;i<sinr.length;i++){
			
			if(acksNnacks[i] != -1337){
				currentAcks = currentAcks + acksNnacks[i];
				ackCounter = ackCounter + 2;
			}
			
			if (currentsinr != sinr[i] && sinr[i] != -1337){
				//System.out.println( " currentCqi: " + tempCqiPerSINR[currentCqi_ind][sinr[i]+15]+ " counter: " + tempCqiPerSINR[cqiCounter_ind][sinr[i]+15]);
				tempBler[currentBler_ind][sinr[i]+15] = tempBler[currentBler_ind][sinr[i]+15] + currentAcks; //accumulated cqi
				tempBler[blerCounter_ind][sinr[i]+15] = tempBler[blerCounter_ind][sinr[i]+15] + ackCounter;
				
				//reset values
				currentsinr = sinr[i];
			}
		}
		
		//calculate mean values
		for(int j=0;j<bler.length;j++){
			if (tempBler[currentBler_ind][j] != 0 && tempBler[blerCounter_ind][j] != 0 )
				bler[j] =tempBler[currentBler_ind][j]/tempBler[blerCounter_ind][j];
		}
			Print.array(bler);
			return bler;
		}
	
	public static float[] avgPrbPerSINR(int[] prb,int[] sinr,int[] SIB){
		
		//indexes
		int counter_ind = 0;
		int currentPrb_ind = 1;

		
		//values from the inputarrays
		int counter = 0;
		int currentsinr = 0;
		int currentPrb = 0;
		
		int[][] tempPrbSINR = new int [45][2]; 
		tempPrbSINR = init(tempPrbSINR);
		
		float[]	bpsPerSINR = new float [45];
		bpsPerSINR = init(bpsPerSINR);
		
		
		for(int i=0;i<sinr.length;i++){
			//we've found legit Prb data, accumulate counter, Prb, bW and see if we have peak data rate.
			if(SIB[i] != -1 && prb[i] != -1337){
				currentPrb=currentPrb+prb[i]; 
				counter++;
			}
			

			//acumulate the legit bandwidth and Prb and put it in Prb_SINR_BW on the sinr index.
			if (currentsinr != sinr[i] && sinr[i] != -1337){
				 
				//System.out.println( " currentPrb: " + currentPrb);
				tempPrbSINR[sinr[i]+15][counter_ind] = tempPrbSINR[sinr[i]+15][0]+counter; //accumulated Counter
				tempPrbSINR[sinr[i]+15][currentPrb_ind] = tempPrbSINR[sinr[i]+15][1]+currentPrb; //accumulated Prb

				//reset values
				currentsinr = sinr[i];
				counter = 0;
				currentPrb = 0;
			}
		}
		//calculate the average throughput/SINR and throughput/SINR/Hz
		for (int j=0;j<tempPrbSINR.length;j++){ 
			//System.out.println("komemr hit");
			if(tempPrbSINR[j][counter_ind] != 0){ //counter index mustn't be 0, division by zero error
				bpsPerSINR[j] = ((float) tempPrbSINR[j][currentPrb_ind]/tempPrbSINR[j][counter_ind]);//divide by thousand, we get Mbits/s/SINR
			}
		}
		Print.array(bpsPerSINR);
		return bpsPerSINR;
	}
	
	
	
	//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
	private static int[] init(int[] arr){
		for(int i=0;i<arr.length;i++){
				arr[i] = 0;
		}
		return arr;
	}
	private static float[] init(float[] arr){
		for(int i=0;i<arr.length;i++){
				arr[i] = 0;
		}
		return arr;
	}
	private static int[][] init(int[][] arr){
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				arr[i][j] = 0;
			}
		}
		return arr;
	}
	private static float[][] init(float[][] arr){
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				arr[i][j] = 0;
			}
		}
		return arr;
	}
	
	private static int findCloseValFrInd(int[] prb,int list_index){
		int bandWidth = 0;
		int inc = list_index;
		int dec = list_index;
		while(true){
				if(prb[dec] != -1337){
					bandWidth = prb[dec];
					break;
				} else if (prb[inc] != -1337){
					bandWidth = prb[inc];
					break;
				}
				if(dec > 0){
					dec--;
				}
				if(inc<prb.length){
					inc++;
				}
		}
		//System.out.println(bandWidth+ " bandwidth");
		return bandWidth;
	}
	
	
}
