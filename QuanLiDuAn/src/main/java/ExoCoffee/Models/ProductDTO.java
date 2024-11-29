package ExoCoffee.Models;

public class ProductDTO {
  private int productId;
  private String name;
  private double price;
  private String category;

  public ProductDTO(String name, int productId, double price, String category) {
    this.name = name;
    this.productId = productId;
    this.price = price;
    this.category = category;
  }

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

