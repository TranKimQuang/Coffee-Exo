package ExoCoffee.Models;

public class StatisticsDTO {
  private int productId;
  private String productName;
  private int totalQuantity;
  private double totalPrice;

  // Constructor đầy đủ
  public StatisticsDTO(int productId, String productName, int totalQuantity, double totalPrice) {
    this.productId = productId;
    this.productName = productName;
    this.totalQuantity = totalQuantity;
    this.totalPrice = totalPrice;
  }

  // Constructor chỉ có productId, totalQuantity, totalPrice
  public StatisticsDTO(int productId, int totalQuantity, double totalPrice) {
    this.productId = productId;
    this.totalQuantity = totalQuantity;
    this.totalPrice = totalPrice;
  }

  // Getters and setters
  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(int totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  @Override
  public String toString() {
    return "Product ID: " + productId + ", Product Name: " + (productName != null ? productName : "N/A") + ", Total Quantity: " + totalQuantity + ", Total Price: " + totalPrice;
  }
}
