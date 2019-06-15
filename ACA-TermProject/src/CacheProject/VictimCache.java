package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class VictimCache extends CommonImpl{ // only clean lines
	public Queue queueL1CtoVictimCache;
	// int victim_Tag;
	// int victim_Index;
	// int victim_Offset;
	// int victim_blocks;
	// int victim_BlockSize;
	// int victim_CpuBits;
	Block vCache[];
	boolean first, second;

	public VictimCache() {
		// victim_Tag = 11;
		// victim_Index = 1;
		// victim_Offset = 5;
		// victim_blocks = 2;
		// victim_BlockSize = 32;
		// victim_CpuBits = 17;
		queueL1CtoVictimCache = new Queue();
		first = false;
		second = true;
		vCache = new Block[2];
		for (int i = 0; i < 2; i++) {
			vCache[i] = new Block();
			vCache[i].setValidBit(0);
		}

	}

	public boolean isVictimCacheHit(Address address) {
		int tag = Integer.parseInt(address.getTag(), 2);
		for (int i = 0; i < 2; i++) {
			if (vCache[i].getTag() == tag && vCache[i].getValidBit() == 1) {
				return true;
			}
		}
		return false;
	}

	public Block getVBlock(Address address) {
		int index = Integer.parseInt(address.getIndex(), 2);
		int tag = Integer.parseInt(address.getTag(), 2);
		Block block = null;
		if (vCache[index].getTag() == tag) {
			block = vCache[index];
		}
		return block;

	}

	public void setVBlock(Block transferBlock, Address address) {
		if (vCache[0].getValidBit() == 0 && transferBlock.getDirtyBit() != 0) {
			vCache[0] = transferBlock;
			System.out.println("written to victim cache after LRU" );
		} else if (vCache[1].getValidBit() == 0 && transferBlock.getDirtyBit() != 0) {
			vCache[1] = transferBlock;
		}else{
			
		}
	}

	// public int getVictim_Tag() {
	// return victim_Tag;
	// }
	//
	// public void setVictim_Tag(int victim_Tag) {
	// this.victim_Tag = victim_Tag;
	// }
}
