package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */

public class L2Controller extends CommonImpl {
	int l2_Tag;
	int l2_Index;
	int l2_Offset;
	int l2_blocks;
	int l2_BlockSize;
	int l2_CpuBits;
	// Block l2cache[];
	public Queue queueL2CtoL2D = new Queue();
	public Queue queueL2CtoMemory = new Queue();
	public Queue queueMemorytoL2C = new Queue();
	public Queue queueL1CtoL2C = new Queue();
	public Queue queueL2CtoL1C = new Queue();
	L2Data l2Data;
	WriteBuffer writeBuffer;

	L2Controller() {
		l2_Tag = 3;
		l2_Index = 9;
		l2_Offset = 5;
		l2_blocks = 512;
		l2_BlockSize = 32;
		l2_CpuBits = 17;

		l2Data = new L2Data();
		writeBuffer = new WriteBuffer();
		// l2cache = new Block[512];
		//
		// ReadInstruction rdIns0 = new ReadInstruction();
		// rdIns0.setInstructionKind(0);
		// rdIns0.setAddress(formatAddress(3, l2_Tag, l2_Index, l2_Offset));
		// rdIns0.setByteEnables(4);
		// rdIns0.setInstructioNum(1);
		// rdIns0.setCommand("Read 3 4");
		//
		// ReadInstruction rdIns1 = new ReadInstruction();
		// rdIns1.setInstructionKind(0);
		// rdIns1.setAddress(formatAddress(4, l2_Tag, l2_Index, l2_Offset));
		// rdIns1.setByteEnables(4);
		// rdIns1.setInstructioNum(1);
		// rdIns1.setCommand("Read 4 4");
		//
		// ReadInstruction rdIns2 = new ReadInstruction();
		// rdIns2.setInstructionKind(0);
		// rdIns2.setAddress(formatAddress(10, l2_Tag, l2_Index, l2_Offset));
		// rdIns2.setByteEnables(4);
		// rdIns2.setInstructioNum(1);
		// rdIns2.setCommand("Read 10 4");
		//
		// ReadInstruction rdIns3 = new ReadInstruction();
		// rdIns3.setInstructionKind(0);
		// rdIns3.setAddress(formatAddress(25, l2_Tag, l2_Index, l2_Offset));
		// rdIns3.setByteEnables(4);
		// rdIns3.setInstructioNum(1);
		// rdIns3.setCommand("Read 25 4");
		//
		// ReadInstruction rdIns4 = new ReadInstruction();
		// rdIns4.setInstructionKind(0);
		// rdIns4.setAddress(formatAddress(50, l2_Tag, l2_Index, l2_Offset));
		// rdIns4.setByteEnables(4);
		// rdIns4.setInstructioNum(1);
		// rdIns4.setCommand("Read 50 4");
		//
		// ReadInstruction rdIns5 = new ReadInstruction();
		// rdIns5.setInstructionKind(0);
		// rdIns5.setAddress(formatAddress(100, l2_Tag, l2_Index, l2_Offset));
		// rdIns5.setByteEnables(4);
		// rdIns5.setInstructioNum(1);
		// rdIns5.setCommand("Read 100 4");
		//
		// ReadInstruction rdIns6 = new ReadInstruction();
		// rdIns6.setInstructionKind(0);
		// rdIns6.setAddress(formatAddress(200, l2_Tag, l2_Index, l2_Offset));
		// rdIns6.setByteEnables(4);
		// rdIns6.setInstructioNum(1);
		// rdIns6.setCommand("Read 200 4");
		//
		// ReadInstruction rdIns7 = new ReadInstruction();
		// rdIns7.setInstructionKind(0);
		// rdIns7.setAddress(formatAddress(400, l2_Tag, l2_Index, l2_Offset));
		// rdIns7.setByteEnables(4);
		// rdIns7.setInstructioNum(1);
		// rdIns7.setCommand("Read 400 4");
		//
		// ReadInstruction[] values = new ReadInstruction[8];
		// values[0] = rdIns0;
		// values[1] = rdIns1;
		// values[2] = rdIns2;
		// values[3] = rdIns3;
		// values[4] = rdIns4;
		// values[5] = rdIns5;
		// values[6] = rdIns6;
		// values[7] = rdIns7;
		//
		// for (int i = 0; i < 512; i++) {
		// l2cache[i] = new Block();
		// l2cache[i].setValidBit(0);
		// }
		//
		// l2cache[0] = new Block(values);
		// System.out.println("Instructions added to L2");
	}

	public boolean isL2Hit(Address address) {
		// Address fAddress = formatAddress(address.getAddress(), l2_Tag,
		// l2_Index, l2_Offset);
		int index = Integer.parseInt(address.getIndex(), 2);
		int tag = Integer.parseInt(address.getTag(), 2);
		boolean isInl2cache = ((l2Data.l2cache[index].getTag() == tag) && l2Data.l2cache[index].getValidBit() == 1);
		if (isInl2cache) {
			return true;
		}
		return false;
	}

	public int isValid(int index, int tag) {
		// Address fAddress = formatAddress(address.getAddress(), l2_Tag,
		// l2_Index, l2_Offset);
		int valid = 1;
		if (!(l2Data.l2cache[index].getValidBit() == 0)) {
			return 0;
		} else {
			if (l2Data.l2cache[index].getTag() == tag) {
				valid = l2Data.l2cache[index].getValidBit();
			}

			return valid;
		}
	}

	public int isDirty(int index, int tag) {
		int dirty = 0;
		if (l2Data.l2cache[index].getTag() == tag) {
			dirty = l2Data.l2cache[index].getDirtyBit();
			return dirty;
		}
		return dirty;
	}

	public void l2write(Block transferBlock, Address fAddress) {
		if (l2Data.l2cache[Integer.parseInt(fAddress.getIndex(), 2)].getValidBit() == 0) {
			l2Data.l2cache[Integer.parseInt(fAddress.getIndex(), 2)] = transferBlock;
		}
	}

	public void l2writeChar(char singleCharData, Address fAddress) {
		int tag = Integer.parseInt(fAddress.getTag());
		int index = Integer.parseInt(fAddress.getIndex(), 2);
		int offset = Integer.parseInt(fAddress.getOffset(), 2);
		if (l2Data.l2cache[index].getTag() == tag) {
			l2Data.l2cache[index].setBitData(offset, singleCharData);
			l2Data.l2cache[index].setDirtyBit(1);
		}

	}

	public Block readBlock(Address fAddress) {
		int tag = Integer.parseInt(fAddress.getTag());
		int index = Integer.parseInt(fAddress.getIndex(), 2);
		Block block = null;
		if (l2Data.l2cache[index].getTag() == tag) {
			block = l2Data.l2cache[index];
		}
		return block;
	}

}
