package ExoCoffee.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MainController {

  @FXML
  private void handleViewProducts(ActionEvent event) {
    openNewWindow("view_products.fxml", "View Products");
  }

  @FXML
  private void handlePlaceOrder(ActionEvent event) {
    openNewWindow("order_placement.fxml", "Place Order");
  }

  @FXML
  private void handleViewOrders(ActionEvent event) {
    openNewWindow("view_orders.fxml", "View Orders");
  }

  private void openNewWindow(String fxmlFile, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/ExoCoffee/FXML/" + fxmlFile));
      Parent root = loader.load();

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      showError("Không thể tải giao diện: " + fxmlFile);
    }
  }

  private void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setContentText(message);
    alert.showAndWait();
    System.err.println(message);
  }
}
