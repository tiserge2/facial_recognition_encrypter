package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import datamodel.*;
import encryption.Encrypter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modeldb.Folder;
import modeldb.Person;
import modeldb.User;
import utils.PopInformation;
import utils.StaticVariable;
//import com.gluonhq.charm.glisten.control.AppBar;

public class MainWindowController implements Initializable {	
	
	private ScheduledExecutorService timer;
	
	@FXML
	private Label connectedUser;
	
	@FXML
//	private AppBar appbar;
	/**
	 * This is the main element in the folder list view
	 */
	private  ObservableList<FolderModel> dataOnFolder = FXCollections.observableArrayList();
	
	// all the buttons *****
	@FXML
	private Button addFolderButton;
	
	@FXML
	private Button removeFolderButton;
	
	@FXML
	private Button decryptButton;
	
	@FXML
	private Button addUser;
	
	@FXML
	private Button addFaceButton;
	
	@FXML
	private Button removeUserButton;
	
	@FXML 
	private Button editUserButton;
	
	// all the buttons *****
	
	@FXML
	private TableView<FolderModel> listOfFolder;
	
	@FXML
	private TableColumn<FolderModel, String> id_folder;
	
	@FXML
	private TableColumn<FolderModel, String> name_folder;
	
	@FXML
	private TableColumn<FolderModel, String> fullpath_folder;
	
	@FXML
	private TableColumn<FolderModel, String> modified_folder;
	
	@FXML
	private TableColumn<FolderModel, String> iscrypted_folder;
	
	
	/**
	 * This is the main part of the person user list
	 */
	private  ObservableList<PersonModel> dataOnPerson = FXCollections.observableArrayList();
	
	@FXML
	private TableView<PersonModel> listOfPerson;
	
	@FXML
	private TableColumn<PersonModel, String> id_person;
	
	@FXML
	private TableColumn<PersonModel, String> lastname_person;
	
	@FXML
	private TableColumn<PersonModel, String> firstname_person;
	
	@FXML
	private TableColumn<PersonModel, String> hasFace_person;
	
	// triggered functions ******8
	@FXML
	protected void editUser() throws IOException {
		System.out.println("pressed editing");
		if(this.listOfPerson.getSelectionModel().isEmpty()) {
			PopInformation.showInformation("Error", "Please select the desired user", "Select User");
		} else {
			if(checkUserIdentity()) {
				StaticVariable.registration_state = "edit_user";
				OpenRegistrationWindow();
				connectedUser.setText("Connected user: " + StaticVariable.connectedUser);
				this.listOfPerson.getItems().clear();
				fillPersonList();
				this.listOfPerson.refresh();
			}else {
				PopInformation.showInformation("Error", "You can't edit another user", "Select User");
			}
		}
	}
	
