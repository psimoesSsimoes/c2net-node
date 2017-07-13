package nodec2net;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	private Connection connection = null;

	public JDBC() {
		startConnection();
	}

	private void startConnection() {
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/c2net", "root", "paflechien");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

	}

	public ResultSet QueryForIntegerValue(String query) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error performing query for integer result on query : " + query);
			e.printStackTrace();
			return null;
		}

	}

	public ResultSet QueryForStringValue(String query) {
		try {
			Statement stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error performing query for string result on query : " + query);
			e.printStackTrace();
			return null;
		}
	}

	public void UpdateQuery(String query) {
		try {
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error performing update for integer result on query : " + query);
			e.printStackTrace();

		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
