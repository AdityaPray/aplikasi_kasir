package aplikasi_kasir;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login_kasir extends JFrame {
 
    private final JLabel lbl_username;
    private final JLabel lbl_password;
    private final JLabel lbl_title;
    private final JTextField txt_username;
    private final JPasswordField txt_password;
    private final JButton btn_login;
    private PreparedStatement stat;
    private ResultSet rs;
    Koneksi k = new Koneksi();

    public Login_kasir() {
        // Mengatur judul jendela
        setTitle("Login Aplikasi Kasir");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); 


        // Membuat panel utama
        JPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Menggunakan layout null agar kita bisa mengatur posisi komponen secara manual
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        

        // Membuat komponen
        lbl_title = new JLabel("LOGIN");
        lbl_title.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_title.setBounds(80, 30, 240, 30); // Atur posisi dan ukuran komponen
        panel.add(lbl_title);

        lbl_username = new JLabel("Username:");
        lbl_username.setFont(new Font("SansSerif",Font.PLAIN,14));
        lbl_username.setBounds(50, 80, 100, 25);
        panel.add(lbl_username);

        txt_username = new JTextField();
        txt_username.setFont(new Font("SansSerif",Font.PLAIN,14));
        txt_username.setBounds(150, 80, 200, 25);
        panel.add(txt_username);

        lbl_password = new JLabel("Password:");
        lbl_password.setFont(new Font("SansSerif",Font.PLAIN,14));
        lbl_password.setBounds(50, 120, 100, 25);
        panel.add(lbl_password);

        txt_password = new JPasswordField();
        txt_password.setFont(new Font("SansSerif",Font.PLAIN,14));
        txt_password.setBounds(150, 120, 200, 25);
        txt_password.setEchoChar('*');
        panel.add(txt_password);
        
        JCheckBox showPassword = new JCheckBox("");
        showPassword.setBounds(145, 145, 145, 20);
        showPassword.setOpaque(false);
        panel.add(showPassword);
        
        showPassword.addActionListener((ActionEvent e) -> {
            if (showPassword.isSelected()) {
                txt_password.setEchoChar((char) 0);
            } else {
                txt_password.setEchoChar('*');
            }
        });
        

        btn_login = new JButton("LOGIN");
        btn_login.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn_login.setBackground(new Color(0,128,0));
        btn_login.setForeground(Color.WHITE);
        btn_login.setBorder(BorderFactory.createRaisedBevelBorder());
        btn_login.setBounds(130, 170, 140, 40);

        // Menambahkan action listener pada tombol login
        btn_login.addActionListener((ActionEvent e) -> {
            loginAction();
        });

        // Menambahkan komponen ke panel
        panel.add(lbl_title);
        panel.add(lbl_username);
        panel.add(txt_username);
        panel.add(lbl_password);
        panel.add(txt_password);
        panel.add(btn_login);

        // Menambahkan panel ke frame
        add(panel);

        k.connect(); // Koneksi ke database
    }
    
     // Panel dengan background image
    class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            // Load image sebagai background
            backgroundImage = new ImageIcon(getClass().getResource("/image/gedung-poltek-tegal-2.jpg")).getImage();
        }

       
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Menggambar gambar latar belakang
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
class user {
    int id_user, id_level;
    String username, password;

    // Constructor yang menerima username dan password
    public user(String username, String password) {
        this.id_user = 0;
        this.username = username;
        this.password = password;
        this.id_level = 0;
    }
}

    // Metode untuk menangani login
    private void loginAction() {
        String username = txt_username.getText();
        String password = new String(txt_password.getPassword());

        try {
            this.stat = k.getcon().prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
            this.stat.setString(1, username);
            this.stat.setString(2, password);
            this.rs = this.stat.executeQuery();

            if (rs.next()) {
                int id_level = rs.getInt("id_level");
                switch (id_level) {
                    case 1 -> {
                        JOptionPane.showMessageDialog(null, "Login sebagai Admin");
                        // Arahkan ke menu admin
                         menu.currentUserId(rs.getInt("id_user"));
                         this.dispose();
                          new Profil(rs.getInt("id_user")).setVisible(true);
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(null, "Login sebagai Kasir");
                        // Arahkan ke menu kasir
                        menu.currentUserId(rs.getInt("id_user"));
                         this.dispose();
                          new Profil(rs.getInt("id_user")).setVisible(true);
                    }
                    default -> JOptionPane.showMessageDialog(null, "Akun tidak ditemukan");
                }
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Akun tidak ditemukan!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login_kasir().setVisible(true);
        });
    }
}
