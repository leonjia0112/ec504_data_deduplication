package FileIO;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;

public class ChunkFileHandler {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream("chunk.xml");
		XMLDecoder decoder = new XMLDecoder(fis);
		Hashtable<String, String> table = (Hashtable<String, String>) decoder.readObject();
		System.out.println(table.size());
		
	}
}
