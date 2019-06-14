package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class WriteBuffer {
	public Queue queuetoWriteBuffer;
	int buffer_Tag;
	int buffer_Index;
	int buffer_Offset;
	int buffer_blocks;
	int buffer_BlockSize;
	int buffer_CpuBits;
	Block bCache[];

	public WriteBuffer() {
		buffer_Tag = 12;
		buffer_Index = 0;
		buffer_Offset = 5;
		buffer_blocks = 2;
		buffer_BlockSize = 32;
		buffer_CpuBits = 17;

		for (int i = 0; i < 2; i++) {
			bCache[i] = new Block();
			bCache[i].setValidBit(0);
		}

	}

	public boolean isBuffercacheHit(Address address) {
		int tag = Integer.parseInt(address.getTag(), 2);
		for (int i = 0; i < 2; i++) {
			if (bCache[i].getTag() == tag) {
				return true;
			}
		}
		return true;
	}

	public Block getBBlock(Address address) {
		int index = Integer.parseInt(address.getIndex(), 2);
		int tag = Integer.parseInt(address.getTag(), 2);
		Block block = null;
		if (bCache[index].getTag() == tag) {
			block = bCache[index];
		}
		return block;

	}

	public void setBBlock(Block transferBlock, Address address) {
		int index = Integer.parseInt(address.getIndex(), 2);
		if (bCache[index].getValidBit() == 0 && transferBlock.getDirtyBit() != 0) {
			bCache[index] = transferBlock;
		} else {
			// replace
		}
	}

	public int getBuffer_Tag() {
		return buffer_Tag;
	}

	public void setBuffer_Tag(int victim_Tag) {
		this.buffer_Tag = victim_Tag;
	}

}
