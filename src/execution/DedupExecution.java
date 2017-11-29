package execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Chunk.BasicSlidingWindowChunk;
import FileIO.ChunkFileHandler;
import FileIO.FileInputHandler;
import utils.FileSaveLoad;
import utils.Utilities;

public class DedupExecution {

	/**
	 * Constructor
	 */
	public void DedupExecution() {
		// initialize a new chunkFileHandler
		// remember each execution log
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
	public void add(String l, String path, String fileName) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + l);
		File f = new File(fileName);
		if(locker.exists()) {
			ChunkFileHandler cfh = new ChunkFileHandler(l);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleSingleFile(f);

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}else {
			// create new locker
			locker.mkdirs();

			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleSingleFile(f);;

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}
	}

	/**
	 * Add all the files until target directory
	 * 
	 * @param locker name
	 * @param target path
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void add(String l, String path) throws IOException, ClassNotFoundException {
		File locker = new File(l);
		FileInputHandler fih = new FileInputHandler(path);
		if(locker.exists()) {

			ChunkFileHandler cfh = new ChunkFileHandler(l);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleListFile(fih.getFiles());

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}else {
			// create new locker
			locker.mkdirs();

			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleListFile(fih.getFiles());

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
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
	public void delete(String l, String path, String fileName) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + l);
		if(locker.exists()){
			ChunkFileHandler cfh = new ChunkFileHandler(l);
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
	public void delete(String l, String path) throws ClassNotFoundException, IOException {
		File locker = new File("locker/" + l);
		if(locker.exists()){
			ChunkFileHandler cfh = new ChunkFileHandler(l);
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
	public void delete(String l){
		File locker = new File("locker/" + l);
		locker.delete();
	}
	
	/**
	 * 
	 * @param locker
	 * @param path
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void get(String locker, String path, String fileName, String targetDir) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(locker);
		String output = cfh.retrieveFile(path +  fileName);
		Utilities.saveTextFile(output, targetDir);
	}

	public void get(String locker, String path, String targetDir) throws ClassNotFoundException, IOException {
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
	public String showFile(String l) throws ClassNotFoundException, IOException {
		ChunkFileHandler cfh = new ChunkFileHandler(l);
		return cfh.getNames();
	}

}
