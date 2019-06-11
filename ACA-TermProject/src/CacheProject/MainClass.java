package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		// Initoiate components
		System.out.println("Implementation Sstarts from here");
		Processor processor = new Processor();
		L1Controller l1Controller = new L1Controller();
		L2Controller l2Controller = new L2Controller();
		Memory memory = new Memory();

		System.out.println("Major Components Created");

	}
}