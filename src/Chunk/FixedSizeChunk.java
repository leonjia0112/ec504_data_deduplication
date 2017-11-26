package Chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import ec504_project_pre_1.FileIndexMap;

public class FixedSizeChunk extends Chunk{

	public FixedSizeChunk(String inputDir) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Hashtable<String, String> handleFile(File[] inputFile) {
		for(File f : inputFile){
			int index = 0;
			int count = 0;
			byte[] readByte = null;
			FileIndexMap fimTemp = new FileIndexMap(f.getName());
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
						fimTemp.addHashValue(index++, hashValue);
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
		return chunkData;
	}
	
}
