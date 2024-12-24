package ExoCoffee.Models;

public class OrderProductDTO {
  private int orderProductId;
  private int orderId;
  private int productId;
  private int quantity;

  // Constructors
  public OrderProductDTO() {}

  public OrderProductDTO(int orderProductId, int orderId, int productId, int quantity) {
    this.orderProductId = orderProductId;
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
  }

  public OrderProductDTO(int productId, int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  // Getters and setters
  public int getOrderProductId() {
    return orderProductId;
  }

  public void setOrderProductId(int orderProductId) {
    this.orderProductId = orderProductId;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
