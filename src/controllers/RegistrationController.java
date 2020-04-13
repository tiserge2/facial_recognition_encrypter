package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modeldb.Person;
import modeldb.User;
import utils.PopInformation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
//import com.gluonhq.charm.glisten.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class RegistrationController {
	/**
	 * Here we load all the controll from the 
	 * FX views
	 */
	@FXML
	private TextField lastname;
	
	@FXML
	private TextField firstname;
	
	@FXML
	private TextField passphrase;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField password_retyped;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField savebutton;
	
	@FXML
	private Label information;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button close;
	
	private String last_name;
	private String first_name;
	private String passphrase_;
	private String password_;
	private String password_retyped_;
	private String username_;
	
	@FXML
	public void initialize() {
		passphrase.setText(generatePassPhrase());
		passphrase.setEditable(false);
	}
	
	@FXML
	protected void closeRegistration(ActionEvent event) {
//		Stage stage = (Stage) close.getScene().getWindow();
//	    stage.close();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
	}
	
	protected void closeRegistration() {
//		Stage stage = (Stage) close.getScene().getWindow();
//	    stage.close();
		((Stage)((close).getScene().getWindow())).close();
	}
	
	/**
	 * lets implement the submit method
	 * @throws IOException 
	 */
	@FXML
	protected void submitInformation() throws IOException {
		last_name 			= lastname.getText();
		first_name 			= firstname.getText();
		passphrase_ 		= passphrase.getText();
		password_ 			= password.getText();
		password_retyped_	= password_retyped.getText();
		username_			= username.getText();
		
		boolean hasUser = User.hasUser();
		//lets verify first that the all the field arent empty
		if(verifyInformations()) {
			System.out.println("passwords: " + password_ + " et " + password_retyped_ );
			if(this.verifyPasswordSame(password_, password_retyped_)) {
				//if password are same ok
				information.setText("Correct.");
				if(this.verifyPassphrase()) {
					if(this.insertPerson() == true) {
						System.out.println("Person inserted successfully");
						if(this.insertUser() == true) {
							System.out.println("User inserted successfully");
							System.out.println("Closing the window");
							closeRegistrationWindow();
							if(hasUser) {
								PopInformation.showInformation("Success","You have successfully created the user " ,"Information Alert");
							} else {
								openLoginWindow();
							}
						}
					}
				} else {
					PopInformation.showInformation("Error", "Passphrase needs 16 characters", "Passphrase setup");
				}
				
			} else {
				information.setText("Password are Incorrect.");
			}
		} else {
			//we need to show a message telling that 
			//some field are empty
			information.setText("Sorry Some fields are required.");
		}
	}
	
	public void openLoginWindow() throws IOException {
		URL location = getClass().getResource("/loginForm.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
//		
		Stage newWindow = new Stage();

//		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/views/feedModelCamera.fxmll"));
//		Parent root = loader.load();
		
		newWindow.setTitle("Login");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		
//		newWindow.initModality(Modality.APPLICATION_MODAL); 
//		FeedModelCameraController controller = loader.getController();
		newWindow.show();
	}
	
	public boolean verifyInformations() {
		//verify is all the field has informations inside them
		return !lastname.getText().isEmpty() && !firstname.getText().isEmpty() &&
				!passphrase.getText().isEmpty() && !password.getText().isEmpty() &&
				!password_retyped.getText().isEmpty() && !username.getText().isEmpty();
	}
	
	public boolean verifyPassphrase() {
		return this.passphrase.getLength() == 16;
	}
	
	public boolean verifyPasswordSame(String pswd1, String pswd2) {
		//verify if the password entered correspond exactly 
		System.out.println("Veryfing password are same");
		System.out.println("Password 1: " + pswd1 + " et Password 2: " + pswd2);
//		if(password_.length() == password_retyped_.length()) {
//			
//		} else {
//			return false;
//		}
		return pswd1.equals(pswd2);
	}
	
	public boolean insertPerson() {
		System.out.println("Starting inserting person");
		return Person.addPerson(last_name, first_name);
	}
	
	public boolean insertUser() {
		System.out.println("Starting inserting user");
		return User.addUser(username_, password_, passphrase_);
	}
	public void closeRegistrationWindow() {
		((Stage)(close).getScene().getWindow()).close();
	}
	
	public static String generatePassPhrase() {
		Random rand = new Random();
		int val, intVal;
		String asc;
		String pass = "";
		
		List<Integer> values = new ArrayList<Integer>();
		//fill the table 
		for(int j = 33; j <= 126; j++) {
			values.add(j);
		}
		
		for(int i = 0; i < 16; i++) {
			val = (int)(Math.random() * values.size());
			intVal = (int)values.get(val);
			asc = String.valueOf((char)(intVal));
			pass += asc;
			
			System.out.println(i + 1);
			System.out.println("ascii: " + intVal);
			System.out.println("Char: " + asc);
			System.out.println("Pass: " + pass + "\n");
		}
		System.out.println("The final pass: " + pass);
		return pass;
	}
	
	public static void main(String argv[]) {
		String pass = generatePassPhrase();
	}
}
