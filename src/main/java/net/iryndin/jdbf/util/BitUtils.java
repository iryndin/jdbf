package net.iryndin.jdbf.util;

public class BitUtils {
	public static int makeInt(byte b1, byte b2) {
		return ((b1 <<  0) & 0x000000FF) +   
			   ((b2 <<  8) & 0x0000FF00);
	}
	
	public static int makeInt(byte b1, byte b2, byte b3, byte b4) {
		return ((b1 <<  0) & 0x000000FF) +   
		       ((b2 <<  8) & 0x0000FF00) + 
		       ((b3 << 16) & 0x00FF0000) + 
		       ((b4 << 24) & 0xFF000000);
	}
	
	public static byte[] makeByte4(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF),
			(byte)((i >> 16)& 0x000000FF),
			(byte)((i >> 24)& 0x000000FF)
		};
		return b;
	}
	
	public static byte[] makeByte2(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF)
		};
		return b;
	}
	
	public static void memset(byte[] bytes, int value) {
		// this approach is a bit faster than Arrays.fill,
		// because there is no rangeCheck
		byte b = (byte)value;
		for (int i=0; i<bytes.length; i++) {
			bytes[i] = b;
		}
//		Arrays.fill(bytes, (byte)value);
	}
}
