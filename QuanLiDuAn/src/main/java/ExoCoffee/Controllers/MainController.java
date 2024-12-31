package ExoCoffee.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

  @FXML
  private void handleViewProducts() {
    openNewWindow("view_products.fxml", "Xem sản phẩm");
  }

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
    openNewWindow("cart_view.fxml", "Xem giỏ hàng");
  }

  private void openNewWindow(String fxmlFile, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ExoCoffee/FXML/" + fxmlFile));
      Parent root = loader.load();

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}