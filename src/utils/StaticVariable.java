package utils;

import java.util.List;
import java.util.Map;

public class StaticVariable {
	public static String connectedUser 				= "";
	public static String connectedUser_fisrtname 	= "";
	public static String connectedUser_lastname 	= "";
	public static String connectedUser_passphrase	= "";
	public static String registration_state			= "";
	public static boolean recognized				= false;
	public static String message					= "";
	public static boolean registeredFace			= false;
	
	public static final String dbDir = System.getProperty("user.dir");
	public static final String url = "jdbc:sqlite:" + dbDir + "/lockdat.db";
}
