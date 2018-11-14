package server.database;

import common.UserLoginToken;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTokenDBO {
    DatabaseConnectionManager db;

    PreparedStatement getUserToken;

    public LoginTokenDBO() {
        try {
            db = DatabaseConnectionManager.getInstance();
            getUserToken = db.getConnection()
                    .prepareStatement("SELECT * FROM UserLoginTokens WHERE userId = ?");

//			loginAccountStatement = db.getConnection()
//					.prepareStatement("INSERT INTO Users VALUES (?, ?)");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated catch block
    }

    public UserLoginToken getToken(int userId) throws SQLException {
        getUserToken.setInt(1, userId);
        ResultSet rs = getUserToken.executeQuery();
        rs.next();
        return getLoginTokenFromResultSet(rs);

    }

    private UserLoginToken getLoginTokenFromResultSet(ResultSet rs) throws SQLException {
        UserLoginToken tok = new UserLoginToken();
        tok.id = rs.getInt("id");
        tok.loginToken = rs.getString("loginToken");
        tok.userId = rs.getInt("userId");
        return tok;
    }

}
