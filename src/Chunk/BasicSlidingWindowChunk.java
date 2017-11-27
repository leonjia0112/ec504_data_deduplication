package Chunk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class BasicSlidingWindowChunk extends Chunk{

	/**
	 * Constructor
	 */
	public BasicSlidingWindowChunk() {
		super();
	}

	@Override
	public Hashtable<String, String> handleFile(File[] inputFile) {
		for(File f: inputFile) {
			if(f.isFile() && !f.isHidden()) {
				initilizeParam();
				makeChunk(f);
			}
		}
		return chunkData;
	}

	private void makeChunk(File inputFile) {
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
//					System.out.printf("wh: %d, bp: %d\n", windowHash, breakpoint);
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
						
						if (!chunkData.containsKey(chunkHashValue)) {
							chunkData.put(chunkHashValue, byteToData(chunk));
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
			
			// store file hash index
			fileHashIndex.put(inputFile.getName(), hashList);
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

	private int firstWindowHash(int length) throws IOException {
		buffer = new int[length]; // create circular buffer
		int hash = 0;

		// calculate the hash sum of p^n * a[x]
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

	private int nextWindowHash(int preHash) throws IOException {
		int c = isForHash.read();
		// bufferpointer points at last char
		int nextHash = (preHash - multiplier*buffer[bufferPointer]) * CONST + c;
		incrementBuffer(c);
		return nextHash;
	}

	private void incrementBuffer(int c) {
		// circular buffer array, first char + 1 is last char
		// last char is changed to first char
		buffer[bufferPointer] = c; 
		bufferPointer++;
		bufferPointer = bufferPointer % buffer.length;
	}
	
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
