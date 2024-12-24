package ExoCoffee.Controllers;

import ExoCoffee.App;
import javafx.fxml.FXML;
import java.io.IOException;

public class MainController {
  @FXML
  private void handleViewProducts() {
    // Chuyển hướng đến giao diện xem sản phẩm
    App.setRoot("view_products");
  }

  @FXML
  private void handlePlaceOrder() {
    // Chuyển hướng đến giao diện đặt hàng
    App.setRoot("order_placement");
  }

  @FXML
  private void handleViewOrders() {
    // Chuyển hướng đến giao diện xem đơn hàng
    App.setRoot("view_order"); // Đảm bảo tên tệp FXML đúng
  }

  private void showError(String message) {
    // Hiển thị thông báo lỗi
    System.err.println(message);
  }
}
