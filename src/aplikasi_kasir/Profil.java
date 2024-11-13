package aplikasi_kasir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.Timer;

public class Profil extends JFrame {

    private final JLabel lbl_idUser, lbl_namaUser, lbl_idLevel, lbl_tanggalLahir, lbl_alamat, lbl_email, lbl_nomorTelepon, lbl_username, lbl_password;
    private final JTextField txt_idUser, txt_namaUser, txt_idLevel, txt_tanggalLahir, txt_alamat, txt_email, txt_nomorTelepon, txt_username, txt_password, txt_search;
    private PreparedStatement stat;
    private ResultSet rs;
    private final JButton btn_logout, btn_insert, btn_update, btn_delete, btn_search;
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    Koneksi k = new Koneksi();
    private final JLabel lbl_searchicon;
    private CardLayout cardLayout;
    
    

    public Profil(int id_user) {
        setTitle("Profil Pengguna");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setJMenuBar(menu.createMenu(this));

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Top panel with logout button
        JPanel panel_top = new JPanel(new BorderLayout(10, 10));
        JPanel panel_search = new JPanel(new FlowLayout(FlowLayout.LEFT,10, 10));
        cardLayout = new CardLayout();
        panel_search.setLayout(cardLayout);
        btn_logout = new JButton("Log Out");
        lbl_searchicon = new JLabel(new ImageIcon(getClass().getResource("/image/search-icon-24.png")));
        
        lbl_searchicon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel_search.add(lbl_searchicon);
        
        
        
        lbl_searchicon.addMouseListener(new MouseAdapter(){
            @Override
            public void 
                    mouseClicked(MouseEvent evt) {
                        cardLayout.show(panel_search, "searchUser");
                        txt_search.requestFocus();
                    }
        });
        
        txt_search = new JTextField(15);
        btn_search = new JButton("Search");
        btn_search.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txt_search.getText().trim();
                searchUser(keyword);
            }
            
        });
        
        txt_search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e){
                Timer timer = new Timer(200, new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!btn_search.isFocusOwner()) {
                            String lastSearch = txt_search.getText().trim();
                            cardLayout.show(panel_search, "searchIcon");
                            txt_search.setText(lastSearch);
                        }
                    }  
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        // Tambahkan kode ini setelah inisialisasi btn_search
        btn_search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
             // Jika txt_search kosong dan tidak ada fokus di kedua komponen
            if (txt_search.getText().trim().isEmpty() && !txt_search.isFocusOwner()) {
            cardLayout.show(panel_search, "searchIcon");
                    }
                }
            });
        
        JPanel searchUserpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchUserpanel.add(txt_search);
        searchUserpanel.add(btn_search);
        
        panel_search.add(lbl_searchicon,"searchIcon");
        panel_search.add(searchUserpanel, "searchUser");
        cardLayout.show(panel_search, "searchIcon");
        panel_top.add(panel_search,BorderLayout.EAST);
        
        
        
        
        
        panel_top.add(btn_logout,BorderLayout.WEST);
        mainPanel.add(panel_top, BorderLayout.NORTH);
        

        // Center panel with form and CRUD buttons
        JPanel panel_center = new JPanel(new BorderLayout(10, 0));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize labels and text fields
        lbl_idUser = new JLabel("ID User:");
        lbl_namaUser = new JLabel("Nama User:");
        lbl_idLevel = new JLabel("ID Level:");
        lbl_tanggalLahir = new JLabel("Tanggal Lahir:");
        lbl_alamat = new JLabel("Alamat:");
        lbl_email = new JLabel("Email:");
        lbl_nomorTelepon = new JLabel("Nomor Telepon:");
        lbl_username = new JLabel("Username:");
        lbl_password = new JLabel("Password:");

        txt_idUser = new JTextField(20);
        txt_namaUser = new JTextField(20);
        txt_idLevel = new JTextField(20);
        txt_tanggalLahir = new JTextField(20);
        txt_alamat = new JTextField(20);
        txt_email = new JTextField(20);
        txt_nomorTelepon = new JTextField(20);
        txt_username = new JTextField(20);
        txt_password = new JTextField(20);

        

        // Add components to form panel
        addFormRow(formPanel, gbc, lbl_idUser, txt_idUser);
        addFormRow(formPanel, gbc, lbl_namaUser, txt_namaUser);
        addFormRow(formPanel, gbc, lbl_idLevel, txt_idLevel);
        addFormRow(formPanel, gbc, lbl_tanggalLahir, txt_tanggalLahir);
        addFormRow(formPanel, gbc, lbl_alamat, txt_alamat);
        addFormRow(formPanel, gbc, lbl_email, txt_email);
        addFormRow(formPanel, gbc, lbl_nomorTelepon, txt_nomorTelepon);
        addFormRow(formPanel, gbc, lbl_username, txt_username);
        addFormRow(formPanel, gbc, lbl_password, txt_password);

        panel_center.add(formPanel, BorderLayout.CENTER);

        // CRUD buttons panel
        JPanel panel_crud = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_insert = new JButton("Insert");
        btn_update = new JButton("Update");
        btn_delete = new JButton("Delete");
        JButton btn_refresh = new JButton("Refresh");

        panel_crud.add(btn_insert);
        panel_crud.add(btn_update);
        panel_crud.add(btn_delete);
        panel_crud.add(btn_refresh);

        panel_center.add(panel_crud, BorderLayout.SOUTH);

        mainPanel.add(panel_center, BorderLayout.CENTER);

        // Table panel
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID User", "Nama User", "ID Level", "Tanggal Lahir", "Alamat", "Email", "Nomor Telepon", "Username", "Password"});
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Connect to database and load user data
        k.connect();
        loadUserData(id_user);
        loadAllUsers();

        // Action listeners
        btn_logout.addActionListener(this::logoutAction);
        btn_insert.addActionListener(e -> insertUser());
        btn_update.addActionListener(e -> updateUser());
        btn_delete.addActionListener(e -> deleteUser());
        btn_refresh.addActionListener(e -> loadAllUsers());
        

        // Table selection listener
        userTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFields(selectedRow);
                    setFieldsEditable(true);
                }
            }
        });

        pack();
        setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField textField) {
        gbc.gridx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(textField, gbc);
        gbc.gridy++;
    }

    private void logoutAction(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin log out?", "Konfirmasi Log Out", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            new Login_kasir().setVisible(true);
            this.dispose();
        }
    }
     private void searchUser(String keyword) {
        tableModel.setRowCount(0); // Clear table data

        if (keyword.isEmpty()) {
            loadAllUsers();
            return;
        }

        try {
            PreparedStatement pst = k.getcon().prepareStatement(
                "SELECT * FROM user WHERE nama_user LIKE ? OR username LIKE ?"
            );
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_user"),
                    rs.getString("nama_user"),
                    rs.getInt("id_level"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("alamat"),
                    rs.getString("email"),
                    rs.getString("nomor_telepon"),
                    rs.getString("username"),
                    rs.getString("password")
                };
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "User Tidak Ditemukan");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    private int id_level;

    private void loadUserData(int id_user) {
        try {
            this.stat = k.getcon().prepareStatement("SELECT * FROM user WHERE id_user = ?");
            this.stat.setInt(1, id_user);
            this.rs = this.stat.executeQuery();

            if (rs.next()) {
                populateFields(rs);
                id_level = rs.getInt("id_level");
            } else {
                JOptionPane.showMessageDialog(this, "Data user tidak ditemukan!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
         // Cek id_level setelah memuat data user
    if (id_level != 1) { // Non-admin
        btn_insert.setEnabled(false);
        btn_update.setEnabled(false);
        btn_delete.setEnabled(false);
        userTable.setVisible(false);
    }else {
        btn_insert.setEnabled(true);
        btn_update.setEnabled(true);
        btn_delete.setEnabled(true);
        userTable.setVisible(true);
    }
    }

    private void loadAllUsers() {
        tableModel.setRowCount(0);
        try {
            PreparedStatement pst = k.getcon().prepareStatement("SELECT * FROM user");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_user"),
                    rs.getString("nama_user"),
                    rs.getInt("id_level"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("alamat"),
                    rs.getString("email"),
                    rs.getString("nomor_telepon"),
                    rs.getString("username"),
                    rs.getString("password")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void insertUser() {
        JTextField[] fields = {new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
        String[] labels = {"Nama User:", "ID Level:", "Tanggal Lahir (YYYY-MM-DD):", "Alamat:", "Email:", "Nomor Telepon:", "Username:", "Password:"};
        Object[] message = new Object[labels.length * 2];
        for (int i = 0; i < labels.length; i++) {
            message[i * 2] = labels[i];
            message[i * 2 + 1] = fields[i];
        }

        int option = JOptionPane.showConfirmDialog(this, message, "Insert User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement pst = k.getcon().prepareStatement(
                    "INSERT INTO user (nama_user, id_level, tanggal_lahir, alamat, email, nomor_telepon, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                );
                for (int i = 0; i < fields.length; i++) {
                    pst.setString(i + 1, fields[i].getText());
                }
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
                loadAllUsers();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void updateUser() {
        if (txt_idUser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin diupdate dari tabel!");
            return;
        }

        try {
            PreparedStatement pst = k.getcon().prepareStatement(
                "UPDATE user SET nama_user = ?, id_level = ?, tanggal_lahir = ?, alamat = ?, email = ?, nomor_telepon = ?, username = ?, password = ? WHERE id_user = ?"
            );
            pst.setString(1, txt_namaUser.getText());
            pst.setInt(2, Integer.parseInt(txt_idLevel.getText()));
            pst.setString(3, txt_tanggalLahir.getText());
            pst.setString(4, txt_alamat.getText());
            pst.setString(5, txt_email.getText());
            pst.setString(6, txt_nomorTelepon.getText());
            pst.setString(7, txt_username.getText());
            pst.setString(8, txt_password.getText());
            pst.setInt(9, Integer.parseInt(txt_idUser.getText()));
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User berhasil diupdate!");
            loadAllUsers();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        if (txt_idUser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin dihapus dari tabel!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus user ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement pst = k.getcon().prepareStatement("DELETE FROM user WHERE id_user = ?");
                pst.setInt(1, Integer.parseInt(txt_idUser.getText()));
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                loadAllUsers();
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        txt_idUser.setText("");
        txt_namaUser.setText("");
        txt_idLevel.setText("");
        txt_tanggalLahir.setText("");
        txt_alamat.setText("");
        txt_email.setText("");
        txt_nomorTelepon.setText("");
        txt_username.setText("");
        txt_password.setText("");
        setFieldsEditable(false);
    }

    private void populateFields(int rowIndex) {
        txt_idUser.setText(tableModel.getValueAt(rowIndex, 0).toString());
        txt_namaUser.setText((String) tableModel.getValueAt(rowIndex, 1));
        txt_idLevel.setText(tableModel.getValueAt(rowIndex, 2).toString());
        txt_tanggalLahir.setText((String) tableModel.getValueAt(rowIndex, 3));
        txt_alamat.setText((String) tableModel.getValueAt(rowIndex, 4));
        txt_email.setText((String) tableModel.getValueAt(rowIndex, 5));
        txt_nomorTelepon.setText((String) tableModel.getValueAt(rowIndex, 6));
        txt_username.setText((String) tableModel.getValueAt(rowIndex, 7));
        txt_password.setText((String) tableModel.getValueAt(rowIndex, 8));
    }

    private void populateFields(ResultSet rs) throws SQLException {
        txt_idUser.setText(String.valueOf(rs.getInt("id_user")));
        txt_namaUser.setText(rs.getString("nama_user"));
        txt_idLevel.setText(String.valueOf(rs.getInt("id_level")));
        txt_tanggalLahir.setText(rs.getString("tanggal_lahir"));
        txt_alamat.setText(rs.getString("alamat"));
        txt_email.setText(rs.getString("email"));
        txt_nomorTelepon.setText(rs.getString("nomor_telepon"));
        txt_username.setText(rs.getString("username"));
        txt_password.setText(rs.getString("password"));
    }

    private void setFieldsEditable(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
       
}
