package ExoCoffee.Controllers;

import ExoCoffee.Models.CartItem;
import ExoCoffee.Repositories.OrderRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class OrderProductsController {
  @FXML
  private TableView<CartItem> orderProductsTable;
  @FXML
  private TableColumn<CartItem, Integer> productIdColumn;
  @FXML
  private TableColumn<CartItem, String> productNameColumn;
  @FXML
  private TableColumn<CartItem, Integer> quantityColumn;
  @FXML
  private TableColumn<CartItem, Double> priceColumn;
  @FXML
  private TableColumn<CartItem, Double> totalProductColumn;

  private ObservableList<CartItem> orderProductsList;
  private OrderRepository orderRepository = new OrderRepository();
  private int orderId;

  @FXML
  public void initialize() {
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    totalProductColumn.setCellValueFactory(new PropertyValueFactory<>("totalProduct"));

    orderProductsList = FXCollections.observableArrayList();
    orderProductsTable.setItems(orderProductsList);
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
    loadOrderProducts();
  }

  private void loadOrderProducts() {
    try {
      List<CartItem> orderProducts = orderRepository.getOrderProducts(orderId);
      orderProductsList.setAll(orderProducts);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu sản phẩm của đơn hàng: " + e.getMessage());
    }
  }

  @FXML
  private void handleClose() {
    Stage stage = (Stage) orderProductsTable.getScene().getWindow();
    stage.close();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
