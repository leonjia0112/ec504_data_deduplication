package FileIO;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import Chunk.BasicSlidingWindowChunk;
import utils.FileSaveLoad;
import utils.Utilities;

public class ChunkFileHandler {

	/**
	 * Constructor 
	 * 
	 * load content is locker exist, otherwise create a new locker
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ChunkFileHandler(String l) throws ClassNotFoundException, IOException {
		locker = l;
		File loc = new File("locker/" + locker);
		
//		System.out.println("locker/" + locker);
//		System.out.println(loc.isDirectory());
//		System.out.println(loc.exists());
		if(loc.exists() && loc.isDirectory()){
			chunkTable = FileSaveLoad.loadChunkTable("locker/" + locker + "chunk.tmp");
			fileIndexList = FileSaveLoad.loadIndexFileList("locker/" + locker + "index.tmp");
		}else if(!loc.exists() && loc.isDirectory()){
			loc.mkdirs();
			chunkTable = new Hashtable<String, String>();
			fileIndexList = new HashMap<String, ArrayList<String>>();
		}else{
			System.out.println("locker name must be a directory.");
		}
	}

	/**
	 * Retrieve the file based on the file name is the file exist
	 * 
	 * @param fileName
	 * @return file content
	 */
	public String retrieveFile(String fileName) {
		if(fileIndexList.containsKey(fileName)) {
			System.out.println("File " + fileName + " exists.");
			return getFileHelper(fileName);
		}else {
			System.out.println("File doesn't exist.");
			return null;
		}
	}
	
	public ArrayList<String> retrieveDir(String dir){
		ArrayList<String> result = new ArrayList<String>();
		for(String s : fileIndexList.keySet()){
			if(Utilities.split(s)[0].compareTo(dir) == 0){
				result.add(s);
			}
		}
		return result;
	}
	
	/**
	 * reload chunk data and file index from locker
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void reloadFiles() throws ClassNotFoundException, IOException {
		chunkTable = FileSaveLoad.loadChunkTable("locker/" + locker + "chunk.tmp");
		fileIndexList = FileSaveLoad.loadIndexFileList("locker/" + locker + "index.tmp");
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
	 * Delete all the file that was in the given original path
	 * 
	 * @param original path for file
	 */
	public void deleteDir(String path) {
		for(String s : fileIndexList.keySet()){
			String dir = Utilities.split(s)[0];
			if(dir.compareTo(path) == 0){
				deleteFile(s);
			}
		}
	}

	/**
	 * Add more file to the chunk table.
	 * 
	 * @param f
	 */
	public void addFile(File f){
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

	/**
	 * @return all the file names in this locker in one string
	 */
	public String getNames() {
		ArrayList<String> result = new ArrayList<String>();
		for(String s: fileIndexList.keySet()) {
			result.add(Utilities.split(s)[1]);
		}
		Collections.sort(result);
		String output = "";
		for(String s : result){
			output += s + "\n";
		}
		return output;
	}

	/**
	 * @return all the file names in this locker in one string
	 */
	public String getAbsoluteNames() {
		ArrayList<String> result = new ArrayList<String>();
		for(String s: fileIndexList.keySet()) {
			result.add(s);
		}
		Collections.sort(result);
		String output = "";
		for(String s : result){
			output += s + "\n";
		}
		return output;
	}
	
	// Fields
	private String locker;
	private Hashtable<String, String> chunkTable;
	private HashMap<String, ArrayList<String>> fileIndexList;

}
