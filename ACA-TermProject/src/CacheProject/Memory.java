package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class Memory {
	public Queue queueMemorytoL2C = new Queue();
	public Queue queueL2CtoMemory = new Queue();

	Memory() {
		System.out.println("Memory Created");
	}
}
