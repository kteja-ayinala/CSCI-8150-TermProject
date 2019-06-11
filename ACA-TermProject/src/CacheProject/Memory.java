package CacheProject;

import java.util.LinkedList;
import java.util.Queue;

public class Memory {
	public Queue<String> queueMemorytoL2C = new LinkedList<String>();
	public Queue<String> queueL2CtoMemory = new LinkedList<String>();

	Memory() {
		System.out.println("Memory Created");
	}
}
