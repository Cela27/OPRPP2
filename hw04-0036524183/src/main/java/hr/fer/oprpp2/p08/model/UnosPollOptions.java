package hr.fer.oprpp2.p08.model;

/**
 * Model for storing rows from PollOptions
 * @author Antonio
 *
 */
public class UnosPollOptions {
	@Override
	public String toString() {
		return "UnosPollOptions [id=" + id + ", optionTitle=" + optionTitle + ", optionLink=" + optionLink + ", pollID="
				+ pollID + ", glasovi=" + glasovi + "]";
	}

	private long id;
	private String optionTitle;
	private String optionLink;
	private int pollID;
	private int glasovi;
	
	public UnosPollOptions() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionLink() {
		return optionLink;
	}

	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	public int getPollID() {
		return pollID;
	}

	public void setPollID(int pollID) {
		this.pollID = pollID;
	}

	public int getGlasovi() {
		return glasovi;
	}

	public void setGlasovi(int glasovi) {
		this.glasovi = glasovi;
	}

	
}