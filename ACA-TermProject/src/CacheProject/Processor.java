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
import java.util.Queue;

public class Processor {
	public Queue<String> queueProcessor = new LinkedList<String>();
	public Queue<String> queueL1CtoProcessor = new LinkedList<String>();
	String testcaseFile = null;

	Processor() throws IOException {
		// BufferedReader reader = new BufferedReader(new
		// FileReader(testcaseFile));
		// while (reader.readLine() != null) ;
		// reader.close();
		System.out.println("Processor Created");
	}
}
