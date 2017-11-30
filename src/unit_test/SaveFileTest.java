package unit_test;

import java.io.File;
import java.io.FileNotFoundException;

import utils.Utilities;

public class SaveFileTest {
	public static void main(String[] args) throws FileNotFoundException{
//		String s = "output";
//		Utilities.saveTextFile(s, new File("~/Desktop/").getAbsolutePath(), "output.txt");
//		
//		File f = new File("../output.txt");
//		if(f.exists()){
//			System.out.println("passed !");
//		}
		
		System.out.println(isDirectory("/home/file.txt"));
	}
	

	public static boolean isDirectory(String s){
		return s.lastIndexOf("/") == s.length() - 1;
	}
}
