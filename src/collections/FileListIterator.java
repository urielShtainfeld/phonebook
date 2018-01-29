package collections;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ListIterator;

import contacts.Contact;
import contacts.FixedLengthStringIO;
import contacts.IContact;

public class FileListIterator<T extends IContact> implements ListIterator<T> {

	private RandomAccessFile raf;
	private int sizeOfT;

	private long lastElementPointer = -1;

	public FileListIterator(RandomAccessFile raf, int sizeOfObject)
			throws IOException {
		this.raf = raf;
		this.sizeOfT = sizeOfObject;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		try {

			return this.raf.getFilePointer() + sizeOfT <= this.raf.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public T next() {
		try {
			if (hasNext()) {
				lastElementPointer = this.raf.getFilePointer();
				return loadContact();
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasPrevious() {

		try {
			// System.out.println(this.raf.getFilePointer() - sizeOfT >= 0);
			return this.raf.getFilePointer() - sizeOfT >= 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public T previous() {
		try {
			// this is the first contact
			if (hasPrevious()) {

				long prevContactPointer = raf.getFilePointer() - sizeOfT;
				lastElementPointer = prevContactPointer;
				this.raf.seek(prevContactPointer);

				T t = loadContact();
				this.raf.seek(prevContactPointer);
				return t;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int nextIndex() {
//		// TODO Auto-generated method stub
//		try {
//			return (int) ((this.raf.getFilePointer()) % sizeOfT);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return -1;
	}

	@Override
	public int previousIndex() {
//		try {
//			return (int) ((this.raf.getFilePointer() - sizeOfT) % sizeOfT);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return -1;
	}

	@Override
	public void remove() {
		// TODO

	}

	@Override
	public void set(T t) {

		try {
			raf.seek(lastElementPointer);
			t.writeObject(raf);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void add(T t) {

		try {
			long pointer = raf.getFilePointer();
			raf.seek(raf.length());
			t.writeObject(raf);
			// System.out.println("sadsad");
			raf.seek(pointer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// generic using - reflection (not in syllabus)
	// @SuppressWarnings("unchecked")
	// public T initContactWorkaround() {
	// Class<T> mClass = null; // this should be received in the constructor
	// try {
	// mClass = (Class<T>) Class.forName(t.getClass().getName());
	// } catch (ClassNotFoundException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// return null;
	// }
	// ;
	// try {
	// Class<?> stringClass = Class.forName("String");
	//
	// try {
	// return mClass.getDeclaredConstructor(Integer.TYPE, stringClass,
	// stringClass, stringClass).newInstance(10);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	//
	// } catch (ClassNotFoundException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// return null;
	//
	// }

	// simple approach

	private T loadContact() throws IOException {
		String idStr = FixedLengthStringIO.readFixedLengthString(
				Contact.SIZE_OF_FIELD, raf).trim();
		int id = Integer.parseInt(idStr);
		String firstName = FixedLengthStringIO.readFixedLengthString(
				Contact.SIZE_OF_FIELD, raf).trim();
		String lastName = FixedLengthStringIO.readFixedLengthString(
				Contact.SIZE_OF_FIELD, raf).trim();
		String phoneNumber = FixedLengthStringIO.readFixedLengthString(
				Contact.SIZE_OF_FIELD, raf).trim();

		return initContactWorkaround(id, firstName, lastName, phoneNumber);

	}

	@SuppressWarnings("unchecked")
	public T initContactWorkaround(int id, String firstName, String lastName,
			String phoneNumber) {
		return (T) new Contact(id, firstName, lastName, phoneNumber);
	}

}
