package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class Block {

	int validBit;
	int dirtyBit;
	int tag;
	int offset;
	char data[];
	ReadInstruction[] rIns;
	WriteInstruction[] wIns;

	public Block() {
		validBit = 1;
		dirtyBit = 0;
		tag = 0;
		offset = 0;
		data = new char[32];
	}

	public Block(char mdata[]) {
		validBit = 1;
		dirtyBit = 0;
		tag = 0;
		offset = 0;
		data = new char[32];
		data = mdata.clone();
	}

	public Block(ReadInstruction mdata[]) {
		validBit = 1;
		dirtyBit = 0;
		tag = 0;
		offset = 0;
		rIns = new ReadInstruction[8];
		rIns = mdata;
	}

	public int getValidBit() {
		return validBit;
	}

	public void setValidBit(int validBit) {
		this.validBit = validBit;
	}

	public int getDirtyBit() {
		return dirtyBit;
	}

	public void setDirtyBit(int dirtyBit) {
		this.dirtyBit = dirtyBit;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public char[] getData() {
		return data;
	}

	public void setData(char[] data) {
		this.data = data;
	}

	public ReadInstruction[] getrIns() {
		return rIns;
	}

	public void setrIns(ReadInstruction[] rIns) {
		this.rIns = rIns;
	}

	public WriteInstruction[] getwIns() {
		return wIns;
	}

	public void setwIns(WriteInstruction[] wIns) {
		this.wIns = wIns;
	}
}
