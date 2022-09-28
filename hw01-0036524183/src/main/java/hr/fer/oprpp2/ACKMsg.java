package hr.fer.oprpp2;

public class ACKMsg extends Message {
	private long uid;
	
	public ACKMsg(long msgNum, long uid) {
		super("ACK", msgNum);
		this.uid=uid;
	}
	
	public long getUid() {
	    return this.uid;
	}

	@Override
	public String toString() {
		return "ACKMsg [uid=" + uid + "]";
	}
	
	
}
