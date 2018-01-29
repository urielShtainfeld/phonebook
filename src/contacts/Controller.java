package contacts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import com.sun.corba.se.pept.transport.EventHandler;

public class Controller implements ActionListener {

	private ArrayList<IView> views;
	private ContactsManager model;

	public final static String SORT_EVENT = "sort";
	public final static String EXPORT_EVENT = "export";
	public final static String GET_FIRST_EVENT = "get_first";
	public final static String GET_NEXT_EVENT = "get_next";
	public final static String GET_PREV_EVENT = "get_prev";
	public final static String GET_LAST_EVENT = "get_last";
	public static final String VIEW_INIT_EVENT = "view_init";
	private static final String MODEL_PREFIX = "MODEL_";
	private static final String VIEW_PREFIX = "VIEW_";
	public static final String ON_NEW_CONTACT_EVENT = "new_contact";
	public static final String ON_UPDATE_EVENT = "update_contact";
	public static final String ON_EDIT_START = "edit_start";
	public static final String ON_LOAD_FROM_FILE = "load_from_file";
	public static final String TIMER_EVENT = "timer_event";
	

	// public static final String TIMER_EVENT = "timer_event";

	public Controller(ContactsManager model) {
		views = new ArrayList<IView>();
		this.model = model;
		model.registerListener(this);
	}

	public void addView(IView view) {
		this.views.add(view);
		view.registerListener(this);
		view.init();

	}

	public void handleViewEvents(ActionEvent e) {
		switch (e.getActionCommand()) {
		case VIEW_PREFIX + VIEW_INIT_EVENT:
		case VIEW_PREFIX + GET_FIRST_EVENT:
			firstContactEvent();
			break;
		case VIEW_PREFIX + GET_LAST_EVENT:
			lastContactEvent();
			break;
		case VIEW_PREFIX + GET_NEXT_EVENT:
			nextContactEvent();
			break;
		case VIEW_PREFIX + GET_PREV_EVENT:
			prevContactEvent();
			// lastContactEvent();
			break;
		case VIEW_PREFIX + ON_NEW_CONTACT_EVENT:
			onNewContactEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + ON_UPDATE_EVENT:
			updateContactEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + ON_EDIT_START:
			editStartContactEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + ON_LOAD_FROM_FILE:
			loadFromFileEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + EXPORT_EVENT:
			exportToFileEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + SORT_EVENT:
			sortEvent((IView) e.getSource());
			break;
		case VIEW_PREFIX + TIMER_EVENT:
			timerEvent((IView) e.getSource());
			break;

		default:
			break;
		}

	}

	private boolean timerStartFlag = false;
	private Timer timer;

	private void timerEvent(IView iView) {
		System.out.println("controller - timerEvent:");
		timer = new Timer(150, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String direction = iView.getTimerDirection();
				IContact c = null;
				switch (direction.toLowerCase()) {
				case "asc":
					if (!timerStartFlag) {
						c = model.getFirst();
						timerStartFlag = true;
					} else {
						c = model.getNextContact();
					}

					break;
				case "desc":
					if (!timerStartFlag) {
						c = model.getLast();
						timerStartFlag = true;
					} else {
						c = model.getPrevContact();
					}

					break;
				}

				// System.out.println(c);
				//
				// if (c != null) {
				// showContact(c);
				// } else {
				//
				// }

			}
		});
		timer.start();
		//
	}

	private void prevContactEvent() {
		this.model.getPrevContact();
	}

	private void nextContactEvent() {
		this.model.getNextContact();
	}

	private void sortEvent(IView iView) {
		System.out.println("sortEvent");
		this.model.sort(iView.getSortOp(), iView.getSortField(),
				iView.getOrderField());

	}

	private void exportToFileEvent(IView iView) {

		iView.editContact(this.model.readSingleContactFromFile(
				iView.getFileName(), iView.getChosenFormat()));

	}

	private void loadFromFileEvent(IView iView) {
		iView.editContact(this.model.readSingleContactFromFile(
				iView.getFileName(), iView.getChosenFormat()));

	}

	private void editStartContactEvent(IView iView) {
		// String[] uiData = iView.getUiData();
		// this.model.updateContact(uiData[0], uiData[1], uiData[2]);
		iView.editContact(this.model.getCurrentContact());
	}

	private void updateContactEvent(IView iView) {
		String[] uiData = iView.getUiData();
		this.model.updateContact(uiData[0], uiData[1], uiData[2]);

	}

	private void onNewContactEvent(IView iView) {
		String[] uiData = iView.getUiData();
		this.model.onNewContact(uiData[0], uiData[1], uiData[2]);

	}

	private void lastContactEvent() {
		model.getLast();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().indexOf(VIEW_PREFIX) > -1) {
			handleViewEvents(e);
		} else {
			handleModelEvents(e);
		}

	}

	private void handleModelEvents(ActionEvent e) {
		// switch (e.getActionCommand()) {
		//
		// case MODEL_PREFIX + GET_FIRST_EVENT:
		// case MODEL_PREFIX + GET_LAST_EVENT:
		// case MODEL_PREFIX + GET_NEXT_EVENT:
		// case MODEL_PREFIX + GET_PREV_EVENT:
		// case MODEL_PREFIX + ON_NEW_CONTACT_EVENT:
		// case MODEL_PREFIX + ON_UPDATE_EVENT:
		//
		// for (IView iView : views) {
		// iView.showContact(this.model.getCurrentContact());
		// }
		//
		// break;
		//
		// default:
		// break;
		// }

		System.out.println("handleModelEvents");
		IContact currentContact = this.model.getCurrentContact();
		for (IView iView : views) {
			// timerStartFlag = false;
			// if (!model.hasNext() )
			// timer.stop();
			if (currentContact != null) {
				iView.showContact(currentContact);
			} else {
				timerStartFlag = false;
				try {
					timer.stop();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

		}
	}

	private void firstContactEvent() {
		System.out.println("firstContactEvent");
		model.getFirst();
	}

	public static String getModelEventName(String eventName) {
		return MODEL_PREFIX + eventName;
	}

	public static String getViewEventName(String eventName) {
		return VIEW_PREFIX + eventName;
	}
	


}
