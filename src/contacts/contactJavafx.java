package contacts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class contactJavafx extends GridPane implements IView {
	ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	public final static int WIDTH = 400;
	public final static int HEIGHT = 500;
	private Label firstNameLabel = new Label("");
	private Label lastNameLabel = new Label("");
	private Label phoneNumberLabel = new Label("");
	private Button updateButton;

	private TextField fileNameField = new TextField();

	private TextField firstNameTextField = new TextField();
	private TextField lastNameTextField = new TextField();
	private TextField phoneNumberTextField = new TextField();
	private String formatTypes[] = { "txt", "obj.dat", "byte.dat" };
	ComboBox<String> formatsComboBox = new ComboBox<String>();

	private String[] sortTypes = { ContactsManager.FIRST_NAME_FILED, ContactsManager.LAST_NAME_FILED,
			ContactsManager.PHONE_NUMBER_FILED };

	ComboBox<String> sortTypesComboBox = new ComboBox<String>();

	private String ops[] = { "sort-field", "sort-count", "reverse" };
	ComboBox<String> opsComboBox = new ComboBox<String>();
	private String sortOrders[] = { "asc", "desc" };
	ComboBox<String> sortOrdersComboBox = new ComboBox<String>();

	private ComboBox<String> presentationSortOrdersComboBox = new ComboBox<String>();
	public enum  colors {
		RED (Color.RED),
		BLUE (Color.BLUE),
		GREEN (Color.GREEN),
		BLACK (Color.BLACK);

	    private final Color ColorCode;

	    colors(Color ColorCode) {
	        this.ColorCode = ColorCode;
	    }
	    
	    public Color getColorCode() {
	        return this.ColorCode;

	    }
	}
	
	public contactJavafx() {
		this.setPrefSize(WIDTH,HEIGHT);
		this.setMinWidth(WIDTH);
		this.setMinHeight(HEIGHT);
		this.add(createTopPanel(), 0, 0);
		this.add(createMidPanel(), 0, 1);
		this.add(createBottomPanel(), 0, 2);
		//Red Color Temp = new Color(1,0,0,1);
		//Green Color Temp = new Color(0,1,0,1);	
		//Blue Color Temp = new Color(0,0,1,1);
		//Black Color Temp = new Color(0,0,0,1);
		//this.setTop(createTopPanel());
		//this.setCenter(createMidPanel());
		//this.setBottom(createBottomPanel());
		setColor(colors.BLACK);
			}

	private GridPane createBottomPanel() {
		GridPane wrapper = new GridPane();
		GridPane bottom = new GridPane();
		GridPane createPanel = new GridPane();
		GridPane fileTextPanel = new GridPane();
		Button loadButton = new Button("load file");
		
		
		fileTextPanel.add(new Label("file path:"), 0, 0);
		fileTextPanel.add(fileNameField, 0, 1);
		fileTextPanel.add(loadButton, 0, 2);
		ObservableList<String> formats = FXCollections.observableArrayList(formatTypes);
		formatsComboBox.getItems().addAll(formats);
		createPanel.add(formatsComboBox, 0, 1);
		Button exportButton = new Button("Export");
		exportButton.setOnAction(e -> {
					processEvent(contactJavafx.this, Controller.getViewEventName(Controller.EXPORT_EVENT));
		});
		loadButton.setOnAction(e -> {
				onLoadFromFile();
		});
		createPanel.add(exportButton, 0, 2);
		
		bottom.add(createPanel, 0, 0);

		bottom.add(fileTextPanel, 1, 0);

		wrapper.add(bottom, 0, 1);

		wrapper.add(createLowestPanels(), 0, 2);

		return wrapper;
	}

	private BorderPane createLowestPanels() {
		BorderPane lowest = new BorderPane();
		lowest.setTop(createSorterPanel());
		lowest.setCenter(createPresentationPanel());
		return lowest;

	}

	private GridPane createPresentationPanel() {
		GridPane presentationPanel = new GridPane();

		Button showButton = new Button("SHOW");

		showButton.setOnAction(e -> {
			processEvent(contactJavafx.this, Controller.getViewEventName(Controller.TIMER_EVENT));
		});

		ObservableList<String> SortFields = FXCollections.observableArrayList(sortOrders);
		presentationSortOrdersComboBox.getItems().addAll(SortFields);
		presentationPanel.add(presentationSortOrdersComboBox, 0, 0);
		presentationPanel.add(showButton, 1, 0);
		return presentationPanel;
	}

	private GridPane createSorterPanel() {
		GridPane sorterPanel = new GridPane();
		Button sortButton = new Button("SORT");

		sortButton.setOnAction(e -> {

			processEvent(contactJavafx.this, Controller.getViewEventName(Controller.SORT_EVENT));
		});
		ObservableList<String> OperetionTypes = FXCollections.observableArrayList(ops);
		opsComboBox.getItems().addAll(OperetionTypes);

		ObservableList<String> SortTypes = FXCollections.observableArrayList(sortTypes);
		sortTypesComboBox.getItems().addAll(SortTypes);

		ObservableList<String> SortFields = FXCollections.observableArrayList(sortOrders);
		sortOrdersComboBox.getItems().addAll(SortFields);

		sorterPanel.add(opsComboBox, 0, 0);
		sorterPanel.add(sortTypesComboBox, 1, 0);
		sorterPanel.add(sortOrdersComboBox, 2, 0);
		sorterPanel.add(sortButton, 3, 0);
		return sorterPanel;

	}

	public BorderPane createTopPanel() {
		BorderPane actualTop = new BorderPane();
		GridPane top = new GridPane();
		top.setMinHeight(HEIGHT/4);
		top.setAlignment(Pos.TOP_CENTER);
		Label fName = new Label("First name:");

		Label lName = new Label("Last name:");

		Label phoneNumber = new Label("Phone number");

		updateButton = new Button("Update");

		updateButton.setOnAction(e -> {
			onUpdatePressed();
		});
		Button createButton = new Button("Create");
		createButton.setOnAction(e -> {
			createContact();
			setColor(colors.BLUE);
		});
		top.add(fName, 0, 0);
		top.add(firstNameTextField, 1, 0);
		top.add(lName, 0, 1);
		top.add(lastNameTextField, 1, 1);
		top.add(phoneNumber, 0, 2);
		top.add(phoneNumberTextField, 1, 2);
		top.add(createButton, 0, 3);
		top.add(updateButton, 1, 3);
		//actualTop.setCenter(top);
		//actualTop.setLeft(top);
		actualTop.setCenter(top);
		return actualTop;
	}

	private void onLoadFromFile() {
		processEvent(contactJavafx.this, Controller.ON_LOAD_FROM_FILE);
	}

	private void createContact() {

		processEvent(contactJavafx.this, Controller.getViewEventName(Controller.ON_NEW_CONTACT_EVENT));

	}

	public BorderPane createMidPanel() {

		BorderPane mid = new BorderPane();
		// mid.setLayout(new BorderLayout());

		Button next = new Button(">");
		next.setOnAction(e -> {
			onNextPressed();
			setColor(colors.GREEN);
		});
		Button last = new Button("Last");
		last.setOnAction(e -> {
			onLastPressed();
			setColor(colors.GREEN);
		});

		Button first = new Button("First");

		first.setOnAction(e -> {
			onFirstPressed();
			setColor(colors.RED);
		});

		Button prev = new Button("<");
		prev.setOnAction(e -> {
			onPrevPressed();
			setColor(colors.RED);	
		});

		Button edit = new Button("Edit contact");
		edit.setOnAction(e -> {
			onEditPressed();
		});

		GridPane centerPanel = new GridPane();

		Label phoneNumberTagLabel = new Label("Phone number");
		Label lNameLabel = new Label("Last name:");
		Label fNameLabel = new Label("First name:");

		centerPanel.add(fNameLabel, 0, 0);
		centerPanel.add(this.firstNameLabel, 1, 0);

		centerPanel.add(lNameLabel, 0, 1);
		centerPanel.add(this.lastNameLabel, 1, 1);
		centerPanel.add(phoneNumberTagLabel, 0, 2);
		centerPanel.add(this.phoneNumberLabel, 1, 2);
		centerPanel.add(edit, 0, 3);
		Font fontBold = Font.font("Times New Roman", 
			      FontWeight.BOLD, FontPosture.REGULAR,14);
		this.lastNameLabel.setFont(fontBold);
		this.phoneNumberLabel.setFont(fontBold);
		this.firstNameLabel.setFont(fontBold);
		centerPanel.setAlignment(Pos.CENTER);
		mid.setCenter(centerPanel);

		GridPane west = new GridPane();
		GridPane east = new GridPane();

		west.add(prev, 0, 0);
		west.add(first, 0, 1);
		east.add(next, 0, 0);
		east.add(last, 0, 1);
		mid.setLeft(west);
		mid.setRight(east);
		
		return mid;
	}

	private void onEditPressed() {

		processEvent(this, Controller.getViewEventName(Controller.ON_EDIT_START));


	}

	private void onUpdatePressed() {
		updateButton.setDisable(true);

		processEvent(this, Controller.getViewEventName(Controller.ON_UPDATE_EVENT));

		// setFileds(null);

	}

	private void onPrevPressed() {
		updateButton.setDisable(true);
		processEvent(this, Controller.getViewEventName(Controller.GET_PREV_EVENT));
	}

	private void onNextPressed() {
		updateButton.setDisable(true);
		processEvent(this, Controller.getViewEventName(Controller.GET_NEXT_EVENT));
		setColor(colors.GREEN);
	}

	public void init() {

		this.setVisible(true);
		processEvent(this, Controller.getViewEventName(Controller.VIEW_INIT_EVENT));
	}

	protected void onFirstPressed() {
		processEvent(this, Controller.getViewEventName(Controller.GET_FIRST_EVENT));

	}

	private void onLastPressed() {

		processEvent(this, Controller.getViewEventName((Controller.GET_LAST_EVENT)));
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
		return new String[] { firstName, lastName, phoneNumber };
	}

	@Override
	public void editContact(IContact contact) {
		updateButton.setDisable(false);
		System.out.println("editContact: " + contact);
		setFileds(contact);

	}

	@Override
	public String getChosenFormat() {

		String format = formatsComboBox.getValue();

		return format;
	}

	@Override
	public String getFileName() {
		String fileName = fileNameField.getText();
		return fileName;
	}

	@Override
	public String getSortOp() {
		String op = opsComboBox.getValue();
		return op;
	}

	@Override
	public String getSortField() {
		String field = sortTypesComboBox.getValue();
		return field;
	}

	@Override
	public String getOrderField() {
		String order = sortOrdersComboBox.getValue();

		return order;
	}

	@Override
	public String getTimerDirection() {
		// return
		// presentationSortOrdersComboBox.getItemAt(presentationSortOrdersComboBox.getSelectedIndex());
		
		return presentationSortOrdersComboBox.getValue();
	}
	public void setColor(colors nextColor) {
		this.firstNameLabel.setTextFill(nextColor.getColorCode());
		this.lastNameLabel.setTextFill(nextColor.getColorCode());
		this.phoneNumberLabel.setTextFill(nextColor.getColorCode());
	}
}
