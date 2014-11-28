package lteAnalyzer;

public class BasicCalc extends Calculate{
	
	
	private static String[] header;
	private int[] SIB;
	

	public static int[] addArrays(int[] arr1, int[] arr2){
		int[] outArr = new int[Math.min(arr1.length, arr2.length)];
		for(int i = 0; i < outArr.length; i++){
			if(arr1[i] == NOT_A_VALUE && arr2[i] == NOT_A_VALUE) outArr[i] = 0;
			else if (arr1[i] == NOT_A_VALUE) outArr[i] = arr2[i];
			else if (arr2[i] == NOT_A_VALUE) outArr[i] = arr1[i];
			else outArr[i] = arr1[i] + arr2[i];
		}
		return outArr;
	}
	public static float[] addArrays(float[] arr1, float[] arr2){
		float[] outArr = new float[Math.min(arr1.length, arr2.length)];
		for(int i = 0; i < outArr.length; i++){
			if(arr1[i] == NOT_A_VALUE && arr2[i] == NOT_A_VALUE) outArr[i] = 0;
			else if (arr1[i] == NOT_A_VALUE) outArr[i] = arr2[i];
			else if (arr2[i] == NOT_A_VALUE) outArr[i] = arr1[i];
			else outArr[i] = arr1[i] + arr2[i];
		}
		return outArr;
	}
	public static int findCloseValFrInd(int[] list,int list_index){
		int listVal = NOT_A_VALUE;
		int inc = list_index;
		int dec = list_index;
		int loopInd = 0;
		while(loopInd < list.length){
				if(list[dec] != NOT_A_VALUE){
					listVal = list[dec];
					break;
				} else if (list[inc] != NOT_A_VALUE){
					listVal = list[inc];
					break;
				}
				if(dec > 0){
					dec--;
				}
				if(inc<list.length){
					inc++;
				}
				loopInd++;
		}
		return listVal;
	}
	
	public static double findCloseValFrInd(double[] list,int list_index){
		double listVal = NOT_A_VALUE;
		int inc = list_index;
		int dec = list_index;
		int loopInd = 0;
		while(loopInd < list.length){
				if(list[dec] != NOT_A_VALUE){
					listVal = list[dec];
					break;
				} else if (list[inc] != NOT_A_VALUE){
					listVal = list[inc];
					break;
				}
				if(dec > 0){
					dec--;
				}
				if(inc<list.length){
					inc++;
				}
				loopInd++;
		}
		return listVal;
	}
	

	// 1 prb is 180 khz
	public static float[] prb2hz(float[] prb){
		float[] hz = new float[prb.length];
		for(int i=0;i<prb.length;i++)
			hz[i] = prb[i]*180000;
		
		return hz;
	}
	

	
	public static double[] multiplyArray(double[] in, double multiplier){
		double[] out = new double[in.length];
		for(int i=0;i<in.length;i++)
			out[i] = in[i]*multiplier;
		return out;
	}
	
	
	public static String[][] transpose(String[][] stringArr){
		
		String[][] output = new String[stringArr[0].length][stringArr.length];
		for(int i=0;i<stringArr.length;i++){
			for(int j=0;j<stringArr[0].length;j++){
				String tjoho = stringArr[i][j];
				output[j][i] = tjoho;
			}
		}
		return output;
	}
	

	
	public static int findHeaderIndex(String headerName,int doublett){
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
		System.out.println("hitta inget index till " + headerName);
		return -1;
	}
	//2014-10-22 12:15:00.329414
	public static double[] timeConverter(String[] stringTime){
		double[] doubleTime = new double[stringTime.length];
		for (int i=0;i<stringTime.length;i++){
			if(!stringTime[i].contains("x")){
				stringTime[i] = stringTime[i].replace(":", "");
				doubleTime[i] = Double.parseDouble(stringTime[i].trim());
			} else {
				doubleTime[i] = (double) NOT_A_VALUE;
			}
		}
		return doubleTime;
	}
	
	public static String[] discardDate(String[] stringTime){
	
		for (int i=0;i<stringTime.length;i++){
			if(!stringTime[i].contains("x")){
				stringTime[i] = stringTime[i].split(" ")[2];
				stringTime[i] = stringTime[i].replace(":", "");
				stringTime[i] = stringTime[i].trim();
			} else {
				stringTime[i] = "Not a valid timestamp!";
			}
		}
	
		return stringTime;
	}
	
	public static int numberOfVals(int[] array){
		int nrOfVals = 0;
		
		for(int i=0;i<array.length;i++){
			if(array[i] != NOT_A_VALUE)
				nrOfVals++;
		}
		return nrOfVals;
	}

