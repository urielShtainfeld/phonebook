package contacts;

public class ContactCompareAble implements Comparable<ContactCompareAble> {

	private Contact contact;
	private String sortBy;
	private int order;

	private String[] supportedFields = { ContactsManager.FIRST_NAME_FILED,
			ContactsManager.LAST_NAME_FILED, ContactsManager.PHONE_NUMBER_FILED };

	public ContactCompareAble(Contact c, String sortBy, int order) {
		this.contact = c;
		if (!isSupported(sortBy)) {
			throw new IllegalArgumentException("sortBy " + sortBy
					+ " is not supported");
		}
		this.order = order;
		this.sortBy = sortBy;
	}

	private boolean isSupported(String sortBy) {
		for (String string : supportedFields) {
			if (string.compareToIgnoreCase(sortBy) == 0) {
				return true;
			}
		}
		return false;
	}

	public Contact getContact() {
		return contact;
	}

	@Override
	public int compareTo(ContactCompareAble c) {
		// this can be far more generic using java reflection - which is not in
		// the syllabus

		switch (this.sortBy) {

		case ContactsManager.FIRST_NAME_FILED:
			return this.contact.getFirstName().compareTo(
					c.contact.getFirstName())
					* order;

		case ContactsManager.LAST_NAME_FILED:
			return this.contact.getLastName()
					.compareTo(c.contact.getLastName()) * order;

		case ContactsManager.PHONE_NUMBER_FILED:
			return this.contact.getPhoneNumber().compareTo(
					c.contact.getPhoneNumber())
					* order;

		}
		throw new IllegalArgumentException("Sdad");
	}

}
