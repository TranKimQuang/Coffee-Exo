package ExoCoffee.Repositories;

import ExoCoffee.Models.UserDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  // Lấy tất cả người dùng
  public List<UserDTO> getAllUsers() throws SQLException {
    String query = "SELECT * FROM users";
    List<UserDTO> users = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int userId = resultSet.getInt("userId");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        users.add(new UserDTO(userId, username, password, role));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu người dùng.", e);
    }

    return users;
  }

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
          return new UserDTO(userId, username, password, role);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi lấy thông tin người dùng.", e);
    }
    return null;
  }

  // Đăng nhập người dùng
  public UserDTO login(String username, String password) throws SQLException {
    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, username);
      statement.setString(2, password);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int userId = resultSet.getInt("userId");
          String role = resultSet.getString("role");
          return new UserDTO(userId, username, password, role);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi đăng nhập người dùng.", e);
    }
    return null;
  }

  // Thêm người dùng mới
  public boolean addUser(UserDTO user) throws SQLException {
    String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole());
      int rowsInserted = statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          user.setUserId(generatedKeys.getInt(1));
        }
      }

      return rowsInserted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi thêm người dùng.", e);
    }
  }

  // Cập nhật thông tin người dùng
  public boolean updateUser(UserDTO user) throws SQLException {
    String query = "UPDATE users SET username = ?, password = ?, role = ? WHERE userId = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole());
      statement.setInt(4, user.getUserId());
      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi cập nhật người dùng.", e);
    }
  }

  // Xóa người dùng
  public boolean deleteUser(int userId) throws SQLException {
    String query = "DELETE FROM users WHERE userId = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, userId);
      int rowsDeleted = statement.executeUpdate();
      return rowsDeleted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi xóa người dùng.", e);
    }
  }
}
