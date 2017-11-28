package runnable;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

// Import Chunk package
import Chunk.*;
import FileIO.FileHandler;
import FileIO.*;

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
			
			FileSaveLoad.save(table, "tmp/chunk.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "tmp/index.tmp");
		}else if(args.length < 1) {
			System.out.println("Please input the directory of the files to be deduplicated.");
		}else {
			System.out.println("This program only take one argument.");
		}
	}
}
