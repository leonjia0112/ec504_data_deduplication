package unit_test;

import java.io.IOException;
import java.io.PrintWriter;

import FileIO.ChunkFileHandler;

public class FileRetriveTest {
	
	/*
	 * Unit text get file1.txt
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		System.out.println("File retrieve test.");
		ChunkFileHandler cfh = new ChunkFileHandler();
		String file1 = cfh.getFile("file1.txt");
		if(file1 != null) {
			System.out.println("File successfully retrieved.");
		}
		PrintWriter out = new PrintWriter("tmp/output.txt");
		out.println(file1);
		out.close();
	}
}
