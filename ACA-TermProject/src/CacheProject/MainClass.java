package CacheProject;

import java.io.FileOutputStream;
/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainClass extends CommonImpl {

	private static InstructionTransferer instruction;

	public static void main(String[] args) throws IOException, InterruptedException {
		// System.setOut(new PrintStream(new FileOutputStream(curDir +
		// "/src/CacheProject/testcaseOutput.txt")));
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
		// testq.enqueue("3");f
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
				instruction = (InstructionTransferer) l1Controller.queueProcessortoL1C.dequeue();

				boolean L1Hit = false;
				if (instruction.getProcessorInstructionKind() == 0) { // Read //
																		// Instruction
					if (l1Controller.getState(instruction.getAddress().getAddress()) != null
							&& !l1Controller.getState(instruction.getAddress().getAddress()).equals("Wralloc")) {
						// l1Controller.setState(instruction.getAddress().getAddress(),
						// "Wrwait");
						System.out.println("Waiting for the read to complete");
						l1Controller.queueProcessortoL1C.enqueue(instruction);
					} else {
						L1Hit = l1Controller.isL1Hit(instruction.getAddress());
						if (L1Hit) {
							if (l1Controller.victimCache.isVictimCacheHit(instruction.getAddress())) {
								System.out.println("Hit in VC: " + instruction.getCommand());
								l1Controller.victimCache.queueL1CtoVictimCache.enqueue(instruction);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "RdwaitVCd");
							} else if (l1Controller.writeBuffer.isBuffercacheHit(instruction.getAddress())) {
								System.out.println("Hit in WB: " + instruction.getCommand());
								l1Controller.writeBuffer.queuetoWriteBuffer.enqueue(instruction);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "RdwaitWBd");
							} else {
								System.out.println("Hit in L1: " + instruction.getCommand());
								l1Controller.l1Data.queueL1CtoL1D.enqueue(instruction);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "Rdwaitd");
							}
						} else {
							int index = Integer.parseInt(instruction.getAddress().getIndex(), 2);
							int tag = Integer.parseInt(instruction.getAddress().getTag(), 2);

							if (l1Controller.isValid(index, tag) == 1) {
								if (l1Controller.isDirty(index, tag) == 1) {
									l2Controller.queueL1CtoL2C.enqueue(instruction);
									// l1Controller.setState(instruction.getAddress().getAddress(),
									// "Rd2waitd");
									System.out.println("L1C to L2C: " + instruction.getCommand());
									System.out.println("L1C main state: missd, state assign: Rd2waitd "
											+ instruction.getCommand());
								} else { // need not write back
									l2Controller.queueL1CtoL2C.enqueue(instruction);
									// l1Controller.setState(instruction.getAddress().getAddress(),
									// "RdwaitL2d");
									System.out.println("L1C to L2C: " + instruction.getCommand());
									System.out.println("L1C main state: missc, state assign: Rd2waitL2d "
											+ instruction.getCommand());
								}
								//
							} else {
								// invalid, missi
								l2Controller.queueL1CtoL2C.enqueue(instruction);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "RdwaitL2d");
								System.out.println("L1C to L2C: " + instruction.getCommand());
								System.out.println(
										"L1C main state: missi, state assign: RdwaitL2d " + instruction.getCommand());

							}
						}
					}
				} else {// Write instruction
					L1Hit = l1Controller.isL1Hit(instruction.getAddress());
					if (L1Hit) {
						// l1Controller.setState(instruction.getAddress().getAddress(),
						// "Wrwaitd");
						l1Controller.l1Data.queueL1CtoL1D.enqueue(instruction);
						System.out.println("L1C to L1D: " + instruction.getCommand());
						System.out.println("Hit in L1 for write: , state assign: Wrwaitd " + instruction.getCommand());
					} else {
						int index = Integer.parseInt(instruction.getAddress().getIndex(), 2);
						int tag = Integer.parseInt(instruction.getAddress().getTag(), 2);
						if (l1Controller.isValid(Integer.parseInt(instruction.getAddress().getIndex(), 2),
								Integer.parseInt(instruction.getAddress().getTag(), 2)) == 1) {
							if (l1Controller.isDirty(index, tag) == 1) {
								// write back
								l2Controller.queueL1CtoL2C.enqueue(instruction);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "Wr2waitd");
								System.out.println("L1C to L2C: " + instruction.getCommand());
								System.out.println(
										"L1C main state: missd, state assign: Wr2waitd " + instruction.getCommand());
								//
							} else { // need not write back
								ReadInstruction rIns = new ReadInstruction();
								rIns.setAddress(instruction.getAddress());
								rIns.setCommand(instruction.getCommand());
								rIns.setInstructioNum(instruction.instructioNum);
								rIns.setSingleCharData(((WriteInstruction) instruction).getWriteData());
								rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
								rIns.setInstructionTransferType(0);
								l2Controller.queueL1CtoL2C.enqueue(rIns);
								// l1Controller.setState(instruction.getAddress().getAddress(),
								// "WrwaitL2d");
								System.out.println("L1C to L2C: " + instruction.getCommand());
								System.out.println(
										"L1C main state: missc, state assign: Rd2waitL2d " + instruction.getCommand());

							}

						} else {
							ReadInstruction rIns = new ReadInstruction();
							rIns.setAddress(instruction.getAddress());
							rIns.setCommand(instruction.getCommand());
							rIns.setInstructioNum(instruction.instructioNum);
							rIns.setSingleCharData(((WriteInstruction) instruction).getWriteData());
							rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
							rIns.setInstructionTransferType(0);
							l2Controller.queueL1CtoL2C.enqueue(rIns);
							// l1Controller.setState(instruction.getAddress().getAddress(),
							// "WrwaitL2d");
							System.out.println("L1C to L2C: " + instruction.getCommand());
							System.out.println(
									"L1C main state: missi, state assign: WrwaitL2d " + instruction.getCommand());
						}
					}
				}

			}

			if (!l1Controller.l1Data.queueL1CtoL1D.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.l1Data.queueL1CtoL1D.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.l1_Tag, l1Controller.l1_Index,
						l1Controller.l1_Offset);
				if (instruction.getProcessorInstructionKind() == 0) {// Read
					if (instruction.getInstructionTransferType() == 0) {
						Block block = l1Controller.readBlock(fAddress);
						char data = block.getData()[Integer.parseInt(fAddress.getOffset(), 2)];
						ReadInstruction rIns = new ReadInstruction();
						rIns.setAddress(fAddress);
						rIns.setTransferBlock(block);
						rIns.setByteEnables(((ReadInstruction) instruction).getByteEnables());
						rIns.setCommand(instruction.getCommand());
						rIns.setSingleCharData(data);
						l1Controller.l1Data.queueL1DtoL1C.enqueue(rIns);
					} else {
						Block block = instruction.getTransferBlock();
						block.setBitData(Integer.parseInt(fAddress.getOffset(), 2),
								((WriteInstruction) instruction).getWriteData());
						l1Controller.l1write(block, fAddress);
						l1Controller.l1writeChar(((WriteInstruction) instruction).getWriteData(), fAddress);
						// System.out.println("L1D Data update: " +
						// instruction.getCommand());
						// System.out.println(block.data);

						System.out.println("L1D block data updated from main memory: " + instruction.getCommand());
					}
				} else {

					Block block = instruction.getTransferBlock();
					block.setBitData(Integer.parseInt(fAddress.getOffset(), 2),
							((WriteInstruction) instruction).getWriteData());
					l1Controller.l1write(block, fAddress);
					l1Controller.l1writeChar(((WriteInstruction) instruction).getWriteData(), fAddress);
					System.out.println("L1D write Data update: " + instruction.getCommand());
					System.out.println(block.data);
					l1Controller.setState(instruction.getAddress().getAddress(), "Wralloc");
					System.out.println("state set to : Wralloc" + instruction.getCommand());
				}
			}

			if (!l1Controller.l1Data.queueL1DtoL1C.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.l1Data.queueL1DtoL1C.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.l1_Tag, l1Controller.l1_Index,
						l1Controller.l1_Offset);
				int byteena = ((ReadInstruction) instruction).getByteEnables();

				ReadInstruction rIns = new ReadInstruction();
				rIns.setByteEnables(byteena);
				rIns.setAddress(fAddress);
				rIns.setCommand(instruction.getCommand());
				rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
				rIns.setInstructionTransferType(0);
				if (byteena != 0) {
					char[] data = new char[byteena];

					for (int i = 0; i < byteena; i++) {
						data[i] = l1Controller.readBlock(fAddress).getData()[Integer.parseInt(fAddress.getOffset(), 2)
								+ i];
					}
					rIns.setByteEnableData(data);
				} else {
					char returnData = instruction.getTransferBlock().getData()[Integer.parseInt(fAddress.getOffset(),
							2)];
					rIns.setSingleCharData(returnData);
				}
				processor.queueL1CtoProcessor.enqueue(rIns);
				System.out.println("L1C to Processor: Data sent after hit " + instruction.getCommand());
			}

			if (!l1Controller.victimCache.queueL1CtoVictimCache.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.victimCache.queueL1CtoVictimCache.dequeue();
				int byteena = ((ReadInstruction) instruction).getByteEnables();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.l1_Tag, l1Controller.l1_Index,
						l1Controller.l1_Offset);
				Block block = null;
				if (instruction.getProcessorInstructionKind() == 0) {
					block = l1Controller.readBlock(fAddress);
					ReadInstruction rIns = new ReadInstruction();
					rIns.setByteEnables(byteena);
					rIns.setAddress(fAddress);
					rIns.setCommand(instruction.getCommand());
					rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					rIns.setInstructionTransferType(0);
					rIns.setSingleCharData(block.getData()[Integer.parseInt(fAddress.getOffset(), 2)]);
					rIns.setTransferBlock(block);
					l1Controller.setState(instruction.getAddress().getAddress(), "RdwaitVCD");
					l1Controller.l1Data.queueL1DtoL1C.enqueue(rIns);
					System.out.println("L1VC to L1C: Data sent after hit " + instruction.getCommand());
				} else {
					block = instruction.getTransferBlock();
					l1Controller.victimCache.queueL1CtoVictimCache.enqueue(block);
					System.out.println("L1C to L1VC: " + instruction.getCommand());
				}
			}

			if (!l1Controller.writeBuffer.queuetoWriteBuffer.isEmpty()) {
				instruction = (InstructionTransferer) l1Controller.writeBuffer.queuetoWriteBuffer.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.writeBuffer.buffer_Tag,
						l1Controller.writeBuffer.buffer_Index, l1Controller.writeBuffer.buffer_Offset);
				Block block = null;
				if (instruction.getProcessorInstructionKind() == 0) {
					block = l1Controller.readBlock(fAddress);
					ReadInstruction rIns = new ReadInstruction();
					rIns.setAddress(fAddress);
					rIns.setCommand(instruction.getCommand());
					rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					rIns.setInstructionTransferType(0);
					rIns.setSingleCharData(block.getData()[Integer.parseInt(fAddress.getOffset(), 2)]);
					rIns.setTransferBlock(block);
					// l1Controller.setState(instruction.getAddress().getAddress(),
					// "RdwaitWBD");
					l1Controller.l1Data.queueL1DtoL1C.enqueue(rIns);
					System.out.println(
							"L1WB to L1C: Data sent after hitin WB, state: RdwaitWBD " + instruction.getCommand());
				} else {
					block = instruction.getTransferBlock();
					l1Controller.writeBuffer.queuetoWriteBuffer.enqueue(block);
					System.out.println("L1C to L1WB: " + instruction.getCommand());
				}
			}

			if (!l1Controller.queueL2CtoL1C.isEmpty()) {

				instruction = (InstructionTransferer) l1Controller.queueL2CtoL1C.dequeue();
				int byteena = ((ReadInstruction) instruction).getByteEnables();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l1Controller.l1_Tag, l1Controller.l1_Index,
						l1Controller.l1_Offset);
				if (l1Controller.getState(instruction.getAddress().getAddress()).equals("Rdwait2D")) {
					// l1Controller.setState(instruction.getAddress().getAddress(),
					// "Rdwait1d");
				}
				if (instruction.getProcessorInstructionKind() == 0) {
					if (instruction.getInstructionTransferType() == 0) {
						WriteInstruction wIns = new WriteInstruction();
						wIns.setTransferBlock(instruction.getTransferBlock());
						wIns.setAddress(fAddress);
						// if(instruction.getInstructionTransferType() == 0)
						// String ins = "Write" + fAddress.getAddress();
						wIns.setCommand(instruction.getCommand());
						wIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
						wIns.setInstructionTransferType(1);
						l1Controller.l1Data.queueL1CtoL1D.enqueue(wIns);
						System.out.println("L1C to L1D: " + instruction.getCommand());
					}
					// logic to return required read information
					ReadInstruction rIns = new ReadInstruction();
					rIns.setByteEnables(byteena);
					rIns.setAddress(fAddress);
					rIns.setCommand(instruction.getCommand());
					rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					rIns.setInstructionTransferType(0);
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
				} else {// Write
					if (l1Controller.isValid(Integer.parseInt(fAddress.getIndex(), 2),
							Integer.parseInt(fAddress.getTag(), 2)) == 1) {
						if (l1Controller.isDirty(Integer.parseInt(fAddress.getIndex(), 2),
								Integer.parseInt(fAddress.getTag(), 2)) == 1) {
							Block block = l1Controller.readBlock(fAddress);
							l1Controller.writeBuffer.setBlock(block, fAddress);
							l1Controller.setDirty(Integer.parseInt(fAddress.getIndex(), 2),
									Integer.parseInt(fAddress.getTag(), 2), 0);
							L1Controller.setvalid(Integer.parseInt(fAddress.getIndex(), 2),
									Integer.parseInt(fAddress.getTag(), 2), 0);

						}

					}
					WriteInstruction wIns = new WriteInstruction();
					wIns.setTransferBlock(instruction.getTransferBlock());
					wIns.setAddress(fAddress);
					wIns.setCommand(instruction.getCommand());
					wIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					wIns.setInstructionTransferType(1);
					wIns.setWriteData(instruction.getCommand().toString().split(" ")[2].charAt(0));
					l1Controller.l1Data.queueL1CtoL1D.enqueue(wIns);
					System.out.println("L1C to L1D:  state set to: Wrwait1d" + instruction.getCommand());
					l1Controller.setState(instruction.getAddress().getAddress(), "Wrwait1d");

				}
			}

			if (!processor.queueL1CtoProcessor.isEmpty()) {
				instruction = (InstructionTransferer) processor.queueL1CtoProcessor.dequeue();
				if (instruction.getProcessorInstructionKind() == 0) {
					if (instruction.getInstructionTransferType() == 0) {
						char[] data = ((ReadInstruction) instruction).getByteEnableData();

						String finaldata = String.valueOf(data);
						System.out.println("!!****************************!!");
						System.out.println("Result: " + finaldata + " for " + instruction.getCommand());
						System.out.println("!!****************************!!");
					}
				}
			}

			if (!l2Controller.queueL1CtoL2C.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.queueL1CtoL2C.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				boolean L2Hit = false;
				if (instruction.getProcessorInstructionKind() == 1) {
					if (instruction.getInstructionTransferType() == 0) { // Read
																			// Instruction
						L2Hit = l2Controller.isL2Hit(fAddress);
						if (L2Hit) {
							System.out.println("Hit in L2!!" + instruction.getCommand());
							l2Controller.l2Data.queueL2CtoL2D.enqueue(instruction);
							l2Controller.setState(instruction.getAddress().getAddress(), "Rdwaitd");
							System.out.println("L2C to L2D: " + instruction.getCommand());
						} else {
							int index = Integer.parseInt(instruction.getAddress().getIndex(), 2);
							int tag = Integer.parseInt(instruction.getAddress().getTag(), 2);
							if (l2Controller.l2Data.l2cache[Integer.parseInt(fAddress.getIndex(), 2)]
									.getValidBit() == 1) {
								if (l2Controller.isDirty(index, tag) == 1) {
									memory.queueL2CtoMemory.enqueue(instruction);
									l2Controller.setState(instruction.getAddress().getAddress(), "Rdwait2d");
									System.out.println("L2C to Memory: " + instruction.getCommand());
									System.out.println("L1C main state: missd, state assign: Wr2waitd "
											+ instruction.getCommand());
									//
								} else { // need not write back

									memory.queueL2CtoMemory.enqueue(instruction);
									l2Controller.setState(instruction.getAddress().getAddress(), "Rdwaitmemd");
									System.out.println("L2C to Memory: " + instruction.getCommand());
									System.out.println("L2C main state: missc, state assign: Rdwaitmemd "
											+ instruction.getCommand());
								}

							}
							memory.queueL2CtoMemory.enqueue(instruction);
							l2Controller.setState(instruction.getAddress().getAddress(), "Rdwaitmemd");
							System.out.println("L2C to Memory: state assign: Rdwaitmemd " + instruction.getCommand());
						}
					} else { // Write instruction
						L2Hit = l2Controller.isL2Hit(fAddress);
						if (L2Hit) {
							// l2Controller.setState(instruction.getAddress().getAddress(),
							// "Wrwaitd");
							l2Controller.l2Data.queueL2CtoL2D.enqueue(instruction);
							System.out.println("Hit in L2: " + instruction.getCommand());

						} else {
							int index = Integer.parseInt(instruction.getAddress().getIndex(), 2);
							int tag = Integer.parseInt(instruction.getAddress().getTag(), 2);
							if (l2Controller.l2Data.l2cache[Integer.parseInt(fAddress.getIndex(), 2)]
									.getValidBit() == 1) {
								if (l2Controller.isDirty(index, tag) == 1) {
									memory.queueL2CtoMemory.enqueue(instruction);
									l2Controller.setState(instruction.getAddress().getAddress(), "Rdwait2d");
									System.out.println("L2C to Memory: " + instruction.getCommand());

								} else {
									l2Controller.setState(instruction.getAddress().getAddress(), "Wrwaitmemd");
									memory.queueL2CtoMemory.enqueue(instruction);
									System.out.println("L2C to Memory: " + instruction.getCommand());
								}
							} else {
								memory.queueL2CtoMemory.enqueue(instruction);
								// l2Controller.setState(instruction.getAddress().getAddress(),
								// "Wrwaitmemd");
								System.out.println("L2C to Memory: " + instruction.getCommand());
							}
						}
					}
				} else {
					L2Hit = l2Controller.isL2Hit(fAddress);
					if (L2Hit) {
						System.out.println("Hit in L2 write!!" + instruction.getCommand());
						l2Controller.l2Data.queueL2CtoL2D.enqueue(instruction);
						l2Controller.setState(instruction.getAddress().getAddress(), "Rdwait2d");
						System.out.println("L2C to L2D: " + instruction.getCommand());
					} else {
						int index = Integer.parseInt(instruction.getAddress().getIndex(), 2);
						int tag = Integer.parseInt(instruction.getAddress().getTag(), 2);
						if (l2Controller.l2Data.l2cache[Integer.parseInt(fAddress.getIndex(), 2)].getValidBit() == 1) {
							if (l2Controller.isDirty(index, tag) == 1) {
								memory.queueL2CtoMemory.enqueue(instruction);
								l2Controller.setState(instruction.getAddress().getAddress(), "Wr2wait2d");
								System.out.println("L2C to Memory: " + instruction.getCommand());
								System.out.println(
										"L2C main state: missd, state assign: Wr2waitd " + instruction.getCommand());
								//
							} else { // need not write back

								memory.queueL2CtoMemory.enqueue(instruction);
								l2Controller.setState(instruction.getAddress().getAddress(), "Rdwaitmemd");
								System.out.println("L2C to Memory: " + instruction.getCommand());
								System.out.println(
										"L2C main state: missc, state assign: Rdwaitmemd " + instruction.getCommand());
							}

						}
						memory.queueL2CtoMemory.enqueue(instruction);
						l2Controller.setState(instruction.getAddress().getAddress(), "Rdwaitmemd");
						System.out.println("L2C to Memory: state assign: Rdwaitmemd " + instruction.getCommand());
					}
				}
			}

			if (!l2Controller.l2Data.queueL2CtoL2D.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.l2Data.queueL2CtoL2D.dequeue();
				int address = instruction.getAddress().getAddress();
				Address fAddress = formatAddress(address, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				if (instruction.getProcessorInstructionKind() == 0) {// Read
					if (instruction.getInstructionTransferType() == 0) {
						int byteena = ((ReadInstruction) instruction).getByteEnables();
						ReadInstruction rIns = new ReadInstruction();
						rIns.setByteEnables(byteena);
						rIns.setAddress(fAddress);
						rIns.setCommand(instruction.getCommand());
						rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
						rIns.setInstructionTransferType(0);
						if (byteena != 0) {
							char[] data = new char[byteena];

							for (int i = 0; i < byteena; i++) {
								data[i] = l1Controller.readBlock(fAddress)
										.getData()[Integer.parseInt(fAddress.getOffset(), 2) + i];
							}
							rIns.setByteEnableData(data);
						} else {
							char returnData = instruction.getTransferBlock().getData()[Integer
									.parseInt(fAddress.getOffset(), 2)];
							rIns.setSingleCharData(returnData);
						}
						l2Controller.l2Data.queueL2DtoL2C.enqueue(rIns);
						System.out.println("L2D to L2C: Data sent after hit in L2" + instruction.getCommand());
					} else {
						Block block = instruction.getTransferBlock();
						// block.setBitData(Integer.parseInt(fAddress.getOffset(),
						// 2),
						// ((ReadInstruction) instruction).getSingleCharData());
						l2Controller.l2write(block, fAddress);
						// l1Controller.l1writeChar(((ReadInstruction)
						// instruction).getSingleCharData(), fAddress);
						System.out.println("L2D Data updated: " + instruction.getCommand());
					}
				} else {
					// if
					// (l2Controller.getState(instruction.getAddress().getAddress()).equals("Wrwaitmemd")
					// ||
					// l2Controller.getState(instruction.getAddress().getAddress()).equals("Wrwait1d"))
					// {
					Block block = instruction.getTransferBlock();
					block.setBitData(Integer.parseInt(fAddress.getOffset(), 2),
							((WriteInstruction) instruction).getWriteData());
					l2Controller.l2write(block, fAddress);
					l2Controller.l2writeChar(((WriteInstruction) instruction).getWriteData(), fAddress);
					System.out.println("L2D Data updated: " + instruction.getCommand());
					System.out.println(block.data);
				}
				// }
			}

			if (!l2Controller.l2Data.queueL2DtoL2C.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.l2Data.queueL2DtoL2C.dequeue();
				int address1 = instruction.getAddress().getAddress();
				Address fAddress1 = formatAddress(address1, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				int byteena = ((ReadInstruction) instruction).getByteEnables();
				Block block = l2Controller.readBlock(fAddress1);
				// char data =
				// block.getData()[Integer.parseInt(fAddress.getOffset(), 2)];
				ReadInstruction rIns = new ReadInstruction();
				rIns.setAddress(fAddress1);
				rIns.setTransferBlock(block);
				rIns.setByteEnables(((ReadInstruction) instruction).getByteEnables());
				rIns.setCommand(instruction.getCommand());
				rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
				rIns.setInstructionTransferType(0);
				// if (byteena != 0) {
				// char[] data = new char[byteena];
				//
				// for (int i = 0; i < byteena; i++) {
				// data[i] =
				// l2Controller.readBlock(fAddress).getData()[Integer.parseInt(fAddress.getOffset(),
				// 2)
				// + i];
				// }
				// rIns.setByteEnableData(data);
				// } else {
				// char returnData =
				// instruction.getTransferBlock().getData()[Integer.parseInt(fAddress.getOffset(),
				// 2)];
				// rIns.setSingleCharData(returnData);
				// }
				l1Controller.queueL2CtoL1C.enqueue(rIns);
				System.out.println("L2C to L1C: Data from L2D sent " + instruction.getCommand());

			}

			// Data will be available in memory, need not check if it is a hit
			Block memBlock;
			if (!memory.queueL2CtoMemory.isEmpty()) {
				instruction = (InstructionTransferer) memory.queueL2CtoMemory.dequeue();
				int address1 = instruction.getAddress().getAddress();
				Address fAddress1 = formatAddress(address1, memory.memory_Tag, memory.memory_Index,
						memory.memory_Offset);
				int index = Integer.parseInt(fAddress1.getIndex(), 2);
				if (instruction.getInstructionTransferType() == 0) {// Read
																	// Instruction
					memBlock = new Block(memory.memory[index].data, index, cycle);
					cycle = cycle + 7;
					memBlock.setBlockAddress(index);
					// memBlock.setValidBit(1);
					// memBlock.setDirtyBit(0);
					ReadInstruction rIns = new ReadInstruction();
					rIns.setCommand(instruction.getCommand());
					rIns.setByteEnables(((ReadInstruction) instruction).getByteEnables());
					rIns.setTransferBlock(memBlock);
					rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					rIns.setInstructioNum(instruction.getInstructioNum());
					rIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
					rIns.setInstructionTransferType(0);
					rIns.setAddress(fAddress1);
					l2Controller.queueMemorytoL2C.enqueue(rIns);
					l2Controller.setState(instruction.getAddress().getAddress(), "RdwaitMemd");
					System.out.println("Memory to L2C: state set to: RdwaitMemdtoL2 " + instruction.getCommand());
				} else {// Write instruction
					memBlock = instruction.getTransferBlock();
					if (memBlock != null) {
						char[] datawrite = memBlock.getData();
						memory.memory[index].setData(datawrite);
						System.out.println("Memory data updated" + instruction.getCommand());
					}
				}
			}

			if (!l2Controller.queueMemorytoL2C.isEmpty()) {
				instruction = (InstructionTransferer) l2Controller.queueMemorytoL2C.dequeue();
				int address1 = instruction.getAddress().getAddress();
				Address fAddress1 = formatAddress(address1, l2Controller.l2_Tag, l2Controller.l2_Index,
						l2Controller.l2_Offset);
				if (instruction.getInstructionTransferType() == 0) {
					if (l2Controller.getState(instruction.getAddress().getAddress()) != null) {
						if (l2Controller.getState(instruction.getAddress().getAddress()).equals("Rdwait2d")) {
							l2Controller.setState(instruction.getAddress().getAddress(), "Rdwait1d");
						} else {
							// Remove unnecessaru instruction parameters after
							// review
							WriteInstruction wIns = new WriteInstruction();
							wIns.setAddress(fAddress1);
							wIns.setTransferBlock(instruction.getTransferBlock());
							wIns.setCommand(instruction.getCommand());
							wIns.setProcessorInstructionKind(1);
							wIns.setInstructioNum(instruction.getInstructioNum());
							wIns.setProcessorInstructionKind(instruction.getProcessorInstructionKind());
							wIns.setInstructionTransferType(1);
							wIns.setWriteData(instruction.getCommand().toString().split(" ")[2].charAt(0));
							l2Controller.l2Data.queueL2CtoL2D.enqueue(wIns);
							System.out.println("L2C to L2D: " + instruction.getCommand()); // Write
																							// Instruction

							l1Controller.setState(instruction.getAddress().getAddress(), "Wrwait1d"); // enqueued
							l2Controller.clear(instruction.getAddress().getAddress());
							l1Controller.queueL2CtoL1C.enqueue(instruction); // Read
																				// Instruction
																				// enqueued
							System.out.println("L2C to L1C :  state set to: Wrwait1d" + instruction.getCommand());

						}
					}
				}
			}

		} while (!processor.queueProcessor.isEmpty() || !l1Controller.queueProcessortoL1C.isEmpty()
				|| !l1Controller.l1Data.queueL1CtoL1D.isEmpty() || !l1Controller.l1Data.queueL1DtoL1C.isEmpty()
				|| !processor.queueL1CtoProcessor.isEmpty() || !l2Controller.queueL1CtoL2C.isEmpty()
				|| !l2Controller.l2Data.queueL2CtoL2D.isEmpty() || !l1Controller.queueL2CtoL1C.isEmpty()
				|| !memory.queueL2CtoMemory.isEmpty() || !l2Controller.queueMemorytoL2C.isEmpty()
				|| !l2Controller.l2Data.queueL2DtoL2C.isEmpty());
	}
}