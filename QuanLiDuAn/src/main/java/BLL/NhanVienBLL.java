package BLL;

import DAL.NhanVienDAL;
import Model_DTO.NhanVien;

public class NhanVienBLL {
  NhanVienDAL nvDAL;
  public boolean addNewBLL(NhanVien nv){
    //cac xu li khac o day
    boolean kq = nvDAL.addNewDAL(nv);
    return kq;
  }
  public boolean checkLogin(String Username,String  PassWord){
    //kiem tra tinh dung dan nhu format,....
    return nvDAL.Login(Username,PassWord);

  }

}
