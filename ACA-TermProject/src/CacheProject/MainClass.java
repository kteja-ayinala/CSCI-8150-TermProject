package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.IOException;

public class MainClass extends CommonImpl {

	public static void main(String[] args) throws IOException {
		// Initoiate components
		// System.out.println("Implementation starts from here");
		Memory memory = new Memory();
		L1Controller l1Controller = new L1Controller();
		L1Data l1Data = new L1Data();
		L2Controller l2Controller = new L2Controller();
		L2Data l2Data = new L2Data();
		Processor processor = new Processor();

		// Address k = formatAddress(4096, 6, 6, 5);
		// System.out.println(k);

		// Queue testq = new Queue();
		// testq.enqueue("1");
		// testq.enqueue("3");
		// testq.enqueue("2");
		// testq.enqueue(" string");
		// testq.dequeue();
		// System.out.println(testq.isFull());
		// System.out.println(testq.isEmpty());
		//
		// System.out.println(testq.size());

	}
}