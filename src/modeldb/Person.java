package modeldb;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import database.Database;

public class Person {
	public static boolean addPerson(String lastname, String firstName) {
		String query = "INSERT INTO personne(lastname, firstname, isfaceregistered) VALUES('"+ lastname +"', '"+ firstName +"','No')";
		Database db = new Database();
		return db.addEntry(query);
	}
	
	public static int lastInsertPerson() {
		String query = "select idpersonne from personne order by idpersonne desc limit 1";
		Database db = new Database();
		List<Map<String, Object>> person = db.getEntry(query);
		return (Integer)person.get(0).get("idpersonne");
	}

	public static List<Map<String, Object>> getAllPerson() {
		String query = "SELECT * FROM personne, user WHERE personne.idpersonne = user.idpersonne;";
		Database db = new Database();
		return db.getEntry(query);
	}
	
	public static boolean setFaceRegistered(String username) {
		Database db = new Database();
		String query1 = "SELECT user.idpersonne from user,personne WHERE user.idpersonne = personne.idpersonne AND user.username='"+  username +"'";
		String idpersonne = (String) db.getEntry(query1).get(0).get("idpersonne");
		String query = "UPDATE personne SET isfaceregistered='Yes' WHERE personne.idpersonne='"+ idpersonne +"'";
		return db.addEntry(query);
	}
	
	public static boolean hasFolderCrypted(String username) {
		Database db = new Database();
		String query1 = "SELECT * from folder WHERE folder.username='" + username + "' AND folder.iscrypted='Yes'";
		List<Map<String, Object>> result = db.getEntry(query1);
		return result.size() > 0;
	}

	public static boolean hasFaceRegistered(String connectedUser) {
		// TODO Auto-generated method stub
		Database db = new Database();
		String query1 = "SELECT user.idpersonne from user,personne WHERE user.idpersonne = personne.idpersonne AND user.username='"+  connectedUser +"'";
		int idpersonne = Integer.parseInt((String)db.getEntry(query1).get(0).get("idpersonne"));
		System.out.println(idpersonne);
		String query = "SELECT isfaceregistered FROM personne WHERE idpersonne="+ idpersonne +"";
		List<Map<String, Object>> result = db.getEntry(query);
		System.out.println(result.get(0).get("isfaceregistered"));
		if(result.get(0).get("isfaceregistered").equals("No")) {
			return false;
		} else {
			return true;	
		}
	}
	
	
	public static void main(String argv[]) {
		System.out.println(Person.hasFaceRegistered("gorse"));
	}

	public static boolean removePerson(int idPersonne) {
		// TODO Auto-generated method stub
		Database db = new Database();
		String query1 = "DELETE FROM personne WHERE idpersonne=" + idPersonne + "";
		return db.addEntry(query1);
	}
}
