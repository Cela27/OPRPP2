package hr.fer.oprpp2;

public class OUTMsg extends Message{
	
	@Override
	public String toString() {
		return "OUTMsg [uid=" + uid + ", text=" + text + "]";
	}

	private long uid;
	  
	private String text;

	public OUTMsg(long msgNum, long uid, String text) {
		super("OUTMSG", msgNum);
		this.uid = uid;
		this.text = text;
	}

	public long getUid() {
		return uid;
	}

	public String getText() {
		return text;
	}
	
	
}
