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
	 * Determine whether the path is a directory or a file path
	 * 
	 * @param path
	 * @return true is path is directory path, false if it is file path
	 */
	public static boolean isDirectory(String s){
		return s.lastIndexOf("/") == s.length() - 1;
	}
	
	/**
	 * Save file to text file
	 * 
	 * @param file content
	 * @param save to path
	 * @param save to file name
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
	 * Save file to text file
	 * 
	 * @param file content
	 * @param file name and its path together
	 * @throws FileNotFoundException
	 */
	public static void saveTextFile(String content, String pathNName) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(pathNName);
		out.write(content);
		out.close();
	}
	
	/**
	 * Check file type, ASCII or not
	 * 
	 * @param inputPath for file
	 * @return true is is ASCII, false elsewhere
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
	 * Check String type, ASCII or not
	 * 
	 * @param string to check
	 * @return true is string is ASCII, false elsewhere 
	 */
	private static boolean isPureAscii(String str) {
		byte bytearray []  = str.getBytes();
		CharsetDecoder d = Charset.forName("UTF-8").newDecoder();
		try {
			CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
			r.toString();
		}catch(CharacterCodingException e) {
			return false;
		}
		return true;
	}
}