	//makes tbs to throughput by looking at time stamp, length of throughput[] shall be the same as the others
	public static double[] tbs2throughput(int[] tbs, String[] ndf,double[] timestamps, int[] sf){
		int[] acknowledged_packets = tbs;
		Print.array(ndf);
		for(int i = 0;i<tbs.length;i++){
			
			if(ndf[i].contains("N")) 
				acknowledged_packets[i] = 0;
		}
			
		double[] throughput = new double[tbs.length];
		double timeDiff = 0;
		for(int j=1;j<tbs.length;j++){
			if(acknowledged_packets[j] != NOT_A_VALUE){
				timeDiff = timestamps[j] - timestamps[j-1];
				if (timeDiff != 0.0) {
					throughput[j] = acknowledged_packets[j]/timeDiff;
				} else {
					//full�sning
					throughput[j] = acknowledged_packets[j]/(timestamps[j] - timestamps[j-2]);
					System.out.println("h�r �r tidsdiffen 0: " + (timestamps[j] - timestamps[j-2]) +" " + throughput[j]);
				}
			}
		}
		
		return throughput;
	}
	

	
	public static double getBiggest(double[] array){
		double max = Double.MIN_VALUE;
		for(int i=0;i<array.length;i++)
			if(array[i] > max)
				max=array[i];
		
		return max;
	}
	
	public static int getBiggest(int[] array){
		int max = Integer.MIN_VALUE;
		for(int i=0;i<array.length;i++)
			if(array[i] > max)
				max=array[i];
		
		return max;
	}
	
	public static double getSmallest(double[] array){
		double smallest = Double.MAX_VALUE;
		for(int i=0;i<array.length;i++)
			if(array[i] < smallest && array[i] != NOT_A_VALUE)
				smallest=array[i];
		
		return smallest;
	}
	
	public static int getSmallest(int[] array){
		int smallest = Integer.MAX_VALUE;
		for(int i=0;i<array.length;i++)
			if(array[i] < smallest && array[i] != NOT_A_VALUE)
				smallest=array[i];
		
		return smallest;
	}
	
	public static double[] intArr2DoubleArr(int[] arr){
		double[] doubleArr = new double[arr.length];
		for(int i=0;i<arr.length;i++)
			doubleArr[i] = (double) arr[i];
		return doubleArr;
	}
	
	//returns the time differens between time1 and time2 in seconds, input eg 12:13:14.001000, 12:13:14.002000 => output 0.001 
	public static double timeSubtract(String time1, String time2){
		
		

		int hour1 = Integer.parseInt((String) time1.subSequence(0, 2));
		int hour2 = Integer.parseInt((String) time2.subSequence(0, 2));		
		
		int min1 = Integer.parseInt((String) time1.subSequence(2, 4));
		int min2 = Integer.parseInt((String) time2.subSequence(2, 4));		
		int sec1 = Integer.parseInt((String) time1.subSequence(4, 6));
		int sec2 = Integer.parseInt((String) time2.subSequence(4, 6));
		

		
		double msec1 = Double.parseDouble((String) time1.subSequence(6, time1.length()));
		double msec2 = Double.parseDouble((String) time2.subSequence(6, time2.length()));
		
		if(sec2<sec1){
			sec2 = sec2 + 60;
			min2 = min2-1;
		}
		if(min2<min1){
			min2 = min2+60;
			hour2 = hour2 - 1;
		}

		double timeDiff = hour2 - hour1 + min2 - min1 + sec2 - sec1 + msec2 - msec1;
		if(timeDiff==0)
			System.out.println("time difference is 0");
		
		return timeDiff;
	}
	
	//input 12:13:14:.11111, .... , 12:14:14.111111
	public static double[] startTimeFrZero(String[] timeStamps){
		int firstTimeStampIndex = findFirstTimeStamp(timeStamps);
		String firstTimeStamp = timeStamps[firstTimeStampIndex];
		double[] timeFrZero = new double[timeStamps.length];
		for(int i = firstTimeStampIndex; i<timeStamps.length ;i++){
			//if(timeStamps[i] != NOT_A_VALUE)
				//timeFrZero[i] = timeSubtract(firstTimeStamp,timeStamps[i]);
			timeFrZero[i] = timeSubtract(firstTimeStamp,timeStamps[i]);
		}
		//Print.array(timeFrZero);
		return timeFrZero;
	}
	
	public static int findFirstTimeStamp(String[] timeStamp){

		double[] timeStampDigits = timeConverter(timeStamp);
		for(int i=0;i<timeStamp.length;i++){
			if(timeStampDigits[i] != NOT_A_VALUE){
				//firstTimeStamp[0] = timeStamp[i];
				return i;
			}
		}
		System.out.println("didnt find any timestamps");
		return -1;
	}
	
	
	public void setHeader(String[] header) {
		// TODO Auto-generated method stub
		this.header = header;
	}
	
	public void setSIB(int[] SIB) {
		// TODO Auto-generated method stub
		this.SIB = SIB;
	}
}