	@FXML 
	protected void removeUser() throws IOException {
		//let's check is there is a selected user
		if(this.listOfPerson.getSelectionModel().isEmpty()) {
			PopInformation.showInformation("Error", "Please select the desired user", "Select User");
		} else {
			//let's check if the selected user is the same as the connected user
			if(checkUserIdentity()) {
				//let's check if the user has folder crypted
				if(Person.hasFolderCrypted(StaticVariable.connectedUser)) {
					PopInformation.showInformation("Error", "You can't remove your user, you have encrypted folders.", "User folder");
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Remove user");
					alert.setContentText("Do you really want to remove your user?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
					    // ... user chose OK
						//let's remove folder added by this user
						if(Folder.removeFolders(StaticVariable.connectedUser)) {
							System.out.println("All folders removed successfully");
							//update views
							this.listOfFolder.getItems().clear();
			            	this.fillFolderList();
			            	this.listOfFolder.refresh();
			            	
			            	//remove the faces
			            	if(Person.hasFaceRegistered(StaticVariable.connectedUser)) {
			            		try {
									if(massDeletion(StaticVariable.connectedUser)) {
										System.out.println("All photos deleted successfully");
										//lets remove datas about user in db
										if(Person.removePerson(Integer.valueOf(this.listOfPerson.getSelectionModel().getSelectedItem().getIdPerson()))) {
											System.out.println("Person removed successfully");
											if(User.removeUser(StaticVariable.connectedUser)) {
												System.out.println("User removed successfully");
												System.out.println("Closing main");
												closeMainWindow();
												if(User.hasUser()) {
													System.out.println("Openning login");
													openLoginWindow();
												} else {
													System.out.println("Openning registration");
													OpenRegistrationWindow();
												}
											} else {
												System.out.println("Cannot remove user");
											}
										} else {
											System.out.println("Cannot remove personne");
										}
									} else {
										System.out.println("Cannot remove the photos");
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			            	} else {
			            		//lets remove datas about user in db
			            		if(Person.removePerson(Integer.valueOf(this.listOfPerson.getSelectionModel().getSelectedItem().getIdPerson()))) {
			            			System.out.println("Person removed successfully");
			            			if(User.removeUser(StaticVariable.connectedUser)) {
			            				System.out.println("User removed successfully");
			            				System.out.println("Closing main");
										closeMainWindow();
										if(User.hasUser()) {
											System.out.println("Openning login");
											openLoginWindow();
										} else {
											System.out.println("Openning registration");
											OpenRegistrationWindow();
										}
									} else {
										System.out.println("Cannot remove user");
									}
								} else {
									System.out.println("Cannot remove personne");
								}
			            	}
						} else {
							System.out.println("Cannot remove folders");
							alert.close();
							PopInformation.showInformation("Error", "Problem, please contact admin", "User remove");
						}
					} else {
					    // ... user chose CANCEL or closed the dialog
						alert.close();
					}
				}
			} else {
				PopInformation.showInformation("Error", "You can't remove another user", "Select User");
			}
		}
	}
	
	public void OpenRegistrationWindow() throws IOException {
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

	@FXML
	protected void removeFolder() {
		if(this.listOfFolder.getSelectionModel().isEmpty()) {
			PopInformation.showInformation("Error", "Please select a folder to remove", "Select Folder");
		} else {
			if(this.checkFolderOwner()) {
				String folderId 	= this.listOfFolder.getSelectionModel().getSelectedItem().getIdFolder();
				String isCrypted 	= this.listOfFolder.getSelectionModel().getSelectedItem().getIsCrypted();
				if(isCrypted.equals("No")) {
					if(Folder.removeFolder(folderId)) {
						PopInformation.showInformation("Succes", "Your folder were removed successfully", "Remove Success");
						this.listOfFolder.getItems().clear();
		            	this.fillFolderList();
		            	this.listOfFolder.refresh();
					}
				} else {
					PopInformation.showInformation("Error", "You can't remove a crypted folder", "Crypted folder");
				}
			} else {
				PopInformation.showInformation("Error", "You don't have the right on that folder", "Select Folder");
			}
		}
	}
	
	private boolean checkFolderOwner() {
		// TODO Auto-generated method stub
		String selectedUser = this.listOfFolder.getSelectionModel().getSelectedItem().getUsername();
		System.out.println("Selected user: " + selectedUser);
		return selectedUser.equals(StaticVariable.connectedUser);
	}
	
	private boolean checkUserIdentity() {
		String connectedUser = StaticVariable.connectedUser;
		//selected user
		String selectedUser = this.listOfPerson.getSelectionModel().getSelectedItem().getUsername();
		return selectedUser.equals(connectedUser);
	}

	@FXML
	protected void addFace() throws IOException {
		System.out.println("Going to add face");
		if(this.listOfPerson.getSelectionModel().isEmpty()) {
			System.out.println("Please select a person to add the face to the system");
			PopInformation.showInformation("Error", "Please select a person to add the face to the system", "Select Person");
		} else {
			//checking if the connected user has clicked under the name related
			//to his name
			PersonModel person 		= this.listOfPerson.getSelectionModel().getSelectedItem();
			String connectedUser 	= StaticVariable.connectedUser;
			String selectedUser 	= person.getUsername();
			String hasFace 			= person.getHasFace();
			System.out.println("Here is the selected user: " + selectedUser);
			System.out.println("From now we are going to open register face windows");
			
			if(connectedUser.equals(selectedUser)) {
				System.out.println("username == selecteduser");
				if(hasFace.equals("No")) {
					this.showRegistrationModelWindow();
					if(StaticVariable.registeredFace) {
						PopInformation.showInformation("Success", StaticVariable.connectedUser + " face has been saved.", "Face registration");
						StaticVariable.registeredFace = false;
					}
					this.listOfPerson.getItems().clear();
					fillPersonList();
					this.listOfPerson.refresh();
				} else {
					System.out.println("Have yet registered the face");
					PopInformation.showInformation("Error", "Registration Face already done", "Face registration");
				}
			} else {
				System.out.println("username != selecteduser");
				PopInformation.showInformation("Error", "You can change your user only", "User Selection");
			}
			
		}
	}
	
	public MainWindowController() {
		System.out.println("We are in the constructor class");
		//setting the connected user to the system
		System.out.println("The connected user is: " + StaticVariable.connectedUser);
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connectedUser.setText("Connected User: " + StaticVariable.connectedUser);
			}
		});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle ressources) {
		
//		this.id_folder.setCellValueFactory(new PropertyValueFactory<FolderModel, String>("idFolder"));
		this.name_folder.setCellValueFactory(new PropertyValueFactory<FolderModel, String>("name"));
		this.fullpath_folder.setCellValueFactory(new PropertyValueFactory<FolderModel, String>("fullPath"));
		this.iscrypted_folder.setCellValueFactory(new PropertyValueFactory<FolderModel, String>("isCrypted"));
		this.modified_folder.setCellValueFactory(new PropertyValueFactory<FolderModel, String>("modified"));
		
	    fillFolderList();
	    this.listOfFolder.refresh();
	    
//	    this.id_person.setCellValueFactory(new PropertyValueFactory<PersonModel, String>("idPerson"));
	    this.lastname_person.setCellValueFactory(new PropertyValueFactory<PersonModel, String>("lastname"));
	    this.firstname_person.setCellValueFactory(new PropertyValueFactory<PersonModel, String>("firstname"));
	    this.hasFace_person.setCellValueFactory(new PropertyValueFactory<PersonModel, String>("hasFace"));
	    
	    fillPersonList();
	    this.listOfPerson.refresh();
	}
	
