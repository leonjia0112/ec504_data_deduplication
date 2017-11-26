package ec504_project_pre_1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class Chunking {

	/*
	 * Fields
	 */
	private byte[] readByte;
	private HashFunction myHash;
	private Hashtable<String, String> myTable;
	private ArrayList<FileIndexMap> fileIndexMapList;
	private FileInputStream fis;
	private final int CONST = 69069;
	private final int READ_SIZE = 1024*10;
	private final int WINDOW_SIZE = 28;
	
	private InputStream is;
	private int[] buffer;
	private int bufferPointer;
	private int segment;
	private int multiplier;
	private int max = Integer.MIN_VALUE;
	private int min = Integer.MAX_VALUE;
	
	/*
	 * Constructor
	 */
	public Chunking(){
		readByte = new byte[READ_SIZE];
		myHash = new HashFunction();
		myTable = new Hashtable<String, String>();
		fileIndexMapList = new ArrayList<FileIndexMap>();
		fis  = null;
		bufferPointer = 0;
		segment = 0;
		multiplier = 1;
		
	}

	/*
	 * @return hash table that contains both hash value and chunk data
	 */
	public Hashtable<String, String> getTable(){
		return myTable;
	}
	
	public void basicSlidingWindow(File[] fileList){
		for(File f : fileList){
			if(!f.isHidden() && f.isFile()){
				multiplier = 0;
				bufferPointer = 0;
				// chunk one file
				basicSlidingWindowSingleFileChunking(f);
				System.out.println("finished " + f.getName());
			}
		}
		System.out.println("max: " + max + ", min: " + min);
	}
	
	private void basicSlidingWindowSingleFileChunking(File inputFile){
		// mask is 0b11111111, 13 bits of 1 according to paper reference
		int chunkMask = 1 << 12;
		chunkMask--;
		
		File f = inputFile;
		FileInputStream fisBSW = null;
		FileInputStream fisChunk = null;
		BufferedInputStream bis = null;
		
		try{
			fisBSW = new FileInputStream(f);
			fisChunk = new FileInputStream(f);
			bis = new BufferedInputStream(fisBSW);
			this.is = bis;
			
			long length = bis.available();
			long curr = length;
			int hash = firstWindowHash();
			curr -= bis.available();
			
			byte[] chunk = null;
			String hashValue = null;
			boolean firstWindow = true;
			int count = 0;
			int duplicate = 0;
			
			while(curr < length){
				// window found
				if((Math.abs(hash) % 105107 == 13 && segment > 300) || segment > 5000){
					
					if(firstWindow == true){
						chunk = new byte[(int) curr];
						firstWindow = false;
					}else{
						chunk = new byte[segment];
					}
					
					if(segment < min){
						min = segment;
					}
					if(segment > max){
						max = segment;
					}
					
					if(fisChunk.read(chunk) != -1){
						hashValue = myHash.getHashValue(chunk);
						
						if(!myTable.contains(hashValue)){
							myTable.put(hashValue, byteToData(chunk));
						}else{
							duplicate++;
						}
					}
					
					// set break point
					segment = 0;
					count++;
				}
//				System.out.println(hash);
				// move to next Window
				hash = nextWindow(hash);
				curr++;
				segment++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int nextWindow(int hash) throws IOException{
		int c = is.read();
		hash = (hash - multiplier * buffer[bufferPointer]) * CONST + c;
		buffer[bufferPointer] = c;
		bufferPointer++;
		bufferPointer %= WINDOW_SIZE;
		return hash;
	}
	
	private int firstWindowHash() throws IOException{
		buffer = new int[WINDOW_SIZE];
		int hash = 0;
		
		for(int i = 0; i < WINDOW_SIZE; i++){
			int c = is.read();
			if(c == -1){
				System.out.println("File is shorter than the required windows size");
				break;
			}
			
			// store byte to buffer
			buffer[bufferPointer++] = c;
			
			// circular bufferpointer, back to head when overflow
			bufferPointer %= WINDOW_SIZE;
			
			hash = hash*CONST + c;
			if(i > 0){
				multiplier *= CONST;
			}
		}
		return hash;
	}
	
	public void fixBlockChunking(File[] fileList){
		for(File f : fileList){
			int index = 0;
			int count = 0;
			FileIndexMap fimTemp = new FileIndexMap(f.getName());

			if(f.isFile() && !f.isHidden()){
				try{
					fis = new FileInputStream(f.getAbsolutePath());

					// read in a chunk of byte file and check existency
					while(fis.read(readByte) != -1){
						String hashValue = myHash.getHashValue(readByte);
						if(!myTable.containsKey(hashValue)){
							myTable.put(hashValue, byteToData(readByte));
						}else{
//							System.out.println(count++ + "th duplicated contents found !");
						}
						fimTemp.addHashValue(index++, hashValue);
					}
					System.out.println("finished " + f.getName());
					fileIndexMapList.add(fimTemp);		
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}



	/*
	 * This method convert the byte array of chunk data to string representation
	 * 
	 * @param byte array of one chunk of data
	 * @return string representation of the read chunk bytes
	 */
	private static String byteToData(byte[] readByte){
		String result = "";
		for(byte b : readByte)
			result += (char)b;			
		return result;
	}

}
