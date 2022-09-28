package hr.fer.oprpp2;

public class BYEMsg extends Message {
	private long uid;

	public BYEMsg(long msgNum, long uid) {
		super("BYE", msgNum);
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "BYEMsg [uid=" + uid + "]";
	}

	public long getUid() {
		return uid;
	}
	
	
}
