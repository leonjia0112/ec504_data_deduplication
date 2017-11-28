package unit_test;

import java.io.IOException;
import java.io.PrintWriter;

import FileIO.ChunkFileHandler;
import FileIO.FileSaveLoad;

public class FileDeleteTest {
	/*
	 * Unit text delete file1.txt
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ChunkFileHandler cfh = new ChunkFileHandler();
		System.out.println("File Delete Test.");
		System.out.println(cfh.getAllFileNames());
		cfh.deleteFile("file4.txt");
		System.out.println(cfh.getAllFileNames());
		FileSaveLoad.save(cfh.getChunkTable(), "tmp/", "chunk_after_delete.tmp");
		FileSaveLoad.save(cfh.getFileIndexList(), "tmp/", "index_after_delete.tmp");
	}
}
