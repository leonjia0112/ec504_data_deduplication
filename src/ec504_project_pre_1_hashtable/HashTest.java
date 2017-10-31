package ec504_project_pre_1_hashtable;
import java.util.*;
import java.beans.*;
import java.io.*;

public class HashTest {
	
	public static void main(String args[]) throws IOException{
		
		
		tryReadByte();
		
	}
	
	public static void tryReadByte(){
		File file = new File("/Users/peijia/Desktop/ec504_sample_file/file1.txt");
		FileInputStream fis;
		System.out.println(file.getAbsolutePath());
		byte[] readByte = new byte[1024];
			
		try{
			fis = new FileInputStream(file.getAbsolutePath());
			fis.read(readByte, 0, 1024);
			System.out.println(readByte);
			String temp = "";
			for(byte b : readByte){
				temp += (char)b;			
			}
			
			System.out.print(temp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void tryReadWriteXml(){
	//// Create a hash map
			//Hashtable balance = new Hashtable();
			//Enumeration names;
			//String str;
			//double bal;
			//
			//balance.put("Zara", new Double(3434.34));
			//balance.put("Mahnaz", new Double(123.22));
			//balance.put("Ayan", new Double(1378.00));
			//balance.put("Daisy", new Double(99.22));
			//balance.put("Qadir", new Double(-19.08));
			//
			//// Show all balances in hash table.
			//names = balance.keys();
			//
			//while(names.hasMoreElements()) {
			//	str = (String) names.nextElement();
			//	System.out.println(str + ": " + balance.get(str));
			//}        
			//System.out.println();
			//
			//// Deposit 1,000 into Zara's account
			//bal = ((Double)balance.get("Zara")).doubleValue();
			//balance.put("Zara", new Double(bal + 1000));
			//System.out.println("Zara's new balance: " + balance.get("Zara"));
			//
			//FileOutputStream fos = new FileOutputStream("/Users/peijia/Desktop/tmp.xml");
			//XMLEncoder e = new XMLEncoder(fos);
			//
			//e.writeObject(balance);
			//
			//// decode xml file and read in hashtable
			//XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream("/Users/peijia/Desktop/tmp.xml")));
			//Hashtable newBalance = (Hashtable)d.readObject();
			//d.close();
			//
			//Enumeration newNames = balance.keys();
			//String newStr;
			//while(names.hasMoreElements()) {
			//	str = (String) names.nextElement();
			//	System.out.println(str + ": " + balance.get(str));
			//}   
	}
	
	
}
