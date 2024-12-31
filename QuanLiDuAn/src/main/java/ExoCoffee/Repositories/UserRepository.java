package ExoCoffee.Repositories;

import ExoCoffee.Models.UserDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;

public class UserRepository {

  // Lấy thông tin người dùng bằng tên đăng nhập
  public UserDTO getUserByUsername(String username) throws SQLException {
    String query = "SELECT * FROM users WHERE username = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setString(1, username);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int userId = resultSet.getInt("userId");
          String password = resultSet.getString("password");
          String role = resultSet.getString("role");

          // Tạo đối tượng UserDTO và trả về
          return new UserDTO(userId, username, password, role);
        }
      }
    }
    return null; // Nếu không tìm thấy người dùng
  }

  // Đăng nhập
  public UserDTO login(String username, String password) throws SQLException {
    String query = "SELECT * FROM user WHERE username = ? AND password = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setString(1, username);
      statement.setString(2, password);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int userId = resultSet.getInt("userId");
          String role = resultSet.getString("role");

          // Tạo đối tượng UserDTO và trả về
          return new UserDTO(userId, username, password, role);
        }
      }
    }
    return null; // Nếu không tìm thấy người dùng
  }

  // Thêm người dùng mới
  public boolean addUser(UserDTO user) throws SQLException {
    String query = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole());

      return statement.executeUpdate() > 0; // Trả về true nếu thêm thành công
    }
  }

  // Cập nhật thông tin người dùng
  public boolean updateUser(UserDTO user) throws SQLException {
    String query = "UPDATE user SET username = ?, password = ?, role = ? WHERE userId = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole());
      statement.setInt(4, user.getUserId());

      return statement.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
    }
  }

  // Xóa người dùng
  public boolean deleteUser(int userId) throws SQLException {
    String query = "DELETE FROM user WHERE userId = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, userId);

      return statement.executeUpdate() > 0; // Trả về true nếu xóa thành công
    }
  }
}