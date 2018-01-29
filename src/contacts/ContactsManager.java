package contacts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import collections.FileListIterator;

public class ContactsManager{

	private RandomAccessFile contactsFile;
	private Contact currentContact;
	private FileListIterator<Contact> contactsListIt;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	public static final String FIRST_NAME_FILED = "FIRST_NAME_FIELD";
	public static final String LAST_NAME_FILED = "LAST_NAME_FIELD";
	public static final String PHONE_NUMBER_FILED = "PHONE_NUMBER_FILED";

	public ContactsManager(String fileName) throws IOException {

		contactsFile = new RandomAccessFile(new File(fileName), "rw");

		contactsListIt = new FileListIterator<Contact>(contactsFile,
				Contact.SIZE_OF_FIELD * 4 * 2);

		if (contactsListIt.hasNext()) {
			currentContact = contactsListIt.next();
		}
		//
		// if (contactsFile.length() > 0) {
		// contactsFile.seek(0);
		// currentContact = loadContact(contactsFile);
		// }
		if (currentContact != null) {
			Contact.setNumOfContacts((int) (contactsFile.length() / currentContact
					.getObjectSize()));
		}

	}

	public void updateContact(String firstName, String lastName,
			String phoneNumber) {
		
		this.currentContact = new Contact(firstName, lastName, phoneNumber);
		System.out.println("cm updateContact: " + currentContact);
		this.contactsListIt.set(currentContact);
		processEvent(this, Controller.getModelEventName(Controller.ON_UPDATE_EVENT));

	}

	public IContact getFirst() {
		System.out.println("model - getFirst");
		// Contact c = currentContact;
		resetIterator();
		currentContact = this.contactsListIt.next();
//		if (c != null) {
//			currentContact = c;
//		}
		System.out.println("getFirst " + currentContact);
		processEvent(this, Controller.getModelEventName(Controller.GET_FIRST_EVENT));
		return currentContact;

	}
	
	private void processEvent(Object source, String command) {
		for (ActionListener listener: listeners) {
			listener.actionPerformed(new ActionEvent(source, -1, command));
		}
	}


	public Contact getLast() {

		Contact c = null;
		resetIterator();

		while (this.contactsListIt.hasNext()) {
			c = this.contactsListIt.next();
		}
		currentContact = c;

		processEvent(this, Controller.getModelEventName(Controller.GET_LAST_EVENT));
		return c;

		// return null;

	}

	public Contact getPrevContact() {

		Contact c = this.contactsListIt.previous();
		if (c != null && currentContact != null && c.compareTo(currentContact) == 0) {
			c = contactsListIt.previous();
		}
//		if (c != null) {
//			/
//			processEvent(this, Controller.getModelEventName(Controller.GET_PREV_EVENT));
//			return c;
//		}
		currentContact = c;
		processEvent(this, Controller.getModelEventName(Controller.GET_PREV_EVENT));
		return null;

	}

	public void onNewContact(String firstName, String lastName,
			String phoneNumber) {

		Contact conatct = new Contact(firstName, lastName, phoneNumber);

		this.contactsListIt.add(conatct);
		this.currentContact = conatct;
		processEvent(this, Controller.getModelEventName(Controller.ON_NEW_CONTACT_EVENT));
	}

	public Contact getCurrentContact() {
		System.out.println(currentContact);
		return currentContact;
	}

	public Contact getNextContact() {
		Contact c = contactsListIt.next();

		if (c != null && c.compareTo(currentContact) == 0) {
			c = contactsListIt.next();
		}
//		if (c != null) {
//			
//			processEvent(this, Controller.getModelEventName(Controller.GET_PREV_EVENT));
//			return c;
//		}
		currentContact = c;
		processEvent(this, Controller.getModelEventName(Controller.GET_PREV_EVENT));
		return null;

	}

	public void exportCurrentContact(String format) throws IOException {
		System.out.println("Exporting " + currentContact.getId()
				+ " to format: " + format);
		currentContact.export(format, new File(currentContact.getId() + "."
				+ format));
	}

