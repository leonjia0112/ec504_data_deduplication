package ec504_project_pre_1;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

// Import Chunk package
import Chunk.*;

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
			writeDataBlock(table);
		}else if(args.length < 1) {
			System.out.println("Please input the directory of the files to be deduplicated.");
		}else {
			System.out.println("This program only take one argument.");
		}
	}
	
	
	
	/**
	 * write each file with the its corresponding hashvalue and the index to record the
	 * sequence of the chunks 
	 * 
	 * @param list of the fileIndexMap object for each file
	 */
	public static void writeFileIndex(ArrayList<FileIndexMap> list) throws IOException{
		FileOutputStream fos;
		XMLEncoder e;
		
		for(int i = 0; i < list.size(); i++){
			fos = new FileOutputStream(Config.LOCAL + list.get(i).getName());
			e = new XMLEncoder(fos);
			e.writeObject(list.get(i).getMap());
			e.close();
			// System.out.println("finished write " + Config.DESKTOP + list.get(i).getName());
		}
	}
	
	/**
	 * write the hashtable to xml file
	 * 
	 * @param hashtable with hash value and its chunk of data
	 */
	public static void writeDataBlock(Hashtable<String, String> table) throws IOException{
		FileOutputStream fos = new FileOutputStream("/home/leojia/Desktop/JavaWorkSpace/ec504_project_pre_1/" + "tmp.xml");
		XMLEncoder e = new XMLEncoder(fos);	
		e.writeObject(table);
		e.close();
	}	
}
