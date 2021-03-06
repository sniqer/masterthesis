package lteAnalyzer;

public class CorruptedParameter extends Calculate{

	public static boolean timeStamp(double[] timeStamps){
		int wrong = 0;
		double timeDiff = 0;
		for(int i = 1;i<timeStamps.length;i++){
			if(timeStamps[i] != NOT_A_VALUE){
				timeDiff = timeStamps[i] - BasicCalc.findClosestPrevTimestamp(timeStamps,i); 
				if(timeDiff > 0.0011 || timeDiff < 0.0009)
					wrong++;
			}
		}
		
		
		if(wrong > 1){
			felmeddelande(wrong,"timeStamp");
			return true;
		}
		return false;
	}
	
	public static boolean SINR(int[] sinr){
		int wrong = 0;
		int toHigh = 15;
		
		for(int i = 1;i<sinr.length;i++){
			if(sinr[i] != NOT_A_VALUE){
				if(Math.abs(sinr[i] - BasicCalc.findCloseValFrInd(sinr, i)) >= toHigh)
					wrong++;
			}
		}
		
		if(wrong > 1){
			felmeddelande(wrong,"puschSINR");
			return true;
		}
		return false;
	}
	
	public static boolean checkIfOneUE(String[] bbUeRef){
		boolean out = true;
		String firstIdSeen = firstIdSeen(bbUeRef);
		for(int i=0;i<bbUeRef.length;i++){
			if(!bbUeRef[i].equals("SIB") || !bbUeRef[i].equals("firstIdSeen")){
				
			}
		}
		return out;
	}
	
	private static void felmeddelande(int wrong, String header_vector){
		     if(wrong>100000) System.out.println("There seems to be a lot of errors in "+header_vector+", check your csv file");
		else if(wrong>1000) System.out.println("there seems to be some errors in "+header_vector+", check your csv file");
		else if(wrong>10) System.out.println("There seems to be a few errors in "+header_vector+", check your csv file");
		else System.out.println("There is "+wrong+" errors in "+header_vector);
	}
	
	private static String firstIdSeen(String[] bbUeRef){
		String firstId = "";
		for(int i=0;i<bbUeRef.length;i++){
			if(!bbUeRef[i].equals("SIB")){
				firstId = bbUeRef[i];
				break;
			}
		}
		return firstId;
	}
	
}
