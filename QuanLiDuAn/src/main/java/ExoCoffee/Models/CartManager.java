package ExoCoffee.Models;


public class CartManager {
  // Đối tượng Cart duy nhất
  private static Cart cart = new Cart();

  // Phương thức để lấy đối tượng Cart
  public static Cart getCart() {
    return cart;
  }


}