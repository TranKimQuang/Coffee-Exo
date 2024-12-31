package ExoCoffee.Models;

public class UserDTO {
  private int userId; // ID của người dùng
  private String username; // Tên đăng nhập
  private String password; // Mật khẩu
  private String role; // Vai trò (ví dụ: admin, user)

  // Constructors
  public UserDTO(int userId, String username, String password, String role) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  // Getters and setters
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}