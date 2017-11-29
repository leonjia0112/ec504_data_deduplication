package Chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * This class create chunk by splitting the file into equal size windows,
 * then a hash value is created for finding duplicated chunk.
 * 
 * @author peijia
 *
 */
public class FixedSizeChunk extends Chunk{

	/**
	 * Constructor
	 * 
	 * @param directory contains all the files
	 */
	public FixedSizeChunk(String inputDir) {
		super();
	}

	@Override
	public void handleListFile(HashSet<File> inputFile) {
		for(File f : inputFile){
			int index = 0;
			int count = 0;
			byte[] readByte = null;
			ArrayList<String> hashList = new ArrayList<String>();
			FileInputStream fis;
			if(f.isFile() && !f.isHidden()){
				try{
					fis = new FileInputStream(f.getAbsolutePath());

					// read in a chunk of byte file and check existency
					while(fis.read(readByte) != -1){
						String hashValue = getChunkHash(readByte);
						if(!chunkData.containsKey(hashValue)){
							chunkData.put(hashValue, byteToData(readByte));
						}else{
//							System.out.println(count++ + "th duplicated contents found !");
						}
						hashList.add(hashValue);
					}
					System.out.println("finished " + f.getName());
					// fileIndexMapList.add(fimTemp);		
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void handleSingleFile(File file) {
		// TODO Auto-generated method stub
	}	
}
