package contacts;

public class ContactScoreDec {

	private Contact contact;
	private int score;

	public ContactScoreDec(Contact c, int score) {
		this.contact = c;
		this.score = score;
	}

	public ContactScoreDec(Contact c) {
		this(c, 1);

	}

	public Contact getContact() {
		return contact;
	}

	public int getScore() {
		return score;
	}

	public void add1() {
		this.score++;
	}

}
