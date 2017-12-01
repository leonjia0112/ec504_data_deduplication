package utils;

import java.io.File;
import java.io.IOException;
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
	 * load content is locker exist, otherwise create a new locker
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ChunkFileHandler(String inputLocker) throws ClassNotFoundException, IOException {
		locker = inputLocker;
		File loc = new File("locker/" + locker);
		if(loc.exists() && loc.isDirectory()){
			if(new File("locker/" + locker + "chunk.tmp").exists() && 
					new File("locker/" + locker + "index.tmp").exists()){
				chunkTable = FileSaveLoad.loadChunkTable("locker/" + locker + "chunk.tmp");
				fileIndexList = FileSaveLoad.loadIndexFileList("locker/" + locker + "index.tmp");
			}else{
				System.out.println("Empty locker. Exiting.");
			}
		}else if(!loc.exists() && loc.isDirectory()){
			loc.mkdirs();
			chunkTable = new Hashtable<String, String>();
			fileIndexList = new HashMap<String, ArrayList<String>>();
		}else{
			System.out.println("locker name must be a directory to initialize.");
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
			return retrieveFileHelper(fileName);
		}else {
			System.out.println("File " + fileName + " doesn't exist.");
			System.out.println("Can't not retrieve file.");
			return null;
		}
	}
	
	/**
	 * Retrieve all the file under the same original directory
	 * 
	 * @param original directory
	 * @return map contains both original file name (original absolute file path) and
	 * 			file content
	 */
	public HashMap<String, String> retrieveDir(String dir){
		HashMap<String, String> result = new HashMap<String, String>();
		for(String s : fileIndexList.keySet()){
			String  originalPath = Utilities.split(s)[0];
			if(originalPath.length() >= dir.length() && 
					originalPath.substring(0, dir.length()).compareTo(dir) == 0 ){
				result.put(s, retrieveFileHelper(s));
			}
		}
		return result;
	}
	
	/**
	 * Look for the file and assemble the file content by the content hash
	 * values.
	 * 
	 * @param full name of the file, absolute path
	 * @return file content
	 */
	private String retrieveFileHelper(String name) {
		if(fileIndexList.containsKey(name)){
			ArrayList<String> indexList = fileIndexList.get(name);
			String content = "";
			for(String s: indexList) {
				content += chunkTable.get(s);
			}
			return content;
		}else{
			return null;
		}
		
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
			System.out.println("File " + fileName + "doesn't exist.");
		}
	}

	/**
	 * Delete all the file that was in the given original path
	 * 
	 * @param original path for file
	 */
	public void deleteDir(String dir) {
		HashSet<String> deleteList = new HashSet<String>();
		
		// search for all the file file that needs to be deleted
		for(String s : fileIndexList.keySet()){
			String  originalPath = Utilities.split(s)[0];
			if(originalPath.length() >= dir.length() && 
					originalPath.substring(0, dir.length()).compareTo(dir) == 0 ){
				deleteList.add(s);
			}
		}
		
		// delete those files
		for(String d: deleteList){
			deleteFile(d);
		}
	}

	/**
	 * Add one file to the chunk table.
	 * 
	 * @param f
	 * @throws IOException 
	 */
	public void addFile(File f) throws IOException{
		BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(chunkTable, fileIndexList);
		bsw.handleSingleFile(f);
		chunkTable = bsw.getTable();
		fileIndexList = bsw.getFileHashIndex();
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
	 * 
	 * @throws IOException
	 */
	public void resaveFiles() throws IOException {
		FileSaveLoad.save(chunkTable, "locker/" + locker, "chunk.tmp");
		FileSaveLoad.save(fileIndexList, "locker/" + locker, "index.tmp");
	}
	
	// Fields
	private String locker;
	private Hashtable<String, String> chunkTable;
	private HashMap<String, ArrayList<String>> fileIndexList;

}
