package FileIO;

import java.io.File;

public class FileInputHandler {
	
	/**
	 * Constructor
	 */
	public FileInputHandler(String inputDirectory){
		dir = inputDirectory;
		f = new File(inputDirectory);
		fileList = f.listFiles();
		totalFileNumber = fileList.length;
	}
	
	/**
	 * Update the files from the current directory to a new directory.
	 * 
	 * @param target file directory
	 */
	public void updateDiectory(String inputDirectory) {
		dir = inputDirectory;
		f = new File(inputDirectory);
		fileList = f.listFiles();
		totalFileNumber = fileList.length;
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
	 * get single file at place the place of index
	 * 
	 * @param index of the file location
	 * @return return the file if the index is within valid range
	 */
	public File getFile(int index) {
		if(index >= 0 && index < totalFileNumber) {
			return fileList[index];
		}else {
			System.out.println("Invalid index.");
			return null;
		}
	}
		
	/**
	 * Retrieve all the file in the list
	 * 
	 * @return a list of file that in this object
	 */
	public File[] getFiles(){
		return fileList;
	}
	
	public String toString() {
		String result = "";
		for(File f: fileList) {
			result += f.getName() + "\n";
		}
		return result;
	}
	
	/**
	 * @return input file directory
	 */
	public String getDirectory() {
		return dir;
	}
	
	// Fields
	private File[] fileList;
	private String dir;
	private File f;
	private int totalFileNumber;
}
