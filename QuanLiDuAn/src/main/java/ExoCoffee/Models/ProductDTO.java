package ExoCoffee.Models;

public class ProductDTO {
  private int id; // Thuộc tính id
  private String name;
  private double price;
  private String category;

  // Constructors
  public ProductDTO() {}

  public ProductDTO(int id, String name, double price, String category) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.category = category;
  }

  // Getters và Setters
  public int getId() { // Phương thức getter cho id
    return id;
  }

  public void setId(int id) {
    this.id = id;
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