package cui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDatabaseConnection {
	// Connect to your database.
	// Replace server name, username, and password with your credentials
	public static void main(String[] args) {
		final String IP_SERVER = "40.118.71.191";
		final String DB_NAME = "IT_LAB";
		final String DB_USER = "simon.dewilde";
		final String DB_PWD = "2903263C2b.e";

		String connectionUrl = "jdbc:sqlserver://" + IP_SERVER + ":1433;" + "database=" + DB_NAME + ";" + "user=" + DB_USER + ";"
			+ "password=" + DB_PWD + ";" + "encrypt=false;" + "trustServerCertificate=false;" + "loginTimeout=30;";

		try (Connection connection = DriverManager.getConnection(connectionUrl);) {
			Statement statement = connection.createStatement();

			String selectSql = "select g.gebruikersnaam, g.naam from dbo.Gebruiker as g";

			ResultSet resultSet = statement.executeQuery(selectSql);

			while (resultSet.next()) {
				System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
			}
		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}