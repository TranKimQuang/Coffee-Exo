package ExoCoffee.Models;

import java.util.Date;

public class OrderDTO {
  private int orderId;
  private double totalAmount;
  private Date orderDate;

  public OrderDTO(int orderId, double totalAmount, Date orderDate) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
  }

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
}
