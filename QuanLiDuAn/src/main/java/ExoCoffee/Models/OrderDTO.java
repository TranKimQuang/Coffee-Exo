package ExoCoffee.Models;

import java.util.Date;
import java.util.List;

public class OrderDTO {
  private int orderId; // ID của đơn hàng
  private double totalAmount; // Tổng số tiền của đơn hàng
  private Date orderDate; // Ngày đặt hàng
  private List<OrderProductDTO> orderProducts; // Danh sách sản phẩm trong đơn hàng

  // Constructors
  public OrderDTO(int orderId, double totalAmount, Date orderDate, List<OrderProductDTO> orderProducts) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
    this.orderProducts = orderProducts;
  }

  public OrderDTO(double totalAmount, Date orderDate, List<OrderProductDTO> orderProducts) {
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
    this.orderProducts = orderProducts;
  }

  public OrderDTO(int orderId, double totalAmount, Date orderDate) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
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
    this.orderProducts = orderProducts;
  }

  // Phương thức thêm sản phẩm vào đơn hàng
  public void addOrderProduct(OrderProductDTO orderProduct) {
    this.orderProducts.add(orderProduct);
  }

  // Phương thức tính tổng giá trị đơn hàng
  public double calculateTotalAmount() {
    if (orderProducts == null || orderProducts.isEmpty()) {
      return 0.0;
    }
    return orderProducts.stream()
        .mapToDouble(op -> op.getProduct().getPrice() * op.getQuantity())
        .sum();
  }
}