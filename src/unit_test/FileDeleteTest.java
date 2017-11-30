package unit_test;

import java.io.IOException;
import java.io.PrintWriter;

import utils.ChunkFileHandler;
import utils.FileSaveLoad;

public class FileDeleteTest {
	/*
	 * Unit text delete file1.txt
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ChunkFileHandler cfh = new ChunkFileHandler("locker1");
		System.out.println("File Delete Test.");
		System.out.println(cfh.getNames());
		cfh.deleteFile("file4.txt");
		System.out.println(cfh.getNames());
		FileSaveLoad.save(cfh.getChunkTable(), "tmp/", "chunk_after_delete.tmp");
		FileSaveLoad.save(cfh.getFileIndexList(), "tmp/", "index_after_delete.tmp");
	}
}
