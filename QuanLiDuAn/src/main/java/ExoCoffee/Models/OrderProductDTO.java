package ExoCoffee.Models;

public class OrderProductDTO {
  private int orderId; // ID của đơn hàng
  private int productId; // ID của sản phẩm
  private int quantity; // Số lượng
  private double price; // Giá sản phẩm (lấy từ bảng products)

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

  public OrderProductDTO(int orderId, int productId, int quantity, double price) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.price = price;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Tính tổng giá trị của sản phẩm trong đơn hàng.
   *
   * @return Tổng giá trị (price * quantity).
   */
  public double getTotalPrice() {
    return price * quantity;
  }
}