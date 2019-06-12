package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */

public class L2Controller {
	int l2_Tag;
	int l2_Index;
	int l2_Offset;
	int l2_blocks;
	int l2_BlockSize;
	int l2_CpuBits;
	public Queue queueL2CtoL2D = new Queue();
	public Queue queueL2CtoMemory = new Queue();
	public Queue queueMemorytoL2C = new Queue();
	public Queue queueL1CtoL2C = new Queue();
	public Queue queueL2CtoL1C = new Queue();

	L2Controller() {
		l2_Tag = 3;
		l2_Index = 9;
		l2_Offset = 5;
		l2_blocks = 512;
		l2_BlockSize = 32;
		l2_CpuBits = 17;
		
		
	}
}
