package ec504_project_pre_1;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {

	/*
	 * Field
	 */
	private MessageDigest myMd;
	private FileInputStream myFis;
	
	/*
	 * Constructor: initialize MessageDigest object for hash value digeset
	 * hash function used SHA-1
	 */
	public HashFunction(){
		try{
			myMd = MessageDigest.getInstance("SHA-1");	
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Get the digest hash value of the read byte
	 * 
	 * @param read byte array from data chunk
	 * @return string representation of the hash value of chunk data
	 */
	public String getHashValue(byte[] inputDataByte){
		myMd.update(inputDataByte);
		byte[] digestByte = myMd.digest();
		String resultHex = byteToHex(digestByte);
		return resultHex;	
	}
	
	/*
	 * Convert the chunk of data from read byte to string
	 * 
	 * @param byte array contain the chunk data
	 * @return string representation of the byte array hash value
	 */
	private String byteToHex(byte[] inputByte){
		StringBuffer mySb = new StringBuffer("");
		for(int i = 0; i < inputByte.length; i++){
			mySb.append(Integer.toString((inputByte[i] & 0xff) + 0x100, 16).substring(1));
		}
		return mySb.toString();
	}
}
