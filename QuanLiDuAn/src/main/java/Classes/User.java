package Classes;

import Utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
  private int userId;
  private String username;
  private String password;
  private int roleId;

  public User(int userId, String username, String password, int roleId) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.roleId = roleId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public boolean hasRole(String roleName) throws SQLException {
    String query = "SELECT * FROM roles WHERE role_id = ?";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, roleId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next() && rs.getString("role_name").equals(roleName)) {
          return true;
        }
      }
    }
    return false;
  }
}
