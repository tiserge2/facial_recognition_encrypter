package database;
import java.sql.*;  
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private Connection connection;  // The database connection object.
    private Statement statement;    // the database statement object, used to execute SQL commands.

    public Database () {          // the constructor for the database manager
        String url = "jdbc:mysql://localhost:3306/lockdat";  // where username is your O'Reilly login username
        try {
            Class.forName ("com.mysql.jdbc.Driver");
        }
        catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

        try {                                                                       // Establish the database connection, create a statement for execution of SQL commands.
            connection = DriverManager.getConnection (url, "root", "" );     // username and password are passed into this Constructor
            statement  = connection.createStatement();
//            System.out.println("Connection established successfully");
//            statement.execute("create table PhoneBook (Name varchar (32), PhoneNumber varchar (18));"); // create a table in the database                                       
        }
        catch (SQLException exception ) {
            System.out.println ("\n*** SQLException caught ***\n");
            while (exception != null) 
            {                                                                         // tell us the problem
                System.out.println ("SQLState:    " + exception.getSQLState()  );
                System.out.println ("Message:     " + exception.getMessage()   );
                System.out.println ("Error code:  " + exception.getErrorCode() );
                exception = exception.getNextException ();

                System.out.println ( "" );
            }
        }
        catch ( java.lang.Exception exception ) {
            exception.printStackTrace();
        }
    }

    public boolean addEntry (String requete){                       // adds an entry to the Phone Book
        try
        {
            statement.execute (requete );
            return true;
//            System.out.println("Insertion successfull");
        }
        catch ( SQLException exception ) 
        {
            System.out.println ("\n*** SQLException caught ***\n");

            while ( exception != null) 
            {
                System.out.println ("SQLState:    " + exception.getSQLState()  );
                System.out.println ("Message:     " + exception.getMessage()   );
                System.out.println ("Error code:  " + exception.getErrorCode() );
                exception = exception.getNextException ();   
//                System.out.println ( "" );
            }
    		return false;
        }
        catch(java.lang.Exception exception ) 
        {
            exception.printStackTrace();
    		return false;
        }
    }
    
    public List<Map<String, Object>>  getEntry(String query) {
    	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	    Map<String, Object> row = null;
    	try {
//    		ResultSet rs = 
//    		statement = connection.createStatement();
    		ResultSet rs = statement.executeQuery(query);//			ResultSet rs=stmt.executeQuery("select * from actor");  
    	    ResultSetMetaData metaData = rs.getMetaData();
    	    Integer columnCount = metaData.getColumnCount();

    	    while (rs.next()) {
    	        row = new HashMap<String, Object>();
    	        for (int i = 1; i <= columnCount; i++) {
    	            row.put(metaData.getColumnName(i), rs.getObject(i));
//    	            System.out.println(columnCount);
    	        }
    	        resultList.add(row);
    	    }
    	} catch(Exception e) {
    		System.out.println(e);
    	} 
    	return resultList;
    }
}