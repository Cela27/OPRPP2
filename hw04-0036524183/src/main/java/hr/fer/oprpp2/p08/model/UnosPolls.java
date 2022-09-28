package hr.fer.oprpp2.p08.model;

/**
 * Model for storing rows from Polls
 * @author Antonio
 *
 */
public class UnosPolls {
	private long id;
	private String title;
	private String message;
	
	public UnosPolls() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}