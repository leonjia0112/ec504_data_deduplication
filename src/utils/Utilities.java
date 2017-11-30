package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Utilities {

	/**
	 * Split a path string into file path and file name
	 * 
	 * @param absolute path string
	 * @return array with file path and file name
	 */
	public static String[] split(String s){
		
		// DEBUG !!!
//		System.out.println(s);

		if(!isDirectory(s)){
			int splitIndex = s.lastIndexOf('/');
			String[] result = {s.substring(0, splitIndex+1), s.substring(splitIndex+1)};
			return result;
		}else{
			System.out.println("Invalid Input: pleaase input file absolute path");
			return null;
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDirectory(String s){
		return s.lastIndexOf("/") == s.length() - 1;
	}
	
	/**
	 * 
	 * @param content
	 * @param path
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public static void saveTextFile(String content, String path, String fileName) throws FileNotFoundException{
		
		// DEBUG
		System.out.print("saving " + fileName + " to " + path + "\n");
		
		PrintWriter out = new PrintWriter(path + fileName);
		out.write(content);
		out.close();
	}
	
	/**
	 * 
	 * @param content
	 * @param pathNName
	 * @throws FileNotFoundException
	 */
	public static void saveTextFile(String content, String pathNName) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(pathNName);
		out.write(content);
		out.close();
	}
	
	/**
	 * 
	 * @param inputPath
	 * @return
	 * @throws IOException
	 */
	public static boolean isPureAsciiFile(String inputPath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    int count = 0;
		    while (line != null && count < 100) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		        count++;
		    }
		    String everything = sb.toString();
		    return isPureAscii(everything);
		} finally {
		    br.close();
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	private static boolean isPureAscii(String v) {
		byte bytearray []  = v.getBytes();
		CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
		try {
			CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
			r.toString();
		}catch(CharacterCodingException e) {
			return false;
		}
		return true;
	}
}
