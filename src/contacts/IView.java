package contacts;

import java.awt.event.ActionListener;

public interface IView {

	void showContact(IContact contact);

	void editContact(IContact contact);
	
	String[] getUiData();
	
	
	String getChosenFormat();
	
	String getFileName();
	
	String getTimerDirection();
	
	String getSortOp();
	
	String getSortField();
	
	String getOrderField();
	
	void init();
//	void showContact(IContact contact);
	
	void registerListener(ActionListener listener);
	
}
