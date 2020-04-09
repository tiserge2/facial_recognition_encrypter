package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PopInformation {
	public static void showInformation(String title, String message, String header) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		String s =message;
		alert.setContentText(s);
		alert.show();
	}
}
