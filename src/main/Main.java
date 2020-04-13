package main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modeldb.User;

public class Main extends Application{
	public static void main(String argv[])  {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(argv);
		/**
		 * when first start the app we are going to check
		 *if there is any user yet registered in the database
		 *we launch the login window if not we launch the registed 
		 *window
		 */
	}
	
	public void openLoginWindow() throws IOException {
		System.out.println(System.getProperty("user.dir"));
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

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> users = User.loadAllUser();

		if(users.isEmpty()) {
			System.out.println("No User registered yet");
			openRegisterWindow();
		} else {
			System.out.println("We have user registered yet");
			//going to open the login form
			openLoginWindow();
		}		
	}

	private void openRegisterWindow() throws IOException {
		// TODO Auto-generated method stub
		URL location = getClass().getResource("/registrationForm.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
//		
		Stage newWindow = new Stage();

//		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/views/feedModelCamera.fxmll"));
//		Parent root = loader.load();
		
		newWindow.setTitle("Create new user");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		
		newWindow.initModality(Modality.APPLICATION_MODAL); 
//		FeedModelCameraController controller = loader.getController();
		newWindow.showAndWait();
	}
}
