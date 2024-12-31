package ExoCoffee.Controllers;

import ExoCoffee.Models.CartManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

  @FXML
  private void handlePlaceOrder() {
    openNewWindow("order_placement.fxml", "Đặt hàng");
  }

  @FXML
  private void handleViewOrders() {
    openNewWindow("view_orders.fxml", "Xem đơn hàng");
  }

  @FXML
  private void handleViewCart() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ExoCoffee/FXML/cart_view.fxml"));
      Parent root = loader.load();

      // Lấy controller của cart_view.fxml
      CartViewController cartController = loader.getController();
      cartController.setCart(CartManager.getCart()); // Truyền giỏ hàng vào controller

      Stage stage = new Stage();
      stage.setTitle("Xem giỏ hàng");
      stage.setScene(new Scene(root));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      showError("Không thể tải cửa sổ giỏ hàng: " + e.getMessage());
    }
  }

  private void openNewWindow(String fxmlFile, String title) {
    try {
      // Tải file FXML
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ExoCoffee/FXML/" + fxmlFile));
      Parent root = loader.load();

      // Tạo một Stage mới
      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));

      // Hiển thị Stage
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      // Hiển thị thông báo lỗi cho người dùng
      showError("Không thể tải cửa sổ: " + e.getMessage());
    }
  }

  private void showError(String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}