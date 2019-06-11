package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class ReadInstruction {
	private int insAddr;
	private int byteEna;
	private char[] data;

	public int getInsAddr() {
		return insAddr;
	}

	public void setInsAddr(int insAddr) {
		this.insAddr = insAddr;
	}

	public int getBytena() {
		return byteEna;
	}

	public void setByteena(int byteEna) {
		this.byteEna = byteEna;
	}

	public char[] getDataBytes() {
		return data;
	}

	public void setDataBytes(char[] data) {
		this.data = data;
	}

}
