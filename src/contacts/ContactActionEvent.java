package contacts;

import java.awt.event.ActionEvent;

public class ContactActionEvent<T> extends ActionEvent {

	private T attached;

	public ContactActionEvent(Object source, int id, String command, T t) {
		super(source, id, command);
		this.attached = t;
	}
	
	
	

}
