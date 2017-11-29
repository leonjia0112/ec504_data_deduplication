package unit_test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFileType {
	public static void main(String[] args) throws IOException{
		String path = "/Users/peijia/Desktop/body_zones.png ";
		File f = new File(path);
		System.out.println(Files.probeContentType(f.toPath()));
		
		System.out.println("a".lastIndexOf("/"));
	}
}
