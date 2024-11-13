/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasi_kasir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class Koneksi {
  private final String url="jdbc:mysql://localhost/kasir"; 
  private final String username_xampp = "root";
  private final String password_xampp = "";
  private Connection con;
  
  public void connect(){
      try {
          con = DriverManager.getConnection(url, username_xampp , password_xampp);
          System.out.println("koneksi berhasil");
      } catch (SQLException e) {
          JOptionPane.showMessageDialog(null, e.getMessage());
      }
     }
  public Connection getcon(){
      return con;
  }
  }