	@FXML 
	protected void encryptFolder() {
		
		if(this.listOfFolder.getSelectionModel().isEmpty()) {
			System.out.println("No selection");
			PopInformation.showInformation("Error", "No folder selected for encryption", "Encryption selection");
		} else {
			String folderId				= this.listOfFolder.getSelectionModel().getSelectedItem().getIdFolder();
			String folderName 			= this.listOfFolder.getSelectionModel().getSelectedItem().getName();
			String folderPath 			= this.listOfFolder.getSelectionModel().getSelectedItem().getFullPath();
			String folderParentPath 	= this.listOfFolder.getSelectionModel().getSelectedItem().getFullPathParent();
			System.out.println("Name  		     : " + folderName);
			System.out.println("Full path 	     : " + folderPath);
			System.out.println("Full parent path : " + folderParentPath);
			System.out.println(this.listOfFolder.getSelectionModel().getSelectedItem().getIdFolder());
			if(this.checkFolderOwner()) {
				//check if the folder is already crypted
				if(this.listOfFolder.getSelectionModel().getSelectedItem().getIsCrypted().equals("Yes")) {
					PopInformation.showInformation("Error", "This folder is already crypted", "Folder Encryption");
				} else {
					if(this.checkHasFace()) {
						Encrypter encr = new Encrypter();
						String fullPathToTarredFolder = folderName + ".tar.gz";
						String cryptedName 			  = folderName + ".sos";
						System.out.println("Tarred folder name: " + fullPathToTarredFolder);
						if(encr.encryptFile(folderName, fullPathToTarredFolder, StaticVariable.connectedUser_passphrase, folderParentPath)) {
							//we are going to add the zipped path to the database
							//and set zipped to yes
							if(Folder.setZippedAndPath(folderId, fullPathToTarredFolder, cryptedName)) {
								System.out.println("Modified successfully");
								PopInformation.showInformation("Success", "Folder locked successfully", "Folder Encryption");
								this.listOfFolder.getItems().clear();
								fillFolderList();
								this.listOfFolder.refresh();
							} else {
								System.out.println("Cannot modified the folder in db");
							}
						} else {
							System.out.println("Cannot encrypt the folder due of something");
						}
					} else {
						PopInformation.showInformation("Error", "You must add your face first", "Face Recognizer");
					}
				}
			} else {
				PopInformation.showInformation("Error", "This folder doesn't belong to yo", "Folder Owner");
			}
		}
	}
	
