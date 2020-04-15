package modeldb;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import database.Database;
import modeldb.Person;
import utils.StaticVariable;

public class User {
	public static boolean addUser(String username, String password, String passphrase) {
		int lastIdPersonInserted = Person.lastInsertPerson();
		String query = "INSERT INTO user(username, password, idpersonne, passphrase) VALUES('"+ username +"', '"+ password +"', '"+ lastIdPersonInserted +"', '" + passphrase + "')";
		Database db = new Database();
		return db.addEntry(query);
	}
	
	public static List<Map<String, Object>> loadAllUser() {
		String query = "SELECT * FROM user;";
		Database db = new Database();
		return db.getEntry(query);
	}
	
	public static boolean userExist(String username) {
		String query = "SELECT * FROM user WHERE username='" + username + "'";
		Database db = new Database();
		System.out.println(db.getEntry(query).size());
		return (db.getEntry(query).size() == 1) ;
	}

	public static boolean verifyPasswordForUser(String username, String password) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM user WHERE username='" + username + "'";
		Database db = new Database();
		List<Map<String, Object>> users = db.getEntry(query);
		for(Map<String, Object> user: users) {
			if(user.get("password").equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getPassphrase(String username) {
		// TODO Auto-generated method stub
		String query = "SELECT passphrase from user,personne WHERE user.idpersonne = personne.idpersonne AND user.username='"+ username +"';";
		Database db = new Database();
		return (String) db.getEntry(query).get(0).get("passphrase");
	}
	
	public static void main(String argv[]) {
		System.out.println(getPassphrase("tiserge2"));
	}

	public static boolean removeUser(String connectedUser) {
		// TODO Auto-generated method stub
		Database db = new Database();
		String query1 = "DELETE FROM user WHERE username='" + connectedUser + "'";
		return db.addEntry(query1);
	}

	public static boolean hasUser() {
		// TODO Auto-generated method stub
		Database db = new Database();
		String query1 = "SELECT * FROM user";
		return db.getEntry(query1).size() > 0;
	}

	public static boolean modifyUser(String new_username_, String old_username_) {
		// TODO Auto-generated method stub
		Database db = new Database();
		String query1 = "UPDATE user SET username='" + new_username_ + "' WHERE username='" + old_username_ + "'";
		return db.addEntry(query1);
	}
}
