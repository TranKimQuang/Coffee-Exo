package ExoCoffee.Controllers;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Repositories.OrderRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ViewOrdersController {
  @FXML
  private TableView<OrderDTO> orderTable;
  @FXML
  private TableColumn<OrderDTO, Integer> orderIdColumn;
  @FXML
  private TableColumn<OrderDTO, Double> totalAmountColumn;
  @FXML
  private TableColumn<OrderDTO, String> orderDateColumn;

  private ObservableList<OrderDTO> orderList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
    orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    orderTable.setItems(orderList);
    loadOrderData();
  }

  private void loadOrderData() {
    OrderRepository orderRepository = new OrderRepository();
    try {
      orderList.setAll(orderRepository.getAllOrders());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu đơn hàng.");
    }
  }

  @FXML
  public void handleDeleteOrder() {
    OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
    if (selectedOrder != null) {
      OrderRepository orderRepository = new OrderRepository();
      try {
        orderRepository.deleteOrder(selectedOrder.getOrderId());
        loadOrderData(); // Tải lại dữ liệu sau khi xóa
        showAlert("Thành công", "Đơn hàng đã được xóa.");
      } catch (SQLException e) {
        e.printStackTrace();
        showError("Lỗi khi xóa đơn hàng.");
      }
    } else {
      showError("Vui lòng chọn một đơn hàng để xóa.");
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
