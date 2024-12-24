package ExoCoffee.Models;

import java.util.Date;
import java.util.List;

public class OrderDTO {
  private int orderId;
  private double totalAmount;
  private Date orderDate;
  private List<OrderProductDTO> orderProducts;
  private String productName;  // Thêm thuộc tính này
  private int quantity;  // Thêm thuộc tính này

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

  // Thêm constructor mới
  public OrderDTO(int orderId, String productName, int quantity, double totalPrice) {
    this.orderId = orderId;
    this.productName = productName;
    this.quantity = quantity;
    this.totalAmount = totalPrice;
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

  // Thêm phương thức getter và setter cho thuộc tính mới
  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  // Thêm phương thức getTotalPrice
  public double getTotalPrice() {
    return this.totalAmount;
  }
}
