/**
 * 
 */
package CacheProject;

/**
 * @author ${Krishna Teja Ayinala, Sindhura Bonthu}
 *
 */
public class Address {
	private String tag;
	private String index;
	private String offset;
	private int address;
	private char[] binaryAddress;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public char[] getBinaryAddress() {
		return binaryAddress;
	}

	public void setBinaryAddress(char[] binaryAddress) {
		this.binaryAddress = binaryAddress;
	}

}
