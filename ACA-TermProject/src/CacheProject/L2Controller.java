package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.util.LinkedList;
import java.util.Queue;

public class L2Controller {
	public Queue<String> queueL2CtoL2D = new LinkedList<String>();
	public Queue<String> queueL2CtoMemory = new LinkedList<String>();
	public Queue<String> queueMemorytoL2C = new LinkedList<String>();
	L2Controller() {
		System.out.println("L2Controller Created");
	}
}
