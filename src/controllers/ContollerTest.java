package controllers;

import java.net.URL;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ContollerTest extends Application {
	public static void main(String argv[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(argv);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		URL location = getClass().getResource("/mainWindow.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
		
		arg0.setTitle("LockDat App");
		arg0.setScene(new Scene(root));
		arg0.show();
		MainWindowController controller =
				(MainWindowController)fxmlLoader.getController();
	}
}
