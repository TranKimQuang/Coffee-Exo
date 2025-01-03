package ExoCoffee.Service;

import ExoCoffee.Repositories.UserRepository;
import ExoCoffee.Models.UserDTO;
import java.sql.SQLException;
import java.util.List;

public class UserService {
  private UserRepository userRepository = new UserRepository();

  // Lấy tất cả người dùng
  public List<UserDTO> getAllUsers() throws SQLException {
    return userRepository.getAllUsers();
  }

  // Lấy thông tin người dùng bằng tên đăng nhập
  public UserDTO getUserByUsername(String username) throws SQLException {
    return userRepository.getUserByUsername(username);
  }

  // Đăng nhập
  public UserDTO login(String username, String password) throws SQLException {
    return userRepository.login(username, password);
  }

  // Đăng ký người dùng mới
  public boolean register(UserDTO user) throws SQLException {
    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    UserDTO existingUser = userRepository.getUserByUsername(user.getUsername());
    if (existingUser != null) {
      throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
    }

    // Thêm người dùng mới vào cơ sở dữ liệu
    return userRepository.addUser(user);
  }

  // Cập nhật thông tin người dùng
  public boolean updateUser(UserDTO user) throws SQLException {
    return userRepository.updateUser(user);
  }

  // Xóa người dùng
  public boolean deleteUser(int userId) throws SQLException {
    return userRepository.deleteUser(userId);
  }
}
