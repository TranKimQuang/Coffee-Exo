package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
  // Phương thức kết nối cơ sở dữ liệu
  public static Connection getConnection() {
    Connection conn = null;
    try {
      // Nạp driver MySQL
      Class.forName("com.mysql.cj.jdbc.Driver");

      // Chuỗi kết nối
      String strConn = "jdbc:mysql://localhost:3306/coffeemanager";

      // Kết nối với cơ sở dữ liệu
      conn = DriverManager.getConnection(strConn, "root", "");
      System.out.println("Kết nối thành công!");
    } catch (ClassNotFoundException e) {
      System.err.println("Không tìm thấy driver: " + e.getMessage());
    } catch (SQLException e) {
      System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
    }
    return conn;
  }

  // Phương thức kiểm tra
  public static void main(String[] args) {
    Connection connection = DBUtils.getConnection();
    if (connection != null) {
      System.out.println("haha");
    }
  }
}
