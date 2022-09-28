package hr.fer.oprpp2;

public class HELLOMsg extends Message {

	@Override
	public String toString() {
		return "HELLOMsg [name=" + name + ", key=" + key + "]";
	}

	private String name;
	
	private long key;
	
	public HELLOMsg(long msgNum, String name, long key) {
		super("HELLO", msgNum);
		this.name = name;
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public long getKey() {
		return key;
	}
	
	
}
