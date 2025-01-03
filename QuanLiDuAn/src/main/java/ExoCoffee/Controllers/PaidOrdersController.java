package ExoCoffee.Controllers;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.StatisticsDTO;
import ExoCoffee.Repositories.OrderRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaidOrdersController {
  @FXML
  private TableView<OrderDTO> ordersTable;
  @FXML
  private TableColumn<OrderDTO, Integer> orderIdColumn;
  @FXML
  private TableColumn<OrderDTO, String> orderDateColumn;
  @FXML
  private TableColumn<OrderDTO, Double> totalAmountColumn;
  @FXML
  private DatePicker datePicker;

  private ObservableList<OrderDTO> ordersList;
  private OrderRepository orderRepository = new OrderRepository();

  @FXML
  public void initialize() {
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

    ordersList = FXCollections.observableArrayList();
    ordersTable.setItems(ordersList);

    loadPaidOrders();
  }

  private void loadPaidOrders() {
    try {
      List<OrderDTO> paidOrders = orderRepository.getPaidOrders();
      ordersList.setAll(paidOrders);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu đơn hàng đã thanh toán: " + e.getMessage());
    }
  }

  @FXML
  private void handleDateChange() {
    LocalDate selectedDate = datePicker.getValue();
    if (selectedDate == null) {
      loadPaidOrders();  // Hiển thị tất cả đơn hàng nếu không có ngày nào được chọn
      return;
    }

    try {
      List<OrderDTO> filteredOrders = orderRepository.getPaidOrdersByDate(selectedDate);
      ordersList.setAll(filteredOrders);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi lọc đơn hàng theo ngày: " + e.getMessage());
    }
  }

  @FXML
  private void handleViewOrderDetails() {
    OrderDTO selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
    if (selectedOrder == null) {
      showError("Vui lòng chọn một đơn hàng để xem chi tiết.");
      return;
    }

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/ExoCoffee/FXML/paidOrder_view.fxml"));
      Parent root = fxmlLoader.load();

      OrderProductsController controller = fxmlLoader.getController();
      controller.setOrderId(selectedOrder.getOrderId());

      Stage stage = new Stage();
      stage.setTitle("Chi tiết đơn hàng");
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleViewStatistics() {
    LocalDate selectedDate = datePicker.getValue();
    if (selectedDate == null) {
      showError("Vui lòng chọn một ngày để xem thống kê.");
      return;
    }

    try {
      List<StatisticsDTO> statistics = orderRepository.getStatisticsByDate(selectedDate);
      displayStatistics(statistics);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu thống kê: " + e.getMessage());
    }
  }

  private void displayStatistics(List<StatisticsDTO> statistics) {
    // Hiển thị thông tin thống kê, ví dụ: trong một TableView hoặc Alert
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Thống kê ngày " + datePicker.getValue().toString());
    alert.setHeaderText(null);
    StringBuilder content = new StringBuilder();
    for (StatisticsDTO stat : statistics) {
      content.append(stat.toString()).append("\n");
    }
    alert.setContentText(content.toString());
    alert.showAndWait();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
