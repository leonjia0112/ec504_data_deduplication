package ec504_project_pre_1;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

public class DedupRunnable {
	public static void main(String args[]) throws IOException{
		//		File file = new File("/Users/peijia/Desktop/ec504_sample_file/file1.txt");

		HashFunction myHash = new HashFunction();

		Hashtable<String, String> myTable = new Hashtable<String, String>();
		
		byte[] readByte = new byte[1024]; // read 1kb
		int i = 0;
		FileInputStream fis  = null;
		ArrayList<FileIndexMap> fimList = new ArrayList<FileIndexMap>();
		
		// get all the files
		File inputFiles = new File(Config.PATH);
		File[] fileList = inputFiles.listFiles();
		int count = 0;
		
		for(File f : fileList){
			System.out.println(f.getPath());
			FileIndexMap fimTemp = new FileIndexMap(f.getName());
			int index = 0;
			if(f.isFile() && !f.isHidden()){
				try{


					fis = new FileInputStream(f.getAbsolutePath());

					while(fis.read(readByte) != -1){
						
						String hashValue = myHash.getHashValue(readByte);
						
						//System.out.println("readByte: " + readByte);
						System.out.println("hashValue: " + hashValue);
						//System.out.println("data: " + byteToData(readByte));
						
						String chunkData = byteToData(readByte);
						if(!myTable.containsKey(hashValue)){
							myTable.put(hashValue, chunkData);
						}else{
							System.out.println(count++ + "th duplicated contents found !");
						}
						fimTemp.addHashValue(index, hashValue);
						index++;
						
					}
					System.out.println("finished file" + f.getName());
					
					
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		Enumeration hashKeys = myTable.keys();
		
		// print out data in hash table
//		while(hashKeys.hasMoreElements()){
//			String str = (String) hashKeys.nextElement();
//			System.out.println(str.toString() + ": " + myTable.get(str));
//		}
		
		FileOutputStream fos = new FileOutputStream("/Users/peijia/Desktop/tmp.xml");
		XMLEncoder e = new XMLEncoder(fos);

		e.writeObject(myTable);
		
		e.close();
	}
	
	public static String byteToData(byte[] readByte){
		String result = "";
		
		for(byte b : readByte){
			result += (char)b;			
		}
		return result;
	}
}
