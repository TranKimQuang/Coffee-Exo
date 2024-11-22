package Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {
  // Phương thức mã hóa mật khẩu bằng MD5
  public static String encodePas(String passPlainText) {
    try {
      // Tạo một đối tượng MessageDigest với thuật toán MD5
      MessageDigest md = MessageDigest.getInstance("MD5");

      // Xử lý chuỗi đầu vào để tạo ra digest (mảng byte)
      byte[] messageDigest = md.digest(passPlainText.getBytes());

      // Chuyển đổi mảng byte thành BigInteger (dạng số)
      BigInteger no = new BigInteger(1, messageDigest);

      // Chuyển đổi thành chuỗi hexa
      String hashtext = no.toString(16);

      // Đảm bảo độ dài đủ 32 ký tự
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }

      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Lỗi khi mã hóa mật khẩu: " + e.getMessage(), e);
    }
  }
}


