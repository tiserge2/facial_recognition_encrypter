package datamodel;

import javafx.beans.property.SimpleStringProperty;

public class FolderModel {

	public  SimpleStringProperty idFolder = new SimpleStringProperty();
	public  SimpleStringProperty name = new SimpleStringProperty();
	public  SimpleStringProperty fullPath = new SimpleStringProperty();
	public  SimpleStringProperty isCrypted = new SimpleStringProperty();
	public  SimpleStringProperty modified = new SimpleStringProperty();
	public  SimpleStringProperty username = new SimpleStringProperty();
	public  SimpleStringProperty zippedname = new SimpleStringProperty();
	public  SimpleStringProperty fullPathParent = new SimpleStringProperty();
	public  SimpleStringProperty cryptedName = new SimpleStringProperty();
    
	public String getCryptedName() {
		return cryptedName.get();
	}
	public String getZippedname() {
		return zippedname.get();
	}
	public String getFullPathParent() {
		return fullPathParent.get();
	}
	public String getUsername() {
		return username.get();
	}
	public String getIdFolder() {
		return idFolder.get();
	}
	public String getName() {
		return name.get();
	}
	public String getFullPath() {
		return fullPath.get();
	}
	public String getIsCrypted() {
		return isCrypted.get();
	}
	public String getModified() {
		return modified.get();
	}
    
    
 
//    public FolderModel(String idF, String nameF, String fullPaF, String isCryp, String modifiedF) {
//        this.idFolder = new SimpleStringProperty(idF);
//        this.name = new SimpleStringProperty(nameF);
//        this.fullPath = new SimpleStringProperty(fullPaF);
//        this.isCrypted = new SimpleStringProperty(isCryp);
//        this.modified = new SimpleStringProperty(modifiedF);
//    }

//	public FolderModel() {
//		// TODO Auto-generated constructor stub
//	}
}
