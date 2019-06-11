package CacheProject;

import java.util.LinkedList;
import java.util.Queue;

public class Processor {
	public Queue<String> queueProcessor = new LinkedList<String>();
	public Queue<String> queueL1CtoProcessor = new LinkedList<String>();
	Processor() {
		System.out.println("Processor Created");
	}

}
