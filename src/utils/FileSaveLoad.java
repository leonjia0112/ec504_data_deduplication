package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class FileSaveLoad {

	/**
	 * save the hashtable or hashmap to file
	 * 
	 * @param hashtable with hash value and its chunk of data
	 */
	public static void save(Object t, String path, String name) throws IOException {
		File dir = new File(path);
		if(!dir.exists()) {
			System.out.println("Creating new directory.");
			dir.mkdirs();
		}
		String output = path + name;
		FileOutputStream fos = new FileOutputStream(output);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(t);
		oos.close();
	}


	/**
	 * load the hashtable to tmp file
	 * 
	 * @param name that contains the object information
	 * @return object that load from file
	 */
	@SuppressWarnings("unchecked")
	public static Hashtable<String, String> loadChunkTable(String name) throws ClassNotFoundException, IOException{
		return (Hashtable<String, String>) loadHelper(name).readObject();
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, ArrayList<String>> loadIndexFileList(String name) throws IOException, ClassNotFoundException{
		return (HashMap<String, ArrayList<String>>) loadHelper(name).readObject();
	}

	public static ObjectInputStream loadHelper(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		return new ObjectInputStream(fis);
	}
}
