package Main;

import Classes.App;
import javafx.fxml.FXML;

public class MainController {
  @FXML
  private void handleViewProducts() {
    // Chuyển hướng đến giao diện quản lý sản phẩm cho người dùng thông thường
    App.setRoot("product_management");
  }

  @FXML
  private void handlePlaceOrder() {
    // Chuyển hướng đến giao diện đặt hàng
    App.setRoot("order_management");
  }
}
