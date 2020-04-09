package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modeldb.Person;
import modeldb.User;
import utils.PopInformation;
import utils.StaticVariable;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

//import com.gluonhq.charm.glisten.control.TextField;


public class LoginController {
	//load the controls
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Button access;
	
	@FXML 
	private Label information;
	
	private String username_;
	private String password_;
	
	@FXML
	protected  void getAccess() throws IOException {
		username_ 		= username.getText();
		password_		= password.getText();
		information.setText("");
		if(verifyInformations()) {
			if(verifyCredentials()) {
				StaticVariable.connectedUser 				= username_;
				StaticVariable.connectedUser_passphrase		= User.getPassphrase(username_);
				closeLoginWindow();
				openMainWindow();
			} else {
				PopInformation.showInformation("Error", "Please try again, with correct information", "Login faillure");
			}
		} else {
			information.setText("Can't leave any field empty.");
		}
		System.out.println("Bonjour");
	}
	
	public boolean verifyInformations() {
		//verify is all the field has informations inside them
		return !username.getText().isEmpty() && !password.getText().isEmpty();
	}
	
	public boolean verifyCredentials() {
		if(User.userExist(username_)) {
			if(User.verifyPasswordForUser(username_, password_)) {
				return true;
			} else {
				information.setText("Incorrect infos");
				return false;
			}
		} else {
			information.setText("No user found");
			return false;
		}
	}
	
	
	public void closeLoginWindow() {
		((Stage)(access).getScene().getWindow()).close();
	}
	
	public void openMainWindow() throws IOException {
		URL location = getClass().getResource("/mainWindow.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
//		
		Stage newWindow = new Stage();

//		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/views/feedModelCamera.fxmll"));
//		Parent root = loader.load();
		
		newWindow.setTitle("Acceuil");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		
		newWindow.initModality(Modality.APPLICATION_MODAL); 
//		FeedModelCameraController controller = loader.getController();
		newWindow.showAndWait();
	}
}
