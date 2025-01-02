package ExoCoffee.Controllers;

import ExoCoffee.Models.Cart;
import ExoCoffee.Models.CartManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

  @FXML
  private void handlePlaceOrder() {
    System.out.println("Đang mở cửa sổ đặt hàng...");
    openNewWindow("order_placement.fxml", "Đặt hàng");
  }

  @FXML
  private void handleViewOrders() {
    System.out.println("Đang mở cửa sổ xem đơn hàng...");
    openNewWindow("view_orders.fxml", "Xem đơn hàng");
  }

  @FXML
  private void handleViewCart() {
    System.out.println("Đang mở cửa sổ giỏ hàng...");
    try {
      String fxmlPath = "/org/ExoCoffee/FXML/cart_view.fxml";
      System.out.println("Đường dẫn FXML: " + fxmlPath);

      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      System.out.println("Đã tải file FXML thành công.");

      CartViewController cartController = loader.getController();
      System.out.println("Đã lấy được controller của cart_view.fxml.");

      Cart cart = CartManager.getCart();
      if (cart == null) {
        System.err.println("Giỏ hàng không tồn tại (null).");
        showError("Giỏ hàng không tồn tại.");
        return;
      }
      System.out.println("Đã lấy được giỏ hàng từ CartManager.");

      Stage stage = new Stage();
      stage.setTitle("Xem giỏ hàng");
      stage.setScene(new Scene(root));
      stage.show();
      System.out.println("Cửa sổ giỏ hàng đã được hiển thị.");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Lỗi khi mở cửa sổ giỏ hàng: " + e.getMessage());
      showError("Không thể tải cửa sổ giỏ hàng: " + e.getMessage());
    }
  }


  private void openNewWindow(String fxmlFile, String title) {
    System.out.println("Đang mở cửa sổ mới: " + title);
    try {
      // Debug: In ra đường dẫn FXML
      String fxmlPath = "/org/ExoCoffee/FXML/" + fxmlFile;
      System.out.println("Đường dẫn FXML: " + fxmlPath);

      // Tải file FXML
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      System.out.println("Đã tải file FXML thành công.");

      // Tạo một Stage mới
      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));

      // Hiển thị Stage
      stage.show();
      System.out.println("Cửa sổ " + title + " đã được hiển thị.");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Lỗi khi mở cửa sổ " + title + ": " + e.getMessage());
      showError("Không thể tải cửa sổ: " + e.getMessage());
    }
  }

  private void showError(String message) {
    System.err.println("Hiển thị thông báo lỗi: " + message);
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}