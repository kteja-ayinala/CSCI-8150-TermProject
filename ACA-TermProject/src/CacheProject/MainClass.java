package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass extends CommonImpl {

	private static InstructionTransferer instruction;

	public static void main(String[] args) throws IOException, InterruptedException {
		// Initiate components
		// System.out.println("Implementation starts from here");
		Memory memory = new Memory();
		L1Controller l1Controller = new L1Controller();
		L2Controller l2Controller = new L2Controller();
		Processor processor = new Processor();
		int cycle = 0;
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
		do {
			cycle++;
			System.out.println("---------cycle:" + cycle + "---------");
			if (!processor.queueProcessor.isEmpty()) {
				instruction = (InstructionTransferer) processor.queueProcessor.dequeue();
				l1Controller.queueProcessortoL1C.enqueue(instruction);
				System.out.println("P to L1C: " + instruction.getCommand());
			}
			if (!l1Controller.queueProcessortoL1C.isEmpty()) {
				boolean L1Hit = false;
				instruction = (InstructionTransferer) l1Controller.queueProcessortoL1C.dequeue();
				if (instruction.getInstructionKind() == 0) { // Read Instruction
					L1Hit = l1Controller.isL1Hit(instruction.getAddress());
					if (L1Hit) {
						System.out.println("Tag matched - Hit in L1!!" + instruction.getCommand());
						// Implement Read Data from L1D
					} else {
						l2Controller.queueL1CtoL2C.enqueue(instruction);
						System.out.println("L1C to L2C: " + instruction.getCommand());
					}
				} else {// Write instruction
					L1Hit = l1Controller.isL1Hit(instruction.getAddress());
					if (L1Hit) {
						System.out.println("Tag matched - Hit in L1!!" + instruction.getCommand());
						// Implement Write Data to L1D and state information
					} else {
						l2Controller.queueL1CtoL2C.enqueue(instruction);
						System.out.println("L1C to L2C: " + instruction.getCommand());
					}
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
			// Data will be available in memory, need not check if it is a hit
			Block memBlock;
			if (!memory.queueL2CtoMemory.isEmpty()) {
				instruction = (InstructionTransferer) memory.queueL2CtoMemory.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, memory.memory_Tag, memory.memory_Index, memory.memory_Offset);
				int index = Integer.parseInt(fAddress.getIndex(), 2);
				if (instruction.getInstructionKind() == 0) {// Read Instruction
					memBlock = new Block(memory.memory[index].data, index, cycle);
					cycle = cycle + 7;
					memBlock.setBlockAddress(index);
					// memBlock.setValidBit(1);
					// memBlock.setDirtyBit(0);
					ReadInstruction rIns = new ReadInstruction();
					rIns.setCommand(instruction.getCommand());
					rIns.setByteEnables(((ReadInstruction) instruction).getByteEnables());
					rIns.setTransferBlock(memBlock);
					rIns.setInstructionKind(instruction.getInstructionKind());
					rIns.setInstructioNum(instruction.getInstructioNum());
					rIns.setAddress(fAddress);
					l2Controller.queueMemorytoL2C.enqueue(rIns);
					System.out.println("Memory to L2C: " + instruction.getCommand());
				} else {
					// Write instruction
				}
			}

			if (!l2Controller.queueMemorytoL2C.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.queueMemorytoL2C.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				if (instruction.getInstructionKind() == 0) {
					// Remove unnecessaru instruction parameters after review
					WriteInstruction wIns = new WriteInstruction();
					wIns.setAddress(fAddress);
					wIns.setTransferBlock(instruction.getTransferBlock());
					wIns.setCommand(instruction.getCommand());
					wIns.setInstructionKind(instruction.getInstructionKind());
					wIns.setInstructioNum(instruction.getInstructioNum());
					wIns.setWriteData(instruction.command.split(" ")[2].charAt(0));
					l2Controller.queueL2CtoL2D.enqueue(wIns);
					System.out.println("L2C to L2D: " + instruction.getCommand()); // Write
																					// Instruction
																					// enqueued
					l1Controller.queueL2CtoL1C.enqueue(instruction); // Read
																		// Instruction
																		// enqueued
					System.out.println("L2C to L1C : " + instruction.getCommand());

				} else {
					// write
				}
			}

			if (!l1Controller.queueL2CtoL1C.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.queueL2CtoL1C.dequeue();
				int byteena = ((ReadInstruction) instruction).getByteEnables();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.l1_Tag, l1Controller.l1_Index,
						l1Controller.l1_Offset);
				if (instruction.getInstructionKind() == 0) {
					WriteInstruction wIns = new WriteInstruction();
					wIns.setTransferBlock(instruction.getTransferBlock());
					wIns.setAddress(fAddress);
					// String ins = "Write" + fAddress.getAddress();
					wIns.setCommand(instruction.getCommand());
					l1Controller.queueL1CtoL1D.enqueue(wIns);
					System.out.println("L1C to L1D: " + instruction.getCommand());
					// logic to return required read information
					ReadInstruction rIns = new ReadInstruction();
					rIns.setByteEnables(byteena);
					rIns.setAddress(fAddress);
					rIns.setCommand(instruction.getCommand());
					if (byteena != 0) {
						char[] data = new char[byteena];
						Block block = instruction.getTransferBlock();
						for (int i = 0; i < byteena; i++) {
							data[i] = block.getData()[Integer.parseInt(fAddress.getOffset(), 2) + i];
						}
						rIns.setByteEnableData(data);
					} else {
						char returnData = instruction.getTransferBlock().getData()[Integer
								.parseInt(fAddress.getOffset(), 2)];
						rIns.setSingleCharData(returnData);
					}
					processor.queueL1CtoProcessor.enqueue(rIns);
					System.out.println("L1C to Processor: " + instruction.getCommand());

				} else {
					// Write
				}
			}

			if (!processor.queueL1CtoProcessor.isEmpty()) {
				instruction = (InstructionTransferer) processor.queueL1CtoProcessor.dequeue();
				if (instruction.getInstructionKind() == 0) {
					char[] data = ((ReadInstruction) instruction).getByteEnableData();
					String finaldata = String.valueOf(data);
					System.out.println("Result: " + finaldata);
				}
			}

		} while (!processor.queueProcessor.isEmpty());
	}
}