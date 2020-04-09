package controllers;

import java.io.IOException;
import modeldb.Person;
import modeldb.User;
import utils.PopInformation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
//import com.gluonhq.charm.glisten.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	 */
	@FXML
	protected void submitInformation() {
		last_name 			= lastname.getText();
		first_name 			= firstname.getText();
		passphrase_ 		= passphrase.getText();
		password_ 			= password.getText();
		password_retyped_	= password_retyped.getText();
		username_			= username.getText();
		
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
							PopInformation.showInformation("Success","You have successfully created the user " ,"Information Alert");
							closeRegistrationWindow();
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
	
	public static void main(String argv[]) {
		RegistrationController reg = new RegistrationController();
		System.out.println(reg.verifyPasswordSame("Je", "Je"));
	}
}
