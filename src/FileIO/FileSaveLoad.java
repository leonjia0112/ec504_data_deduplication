package FileIO;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class FileSaveLoad {

	/**
	 * write the hashtable to xml file
	 * 
	 * @param hashtable with hash value and its chunk of data
	 */
	public static void writeToFile(Hashtable<String, String> table, String fileName) throws IOException{
		writeToXmlFileHelper(fileName).writeObject(table);
	}
	
	public static void writeToFile(HashMap<String, ArrayList<String>> fileHashIndex, String fileName) throws IOException{
		writeToXmlFileHelper(fileName).writeObject(fileHashIndex);
	}

	public static Hashtable<String, String> readChunkTable(String name) throws FileNotFoundException{
		XMLDecoder decoder = readFromXmlFileHelper(name);
		return (Hashtable<String, String>) decoder.readObject();
	}
	
	public static HashMap<String, ArrayList<String>> readIndexFileList(String name) throws FileNotFoundException{
		XMLDecoder decoder = readFromXmlFileHelper(name);
		return (HashMap<String, ArrayList<String>>) decoder.readObject();
	}
	
	public static XMLEncoder writeToXmlFileHelper(String fileName) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(fileName);
		return new XMLEncoder(fos);	
	}
	
	public static XMLDecoder readFromXmlFileHelper(String fileName) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
		return new XMLDecoder(fis);
	}
}
