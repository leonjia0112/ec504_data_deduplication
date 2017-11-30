package unit_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class BinaryAsciiDetermine {

	public static void main(String[] args) throws IOException{
		String path = "/Users/peijia/Desktop/temp.npy";
		System.out.println(isPureAsciiFile(path));
	}

	public static boolean isPureAsciiFile(String inputPath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    int count = 0;
		    while (line != null && count < 100) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		        count++;
		    }
		    String everything = sb.toString();
		    return isPureAscii(everything);
		} finally {
		    br.close();
		}
	}
	
	public static boolean isPureAscii(String v) {
		byte bytearray []  = v.getBytes();
		CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
		try {
			CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
			r.toString();
		}catch(CharacterCodingException e) {
			return false;
		}
		return true;
	}
}
