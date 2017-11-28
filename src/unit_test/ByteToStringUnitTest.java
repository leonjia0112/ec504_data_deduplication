package unit_test;

public class ByteToStringUnitTest {

	public static void main(String[] argv) {

		String example = "This is an example, he's good.";
		byte[] bytes = example.getBytes();

		System.out.println("Text : " + example);
		System.out.println("Text [Byte Format] : " + bytes);
		System.out.println("Text [Byte Format] : " + bytes.toString());
		System.out.println("old method: " + byteToData(bytes));
		String s = new String(bytes);
		System.out.println("Text Decryted : " + s);


	}
	
	/**
	 * This method convert the byte array of chunk data to string representation
	 * 
	 * @param byte array of one chunk of data
	 * @return string representation of the read chunk bytes
	 */
	public static String byteToData(byte[] readByte){
		String result = "";
		for(byte b : readByte)
			result += (char)b;			
		return result;
	}
}

