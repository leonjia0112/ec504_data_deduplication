package Chunk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import utils.Utilities;

/**
 * This class using Basic Sliding Window algorithm to create chunk for each
 * file. This is a content define chunking algorithm that has variable chunk
 * size based on file content.
 * 
 * @author Pei Jia
 */
public class BasicSlidingWindowChunk extends Chunk{

	/**
	 * Constructor
	 */
	public BasicSlidingWindowChunk() {
		super();
	}

	public BasicSlidingWindowChunk(Hashtable<String, String> data, HashMap<String, ArrayList<String>> file) {
		super(data, file);
	}
	
	/**
	 * This method split the file content into chunk and store the value
	 * with hash value and chunk data in one table.
	 * 
	 * @param list of file object
	 * @throws IOException 
	 */
	@Override
	public void handleListFile(HashSet<File> hashSet) throws IOException {
		for(File f: hashSet) {
			chunkOneFile(f);
		}
	}

	/**
	 * This method perform chunking of single file instead of a list of file
	 * 
	 * @param single File object
	 * @throws IOException 
	 */
	@Override
	public void handleSingleFile(File file) throws IOException {
		chunkOneFile(file);
	}
	
	/**
	 * Chunk single file
	 * 
	 * @param single file
	 * @throws IOException 
	 */
	private void chunkOneFile(File f) throws IOException {
		if(f.isFile() && !f.isHidden()) {
			initilizeParam();
			makeChunk(f);
		}
	}
	
	/**
	 * For one file, chunk the file using sliding window and add those chunks
	 * to a hash table to store the chunk hash and chunk data.
	 * 
	 * @param inputFile that needs to be chunked
	 * @throws IOException 
	 */
	private void makeChunk(File inputFile) throws IOException {
		
		// Check ASCII file
		if(!Utilities.isPureAsciiFile(inputFile.getAbsolutePath())){
			System.out.println("Can't chunk non ASCII file.");
			return;
		}
		
		ArrayList<String> hashList = new ArrayList<String>();
		FileInputStream fis = null;
		FileInputStream fisForChunk = null;
		try {
			fis = new FileInputStream(inputFile);
			fisForChunk = new FileInputStream(inputFile);
			isForHash = new BufferedInputStream(fis);
			
			int chunkNumberCount = 0; // count chunks
			int duplicateChunkCount = 0; // count duplicate
			int breakpoint = 1 << 11;
			breakpoint--;
			boolean firstChunk = true;
			long totalByte = isForHash.available();
			String chunkHashValue = null;

			int windowHash = firstWindowHash(windowSize);
			long currByte = totalByte - isForHash.available(); // move the curr to next byte of the initial hash window
			while (currByte < totalByte) {
				
				// check whether window is found
				if ((windowHash & breakpoint) == 0) {
					byte[] chunk = null;
					if (firstChunk == true) {
						chunk = new byte[(int) currByte];
						firstChunk = false;
					} else {
						chunk = new byte[chunkRange];
					}
					
					// hash and save chunk
					if (fisForChunk.read(chunk) != -1) {
						chunkHashValue = getChunkHash(chunk);
						hashList.add(chunkHashValue);
						
						// DEBUG !!!
//						System.out.println(new String(chunk, "UTF-8"));
						if (!chunkData.containsKey(chunkHashValue)) {
							
							// DEBUG !!!
//							System.out.println(new String(chunk));
							chunkData.put(chunkHashValue, new String(chunk, "UTF-8"));
						} else {
							duplicateChunkCount++;
						}
					}
					chunkRange = 0;
					chunkNumberCount++;
				}
				
				// move to next window
				windowHash = nextWindowHash(windowHash);
				currByte++;
				chunkRange++;
			}
			
			// display progress
			System.out.println(chunkNumberCount + " chunks generated for: " + inputFile.getName());
			if (duplicateChunkCount != 0) {
				System.out.println(duplicateChunkCount + " duplicated chunks in: " + inputFile.getName());
			}
			
			// store file hash absolute path and data hash in order
			fileHashIndex.put(inputFile.getAbsolutePath(), hashList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					this.isForHash.close();
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Create the window hash value for the first time that is the hash
	 * value for all the element within the size of the window
	 * 
	 * @param length of the window
	 * @return hash value for each window
	 * @throws IOException
	 */
	private int firstWindowHash(int length) throws IOException {
		buffer = new int[length];
		int hash = 0;

		for (int i = 0; i < length; i++) {
			int c = isForHash.read();
			if (c == -1) 
				break;
			incrementBuffer(c);
			hash = (hash * CONST) + c;
			if (i > 0) 
				multiplier *= CONST;
		}
		return hash;
	}

	/**
	 * Use rabin fingerprint algorithm to calculate the window hash value
	 * by moving the window one byte forward
	 * 
	 * @param previous hash value of the last window
	 * @return hash value of the current window
	 * @throws IOException
	 */
	private int nextWindowHash(int preHash) throws IOException {
		int c = isForHash.read();
		// bufferpointer points at last char
		int nextHash = (preHash - multiplier*buffer[bufferPointer]) * CONST + c;
		incrementBuffer(c);
		return nextHash;
	}

	/**
	 * This is a circular buffer implementation, it keep track of the last and
	 * first byte element of the window
	 * 
	 * @param next byte value
	 */
	private void incrementBuffer(int c) {
		// circular buffer array, first char + 1 is last char
		// last char is changed to first char
		buffer[bufferPointer] = c; 
		bufferPointer++;
		bufferPointer = bufferPointer % buffer.length;
	}
	
	/**
	 * initialize parameters for sliding window
	 */
	private void initilizeParam() {
		multiplier = 1;
		bufferPointer = 0;
	}

	// Fields
	private InputStream isForHash;
	private int bufferPointer = 0;
	private int[] buffer;
	private int multiplier = 0;
	private int chunkRange = 0;
	private int windowSize = 1024;
	private final int CONST = 69069;
}
