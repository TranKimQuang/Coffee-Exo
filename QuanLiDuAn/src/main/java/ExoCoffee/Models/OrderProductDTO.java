package ExoCoffee.Models;

public class OrderProductDTO {
  private int orderProductId; // ID của sản phẩm trong đơn hàng
  private int orderId; // ID của đơn hàng
  private int productId; // ID của sản phẩm
  private ProductDTO product; // Thông tin sản phẩm
  private int quantity; // Số lượng sản phẩm

  // Constructors
  public OrderProductDTO(int orderProductId, int orderId, ProductDTO product, int quantity) {
    this.orderProductId = orderProductId;
    this.orderId = orderId;
    this.product = product;
    this.quantity = quantity;
  }

  public OrderProductDTO(int orderId, ProductDTO product, int quantity) {
    this.orderId = orderId;
    this.product = product;
    this.quantity = quantity;
  }

  // Thêm constructor mới
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

  public ProductDTO getProduct() {
    return product;
  }

  public void setProduct(ProductDTO product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}