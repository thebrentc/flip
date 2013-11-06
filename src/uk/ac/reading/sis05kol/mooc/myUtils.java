package uk.ac.reading.sis05kol.mooc;

public class myUtils {

	public boolean in_array(int needle, int[] haystack) {
		 for(int item : haystack){
			 if (item == needle) return true;
		 }
		 return false;
	}
	
	public boolean booleanRandom()
	{
		return (Math.random() < 0.5);		
	}
}
