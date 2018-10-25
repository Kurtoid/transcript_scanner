package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.User;

public class UserDBO {
	DatabaseConnectionManager db;

	PreparedStatement loginAccountStatement;

	public UserDBO() {
		try {
			db = DatabaseConnectionManager.getInstance();
			loginAccountStatement = db.getConnection()
					.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
			
//			loginAccountStatement = db.getConnection()
//					.prepareStatement("INSERT INTO Users VALUES (?, ?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated catch block
	}

	public User loginUser(String username, byte[] hash) throws SQLException {
		loginAccountStatement.setString(1, username);
		loginAccountStatement.setBytes(2, hash);
		ResultSet rs = loginAccountStatement.executeQuery();
		rs.next();
		return getPlayerAccountFromResultSet(rs);
		
	}
	private User getPlayerAccountFromResultSet(ResultSet rs) throws SQLException {
		User u = new User();
		u.id = rs.getInt("id");
		u.username = rs.getString("username");
		return u;
	}

}
