import java.io.IOException;

import execution.DedupExecution;

public class Dedup {

	public static String helpPrompt = 
			"\nAdd one file or a directories of files to a locker.\n"
			+ "  -a\t[locker] [file/directory]\n"
			+ "\nDelete one file or all the files under the same original directories from a locker\n"
			+ "  -d\t[locker] [file/directory]\n"
			+ "\nRetrive one file or all the files under the same original directories from a locker\n"
			+ "  -r\t[locker] [file/directory] [target directory]\n"
			+ "\nDisplay all lockers.\n"
			+ "  -s\t-locker\n"
			+ "\nDisplay all files in one locker.\n"
			+ "  -s\t-file [locker]\n"
			+ "\nHelp\n"
			+ "  -h\t show help prompt.\n";

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		if(args.length > 0) {
			DedupExecution de = new DedupExecution();

			switch(args[0]) {
			case "-a":
				if(args.length == 3){
					System.out.println("Adding file.");
					de.add(args[1] + "/", args[2]);
				}else{
					System.out.println("Invalid -a arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-d":
				if(args.length == 3){
					System.out.println("Deleting file.");
					de.delete(args[1] + "/", args[2]);
				}else{
					System.out.println("Invalid -d arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-r":
				if(args.length == 4){
					System.out.println("Retrieving file.");
					de.get(args[1] + "/", args[2], args[3]);
				}else{
					System.out.println("Invalid -r arguments number. Please Follow instruction.\n");
					System.out.println(helpPrompt);
				}
				break;
			case "-s":
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
				}
				break;
			case "-h":
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
