package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class L2Data {

	int l2_Tag;
	int l2_Index;
	int l2_Offset;
	int l2_blocks;
	int l2_BlockSize;
	int l2_CpuBits;
	Block l2cache[];
	public Queue queueL2CtoL2D = new Queue();
	public Queue queueL2DtoL2C = new Queue();

	public L2Data() {
		l2_Tag = 3;
		l2_Index = 9;
		l2_Offset = 5;
		l2_blocks = 512;
		l2_BlockSize = 32;
		l2_CpuBits = 17;
		l2cache = new Block[512];
		for (int i = 0; i < 512; i++) {
			l2cache[i] = new Block();
			l2cache[i].setValidBit(0);
		}
	}

}
