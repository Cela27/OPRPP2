package hr.fer.oprpp2;

public class Message {
	
	
	private String msgType;
	private long msgNum;
	
	public Message(String msgType, long msgNum) {
		this.msgType=msgType;
		this.msgNum=msgNum;
	}

	public String getMsgType() {
		return msgType;
	}


	public long getMsgNum() {
		return msgNum;
	}	
	
	public int getNum() {
		switch (getMsgType()) {
	      case "HELLO":
	        return 1;
	      case "ACK":
	        return 2;
	      case "BYE":
	        return 3;
	      case "OUTMSG":
	        return 4;
	      case "INMSG":
	        return 5;
	    } 
		return -1;
	}
	
}
