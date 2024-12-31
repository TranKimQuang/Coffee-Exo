package ExoCoffee.Models;

public class ProductDTO {
  private int productId;
  private String name;
  private double price;
  private String category;

  // Constructors
  public ProductDTO() {}

  public ProductDTO(int productId, String name, double price, String category) {
    this.productId = productId;
    this.name = name;
    this.price = price;
    this.category = category;
  }

  // Getters và Setters
  public int getProductId() { // Đổi từ getId thành getProductId
    return productId;
  }

  public void setProductId(int productId) { // Đổi từ setId thành setProductId
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}