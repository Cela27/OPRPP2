package hr.fer.oprpp2;

public class INMsg extends Message {
	
	@Override
	public String toString() {
		return "INMsg [name=" + name + ", text=" + text + "]";
	}

	private String name;
	
	private String text;

	public INMsg(long msgNum, String name, String text) {
		super("INMSG", msgNum);
		this.name=name;
		this.text=text;
	}

	public String getName() {
		return this.name;
	}

	public String getText() {
		return this.text;
	}

}