	@FXML
	public void decryptFolder() throws IOException {
		System.out.println("Going to decrypt folder");
		
		if(this.listOfFolder.getSelectionModel().isEmpty()) {
			System.out.println("No selection");
			PopInformation.showInformation("Error", "No folder selected for encryption", "Encryption selection");
		} else {
			String folderId				= this.listOfFolder.getSelectionModel().getSelectedItem().getIdFolder();
			String folderName 			= this.listOfFolder.getSelectionModel().getSelectedItem().getName();
			String folderPath 			= this.listOfFolder.getSelectionModel().getSelectedItem().getFullPath();
			String folderParentPath 	= this.listOfFolder.getSelectionModel().getSelectedItem().getFullPathParent();
			String cryptedName		 	= this.listOfFolder.getSelectionModel().getSelectedItem().getCryptedName();
			String tarredName 			= this.listOfFolder.getSelectionModel().getSelectedItem().getZippedname();
			System.out.println("Name  		     : " + folderName);
			System.out.println("Full path 	     : " + folderPath);
			System.out.println("Full parent path : " + folderParentPath);
			System.out.println("Full crypted name : " + cryptedName);
			System.out.println("Full tarred name : " + tarredName);
			System.out.println(this.listOfFolder.getSelectionModel().getSelectedItem().getIdFolder());
			if(this.checkFolderOwner()) {
				//check if the folder is already crypted
				if(this.listOfFolder.getSelectionModel().getSelectedItem().getIsCrypted().equals("No")) {
					PopInformation.showInformation("Error", "This folder hasn't been crypted yet", "Folder Encryption");
				} else {
					if(this.checkHasFace()) {
						Encrypter encr = new Encrypter();
						String fullPathToTarredFolder = folderName + ".tar.gz";
						System.out.println("Tarred folder name: " + folderName);
						//open the face recognizer to decrypt it
						openFaceRecognizer();
						System.out.println("Going to check if the face was recognized.");
						if(StaticVariable.recognized) {
							StaticVariable.recognized = false;
							System.out.println("Going to decrypt the folder");
							if(encr.decryptFile(cryptedName, tarredName, StaticVariable.connectedUser_passphrase, folderParentPath)) {
								Folder.setDecrypted(folderId);
								this.listOfFolder.getItems().clear();
								fillFolderList();
								this.listOfFolder.refresh();
								PopInformation.showInformation("Success", "Your folder has been decrypted", "Folder encryption");
							} else {
								PopInformation.showInformation("Error", "Cannot decrypt folder", "Folder encryption");
							}
						} else {
							PopInformation.showInformation("Error", "Unexpected face", "Face recognition");
						}
//						openFaceRecognizer();
					} else {
						PopInformation.showInformation("Error", "You must add your face first", "Face Recognizer");
					}
				}
			} else {
				PopInformation.showInformation("Error", "This folder doesn't belong to you", "Folder Owner");
			}
		}
	}
	
