import java.io.IOException;

import execution.DedupExecution;

public class Dedup {

	public static String helpPrompt = 
			"\nAdd file/s to a locker.\n"
			+ "  -a\t[locker] [file path] [file name]\n"
			+ "  -a\t[locker] [directory path]\n"
			+ "\nDelete a file from a locker\n"
			+ "  -d\t[locker] [file name]\n"
			+ "\nDisplay files in a locker.\n"
			+ "  -s\t[locker]\n";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		if(args.length > 0) {
			DedupExecution de = new DedupExecution();
			
			switch(args[0]) {
				case "-a":
					if(args.length == 3){
						// add the whole directory
						de.add(args[1], args[2]);
					}else if(args.length == 4){
						// add single file in a directory
						de.add(args[1], args[2], args[3]);
					}else{
						System.out.println("Need more arguments for -a option.");
					}
					break;
				case "-d":
					if(args.length == 3){
						// do delete
					}else{
						System.out.println("Need more arguments for -d option.");
					}
					break;
				case "-s":
					if(args.length == 2){
						// show content
					}else{
						System.out.println("Need more arguments for -s option.");
					}
					break;
				default: 
					System.out.println("Please input valid option.");
					System.out.println(helpPrompt);
					break;
			}
				
					
		}else {
			System.out.println("Please input Argument.");
			System.out.println(helpPrompt);
		}
	}
}
