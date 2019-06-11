package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */

public class L1Controller {
	public Queue queueL1CtoL1D = new Queue();
	public Queue queueL1CtoL2C = new Queue();
	public Queue queueL1CtoProcessor = new Queue();

	L1Controller() {
		System.out.println("L1Controller Created");
	}
}
