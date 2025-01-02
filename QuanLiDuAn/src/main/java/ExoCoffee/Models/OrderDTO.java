package ExoCoffee.Models;

public class OrderDTO {
  private int orderId;
  private double totalAmount;
  private String orderDate;

  public OrderDTO(int orderId, double totalAmount, String orderDate) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
  }

  public int getOrderId() {
    return orderId;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public String getOrderDate() {
    return orderDate;
  }
}
