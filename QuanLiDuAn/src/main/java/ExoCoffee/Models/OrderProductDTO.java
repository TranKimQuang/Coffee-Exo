package ExoCoffee.Models;

public class OrderProductDTO {
  private int orderId; // ID của đơn hàng
  private int productId; // ID của sản phẩm
  private int quantity; // Số lượng

  // Constructors
  public OrderProductDTO() {}

  public OrderProductDTO(int orderId, int productId) {
    this.orderId = orderId;
    this.productId = productId;
  }

  public OrderProductDTO(int orderId, int productId, int quantity) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
  }

  // Getters và Setters
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