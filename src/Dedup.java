
public class Dedup {

	public static void main(String[] args) {
		
		if(args.length > 0) {
			
		}else {
			System.out.println("Please input Argument.");
			String helpPrompt = "\t-a\tAdd a single file to a locker.\n"
					+ "\t\t-a [locker] [file path] [file name]\n"
					+ "\t\t-a [locker] [directory path]\n"
					+ "\t-d\tDelete a file from a locker\n"
					+ "\t\t-d [locker] [file name]\n"
					+ "\t-s\tDisplay files in a locker.\n"
					+ "\t\t-s [locker]\n";
			System.out.println(helpPrompt);
		}
	}
	

}