	public IContact readSingleContactFromFile(String fileName, String format) {

		// System.out.println(fileName);
		Contact contact = null;
		try {
			File f = new File(fileName);

			switch (format) {

			case "obj.dat":
				ObjectInputStream input;

				input = new ObjectInputStream(new FileInputStream(f));

				Object o = input.readObject();
				contact = (Contact) o;

				// System.out.println(contact);

				input.close();
				return contact;

			case "byte.dat":

				DataInputStream dataInputStream = new DataInputStream(
						new FileInputStream(f));

				int id = dataInputStream.readInt();
				String firstName = dataInputStream.readUTF();
				String lastName = dataInputStream.readUTF();
				String phoneNumber = dataInputStream.readUTF();

				contact = new Contact(id, firstName, lastName, phoneNumber);
				return contact;

			case "txt":

				Scanner s = new Scanner(f);

				String line = s.nextLine();
				contact = createContactFromTextLine(line.split(","));
				return contact;

			default:
				System.out.println(format + "not supported");
				break;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	private Contact createContactFromTextLine(String[] split) {

		int id = 0;
		ArrayList<String> params = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {

			String[] data = split[i].split(":");
			switch (data[0]) {
			case "int":

				id = Integer.parseInt(data[2].trim());
				break;

			case "str":
				params.add(data[2].trim());
				break;

			}
		}
		Contact c = new Contact(id, params.get(0), params.get(1), params.get(2));

		return c;
	}

	private void sortNoDups(String sortBy, int order) throws IOException {
		Set<ContactCompareAble> set = new TreeSet<ContactCompareAble>();
		resetIterator();

		do {
			Contact c = this.contactsListIt.next();
			if (c == null) {
				break;
			}
			ContactCompareAble cc = new ContactCompareAble(c, sortBy, order);
			set.add(cc);
		} while (this.contactsListIt.hasNext());

		this.contactsFile.setLength(0);
		for (ContactCompareAble contactCompareAble : set) {
			contactCompareAble.getContact().writeObject(this.contactsFile);
		}

		resetIterator();

	}

	private void sortByOccurencesDups(String coutBy, int order)
			throws IOException {

		Map<String, ContactScoreDec> map = new HashMap<String, ContactScoreDec>();

		resetIterator();
		do {

			Contact c = this.contactsListIt.next();

			switch (coutBy) {
			case FIRST_NAME_FILED:
				if (map.get(c.getFirstName()) != null) {
					ContactScoreDec csd = map.get(c.getFirstName());
					csd.add1();
				} else {
					map.put(c.getFirstName(), new ContactScoreDec(c));
				}
				break;
			case LAST_NAME_FILED:
				if (map.get(c.getLastName()) != null) {
					ContactScoreDec csd = map.get(c.getLastName());
					csd.add1();
				} else {
					map.put(c.getFirstName(), new ContactScoreDec(c));
				}
				break;
			case PHONE_NUMBER_FILED:
				if (map.get(c.getPhoneNumber()) != null) {
					ContactScoreDec csd = map.get(c.getPhoneNumber());
					csd.add1();
				} else {
					map.put(c.getFirstName(), new ContactScoreDec(c));
				}
				break;
			}

		} while (this.contactsListIt.hasNext());

		Comparator<ContactScoreDec> scoreComparator = new Comparator<ContactScoreDec>() {

			@Override
			public int compare(ContactScoreDec c1, ContactScoreDec c2) {
				return (c1.getScore() - c2.getScore()) * order >= 0 ? 1 : -1;
			}
		};

		TreeSet<ContactScoreDec> tm = new TreeSet<ContactScoreDec>(
				scoreComparator);

		for (Entry<String, ContactScoreDec> entry : map.entrySet()) {
			tm.add(entry.getValue());
		}

		for (ContactScoreDec contactScoreDec : tm) {
			System.out.println(contactScoreDec.getContact().getFirstName()
					+ " appeared " + contactScoreDec.getScore() + " times ");
		}
		this.contactsFile.setLength(0);
		for (ContactScoreDec contactCompareAble : tm) {
			contactCompareAble.getContact().writeObject(this.contactsFile);
		}
		resetIterator();

	}

	private void resetIterator() {
		while (this.contactsListIt.hasPrevious()) {
			this.contactsListIt.previous();
		}

	}

	private void reverese() throws IOException {
		resetIterator();
		Stack<Contact> stack = new Stack<Contact>();

		do {
			stack.push(this.contactsListIt.next());
		} while (this.contactsListIt.hasNext());

		this.contactsFile.setLength(0);
		while (!stack.isEmpty()) {
			Contact c = stack.pop();
			c.writeObject(this.contactsFile);
		}
		this.contactsFile.seek(Contact.SIZE_OF_FIELD * 4 * 2);

	}

	public void sort(String op, String field, String order) {
		System.out.println("cm sort: " + op + " " + field + " " + order);
		try {
			int o = order.compareToIgnoreCase("asc") == 0 ? 1 : -1;
			switch (op) {
			case "sort-field":
				sortNoDups(field, o);
				break;

			case "sort-count":
				sortByOccurencesDups(field, o);

				break;

			case "reverse":

				reverese();

				break;

			default:
				break;
			}
		} catch (IOException e) {

		}
	}

	
	public void registerListener(ActionListener listener) {
		listeners.add(listener);		
	}
	
}
