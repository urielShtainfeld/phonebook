package contacts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface IModel {

	IContact getFirst();
	IContact getLast();
	IContact getNext();
	IContact getPrev();
	
	void registerListener(ActionListener listener);
	
}
