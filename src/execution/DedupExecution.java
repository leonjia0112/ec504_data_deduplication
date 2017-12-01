package execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import Chunk.BasicSlidingWindowChunk;
import utils.ChunkFileHandler;
import utils.FileInputHandler;
import utils.FileSaveLoad;
import utils.Utilities;

public class DedupExecution {

	// Fields
//	private long originalFileSize = 0;
//	private long compressedSize = 0;

	/**
	 * Constructor
	 */
	public DedupExecution() {
		// initialize a new chunkFileHandler
		// remember each execution log
	}

	/**
	 * Add all the file/s from input path to target locker, either single file or all files
	 * in directory
	 * 
	 * @param locker name, locker should be in directory format, e.x. "locker1/"
	 * @param input path, abosulte path (full path)
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void add(String inputLocker, String inputFile) throws IOException, ClassNotFoundException {
		File input = new File(inputFile);
		if(!input.isHidden()){
			if(input.isDirectory()){
				addDirectory(inputLocker, inputFile);
			}else if(input.isFile()){
				addSingleFile(inputLocker, inputFile);
			}
		}
	}

	/**
	 * Add one single file to target locker
	 * 
	 * @param locker to store the file
	 * @param path of 
	 * @param filelName
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void addSingleFile(String inputLocker, String filePath) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + inputLocker);
		File f = new File(filePath);
		if(f.exists()){
			if(locker.exists()) {
				ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);
				BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
				bsw.handleSingleFile(f);
				saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
			}else {
				locker.mkdirs();
				BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
				bsw.handleSingleFile(f);;
				saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
			}
		}else{
			System.out.println("File doesn't exist. Can't add file.");
		}
	}

	/**
	 * Add all the files in the directories
	 * 
	 * @param inputLocker
	 * @param path
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void addDirectory(String inputLocker, String path) throws ClassNotFoundException, IOException{
		File locker = new File("locker/" + inputLocker);
		File p = new File(path);
		if(p.exists()){
			FileInputHandler fih = new FileInputHandler(path);
			if(locker.exists()) {
				ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);
				BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
				bsw.handleListFile(fih.getFiles());
				saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
			}else {
				locker.mkdirs();
				BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
				bsw.handleListFile(fih.getFiles());
				saveLockerContent(inputLocker, bsw.getTable(), bsw.getFileHashIndex());
			}
		}else{
			System.out.println("Path doesn't exist. Can't add files.");
		}
	}

	/**
	 * Determine whether to delete one single file or a directory of file base on the 
	 * input path name
	 * 
	 * @param locker name
	 * @param input path name
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void delete(String inputLocker, String inputFile) throws ClassNotFoundException, IOException{
		if(Utilities.isDirectory(inputFile)){
			System.out.println("Deleting file under directory: " + inputFile);
			deleteDirectory(inputLocker, inputFile);
		}else{
			System.out.println("Deleting file : " + inputFile);
			String[] components = Utilities.split(inputFile);
			deleteSingleFile(inputLocker, components[0], components[1]);
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

			// DEBUG 
			System.out.println("deleting single file: should print");

			ChunkFileHandler cfh = new ChunkFileHandler(inputLocker);
			cfh.deleteFile(path+fileName);
			cfh.resaveFiles();
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
			cfh.resaveFiles();
		}else{
			System.out.println("locker doesn't exist. No file deleted.");
		}
	}

	/**
	 * Delete the whole locker
	 * 
	 * @param locker name
	 */
	public void deleteLocker(String inputLocker){
		File locker = new File("locker/" + inputLocker);
		File chunk = new File("locker/" + inputLocker + "chunk.tmp");
		File index = new File("locker/" + inputLocker + "index.tmp");
		chunk.delete();
		index.delete();
		if(locker.delete()){
			System.out.println("Successfully deleted " + inputLocker);
		}
	}

	/**
	 * Retrieve one single file or all the files under the same original directory
	 * This method determine whether it is a single file or directory of files bsed
	 * on the input pathname
	 * 
	 * @param target locker
	 * @param input path name
	 * @param target outputPath
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void retrieve(String inputLocker, String inputPath, String targetPath) throws ClassNotFoundException, IOException{
		File locker = new File("locker/" + inputLocker);
		if(locker.exists() && !locker.isHidden()){
			if(Utilities.isDirectory(inputPath)){
				System.out.println("This is a directory.");
				retrieveDirectory(inputLocker, inputPath, targetPath);
			}else{
				System.out.println("This is a file.");
				retrieveSingleFile(inputLocker, inputPath, targetPath);
			}
		}else{
			System.out.println("Locker doesn't exist. Please input exist locker.");
		}
	}

	/**
	 * 
	 * @param locker
	 * @param path
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void retrieveSingleFile(String locker, String fileAbsolutePath, String targetDir) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		String output = cfh.retrieveFile(fileAbsolutePath);
		if(output != null){
			
			// save file
			Utilities.saveTextFile(output, targetDir, Utilities.split(fileAbsolutePath)[1]);
		}
	}

	/**
	 * Retrieve all the files under the same original directory, and save the content to 
	 * target directory
	 * 
	 * @param locker
	 * @param original directory
	 * @param target directory
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void retrieveDirectory(String locker, String path, String targetDir) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		HashMap<String, String> output = cfh.retrieveDir(path);
		
		// save files
		for(String s : output.keySet()){
			Utilities.saveTextFile(output.get(s), targetDir, Utilities.split(s)[1]);
		}
	}

	/**
	 * Show all the locker names
	 * 
	 * @return all the locker names
	 */
	public String showLocker() {
		String result = "";
		File locker = new File("locker/");
		for(File f : locker.listFiles()){
			if(f.isDirectory()){
				result += f.getName() + "\n";
			}
		}
		return result;
	}

	/**
	 * Show all the files in target locker
	 * 
	 * @param locker name
	 * @return a string contains all the files name
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String showFile(String locker) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		//		cfh.getNames();
		return cfh.getAbsoluteNames();
	}

	/**
	 * Save a hashtable and a hashmap to locker
	 * 
	 * @param locker
	 * @param t
	 * @param m
	 * @throws IOException
	 */
	private void saveLockerContent(String locker, Hashtable<String, String> t, HashMap<String, ArrayList<String>> m) throws IOException{
		FileSaveLoad.save(t, "locker/" + locker, "chunk.tmp");
		FileSaveLoad.save(m, "locker/" + locker, "index.tmp");
	}

}
