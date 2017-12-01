import java.io.IOException;
import execution.DedupExecution;

/**
 * Command line user interface for this data deduplication application
 * 
 * @author Pei Jia
 */
public class Dedup {
	private static String helpPrompt = "General Options:\n"
			+ "\n  -a, -add\tAdd File/s to locker\n"
			+ "\t\t<locker name> <file input path>\n\t\t  Add one file to target locker\n"
			+ "\t\t<locker> <directory input path>\n\t\t  Add all files in directory to target locker\n"
			+ "\n  -d, -delete\tDelete File/s from locker, or delete locker\n"
			+ "\t\t<locker name>\n\t\t  Delete whole locker\n"
			+ "\t\t<locker name> <file path>\n\t\t  Delete one file from target locker, full absolute original path of the file\n"
			+ "\t\t<locker name> <directory path>\n\t\t  Delete all the file in the same original path from target locker\n"
			+ "\n  -r, -retrieve\tRetrieve File/s from locker\n"
//			+ "\t\t<locker>\t\t  Retrieve whole locker\n"
			+ "\t\t<locker name> <file path> <target output path>\n\t\t  Retrieve one file from target locker, full absolute original path of the file\n"
			+ "\t\t<locker name> <directory path> <target output path>\n\t\t  Retrieve all the file in the same original path from target locker\n"
			+ "\n  -s, -show\tShow content\n"
			+ "\t\t-locker\t\t\t  Display all lockers\n"
			+ "\t\t-file <locker name>\t  Display all files in target locker\n"
			+ "\n  -h, -help\tHelp Prompt";
			

	/**
	 * Command line user interface, supporting following file storage operations. Addition, deletion
	 * retrieve, and display content.
	 * 
	 * @param input command line arguments to use the interface
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if(args.length > 0) {
			DedupExecution de = new DedupExecution();
			switch(args[0]) {
			case "-a":
			case "-add": // add option
				if(args.length == 3){
					System.out.println("Adding file/s.");
					de.add(args[1] + "/", args[2]);
				}else{
					System.out.println("Invalid -a arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-d": 
			case "-delete": // delete option
				if(args.length == 3){
					System.out.println("Deleting file/s.");
					de.delete(args[1] + "/", args[2]);
				}else if(args.length == 2){
					System.out.println("Deleting locker.");
					de.deleteLocker(args[1] + "/");
				}else{
					System.out.println("Invalid -d arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-r": 
			case "-retrieve": // retrieve option
				if(args.length == 4){
					System.out.println("Retrieving file/s.");
					de.retrieve(args[1] + "/", args[2], args[3]);
				}else{
					System.out.println("Invalid -r arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-s": 
			case "-show": // show option
				if(args.length >= 2){
					if(args[1].compareTo("-locker") == 0){
						System.out.println("Here are all the existing lockers.");
						System.out.println(de.showLocker());
					}else if(args[1].compareTo("-file") == 0){
						System.out.println("Here are all the files in locker: " + args[2]);
						System.out.println(de.showFile(args[2] + "/"));
					}
				}else{
					System.out.println("Need more arguments for -s option.");
					System.out.println(helpPrompt);
				}
				break;
			case "-h": 
			case "-help": // help option
				System.out.println(helpPrompt);
				break;
			default: 
				System.out.println("Please input valid option.");
				System.out.println(helpPrompt);
				break;
			}
		}else {
			System.out.println("Please input Arguments following instructions.");
			System.out.println(helpPrompt);
		}
	}
}
