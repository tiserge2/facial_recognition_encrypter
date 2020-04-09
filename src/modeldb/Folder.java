package modeldb;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import database.Database;
public class Folder {
	public static boolean addFolder(String name, String fullPath, String username, String fullPathParent) {
		//insert data into the table folder
		String query = "INSERT INTO folder(name, fullpath, iscrypted, modified, username, fullpathparent) VALUES('"+ name +"', '"+ fullPath +"', 'No', '"+ LocalTime.now()+"', '" + username +"', '" + fullPathParent + "')";
		Database db = new Database();
		return db.addEntry(query);
	}
	
	public static List<Map<String, Object>> getAllFolder() {
		String query = "SELECT * FROM folder";
		Database db = new Database();
		return db.getEntry(query);
	}
	
	public static void main(String argv[]) {
		System.out.println(Folder.removeFolder("15"));
	}

	public static boolean removeFolder(String folderId) {
		// TODO Auto-generated method stub
		String query = "DELETE FROM lockdat.folder WHERE idfolder="+ Integer.valueOf(folderId)+"";
		Database db = new Database();
		return db.addEntry(query);
	}
	
	public static boolean setZippedAndPath(String folderId, String path, String cryptedName) {
		String query = "UPDATE lockdat.folder SET iscrypted='Yes', zippedname='" + path + "', cryptedName='"+ cryptedName +"' WHERE idfolder=" + Integer.parseInt(folderId) + "";
		Database db = new Database();
		return db.addEntry(query);
	}

	public static boolean setDecrypted(String folderId) {
		// TODO Auto-generated method stub
		String query = "UPDATE lockdat.folder SET iscrypted='No' WHERE idfolder='" + folderId + "'";
		Database db = new Database();
		return db.addEntry(query);
	}
}
