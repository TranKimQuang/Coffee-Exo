package ExoCoffee.Custom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ExoCoffee.App; // Import lớp App để sử dụng phương thức setRoot

public class CustomTitleBarController {

  @FXML
  private Label titleLabel;

  @FXML
  private Button minimizeButton;

  @FXML
  private Button maximizeButton;

  @FXML
  private Button closeButton;

  private Stage stage;
  private double xOffset = 0;
  private double yOffset = 0;

  public void setStage(Stage stage) {
    this.stage = stage;
    setupEventHandlers();
  }

  private void setupEventHandlers() {
    // Nút thu nhỏ
    minimizeButton.setOnAction(e -> stage.setIconified(true));

    // Nút "Về trang login"
    maximizeButton.setOnAction(e -> {
      // Chuyển về trang đăng nhập
      App.setRoot("login");
    });

    // Nút đóng
    closeButton.setOnAction(e -> stage.close());

    // Cho phép di chuyển cửa sổ khi nhấn giữ và kéo thanh tiêu đề
    titleLabel.setOnMousePressed(this::handleMousePressed);
    titleLabel.setOnMouseDragged(this::handleMouseDragged);
  }

  private void handleMousePressed(MouseEvent event) {
    xOffset = event.getSceneX();
    yOffset = event.getSceneY();
  }

  private void handleMouseDragged(MouseEvent event) {
    stage.setX(event.getScreenX() - xOffset);
    stage.setY(event.getScreenY() - yOffset);
  }
}