	private void openFaceRecognizer() throws IOException {
		// TODO Auto-generated method stub
		URL location = getClass().getResource("/recognitionCamera.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
		Stage newWindow = new Stage();
		newWindow.setTitle("Face Unlocker");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		newWindow.initModality(Modality.APPLICATION_MODAL); 
		newWindow.showAndWait();
	}

	private boolean checkHasFace() {
		// TODO Auto-generated method stub
		return Person.hasFaceRegistered(StaticVariable.connectedUser);
	}

	@FXML
	protected void addFolder() {
		//here we are going to open the folder
		//chooser
		DirectoryChooser dirChoos = new DirectoryChooser();
		File dir = dirChoos.showDialog(null);
		if (dir != null) {
			System.out.println(dir.getParentFile().getAbsolutePath());
            if(Folder.addFolder(dir.getName(), dir.getAbsolutePath(), StaticVariable.connectedUser, dir.getParentFile().getAbsolutePath()) == true) {
                System.out.println("The " + dir.getName() + " has been added.");
                this.listOfFolder.getItems().clear();
                fillFolderList();
        	    this.listOfFolder.refresh();
            } else {
            	System.out.println("Cannot add the folder.");
//            	this.listOfFolder.getItems().clear();
//            	this.fillFolderList();
//            	this.listOfFolder.refresh();
            }
        } else {
        	System.out.println("Can't open");
        }
	}
	
	@FXML
	protected void addUser() throws IOException {
		System.out.println("From now we are going to open register windows");
		URL location = getClass().getResource("/registrationForm.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
		
		Stage newWindow = new Stage();
		newWindow.setTitle("Register User");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		
		newWindow.initModality(Modality.APPLICATION_MODAL); 
		newWindow.showAndWait();
    	this.listOfPerson.getItems().clear();
		fillPersonList();
	    this.listOfPerson.refresh();
	}
	
//	@SuppressWarnings("unchecked")
	public void fillFolderList() {
		List<Map<String, Object>> folders = Folder.getAllFolder();
		for(Map<String, Object> folder: folders) {
			FolderModel folderObject = new FolderModel();
			String idFolder 		= String.valueOf(folder.get("idfolder"));
			String fullPath 		= String.valueOf(folder.get("fullpath"));
			String name 			= String.valueOf(folder.get("name"));
			String isCrypted 		= String.valueOf(folder.get("iscrypted"));
			String username 		= String.valueOf(folder.get("username"));
			String zippedName 		= String.valueOf(folder.get("zippedname"));
			String fullPathParent 	= String.valueOf(folder.get("fullpathparent"));
			String cryptedName 	= String.valueOf(folder.get("cryptedname"));
//			System.out.println("is crypted: " + String.valueOf(folder.get("iscrypted")));
			
			String modified 	= String.valueOf(folder.get("modified"));
			
			folderObject.idFolder.set(idFolder);
			folderObject.fullPath.set(fullPath);
			folderObject.name.set(name);
			folderObject.isCrypted.set(isCrypted);
			folderObject.modified.set(modified);
			folderObject.username.set(username);
			folderObject.zippedname.set(zippedName);
			folderObject.fullPathParent.set(fullPathParent);
			folderObject.cryptedName.set(cryptedName);
			
//			System.out.println("This the iteration on the folder");
//			System.out.println(idFolder + " " + name + " " + fullPath + " " + isCrypted + " " + modified);
			dataOnFolder.add(folderObject);
		}
		this.listOfFolder.setItems(dataOnFolder);
	}
	
	public void showRegistrationModelWindow() throws IOException {
		URL location = getClass().getResource("/feedModelCamera.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane root = (Pane)fxmlLoader.load();
//		
		Stage newWindow = new Stage();

//		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/views/feedModelCamera.fxmll"));
//		Parent root = loader.load();
		
		newWindow.setTitle("Add Face to System");
		newWindow.setScene(new Scene(root));
		newWindow.initOwner(null);
		
		newWindow.initModality(Modality.APPLICATION_MODAL); 
//		FeedModelCameraController controller = loader.getController();
		newWindow.showAndWait();
	}
	
	public void fillPersonList() {
//		System.out.println("Filling person list");
		List<Map<String, Object>> persons = Person.getAllPerson();
		for(Map<String, Object> person: persons) {
			PersonModel personObject = new PersonModel();
			String idPerson 	= String.valueOf(person.get("idpersonne"));
			String lastname 	= String.valueOf(person.get("lastname"));
			String firstname 	= String.valueOf(person.get("firstname"));
			String hasFace		= String.valueOf(person.get("isfaceregistered"));	
			String username 	= String.valueOf(person.get("username"));
			String passphrase 	= String.valueOf(person.get("passphrase"));
			
			personObject.idPerson.set(idPerson);
			personObject.lastname.set(lastname);
			personObject.firstname.set(firstname);
			personObject.hasFace.set(hasFace);
			personObject.username.set(username);
			personObject.passphrase.set(passphrase);
			
//			System.out.println("This the iteration in all the person");
//			System.out.println(idPerson + " " + lastname + " " + firstname + " " + hasFace );
			dataOnPerson.add(personObject);
		}
		this.listOfPerson.setItems(dataOnPerson);
	}
	
	public static  boolean massDeletion(String file) throws IOException, InterruptedException {
		System.out.println("Deleting");
		String path = System.getProperty("user.dir") + "/imagedb";
		System.setProperty("user.dir", path);
		String[] command = {"/bin/sh", "-c", "rm -v *" + file + "*"};
		File dir = new File(path);
		Runtime  r = Runtime.getRuntime();
		Process p = r.exec(command, null, dir);
		final int returnCode = p.waitFor();
		System.out.println("Deleting from folder: " + path);
		System.out.println("Deleting command: " + command);
		System.out.println("Value of return: " + returnCode);
		
		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		
		while((line = is.readLine()) != null) {
			System.out.println( line);
		}
		return returnCode == 0;
	}
	
	public void closeMainWindow() {
		((Stage)(removeUserButton).getScene().getWindow()).close();
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
	
	public static void main(String argv[]) throws IOException, InterruptedException {
		massDeletion("rara");
	}
}
