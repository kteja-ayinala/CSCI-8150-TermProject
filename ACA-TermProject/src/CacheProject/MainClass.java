package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.IOException;

public class MainClass extends CommonImpl {

	private static InstructionTransferer instruction;

	public static void main(String[] args) throws IOException, InterruptedException {
		// Initoiate components
		// System.out.println("Implementation starts from here");
		Memory memory = new Memory();
		L1Controller l1Controller = new L1Controller();
		L2Controller l2Controller = new L2Controller();
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

		int cycle = 0;
		do {
			cycle++;
			System.out.println("cycle:" + cycle);
			if (!processor.queueProcessor.isEmpty()) {
				instruction = (InstructionTransferer) processor.queueProcessor.dequeue();
				l1Controller.queueProcessortoL1C.enqueue(instruction);
				System.out.println("P to L1C: " + instruction.getCommand());
			}
			if (!l1Controller.queueProcessortoL1C.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.queueProcessortoL1C.dequeue();
				if (instruction.getInstructionKind() == 0) { // Read
																// Instructions
					boolean L1Hit = false;
					L1Hit = l1Controller.isL1Hit(instruction.getAddress());
					if (L1Hit) {
						System.out.println("Tag matched - Hit in L1!!");
					} else {
						l2Controller.queueL1CtoL2C.enqueue(instruction);
						System.out.println("L1C to L2C: " + instruction.getCommand());
					}
				} else {
					// Write instruction
				}

			}
			if (!l2Controller.queueL1CtoL2C.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.queueL1CtoL2C.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				if (instruction.getInstructionKind() == 0) { // Read Instruction
					boolean L2Hit = false;
					L2Hit = l2Controller.isL2Hit(fAddress);
					if (L2Hit) {
						System.out.println("Tag matched - Hit in L2!!");
					} else {
						memory.queueL2CtoMemory.enqueue(instruction);
						System.out.println("L2C to Memory: " + instruction.getCommand());
					}
				} else {
					// Write instruction
				}
			}
			Block memBlock;
			if (!memory.queueL2CtoMemory.isEmpty()) {
				instruction = (InstructionTransferer) memory.queueL2CtoMemory.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, memory.memory_Tag, memory.memory_Index, memory.memory_Offset);
				int index = Integer.parseInt(fAddress.getIndex(), 2);
				if (instruction.getInstructionKind() == 0) {// Read Instruction
					memBlock = new Block(memory.memory[index].data, index, cycle);
					cycle = cycle + 7;
				} else {
					// Write instruction
				}
			}

		} while (!processor.queueProcessor.isEmpty());
	}
}