package ExoCoffee.Models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class OrderDTO {
  private int orderId; // ID của đơn hàng
  private double totalAmount; // Tổng số tiền của đơn hàng
  private Date orderDate; // Ngày đặt hàng
  private List<OrderProductDTO> orderProducts; // Danh sách sản phẩm trong đơn hàng

  // Constructors
  public OrderDTO() {
    this.orderProducts = new ArrayList<>(); // Khởi tạo danh sách sản phẩm trống
  }

  public OrderDTO(int orderId, double totalAmount, Date orderDate, List<OrderProductDTO> orderProducts) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
    this.orderProducts = orderProducts != null ? orderProducts : new ArrayList<>();
  }

  public OrderDTO(double totalAmount, Date orderDate, List<OrderProductDTO> orderProducts) {
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
    this.orderProducts = orderProducts != null ? orderProducts : new ArrayList<>();
  }

  public OrderDTO(int orderId, double totalAmount, Date orderDate) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
    this.orderProducts = new ArrayList<>();
  }

  // Getters and setters
  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public List<OrderProductDTO> getOrderProducts() {
    return orderProducts;
  }

  public void setOrderProducts(List<OrderProductDTO> orderProducts) {
    this.orderProducts = orderProducts != null ? orderProducts : new ArrayList<>();
  }

  // Phương thức thêm sản phẩm vào đơn hàng
  public void addOrderProduct(OrderProductDTO orderProduct) {
    if (orderProduct != null) {
      this.orderProducts.add(orderProduct);
    }
  }

  // Phương thức tính tổng giá trị đơn hàng
  public double calculateTotalAmount(List<ProductDTO> products) {
    if (orderProducts == null || orderProducts.isEmpty() || products == null) {
      return 0.0;
    }

    return orderProducts.stream()
        .mapToDouble(op -> {
          // Tìm sản phẩm dựa trên productId
          ProductDTO product = products.stream()
              .filter(p -> p.getId() == op.getProductId())
              .findFirst()
              .orElse(null);

          // Tính tổng giá trị nếu sản phẩm tồn tại
          return (product != null) ? product.getPrice() * op.getQuantity() : 0.0;
        })
        .sum();
  }

  // Phương thức toString để hiển thị thông tin đơn hàng
  @Override
  public String toString() {
    return "OrderDTO{" +
        "orderId=" + orderId +
        ", totalAmount=" + totalAmount +
        ", orderDate=" + orderDate +
        ", orderProducts=" + orderProducts +
        '}';
  }
}