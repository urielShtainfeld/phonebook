package contacts;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ContactsManagerFrame extends JFrame implements IView{

	private static final long serialVersionUID = 1L;
    public static final String CONTACT_MANGER_TITLE = "Contacts manager";
	ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private JLabel firstNameLabel = new JLabel("");
	private JLabel lastNameLabel = new JLabel("");
	private JLabel phoneNumberLabel = new JLabel("");
	private JButton updateButton;

	private JTextField fileNameField = new JTextField(10);

	private JTextField firstNameTextField = new JTextField(10);
	private JTextField lastNameTextField = new JTextField(10);
	private JTextField phoneNumberTextField = new JTextField(10);

	private String formatTypes[] = { "txt", "obj.dat", "byte.dat" };
	JComboBox<String> formatsComboBox = new JComboBox<String>(formatTypes);

	private String[] sortTypes = { ContactsManager.FIRST_NAME_FILED,
			ContactsManager.LAST_NAME_FILED, ContactsManager.PHONE_NUMBER_FILED };

	JComboBox<String> sortTypesComboBox = new JComboBox<String>(sortTypes);

	private String ops[] = { "sort-field", "sort-count", "reverse" };
	JComboBox<String> opsComboBox = new JComboBox<String>(ops);
	private String sortOrders[] = { "asc", "desc" };
	JComboBox<String> sortOrdersComboBox = new JComboBox<String>(sortOrders);

	private JComboBox<String> presentationSortOrdersComboBox = new JComboBox<String>(
			sortOrders);

	private Timer timer;
	private boolean timerStartFlag = false;

	public ContactsManagerFrame() {
		this.setTitle(CONTACT_MANGER_TITLE);
		this.setSize(500, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.add(createTopPanel(), BorderLayout.NORTH);
		this.add(createMidPanel(), BorderLayout.CENTER);

//		processEvent(this, Controller.getViewEventName(Controller.VIEW_INIT_EVENT));

		// if (cm.getCurrentContact() != null) {
		// showContact(cm.getCurrentContact());
		// }

		this.add(createBottomPanel(), BorderLayout.SOUTH);

		
	}

	private JPanel createBottomPanel() {

		JPanel wrapper = new JPanel(new BorderLayout());

		JPanel bottom = new JPanel();

		bottom.setPreferredSize(new Dimension(this.getWidth(),
				this.getHeight() / 4));

		bottom.setLayout(new GridLayout(1, 3, 20, 20));

		JPanel createPanel = new JPanel();
		createPanel.setLayout(new GridLayout(1, 2, 10, 10));

		JPanel fileTextPanel = new JPanel(new GridLayout(3, 1, 30, 30));

		JButton loadButton = new JButton("load file");
		fileTextPanel.add(new JLabel("file path:"));
		fileTextPanel.add(fileNameField);
		fileTextPanel.add(loadButton);
		createPanel.add(formatsComboBox);
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String format = formatsComboBox.getItemAt(formatsComboBox
						.getSelectedIndex());
				// System.out.println(format);
//				try {
					processEvent(ContactsManagerFrame.this, Controller.getViewEventName(Controller.EXPORT_EVENT));
				
//					cm.exportCurrentContact(format);
//				} catch (IOException e1) {
					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

			}
		});

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				onLoadFromFile();

			}
		});

		createPanel.add(exportButton);

		bottom.add(createPanel);

		bottom.add(fileTextPanel);

		wrapper.add(bottom, BorderLayout.NORTH);

		wrapper.add(createLowestPanels(), BorderLayout.CENTER);

		return wrapper;
		// return null;

	}

	private Component createLowestPanels() {
		JPanel lowest = new JPanel(new BorderLayout());
		lowest.add(createSorterPanel(), BorderLayout.NORTH);
		lowest.add(createPresentationPanel(), BorderLayout.CENTER);
		return lowest;

	}

	private Component createPresentationPanel() {
		JPanel presentationPanel = new JPanel(new GridLayout(1, 2, 20, 20));
		// bottom.setLayout();

		JButton showButton = new JButton("SHOW");

		showButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processEvent(ContactsManagerFrame.this, Controller.getViewEventName(Controller.TIMER_EVENT));
			}
		});

		presentationPanel.add(presentationSortOrdersComboBox);
		presentationPanel.add(showButton);
		return presentationPanel;
	}

	private JPanel createSorterPanel() {
		JPanel sorterPanel = new JPanel(new GridLayout(1, 4, 20, 20));
		// bottom.setLayout();

		JButton sortButton = new JButton("SORT");

		sortButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				processEvent(ContactsManagerFrame.this,
						Controller.getViewEventName(Controller.SORT_EVENT));
				// cm.sort(op, field, order);
				// showContact(cm.getFirst());
			}
		});

		sorterPanel.add(opsComboBox);
		sorterPanel.add(sortTypesComboBox);
		sorterPanel.add(sortOrdersComboBox);
		sorterPanel.add(sortButton);
		return sorterPanel;

	}

	public JPanel createTopPanel() {
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension(this.getWidth(),
				this.getHeight() / 3));
		// top.setLayout(new FlowLayout(FlowLayout.LEADING));
		top.setLayout(new GridLayout(4, 2, 20, 20));
		JLabel fName = new JLabel("First name:");

		JLabel lName = new JLabel("Last name:");

		JLabel phoneNumber = new JLabel("Phone number");

		updateButton = new JButton("Update");

		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onUpdatePressed();
			}
		});

		updateButton.setEnabled(false);
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createContact();
			}
		});

		top.add(fName);
		top.add(firstNameTextField);
		top.add(lName);
		top.add(lastNameTextField);
		top.add(phoneNumber);
		top.add(phoneNumberTextField);
		top.add(createButton);
		top.add(updateButton);

		return top;
	}

	private void onLoadFromFile() {
		// String fileName = fileNameField.getText();
		// String format = formatsComboBox.getItemAt(formatsComboBox
		// .getSelectedIndex());
		// setFileds(cm.readSingleContactFromFile(fileName, format));

		processEvent(ContactsManagerFrame.this, Controller.ON_LOAD_FROM_FILE);
	}

	private void createContact() {

		processEvent(ContactsManagerFrame.this, Controller.getViewEventName(Controller.ON_NEW_CONTACT_EVENT));


	}

	public JPanel createMidPanel() {

		JPanel mid = new JPanel();
		mid.setLayout(new BorderLayout());

		JButton next = new JButton(">");
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onNextPressed();

			}
		});
		JButton last = new JButton("Last");

		last.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onLastPressed();

			}

		});

		JButton first = new JButton("First");

		first.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onFirstPressed();

			}

		});

		JButton prev = new JButton("<");
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onPrevPressed();

			}
		});

		JButton edit = new JButton("Edit contact");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onEditPressed();

			}

		});

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(4, 2, 50, 50));

		JLabel phoneNumberTagLabel = new JLabel("Phone number");
		JLabel lNameLabel = new JLabel("Last name:");
		JLabel fNameLabel = new JLabel("First name:");

		centerPanel.add(fNameLabel);
		centerPanel.add(this.firstNameLabel);

		centerPanel.add(lNameLabel);
		centerPanel.add(this.lastNameLabel);
		centerPanel.add(phoneNumberTagLabel);
		centerPanel.add(this.phoneNumberLabel);
		centerPanel.add(edit);
		mid.add(next, BorderLayout.EAST);
		mid.add(last, BorderLayout.WEST);

		mid.add(centerPanel, BorderLayout.CENTER);

		JPanel west = new JPanel();
		JPanel east = new JPanel();

		west.setLayout(new GridLayout(2, 1, 5, 5));
		east.setLayout(new GridLayout(2, 1, 5, 5));
		west.add(prev);
		west.add(first);
		east.add(next);
		east.add(last);
		mid.add(west, BorderLayout.WEST);
		mid.add(east, BorderLayout.EAST);

		return mid;
	}

	private void onEditPressed() {

		// setFileds(cm.getCurrentContact());
		processEvent(this,
				Controller.getViewEventName(Controller.ON_EDIT_START));
		
	}

	private void onUpdatePressed() {

		updateButton.setEnabled(false);
		// cm.updateContact(firstNameTextField.getText(),
		// lastNameTextField.getText(), phoneNumberTextField.getText());
		// showContact(cm.getCurrentContact());

		processEvent(this,
				Controller.getViewEventName(Controller.ON_UPDATE_EVENT));

//		setFileds(null);

	}

	private void onPrevPressed() {
		updateButton.setEnabled(false);
		// IContact c = cm.getPrevContact();
		// showContact(c);
		processEvent(this,
				Controller.getViewEventName(Controller.GET_PREV_EVENT));
	}

	private void onNextPressed() {
		updateButton.setEnabled(false);
		
		processEvent(this,
				Controller.getViewEventName(Controller.GET_NEXT_EVENT));
	}

	public void init() {

		this.setVisible(true);
		processEvent(this, Controller.getViewEventName(Controller.VIEW_INIT_EVENT));
	}

	protected void onFirstPressed() {
		IContact c = null;

		// c = cm.getFirst();
		processEvent(this,
				Controller.getViewEventName(Controller.GET_FIRST_EVENT));

//		showContact(c);
	}

	private void onLastPressed() {
		// IContact c = null;
		//
		// c = cm.getLast();
		//
		// showContact(c);
		processEvent(this,
				Controller.getViewEventName((Controller.GET_LAST_EVENT)));
	}

	public void setFileds(IContact contact) {

		if (contact == null) {
			this.firstNameTextField.setText("");
			this.lastNameTextField.setText("");
			this.phoneNumberTextField.setText("");
		} else {
			String[] data = contact.getUiData();
			this.firstNameTextField.setText(data[0]);
			this.lastNameTextField.setText(data[1]);
			this.phoneNumberTextField.setText(data[2]);
		}

	}

	@Override
	public void showContact(IContact contact) {
		setFileds(null);
		System.out.println("showContact: " + contact);
		if (contact == null) {
			return;
		}

		String[] data = contact.getUiData();

		this.firstNameLabel.setText(data[0]);
		this.lastNameLabel.setText(data[1]);
		this.phoneNumberLabel.setText(data[2]);

	}

	private void processEvent(Object source, String command) {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(source, -1, command));

		}
	}

	@Override
	public void registerListener(ActionListener listener) {
		listeners.add(listener);
	}

	@Override
	public String[] getUiData() {
		String firstName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();
		String phoneNumber = phoneNumberTextField.getText();
		//
		// if (firstName.length() == 0) {
		// return;
		// }
		return new String[] { firstName, lastName, phoneNumber };
	}

	@Override
	public void editContact(IContact contact) {
		updateButton.setEnabled(true);
		System.out.println("editContact: "+contact);
		setFileds(contact);

	}

	@Override
	public String getChosenFormat() {

		String format = formatsComboBox.getItemAt(formatsComboBox
				.getSelectedIndex());
		return format;
	}

	@Override
	public String getFileName() {
		String fileName = fileNameField.getText();
		return fileName;
	}

	@Override
	public String getSortOp() {
		String op = opsComboBox.getItemAt(opsComboBox.getSelectedIndex());
		return op;
	}

	@Override
	public String getSortField() {
		String field = sortTypesComboBox.getItemAt(sortTypesComboBox
				.getSelectedIndex());
		return field;
	}

	@Override
	public String getOrderField() {
		String order = sortOrdersComboBox.getItemAt(sortOrdersComboBox
				.getSelectedIndex());
		return order;
	}

	@Override
	public String getTimerDirection() {
		return presentationSortOrdersComboBox.getItemAt(presentationSortOrdersComboBox.getSelectedIndex());
	}
}
