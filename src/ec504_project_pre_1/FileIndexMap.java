package ec504_project_pre_1;

import java.util.*;

public class FileIndexMap {

	private HashMap<Integer, String> hashIndexMap;
	private String fileName;
	
	public FileIndexMap(String name){
		fileName = name;
		hashIndexMap = new HashMap<Integer, String>();
	}
	
	public FileIndexMap(){
		this("");
	}
	
	public String getName(){
		return this.fileName;
	}
	
	public HashMap<Integer, String> getMap(){
		return hashIndexMap;
	}
	
	public void addHashValue(Integer index, String hash){
		hashIndexMap.put(index, hash);
	}
	
	public void deleteHashValue(Integer index){
		hashIndexMap.remove(index);
	}
	
	public String getIndexHash(Integer index){
		return hashIndexMap.get(index);
	}
}
