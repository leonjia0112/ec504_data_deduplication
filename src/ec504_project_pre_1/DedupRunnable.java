package ec504_project_pre_1;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

// Import Chunk package
import Chunk.*;
import FileIO.FileHandler;

public class DedupRunnable {
	public static void main(String args[]) throws IOException{
		if(args.length == 1) {
			//String dir = "/home/leojia/Desktop/JavaWorkSpace/ec504_project_pre_1/src/ec504_sample_file";
			
			FileHandler fh = new FileHandler(args[0]);
			// FileHandler fh = new FileHandler(dir);
			
			Hashtable<String, String> table = new Hashtable<String, String>();
			File[] list = fh.getFiles();
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			table = bsw.handleFile(list);
			writeToFile(table, "chunk.xml");
			writeToFile(bsw.getFileHashIndex(), "index.xml");
		}else if(args.length < 1) {
			System.out.println("Please input the directory of the files to be deduplicated.");
		}else {
			System.out.println("This program only take one argument.");
		}
	}
	
	
	/**
	 * write the hashtable to xml file
	 * 
	 * @param hashtable with hash value and its chunk of data
	 */
	public static void writeToFile(Hashtable<String, String> table, String fileName) throws IOException{
		FileOutputStream fos = new FileOutputStream(fileName);
		XMLEncoder e = new XMLEncoder(fos);	
		e.writeObject(table);
		e.close();
	}
	
	public static void writeToFile(HashMap<String, ArrayList<String>> fileHashIndex, String fileName) throws IOException{
		FileOutputStream fos = new FileOutputStream(fileName);
		XMLEncoder e = new XMLEncoder(fos);	
		e.writeObject(fileHashIndex);
		e.close();
	}
}
