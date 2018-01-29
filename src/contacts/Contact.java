package contacts;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Contact implements IContact, Serializable, Comparable<Contact> {

	protected static int numOfContacts = 0;
	public static final int SIZE_OF_FIELD = 10;

	private static final long serialVersionUID = 1L;
	private static final int NUM_OF_FIELDS = 4;

	protected int id;
	protected String firstName;
	protected String lastName;
	protected String phoneNumber;

	public Contact(int id, String firstName, String lastName, String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	public Contact(String firstName, String lastName, String phoneNumber) {
		this(++numOfContacts, firstName, lastName, phoneNumber);
	}

	public static void setNumOfContacts(int num) {
		numOfContacts = num;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public void writeObject(RandomAccessFile randomAccessFile)
			throws IOException {

		FixedLengthStringIO.writeFixedLengthString("" + this.id, SIZE_OF_FIELD,
				randomAccessFile);
		FixedLengthStringIO.writeFixedLengthString(this.firstName,
				SIZE_OF_FIELD, randomAccessFile);
		FixedLengthStringIO.writeFixedLengthString(this.lastName,
				SIZE_OF_FIELD, randomAccessFile);
		FixedLengthStringIO.writeFixedLengthString(this.phoneNumber,
				SIZE_OF_FIELD, randomAccessFile);

	}

	public String toString() {
		return "int:id:" + this.id + ",str:firstName:" + this.firstName
				+ ",str:lastName:" + this.lastName + ",str:phoneNumber:"
				+ this.phoneNumber;
	}

	@Override
	public void export(String format, File file) throws IOException {
		switch (format) {
		case "txt":
			PrintWriter pw = new PrintWriter(file);
			pw.write(this.toString());
			pw.close();

			break;

		case "obj.dat":
			ObjectOutputStream output = new ObjectOutputStream(
					new FileOutputStream(file));

			output.writeObject(this);

			output.close();

			break;

		case "byte.dat":
			DataOutputStream dataOutput = new DataOutputStream(
					new FileOutputStream(file));

			dataOutput.writeInt(this.id);
			dataOutput.writeUTF(this.firstName);
			dataOutput.writeUTF(this.lastName);
			dataOutput.writeUTF(this.phoneNumber);

			break;

		default:
			break;
		}

		return;

	}

	@Override
	public String[] getUiData() {
		String[] uiData = { this.firstName, this.lastName, this.phoneNumber };
		System.out.println("getUiData: " + uiData[0]);
		return uiData;
	}

	@Override
	public int getObjectSize() {

		return (int) (SIZE_OF_FIELD * NUM_OF_FIELDS * 2);
	}

	@Override
	public int compareTo(Contact o) {

		return this.firstName.compareTo(o.firstName);
	}

}
