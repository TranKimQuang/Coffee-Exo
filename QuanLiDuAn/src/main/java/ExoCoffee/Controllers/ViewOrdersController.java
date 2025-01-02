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
import java.util.List;

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
  private OrderRepository orderRepository = new OrderRepository();

  @FXML
  public void initialize() {
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
    orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    orderTable.setItems(orderList);
    loadOrderData();
  }

  private void loadOrderData() {
    try {
      List<OrderDTO> unpaidOrders = orderRepository.getUnpaidOrders();
      orderList.setAll(unpaidOrders);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu đơn hàng.");
    }
  }

  @FXML
  public void handleCheckout() {
    OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
    if (selectedOrder != null) {
      int selectedOrderId = selectedOrder.getOrderId();
      System.out.println("ID Đơn hàng được chọn: " + selectedOrderId);

      try {
        orderRepository.markOrderAsPaid(selectedOrderId);
        System.out.println("Đã thanh toán đơn hàng: " + selectedOrderId);

        // Cập nhật lại bảng để chỉ hiển thị các đơn hàng chưa thanh toán
        loadOrderData();

      } catch (SQLException e) {
        e.printStackTrace();
        showError("Lỗi khi thanh toán đơn hàng: " + e.getMessage());
      }
    } else {
      showError("Vui lòng chọn một đơn hàng để thanh toán.");
    }
  }

  @FXML
  public void handleDeleteOrder() {
    OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
    if (selectedOrder != null) {
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
