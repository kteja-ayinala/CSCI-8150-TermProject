package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Processor {
	public Queue queueProcessor = new Queue();
	public Queue queueL1CtoProcessor = new Queue();
	String testcaseFile = "";

	Processor() throws IOException {
		 BufferedReader reader = new BufferedReader(new
		 FileReader(testcaseFile));
		// while (reader.readLine() != null) ;
		// reader.close();
		System.out.println("Processor Created");
	}
}
