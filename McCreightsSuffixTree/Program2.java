import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;

public class Program2 {

	public static void main(String[] args) throws FileNotFoundException {
		String alphabetFile="";
		String sequenceFile="";
		if(args.length==2) {
			sequenceFile=args[0];
			alphabetFile=args[1];
		}
		else {
			sequenceFile="resources/s1.fasta";
			alphabetFile="resources/alphabetS1.txt";
		}
		String sequence = fileReadSequence(sequenceFile);
		char[] alphabet = fileReadAlphabet(alphabetFile);
		
		long startTime = Instant.now().toEpochMilli();

		McCreightsSuffixTree suffixTree = new McCreightsSuffixTree(sequence,alphabet);
		
		long endTime = Instant.now().toEpochMilli();

		long timeElapsed = endTime - startTime;

		suffixTree.printBTWIndex();
		System.out.println("Execution time in milliseconds: " + timeElapsed);	
	}
	
	private static String fileReadSequence(String fName) throws FileNotFoundException {
		String sequence="";

		BufferedReader bufReader = new BufferedReader(new FileReader(fName));

        String line;
		try {
			line = bufReader.readLine();
	        while (line != null) {
	        	if(line.contains(">")) {
	        		//do nothing
	        	}
	        	else {
	        		sequence=sequence+line;
	        	}
	        	
	        	line = bufReader.readLine();
	          }

	          bufReader.close();
	  		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return sequence;
	}
	
	private static char[]  fileReadAlphabet(String fName) throws FileNotFoundException {

		BufferedReader bufReader = new BufferedReader(new FileReader(fName));
		 try {
			String[] line = bufReader.readLine().toString().split("\\s");
			char[]  alphabet= new char[line.length];
		    for(int i = 0; i < line.length; i++){
			      alphabet[i] = line[i].charAt(0);
			    }
			return alphabet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	
	

}
