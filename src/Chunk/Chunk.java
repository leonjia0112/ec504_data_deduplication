package Chunk;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public abstract class Chunk {

	/**
	 * Class constructor with not data
	 */
	public Chunk() {
		chunkData = new Hashtable<String, String>();
		fileHashIndex = new HashMap<String, ArrayList<String>>();
		hashInit();
	}
	
	/**
	 * Class constructor with input chunk data and file hash list
	 */
	public Chunk(Hashtable<String, String> data, HashMap<String, ArrayList<String>> file){
		chunkData = data;
		fileHashIndex = file;
		hashInit();
	}
	
	private void hashInit(){
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// These methods need to be implemented
	public abstract void handleListFile(HashSet<File> inputFile);
	public abstract void handleSingleFile(File file);
	
	/**
	 * @return table contains both hash value and its data 
	 */
	public Hashtable<String, String> getTable(){
		return chunkData;
	}
	
	/**
	 * @return map contains file and the chunk hash value in order
	 */
	public HashMap<String, ArrayList<String>> getFileHashIndex(){
		return fileHashIndex;
	}
	
	/**
	 * Get hash value for one chunk of data
	 * 
	 * @return hash value
	 */
	protected String getChunkHash(byte[] dataBytes){
		md.update(dataBytes);
		return byteToHex(md.digest());
	}

	/**
	 *  convert the byte to hex format
	 */
	protected String byteToHex(byte[] mdbytes) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	/**
	 * This method convert the byte array of chunk data to string representation
	 * 
	 * @param byte array of one chunk of data
	 * @return string representation of the read chunk bytes
	 */
	protected String byteToData(byte[] readByte){
		String result = "";
		for(byte b : readByte)
			result += (char)b;			
		return result;
	}
	
	//Fields
	protected Hashtable<String, String> chunkData;
	protected MessageDigest md;
	protected HashMap<String, ArrayList<String>> fileHashIndex;
}
