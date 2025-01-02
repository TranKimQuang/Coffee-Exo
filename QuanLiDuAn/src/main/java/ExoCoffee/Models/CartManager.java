package ExoCoffee.Models;


public class CartManager {
  // Đối tượng Cart duy nhất
  public static Cart cart = new Cart();

  // Phương thức để lấy đối tượng Cart
  public static Cart getCart() {
    return cart;
  }

  public static void resetCart() {
    cart = new Cart();
  }
}
