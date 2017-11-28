package execution;

import java.io.File;
import java.io.IOException;

import Chunk.BasicSlidingWindowChunk;
import FileIO.ChunkFileHandler;
import FileIO.FileInputHandler;
import FileIO.FileSaveLoad;

public class DedupExecution {

	public void DedupExecution() {
		// initialize a new chunkFileHandler
	}

	/**
	 * Add file or directory 
	 * @param locker
	 * @param path
	 * @param filelName
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void add(String l, String path, String fileName) throws ClassNotFoundException, IOException {
		File locker = new File(l);
		File f = new File(fileName);
		if(locker.exists()) {
			ChunkFileHandler cfh = new ChunkFileHandler(l);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleSingleFile(f);

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}else {
			locker.mkdirs();

			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleSingleFile(f);;

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}
	}

	public void add(String l, String path) throws IOException, ClassNotFoundException {
		File locker = new File(l);
		FileInputHandler fih = new FileInputHandler(path);
		if(locker.exists()) {

			ChunkFileHandler cfh = new ChunkFileHandler(l);

			// create chunk object with existing data
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk(cfh.getChunkTable(), cfh.getFileIndexList());
			bsw.handleListFile(fih.getFiles());

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}else {
			// create new locker

			locker.mkdirs();

			// create new chunking object with empty content
			BasicSlidingWindowChunk bsw = new BasicSlidingWindowChunk();
			bsw.handleListFile(fih.getFiles());

			// save to locker
			FileSaveLoad.save(bsw.getTable(), "locker/" + locker.getPath() + "/", "table.tmp");
			FileSaveLoad.save(bsw.getFileHashIndex(), "locker/" + locker.getPath() + "/", "index.tmp");
		}
	}

	/**
	 * 
	 * @param locker
	 * @param path
	 * @param fileName
	 */
	public void delete(String locker, String path, String fileName) {

	}

	public void delete(String locker, String path) {

	}

	/**
	 * 
	 * @param locker
	 * @param path
	 */
	public void get(String locker, String path) {

	}

	public void get(String locker, String path, String fileName) {

	}

	/**
	 * 
	 * @param locker
	 * @return
	 */
	public String show(String locker) {
		return null;
	}

	public String show(String locker, String path) {
		return null;
	}

}
