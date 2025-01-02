package ExoCoffee.Models;

public class OrderProductDTO {
  private int id;
  private int orderId;
  private int productId;
  private int quantity;
  private double price; // Giá sản phẩm tại thời điểm đặt hàng
  private double totalProduct; // Tổng giá trị của sản phẩm (price * quantity)

  // Constructors
  public OrderProductDTO() {}

  public OrderProductDTO(int id, int orderId, int productId, int quantity, double price, double totalProduct) {
    this.id = id;
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.price = price;
    this.totalProduct = totalProduct;
  }

  // Getters và Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getTotalProduct() {
    return totalProduct;
  }

  public void setTotalProduct(double totalProduct) {
    this.totalProduct = totalProduct;
  }
}