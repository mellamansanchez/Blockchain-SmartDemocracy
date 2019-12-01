package contracts.settings;

import java.util.Collections;

import org.web3j.utils.Numeric;

public class Utils {
		
	// String to 64 length HexString (equivalent to 32 Hex lenght)
	private static String asciiToHex(String asciiValue) {
	    char[] chars = asciiValue.toCharArray();
	    StringBuffer hex = new StringBuffer();
	    for (int i = 0; i < chars.length; i++)
	    {
	        hex.append(Integer.toHexString((int) chars[i]));
	    }

	    return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
	}
	
	public static byte[] getByte32FromString(String asciiValue) {
		return Numeric.hexStringToByteArray(Utils.asciiToHex(asciiValue));
	}
}
