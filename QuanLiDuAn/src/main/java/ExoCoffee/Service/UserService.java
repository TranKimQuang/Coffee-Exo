package ExoCoffee.Service;

import ExoCoffee.Repositories.UserRepository;
import ExoCoffee.Models.UserDTO;
import java.sql.SQLException;

public class UserService {
  private UserRepository userRepository = new UserRepository();

  public UserDTO getUserByUsername(String username) throws SQLException {
    return userRepository.getUserByUsername(username);
  }

  // Thêm các phương thức khác
}

