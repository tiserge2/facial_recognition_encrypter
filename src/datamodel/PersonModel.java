package datamodel;

import javafx.beans.property.SimpleStringProperty;

public class PersonModel {
	public  SimpleStringProperty idPerson = new SimpleStringProperty();
	public  SimpleStringProperty lastname = new SimpleStringProperty();
	public  SimpleStringProperty firstname = new SimpleStringProperty();
	public  SimpleStringProperty hasFace = new SimpleStringProperty();
	
	//info about the user of that person
	public  SimpleStringProperty username = new SimpleStringProperty();
	public  SimpleStringProperty passphrase = new SimpleStringProperty();
	
	public String getIdPerson() {
		return idPerson.get();
	}
	public String getLastname() {
		return lastname.get();
	}
	public String getFirstname() {
		return firstname.get();
	}
	public String getHasFace() {
		return hasFace.get();
	}
	public String getUsername() {
		return username.get();
	}
	public String getPassphrase() {
		return passphrase.get();
	}
}
