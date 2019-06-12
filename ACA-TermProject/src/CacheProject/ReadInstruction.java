package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class ReadInstruction extends InstructionTransferer {

	private int byteEnables;
	private char[] data;
	private char returnData;

	public char getReturnData() {
		return returnData;
	}

	public void setReturnData(char returnData) {
		this.returnData = returnData;
	}

	public int getByteEnables() {
		return byteEnables;
	}

	public void setByteEnables(int byteEnables) {
		this.byteEnables = byteEnables;
	}

	// private int insAddr;

	// public int getInsAddr() {
	// return insAddr;
	// }
	//
	// public void setInsAddr(int insAddr) {
	// this.insAddr = insAddr;
	// }

	public char[] getDataBytes() {
		return data;
	}

	public void setDataBytes(char[] data) {
		this.data = data;
	}

}
