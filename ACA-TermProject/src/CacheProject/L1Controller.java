package CacheProject;

import java.util.LinkedList;
import java.util.Queue;

public class L1Controller {
	public Queue<String> queueL1CtoL1D = new LinkedList<String>();
	public Queue<String> queueL1CtoL2C = new LinkedList<String>();
	public Queue<String> queueL1CtoProcessor = new LinkedList<String>();

	L1Controller() {
		System.out.println("L1Controller Created");
	}
}
