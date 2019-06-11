package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */

public class L2Controller {
	public Queue queueL2CtoL2D = new Queue();
	public Queue queueL2CtoMemory = new Queue();
	public Queue queueMemorytoL2C = new Queue();
	public Queue queueL1CtoL2C = new Queue();
	public Queue queueL2CtoL1C = new Queue();

	L2Controller() {
		System.out.println("L2Controller Created");
	}
}
