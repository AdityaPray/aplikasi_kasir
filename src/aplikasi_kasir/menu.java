/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasi_kasir;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author ASUS
 */
public class menu {
    private static int currentUserId;
    
    public static void currentUserId(int id){
    currentUserId = id;
}
    
    
    public static JMenuBar createMenu (JFrame currentframe){
        JMenuBar menuBar = new JMenuBar();
        
        JMenuItem Produk = new JMenuItem("Produk");
        Produk.addActionListener((e) -> {
            currentframe.dispose();
            Produk newpage = new Produk();
            newpage.setVisible(true);
                    
        });
        JMenuItem Profil = new JMenuItem("Profil");
        Profil.addActionListener((e) -> {
            try {
            System.out.println("Menu Diklik");
            System.out.println("Current User Id" + currentUserId);
            currentframe.dispose();
            new Profil(currentUserId).setVisible(true);
            System.out.println("Form Profil Dibuka");
            } catch (Exception ex) {
                System.out.println("Error" + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(currentframe, "Eror :" + ex.getMessage());
            }
        });
        JMenu fileMenu = new JMenu("Menu");
        fileMenu.add(Profil);
        fileMenu.add(Produk);
        menuBar.add(fileMenu);
        return menuBar;
    }
}
