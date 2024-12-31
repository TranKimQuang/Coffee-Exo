package ExoCoffee.Models;

public class ProductDTO {
  private int productId; // ID của sản phẩm
  private String name; // Tên sản phẩm
  private double price; // Giá sản phẩm
  private String category; // Danh mục sản phẩm

  // Constructors
  public ProductDTO(int productId, String name, double price, String category) {
    this.productId = productId;
    this.name = name;
    this.price = price;
    this.category = category;
  }

  // Getters and setters
  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
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