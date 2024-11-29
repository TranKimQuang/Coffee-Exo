package ExoCoffee.Controllers;

import ExoCoffee.Models.App;
import ExoCoffee.Utils.CommonUtils;
import ExoCoffee.Data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ExoCoffee.Models.User;
import java.sql.SQLException;

public class LoginController {
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label errorLabel;

  private UserDAO userDAO = new UserDAO();

  @FXML
  public void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      User user = userDAO.getUserByUsername(username);
      if (user != null && CommonUtils.encodePas(password).equals(user.getPassword())) {
        // Đăng nhập thành công, kiểm tra quyền truy cập
        if (user.hasRole("Admin")) {
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
