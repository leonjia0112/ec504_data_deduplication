package FileIO;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ChunkFileHandler {
	
	public ChunkFileHandler() throws ClassNotFoundException, IOException {
		chunkTable = FileSaveLoad.loadChunkTable("tmp/chunk.tmp");
		fileIndexList = FileSaveLoad.loadIndexFileList("tmp/index.tmp");
	}
	
	public String getFile(String fileName) {
		if(fileIndexList.containsKey(fileName)) {
			return getFileHelper(fileName);
		}else {
			System.out.println("File doesn't exist.");
			return null;
		}
	}
	
	public void deleteFile(String fileName) {
		// do something
	}
	
	public String getFileHelper(String name) {
		ArrayList<String> indexList = fileIndexList.get(name);
		String file = "";
		for(String s: indexList) {
			file += chunkTable.get(s);
		}
		return file;
	}
	
	public Hashtable<String, String> getChunkTable(){
		return chunkTable;
	}
	
	public HashMap<String, ArrayList<String>> getFileIndexList(){
		return fileIndexList;
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ChunkFileHandler cfh = new ChunkFileHandler();
		String file1 = cfh.getFile("file1.txt");
		if(file1 != null) {
			System.out.println(file1);
		}
		PrintWriter out = new PrintWriter("tmp/output.txt");
		out.println(file1);
		out.close();
	}
	
	// Fields
	private Hashtable<String, String> chunkTable;
	private HashMap<String, ArrayList<String>> fileIndexList;
}
