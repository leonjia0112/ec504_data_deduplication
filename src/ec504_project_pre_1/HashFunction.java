package ec504_project_pre_1;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {

	private MessageDigest myMd;
	private FileInputStream myFis;
	
	public HashFunction(){
		try{
			myMd = MessageDigest.getInstance("SHA-1");
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}
	
	public String getHashValue(byte[] inputDataByte){
		myMd.update(inputDataByte);
		byte[] digestByte = myMd.digest();
		String resultHex = byteToHex(digestByte);
		return resultHex;	
	}
	
	
	private String byteToHex(byte[] inputByte){
		StringBuffer mySb = new StringBuffer("");
		for(int i = 0; i < inputByte.length; i++){
			mySb.append(Integer.toString((inputByte[i] & 0xff) + 0x100, 16).substring(1));
		}
		return mySb.toString();
	}
}
