package execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import Chunk.BasicSlidingWindowChunk;
import FileIO.ChunkFileHandler;
import FileIO.FileInputHandler;
import utils.FileSaveLoad;
import utils.Utilities;

public class DedupExecution {

	private long originalFileSize = 0;
	private long compressedSize = 0;
	
	/**
	 * Constructor
	 */
	public DedupExecution() {
		// initialize a new chunkFileHandler
		// remember each execution log
	}

	/**
	 * Add all the files until target directory
	 * 
	 * @param locker name, locker is a directory format
	 * @param target path
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void add(String inputLocker, String inputFile) throws IOException, ClassNotFoundException {
		// originalFileSize = FileUtils.sizeOfDirectory(new File("C:/Windows/folder"));
		File input = new File(inputFile);
		if(!input.isHidden()){
			if(input.isDirectory()){
				addDirectory(inputLocker, inputFile);
			}else if(input.isFile()){
				String[] components = Utilities.split(inputFile);
				addSingleFile(inputLocker, components[0], components[1]);
			}
		}
	}

	/**
	 * Add one file
	 * 
	 * @param locker
	 * @param path
	 * @param filelName
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void addSingleFile(String inputLocker, String path, String fileName) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + inputLocker);
		File f = new File(fileName);
		if(locker.exists()) {
			ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleSingleFile(f);

			saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
		}else {
			// create new locker
			locker.mkdirs();

			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleSingleFile(f);;

			saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
		}
	}

	private void addDirectory(String inputLocker, String path) throws ClassNotFoundException, IOException{
		File locker = new File("locker/" + inputLocker);
		
		// debug
		System.out.println(path);
		
		FileInputHandler fih = new FileInputHandler(path);
		System.out.println("show input files.\n" + fih.toString());
		if(locker.exists()) {

			ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleListFile(fih.getFiles());

			saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
		}else {
			// create new locker
			locker.mkdirs();
			
			System.out.println("this should print");
			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleListFile(fih.getFiles());

			saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
		}
	}

	private void saveLockerContent(String locker, Hashtable<String, String> t, HashMap<String, ArrayList<String>> m) throws IOException{
		FileSaveLoad.save(t, "locker/" + locker, "chunk.tmp");
		FileSaveLoad.save(m, "locker/" + locker, "index.tmp");
	}

	public void delete(String inputLocker, String inputFile) throws ClassNotFoundException, IOException{
		File input = new File(inputFile);
		if(input.exists() && !input.isHidden()){
			if(input.isDirectory()){
				deleteDirectory(inputLocker, inputFile);
			}else if(input.isFile()){
				String[] components = Utilities.split(inputFile);
				deleteSingleFile(inputLocker, components[0], components[1]);
			}
		}
	}

	/**
	 * Delete one target file in locker with target original path
	 * 
	 * @param locker name
	 * @param target path
	 * @param fileName
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void deleteSingleFile(String inputLocker, String path, String fileName) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + inputLocker);
		if(locker.exists()){
			ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);
			cfh.deleteFile(path+fileName);
		}else{
			System.out.println("locker doesn't exist. No file deleted.");
		}

	}

	/**
	 * Delete all the files in locker until a target original path
	 * 
	 * @param locker name
	 * @param target original path
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void deleteDirectory(String inputLocker, String path) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + inputLocker);
		if(locker.exists()){
			ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);
			cfh.deleteDir(path);
		}else{
			System.out.println("locker doesn't exist. No file deleted.");
		}
	}

	/**
	 * Delete the whole locker
	 * 
	 * @param locker name
	 */
	private void deleteLocker(String l){
		File locker = new File("locker/" + l);
		locker.delete();
	}


	public void get(String inputLocker, String inputFile, String targetPath) throws ClassNotFoundException, IOException{
		File locker = new File("locker/" + inputLocker);
		File f = new File(inputLocker);
		if(locker.exists() && !locker.isHidden()){
			if(isFile(inputFile)){
				System.out.println("This is a file.");
				getDirectory(inputLocker, inputFile, targetPath);
			}else{
				System.out.println("This is a directory.");
				String[] components = Utilities.split(inputFile);
				getSingleFile(inputLocker, components[0], components[1], targetPath);
			}
		}else{
			System.out.println("Locker doesn't exist. Please input exist locker.");
		}
	}

	private boolean isFile(String s){
		return s.lastIndexOf("/") < 0;
	}
	
	/**
	 * 
	 * @param locker
	 * @param path
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void getSingleFile(String locker, String path, String fileName, String targetDir) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		String output = cfh.retrieveFile(path +  fileName);
		Utilities.saveTextFile(output, targetDir);
	}

	public void getDirectory(String locker, String path, String targetDir) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		HashMap<String, ArrayList<String>> fileList = cfh.getFileIndexList();
		ArrayList<String> output = cfh.retrieveDir(path);
		for(String s : output){
			Utilities.saveTextFile(s, targetDir);
		}
	}

	/**
	 * Show all the locker names
	 * 
	 * @return
	 */
	public String showLocker() {
		String result = "";
		File locker = new File("locker/");
		for(File f : locker.listFiles()){
			result += f.getName();
		}
		return result;
	}

	/**
	 * Show all the files in locker
	 * 
	 * @param locker name
	 * @return a string contains all the files name
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String showFile(String locker) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		return cfh.getNames();
	}

}
