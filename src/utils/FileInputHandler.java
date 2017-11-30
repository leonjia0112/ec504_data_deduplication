package utils;

import java.io.File;
import java.util.HashSet;

/**
 * 
 * @author peijia
 *
 */
public class FileInputHandler {
	
	/**
	 * Construct object contains all the files in this directories and
	 * all the files in its sub-directories
	 * 
	 * @param inputDirectory
	 */
	public FileInputHandler(String inputDirectory){
		root = inputDirectory;
		File rootDir = new File(inputDirectory);
		System.out.println(rootDir.getName());
		fileList = scanFiles(rootDir);
		fileList.size();
	}
	
	/**
	 * Scan all the files and directories in current root directories recursively
	 * 
	 * @param current directory
	 * @return all files in this directory and sub-directories
	 */
	private HashSet<File> scanFiles(File dir){
		System.out.println(dir.exists());
		if(dir.isDirectory()){
			File[] list = dir.listFiles();
			HashSet<File> fileSet = new HashSet<File>();
			
			// goes through all the files and directories in this current root
			for(File f : list){
				if(!f.isHidden() && f.isFile()){
					fileSet.add(f);
				}else if(!f.isHidden() && f.isDirectory()){
					fileSet.addAll(scanFiles(f));
				}
			}
			return fileSet;
		}else{
			System.out.println("This is not a directory.");
			return null;
		}
	}
	
	/**
	 * Update the files from the current directory to a new directory.
	 * 
	 * @param target file directory
	 */
	public void updateDiectory(String inputDirectory) {
		root = inputDirectory;
		File rootDir = new File(inputDirectory);
		fileList = scanFiles(rootDir);
		fileList.size();
	}
	
	/**
	 * Check whether file exist
	 * 
	 * @param name of the file
	 * @return true if the file exist, false otherwise
	 */
	public boolean hasFile(String fileName) {
		for(File f: fileList) {
			if(f.getName().compareTo(fileName) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Convert to printable string shows all the file content
	 * 
	 * @return string
	 */
	public String toString() {
		String result = "";
		for(File f: fileList) {
			result += f.getAbsolutePath() + "\n";
		}
		return result;
	}
	
	/**
	 * Retrieve all the file in the list
	 * 
	 * @return a list of file that in this object
	 */
	public HashSet<File> getFiles(){
		return fileList;
	}
	
	/**
	 * @return input file directory
	 */
	public String getRoot() {
		return root;
	}
	
	// Fields
	private HashSet<File> fileList;
	private String root;
}
