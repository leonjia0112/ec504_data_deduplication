package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Utilities {

	/**
	 * Split a path string into file path and file name
	 * 
	 * @param absolute path string
	 * @return array with file path and file name
	 */
	public static String[] split(String s){
		System.out.println(s);
		File f = new File(s);
		if(f.isFile()){
			int splitIndex = s.lastIndexOf('/');
			String[] result = {s.substring(0, splitIndex+1), s.substring(splitIndex+1)};
			return result;
		}else{
			System.out.println("Invalid Input: pleaase input file absolute path");
			return null;
		}
	}
	
	
	public static void saveTextFile(String content, String path, String fileName) throws FileNotFoundException{
		System.out.print("saving " + fileName + " to " + path);
		PrintWriter out = new PrintWriter(path + fileName);
		out.write(content);
		out.close();
	}
	
	public static void saveTextFile(String content, String pathNName) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(pathNName);
		out.write(content);
		out.close();
	}
}
