/**
 * 
 */
package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class InstructionTransferer {
	String command;
	int instructioNum;
	int instructionKind;
	Address address;
	// String bAddress;
	Block transferBlock;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getInstructioNum() {
		return instructioNum;
	}

	public void setInstructioNum(int instructioNum) {
		this.instructioNum = instructioNum;
	}

	public int getInstructionKind() {
		return instructionKind;
	}

	public void setInstructionKind(int instructionKind) {
		this.instructionKind = instructionKind;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	// public String getbAddress() {
	// return bAddress;
	// }
	//
	// public void setbAddress(String bAddress) {
	// this.bAddress = bAddress;
	// }

	public Block getTransferBlock() {
		return transferBlock;
	}

	public void setTransferBlock(Block transferBlock) {
		this.transferBlock = transferBlock;
	}
}
