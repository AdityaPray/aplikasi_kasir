/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasi_kasir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author ASUS
 */
public class Produk extends JFrame{
    private PreparedStatement stat;
    private  ResultSet rs;
    Koneksi k = new Koneksi();
    private final JTextField txt_search;
    private final JButton btn_search;
    private final JButton btn_insert;
    private final JButton btn_update;
    private final JButton btn_delete;
    private final DefaultTableModel tableModel;
    private final JTable produkTable;
    private final JButton btn_refresh;
    private final JLabel lbl_searchicon;
    private CardLayout cardLayout;
    
    public Produk (){
        setTitle("Produk");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setJMenuBar(menu.createMenu(this));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        
        /*mengatur panel atas*/
        JPanel panel_top = new JPanel(new BorderLayout());
        panel_top.setLayout(new BorderLayout(10, 10));
        JPanel panel_search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardLayout = new CardLayout();
        panel_search.setLayout(cardLayout);
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
                searchProduk(keyword);
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
        
        
        
        panel_search.add(lbl_searchicon,"searchIcon");
        cardLayout.show(panel_search, "searchIcon");
        panel_top.add(panel_search,BorderLayout.EAST);
        
        
         JPanel panel_crud = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btn_insert = new JButton("Insert");
        btn_update = new JButton("Update");
        btn_delete = new JButton("Delete");
        btn_refresh = new JButton("Refresh");
        

        panel_crud.add(btn_insert);
        panel_crud.add(btn_update);
        panel_crud.add(btn_delete);
        panel_crud.add(btn_refresh);
        

        panel_top.add(panel_search, BorderLayout.EAST);
        panel_top.add(panel_crud, BorderLayout.WEST);

        mainPanel.add(panel_top, BorderLayout.NORTH);
        
         tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID Produk", "Nama Barang", "Harga Jual", "Harga Beli", "Stok"});
        produkTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(produkTable);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        mainPanel.add(scrollPane);
        
        
        k.connect();
        loadAllProduk();
        
        
        btn_insert.addActionListener(e -> insertProduk() );
        btn_update.addActionListener(e -> updateProduk());
        btn_delete.addActionListener(e -> deleteProduk());
        btn_refresh.addActionListener(e -> loadAllProduk());
        
         // Table selection listener
        produkTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = produkTable.getSelectedRow();
                if (selectedRow >= 0) {
                   
                }
            }
        });
        
        pack();
        setVisible(true);
        
       
    }
    
     private void loadAllProduk() {
        tableModel.setRowCount(0);
        try {
            PreparedStatement pst = k.getcon().prepareStatement("SELECT * FROM produk");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getDouble("harga_jual"),
                    rs.getDouble("harga_beli"),
                    rs.getInt("stok")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
     private void insertProduk() {
        JTextField[] fields = {new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
        String[] labels = {"ID Produk", "Nama Barang", "Harga Jual", "Harga Beli", "Stok"};
        Object[] message = new Object[labels.length * 2];
        for (int i = 0; i < labels.length; i++) {
            message[i * 2] = labels[i];
            message[i * 2 + 1] = fields[i];
        }

        int option = JOptionPane.showConfirmDialog(this, message, "Insert Produk", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement pst = k.getcon().prepareStatement(
                    "INSERT INTO produk (id_barang, nama_barang, harga_jual, harga_beli, stok) VALUES (?, ?, ?, ?, ?)"
                );
                for (int i = 0; i < fields.length; i++) {
                    pst.setString(i + 1, fields[i].getText());
                }
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
                loadAllProduk();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void updateProduk(){
        int selectedRow = produkTable.getSelectedRow();
        if(selectedRow < 0){
            JOptionPane.showMessageDialog(this, "Pilih Produk Yang Ingin Diupdate Dari Table!");
            return;
        }
    // Mendapatkan data produk dari baris yang dipilih
    int idProduk = (int) tableModel.getValueAt(selectedRow, 0);
    String namaBarang = (String) tableModel.getValueAt(selectedRow, 1);
    double hargaJual = (double) tableModel.getValueAt(selectedRow, 2);
    double hargaBeli = (double) tableModel.getValueAt(selectedRow, 3);
    int stok = (int) tableModel.getValueAt(selectedRow, 4);
    
    // Membuat field input untuk update data
    JTextField txtNamaBarang = new JTextField(namaBarang);
    JTextField txtHargaJual = new JTextField(String.valueOf(hargaJual));
    JTextField txtHargaBeli = new JTextField(String.valueOf(hargaBeli));
    JTextField txtStok = new JTextField(String.valueOf(stok));
    
    Object[] message = {
        "Nama Barang", txtNamaBarang,
        "HargaJual", txtHargaJual,
        "Harga Beli", txtHargaBeli,
        "Stok", txtStok
    };
    
    int option = JOptionPane.showConfirmDialog(this, message, "Update Produk", JOptionPane.OK_CANCEL_OPTION);
    if(option == JOptionPane.OK_OPTION){
        try {
            PreparedStatement pst = k.getcon().prepareStatement(
                    "Update produk SET nama_barang = ?, harga_jual = ?, harga_beli = ?, stok = ?, id_barang = ?"
            );
            pst.setString(1, txtNamaBarang.getText());
            pst.setDouble(2, Double.parseDouble(txtHargaJual.getText()));
            pst.setDouble(3, Double.parseDouble(txtHargaBeli.getText()));
            pst.setInt(4, Integer.parseInt(txtStok.getText()));
            pst.setInt(5, idProduk);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Produk berhasil diupdate!");
            loadAllProduk(); // Muat ulang data produk setelah update
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "error : " + ex.getMessage());
        }
        
    }
}
    
    private void deleteProduk() {
        int selectedRow = produkTable.getSelectedRow();
        if(selectedRow < 0){
            JOptionPane.showMessageDialog(this, "Pilih Produk Yang Ingin Diupdate Dari Table!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus Produk ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                int idproduk = (int) tableModel.getValueAt(selectedRow, 0);
                PreparedStatement pst = k.getcon().prepareStatement("DELETE FROM produk WHERE id_barang = ?");
                pst.setInt(1, idproduk);
                pst.executeUpdate();
                


                JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
                loadAllProduk();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
        
    }
    private void searchProduk(String keyword) {
        tableModel.setRowCount(0); // Clear table data

        if (keyword.isEmpty()) {
            loadAllProduk();
            return;
        }

        try {
            PreparedStatement pst = k.getcon().prepareStatement(
                "SELECT * FROM produk WHERE nama_barang LIKE ?"
            );
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getDouble("harga_jual"),
                    rs.getDouble("harga_beli"),
                    rs.getInt("stok")
                };
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Produk Tidak Ditemukan");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Produk().setVisible(true);
            }
        });
    }

   
}
