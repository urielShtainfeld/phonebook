
//200956795 uriel shtainfeld

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import contacts.ContactsManager;
import contacts.ContactsManagerFrame;
import contacts.Controller;
import contacts.contactJavafx;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			ContactsManagerFrame cmf = new ContactsManagerFrame();
			ContactsManager cm;
			cm = new ContactsManager("contacts.dat");			
			Controller controller = new Controller(cm);		
			controller.addView(cmf);
			
			contactJavafx cjf = new contactJavafx();
			Scene scene = new Scene(cjf,contactJavafx.WIDTH, contactJavafx.HEIGHT);		
			primaryStage.setTitle(ContactsManagerFrame.CONTACT_MANGER_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setAlwaysOnTop(true);
			controller.addView(cjf);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}

}
