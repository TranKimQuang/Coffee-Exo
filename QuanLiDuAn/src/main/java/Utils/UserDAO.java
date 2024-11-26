package Utils;

import Classes.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
  public User getUserByUsername(String username) throws SQLException {
    String query = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int id = rs.getInt("user_id");
          String password = rs.getString("password");
          int roleId = rs.getInt("role_id");
          return new User(id, username, password, roleId);
        }
      }
    }
    return null;
  }
}
