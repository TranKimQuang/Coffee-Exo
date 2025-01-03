package ExoCoffee.Controllers;

import ExoCoffee.Models.UserDTO;
import ExoCoffee.Service.UserService;
import ExoCoffee.Utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {
  @FXML
  private TableView<UserDTO> userTable;
  @FXML
  private TableColumn<UserDTO, Integer> userIdColumn;
  @FXML
  private TableColumn<UserDTO, String> usernameColumn;
  @FXML
  private TableColumn<UserDTO, String> passwordColumn;
  @FXML
  private TableColumn<UserDTO, String> roleColumn;
  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;
  @FXML
  private ComboBox<String> roleComboBox;

  private ObservableList<UserDTO> userList;
  private UserService userService = new UserService();

  @FXML
  public void initialize() {
    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

    userList = FXCollections.observableArrayList();
    userTable.setItems(userList);

    loadUserData();

    // Thêm sự kiện khi click vào một nhân viên trong bảng
    userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadUserDetails(newValue));
  }

  private void loadUserData() {
    try {
      List<UserDTO> users = userService.getAllUsers();
      userList.setAll(users);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu nhân viên: " + e.getMessage());
    }
  }

  private void loadUserDetails(UserDTO user) {
    if (user != null) {
      usernameField.setText(user.getUsername());
      passwordField.setText(user.getPassword());
      roleComboBox.setValue(user.getRole());
    } else {
      clearFields();
    }
  }

  @FXML
  private void handleAddUser() {
    String username = usernameField.getText();
    String password = CommonUtils.encodePas(passwordField.getText()); // Mã hóa mật khẩu
    String role = roleComboBox.getValue();

    UserDTO userDTO = new UserDTO(0, username, password, role); // userId sẽ được tự động tạo
    try {
      userService.register(userDTO);
      userList.add(userDTO);
      clearFields();
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm nhân viên: " + e.getMessage());
    }
  }

  @FXML
  private void handleDeleteUser() {
    UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser != null) {
      try {
        userService.deleteUser(selectedUser.getUserId());
        userList.remove(selectedUser);
        clearFields();
      } catch (SQLException e) {
        e.printStackTrace();
        showError("Lỗi khi xóa nhân viên: " + e.getMessage());
      }
    }
  }

  @FXML
  private void handleClearFields() {
    clearFields();
  }

  @FXML
  private void handleClose() {
    Stage stage = (Stage) userTable.getScene().getWindow();
    stage.close();
  }

  private void clearFields() {
    usernameField.clear();
    passwordField.clear();
    roleComboBox.setValue(null);
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
