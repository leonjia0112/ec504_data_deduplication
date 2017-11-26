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
		int mask = 1 << 13;
		mask--; // 13 bit of '1's

		ArrayList<String> hashList = new ArrayList<String>();
		
		File f = inputFile;
		FileInputStream fis = null; // For sliding window
		FileInputStream fisForChunk = null; // For chunking the input stream
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(f);
			fisForChunk = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			int chunkNumberCount = 0; // count chunks
			int duplicateChunkCount = 0; // count duplicate
			byte[] chunk = null;
			String hashValue = null;
			boolean firstChunk = true;
			
			// BufferedInputStream is faster to read byte-by-byte from
			this.isForHash = bis;

			long length = bis.available();
			long curr = length;
			int windowHash = firstWindowHash(windowSize);
			curr -= bis.available(); // move the curr to next byte of the initial hash window

			while (curr < length) {
				
				// check whether window is found
				if ((windowHash & mask) == 0) {
					
					// determine window chunk size
					if (firstChunk == true) {
						chunk = new byte[(int) curr];
						firstChunk = false;
					} else {
						chunk = new byte[chunkRange];
					}
					
					// hash and save chunk
					if (fisForChunk.read(chunk) != -1) {
						hashValue = getChunkHash(chunk);
						hashList.add(hashValue);
						
						// If not exist then save, otherwise is duplicate
						if (!chunkData.containsKey(hashValue)) {
							chunkData.put(hashValue, byteToData(chunk));
						} else {
							duplicateChunkCount++;
						}
					}
					chunkRange = 0;
					chunkNumberCount++;
				}
				windowHash = nextWindowHash(windowHash);
				curr++;
				chunkRange++;
			}
			
			// display progress
			System.out.println(chunkNumberCount + " chunks generated for: " + f.getName());
			if (duplicateChunkCount != 0) {
				System.out.println(duplicateChunkCount + " duplicated chunks in: " + f.getName());
			}
			
			fileHashIndex.put(inputFile.getName(), hashList);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// clean up
			if (fis != null) {
				try {
					this.isForHash.close();
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
		int c = isForHash.read(); // next byte from stream
		int nextHash = (preHash - multiplier*buffer[bufferPointer]) * CONST + c;
		incrementBuffer(c);
		return nextHash;
	}

	private void incrementBuffer(int c) {
		buffer[bufferPointer] = c; // circular buffer, 1st pos == lastpos
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
