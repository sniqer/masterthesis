package lteAnalyzer;

public class Print {

	public static void array(String[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	public static void array(double[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	public static void array(float[] stringArr){
		System.out.println("printing!!!!!!");
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	public static void array(int[] stringArr){
		for(int i=0;i<stringArr.length;i++){
			System.out.println(stringArr[i]);
		}
	}
	
	public static void array2D(float[][] outputdata){
		System.out.println("printing!!!!!!");
		for(int i=0;i<outputdata[0].length;i++){
			for(int j=0;j<outputdata.length;j++){
				System.out.print(outputdata[j][i] + " "); 
			}
			System.out.println();
		}
	}
	public static void array2D(int[][] intArr){
		for(int i=0;i<intArr.length;i++){
			for(int j=0;j<intArr[0].length;j++){
				System.out.print(intArr[i][j] + " "); 
			}
			System.out.println();
		}
	}
	public static void array2D(String[][] intArr){
		for(int i=0;i<intArr.length;i++){
			for(int j=0;j<intArr[0].length;j++){
				System.out.print(intArr[i][j] + " "); 
			}
			System.out.println();
		}
	}
	
}
