package FileIO;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import Chunk.BasicSlidingWindowChunk;

public class ChunkFileHandler {
	
	/**
	 * Constructor 
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ChunkFileHandler(String l) throws ClassNotFoundException, IOException {
		locker = l;
		chunkTable = FileSaveLoad.loadChunkTable("locker/" + locker + "chunk.tmp");
		fileIndexList = FileSaveLoad.loadIndexFileList("locker/" + locker + "tmp/index.tmp");
	}
	
	public ChunkFileHandler() {
		locker = null;
		chunkTable = new Hashtable<String, String>();
		fileIndexList = new HashMap<String, ArrayList<String>>();
	}
	
	/**
	 * Retrive the file based on the file name is the file exist
	 * 
	 * @param fileName
	 * @return file content
	 */
	public String getFile(String fileName) {
		if(fileIndexList.containsKey(fileName)) {
			System.out.println("File " + fileName + " exists.");
			return getFileHelper(fileName);
		}else {
			System.out.println("File doesn't exist.");
			return null;
		}
	}
	
	public void reloadFiles() throws ClassNotFoundException, IOException {
		chunkTable = FileSaveLoad.loadChunkTable("tmp/chunk.tmp");
		fileIndexList = FileSaveLoad.loadIndexFileList("tmp/index.tmp");
	}
	
	/**
	 * Delete specific file based on the file name, pass if the file dosen't exist
	 * 
	 * @param fileName
	 */
	public void deleteFile(String fileName) {
		if(fileIndexList.containsKey(fileName)){
			System.out.println("Deleting " + fileName + " .");
			// add all the hash for all other files
			HashSet<String> usefullHash = new HashSet<String>();
			HashSet<String> deleteHash = new HashSet<String>();
			
			// get all the hash that is used by all other files
			for(String name : fileIndexList.keySet()){
				if(name.compareTo(fileName) != 0){
					for(String h : fileIndexList.get(name)){
						usefullHash.add(h);
					}
				}
			}
			
			// check exclude hashes
			for(String h : fileIndexList.get(fileName)) {
				if(!usefullHash.contains(h)) {
					deleteHash.add(h);
				}
			}
			
			// delete hash from chunk table
			for(String h: deleteHash) {
				chunkTable.remove(h);
			}
			
			// delete file
			fileIndexList.remove(fileName);
			
		}else {
			System.out.println("File doesn't exist.");
		}
	}
	
	/**
	 * Add more file to the chunk table.
	 * 
	 * @param f
	 */
	public void addFile(File f){
		// do something
		BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(chunkTable, fileIndexList);
		bsw.handleSingleFile(f);
		chunkTable = bsw.getTable();
		fileIndexList = bsw.getFileHashIndex();
		
	}
	
	/**
	 * Look for the file and assemble the file content by the content hash
	 * values.
	 * 
	 * @param name
	 * @return file content
	 */
	private String getFileHelper(String name) {
		ArrayList<String> indexList = fileIndexList.get(name);
		String file = "";
		for(String s: indexList) {
			file += chunkTable.get(s);
		}
		return file;
	}
	
	/**
	 * @return chunk content table
	 */
	public Hashtable<String, String> getChunkTable(){
		return chunkTable;
	}
	
	/**
	 * @return list of file name and its hash value list
	 */
	public HashMap<String, ArrayList<String>> getFileIndexList(){
		return fileIndexList;
	}
	
	public String getAllFileNames() {
		ArrayList<String> result = new ArrayList<String>();
		for(String s: fileIndexList.keySet()) {
			result.add(s);
		}
		Collections.sort(result);
		return result.toString();
	}

	// Fields
	private String locker;
	private Hashtable<String, String> chunkTable;
	private HashMap<String, ArrayList<String>> fileIndexList;
}
