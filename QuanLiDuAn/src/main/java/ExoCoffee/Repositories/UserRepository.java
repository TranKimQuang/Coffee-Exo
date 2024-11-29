package ExoCoffee.Repositories;

import ExoCoffee.Models.UserDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
  public UserDTO getUserByUsername(String username) throws SQLException {
    String query = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String dbUsername = rs.getString("username");
          String password = rs.getString("password");
          String role = rs.getString("role");
          return new UserDTO(dbUsername, password, role);
        }
      }
    }
    return null;
  }

}
