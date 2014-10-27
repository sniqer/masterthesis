package lteAnalyzer;

public class BasicCalc {
	static int NotAValue = -1337;
	
	public static int[] init(int[] arr){
		for(int i=0;i<arr.length;i++){
				arr[i] = 0;
		}
		return arr;
	}
	public static float[] init(float[] arr){
		for(int i=0;i<arr.length;i++){
				arr[i] = 0;
		}
		return arr;
	}
	public static int[][] init(int[][] arr){
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				arr[i][j] = 0;
			}
		}
		return arr;
	}
	public static float[][] init(float[][] arr){
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				arr[i][j] = 0;
			}
		}
		return arr;
	}
	public static int[] addArrays(int[] arr1, int[] arr2){
		int[] outArr = new int[Math.min(arr1.length, arr2.length)];
		for(int i = 0; i < outArr.length; i++){
			outArr[i] = arr1[i]+arr2[i];
		}
		return outArr;
	}
	public static int findCloseValFrInd(int[] prb,int list_index){
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
	
	//tbs is bits sending per ms, divide it by thousand to get it in Mbits/s
	public static float[] tbs2Mbps(float[] tbs){
		float[] bps = new float[tbs.length];
		for(int i=0;i<tbs.length;i++)
			bps[i] = tbs[i]/1000;
		
		return bps;
	}
	
	// 1 prb is 180 khz
	public static float[] prb2hz(float[] prb){
		float[] hz = new float[prb.length];
		for(int i=0;i<prb.length;i++)
			hz[i] = prb[i]*180000;
		
		return hz;
	}
	
	//divide the average Mbit/s/SINR to the average of allocated bandwidth/SINR
	public static float[] spectralEfficiencyPerSINR(float[] bpsPerSINR, float[] hzPerSINR){
		float[] specEff = new float[hzPerSINR.length];
		for(int i=0;i<hzPerSINR.length;i++){
			if(hzPerSINR[i] != 0){
				specEff[i] = bpsPerSINR[i]/hzPerSINR[i];
			} else {
				specEff[i] = 0;
			}
			
		}
		return specEff;
	}
	
	public static float[] prefixChanger(float[] in, float prefix){
		float[] out = new float[in.length];
		for(int i=0;i<in.length;i++)
			out[i] = in[i]*prefix;
		
		return out;
	}
	
	public static void printVals(int[] array){
		System.out.println("printing vals");
		for(int i=0;i<array.length;i++)
		if(array[i] != NotAValue)
			System.out.println(array[i]);
	}
	
}


