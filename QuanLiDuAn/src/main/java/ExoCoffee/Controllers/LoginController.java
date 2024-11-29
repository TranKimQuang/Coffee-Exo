package ExoCoffee.Controllers;

import ExoCoffee.App;
import ExoCoffee.Utils.CommonUtils;
import ExoCoffee.Repositories.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ExoCoffee.Models.UserDTO;
import java.sql.SQLException;

public class LoginController {
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label errorLabel;

  private UserRepository userRepository = new UserRepository();

  @FXML
  public void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      UserDTO userDTO = userRepository.getUserByUsername(username);
      if (userDTO != null && CommonUtils.encodePas(password).equals(userDTO.getPassword())) {
        // Đăng nhập thành công, kiểm tra quyền truy cập
        if (userDTO.hasRole("Admin")) {
          // Chuyển hướng đến giao diện quản lý dành cho Admin
          App.setRoot("admin");
        } else {
          // Chuyển hướng đến giao diện người dùng thông thường
          App.setRoot("main");
        }
      } else {
        errorLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      errorLabel.setText("Lỗi kết nối cơ sở dữ liệu");
    }
  }
}
