package ExoCoffee.Models;

public class OrderDTO {
  private int orderId;
  private String productName;
  private int quantity;
  private double totalPrice;

  public OrderDTO(int orderId, String productName, int quantity, double totalPrice) {
    this.orderId = orderId;
    this.productName = productName;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

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

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }
}
