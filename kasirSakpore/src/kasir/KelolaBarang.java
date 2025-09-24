/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author yaniyan
 */
public class KelolaBarang extends javax.swing.JPanel {

    private boolean editMode = false; 
    private int editId = -1; // simpan id user yg diedit
    /**
     * Creates new form KelolaBarang
     */
    public KelolaBarang() {
        initComponents();
        tampilData();
        loadKategori();
        
        tPajak.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        hitungHargaJual();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        hitungHargaJual();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        hitungHargaJual();
    }
});
        
        tHargaPokok.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) { hitungHargaJual(); }
    @Override
    public void removeUpdate(DocumentEvent e) { hitungHargaJual(); }
    @Override
    public void changedUpdate(DocumentEvent e) { hitungHargaJual(); }
});
      
        tCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang();
    }
    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang();
    }
    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        cariBarang();
    }
});

        
    }
    private void hitungHargaJual() {
    try {
        int hargaPokok = Integer.parseInt(tHargaPokok.getText().trim());
        int pajak = Integer.parseInt(tPajak.getText().trim());

        int hargaJual = hargaPokok + (hargaPokok * pajak / 100);
        tHargaJual.setText(String.valueOf(hargaJual));

    } catch (NumberFormatException ex) {
        // kalau textfield kosong atau bukan angka
        tHargaJual.setText("");
    }
}
    
    private void resetForm() {
        tKdBarang.setText("");
        tSku.setText("");
        tNamaBarang.setText("");
        cbKategori.setSelectedIndex(0);
        tSatuan.setText("");
        tHargaPokok.setText("");
        tPajak.setText("");
        tHargaJual.setText("");
        tGambar.setText("");
        lbGambar.setIcon(null);
    }
    
    private void cariBarang() {
    String key = tCari.getText().trim();

    // Kosongkan tabel sebelum isi ulang
    DefaultTableModel model = (DefaultTableModel) tblBarang.getModel();
    model.setRowCount(0);

    // Query dengan multi kolom + CAST untuk integer
    String sql = "SELECT kodebarang, skubarang, nama, kategori, satuan, " +
                 "hargapokok, ppn, hargabarang, stok, gambar " +
                 "FROM barang " +
                 "WHERE CAST(kodebarang AS TEXT) ILIKE ? " +
                 "OR skubarang ILIKE ? " +
                 "OR nama ILIKE ? " +
                 "OR kategori ILIKE ? " +
                 "OR satuan ILIKE ? " +
                 "OR CAST(hargapokok AS TEXT) ILIKE ? " +
                 "OR CAST(ppn AS TEXT) ILIKE ? " +
                 "OR CAST(hargabarang AS TEXT) ILIKE ? " +
                 "OR CAST(stok AS TEXT) ILIKE ? " +
                 "OR gambar ILIKE ?";

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        // Set semua parameter dengan key pencarian
        for (int i = 1; i <= 10; i++) {
            pst.setString(i, "%" + key + "%");
        }

        ResultSet rs = pst.executeQuery();
        int no = 1;
        while (rs.next()) {
            Object[] row = {
                no++,
                rs.getString("skubarang"),
                rs.getString("nama"),
                rs.getString("kategori"),
                rs.getString("satuan"),
                rs.getInt("hargapokok"),
                rs.getInt("ppn"),
                rs.getInt("hargabarang"),
                rs.getInt("stok"),
                rs.getString("gambar")
            };
            model.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error pencarian: " + e.getMessage());
        e.printStackTrace();
    }
}

    
    private void loadKategori() {
    cbKategori.removeAllItems(); // kosongkan dulu
    try (Connection conn = koneksi.dbKonek()) {
        String sql = "SELECT namakategori FROM kategori ORDER BY namakategori ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cbKategori.addItem(rs.getString("namakategori"));
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal load kategori: " + ex.getMessage());
        ex.printStackTrace();
    }
    }
    
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Kode Barang");
        model.addColumn("SKU Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Kategori");
        model.addColumn("Satuan");
        model.addColumn("Harga Pokok");
        model.addColumn("PPN");
        model.addColumn("Harga Jual");
        model.addColumn("Stok");
        model.addColumn("Gambar");
        
        try (Connection conn = kasir.koneksi.dbKonek()) {
            String sql = "SELECT *FROM barang ORDER BY kodebarang ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getInt("kodebarang"),
                    rs.getString("skubarang"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getString("satuan"),
                    rs.getInt("hargapokok"),
                    rs.getInt("ppn"),
                    rs.getInt("hargabarang"),
                    rs.getInt("stok"),
                    rs.getString("gambar")
                });
            }
            tblBarang.setModel(model);
            
            // sembunyikan kolom ID (jangan ditampilkan ke user)
            tblBarang.getColumnModel().getColumn(1).setMinWidth(0);
            tblBarang.getColumnModel().getColumn(1).setMaxWidth(0);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error tampil data: " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tKdBarang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tSku = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tNamaBarang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tSatuan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tHargaPokok = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tPajak = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tHargaJual = new javax.swing.JTextField();
        cbKategori = new javax.swing.JComboBox<>();
        btSimpan = new javax.swing.JButton();
        btEdit = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        lbGambar = new javax.swing.JLabel();
        tGambar = new javax.swing.JTextField();
        btGambar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        tCari = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel1.setText("Kelola Barang");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Input Barang");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Kode Barang");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("SKU Barang");

        tSku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tSkuActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Nama Barang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Kategori");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Satuan");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Harga Pokok");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("PPN");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Harga Jual");

        cbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btSimpan.setBackground(new java.awt.Color(255, 255, 51));
        btSimpan.setText("Simpan");
        btSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSimpanActionPerformed(evt);
            }
        });

        btEdit.setBackground(new java.awt.Color(255, 153, 51));
        btEdit.setText("Edit");
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        btDelete.setBackground(new java.awt.Color(255, 51, 51));
        btDelete.setText("Hapus");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btCancel.setBackground(new java.awt.Color(204, 204, 204));
        btCancel.setText("Batal");
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        lbGambar.setBackground(new java.awt.Color(204, 204, 204));

        btGambar.setText("Pilih Gambar");
        btGambar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGambarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(tSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(tHargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tPajak, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(tHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43))
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(tKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(tSku, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(tNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(304, 304, 304)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btGambar, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lbGambar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tGambar))
                .addGap(405, 405, 405))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tSku, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(40, 40, 40))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tHargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tPajak, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(61, 61, 61))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbGambar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tGambar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btGambar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Daftar Barang");

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblBarang);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Cari Barang :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tCari, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tSkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tSkuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tSkuActionPerformed

    private void btEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditActionPerformed
        // TODO add your handling code here:
        int row = tblBarang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
                return;
            }
            
            // ambil data dari tabel
            int kode = Integer.parseInt(tblBarang.getValueAt(row, 1).toString());
            String sku = tblBarang.getValueAt(row, 2).toString();
            String nama     = tblBarang.getValueAt(row, 3).toString();
            String kategori   = tblBarang.getValueAt(row, 4).toString();
            String satuan   = tblBarang.getValueAt(row, 5).toString();
            int hargapokok = Integer.parseInt(tblBarang.getValueAt(row, 6).toString());
            int ppn = Integer.parseInt(tblBarang.getValueAt(row, 7).toString());
            int hargajual = Integer.parseInt(tblBarang.getValueAt(row, 8).toString());
            
            
            // isi form
            tKdBarang.setText(String.valueOf(kode));
            tSku.setText(sku);
            tNamaBarang.setText(nama);
            cbKategori.setSelectedItem(kategori);
            tSatuan.setText(satuan);
            tHargaPokok.setText(String.valueOf(hargapokok));
            tPajak.setText(String.valueOf(ppn));
            tHargaJual.setText(String.valueOf(hargajual));
            
            editMode = true;
            editId = kode;
    }//GEN-LAST:event_btEditActionPerformed

    private void btSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSimpanActionPerformed
        // TODO add your handling code here:
            int kode = Integer.parseInt(tKdBarang.getText().trim());
            String skubarang = tSku.getText().trim();
            String nama     = tNamaBarang.getText().trim();
            String kategori   = cbKategori.getSelectedItem().toString();
            String satuan = tSatuan.getText().trim();
            int hargapokok = Integer.parseInt(tHargaPokok.getText().trim());
            int ppn        = Integer.parseInt(tPajak.getText().trim());
            int hargajual  = Integer.parseInt(tHargaJual.getText().trim());


            // ambil path lengkap dari textfield
            File source = new File(tGambar.getText().trim());
            String gambar = source.getName(); // nama file saja
            try (Connection conn = koneksi.dbKonek()) {
                
                 // copy file ke folder images
                File destFolder = new File("images");
                if (!destFolder.exists()) destFolder.mkdirs(); // buat folder jika belum ada
                File dest = new File(destFolder, gambar);
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (!editMode) { 
                    // mode tambah user baru
                    String sql = "INSERT INTO barang (kodebarang, skubarang, nama, hargabarang, kategori, gambar, hargapokok, ppn, satuan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, kode);
                    ps.setString(2, skubarang);
                    ps.setString(3, nama);
                    ps.setInt(4, hargajual);
                    ps.setString(5, kategori);
                    ps.setString(6, gambar);
                    ps.setInt(7, hargapokok);
                    ps.setInt(8, ppn);
                    ps.setString(9, satuan);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
                    
                    
                    
                    
                } else {
                    // mode edit user
                    String sql = "UPDATE barang SET nama=?, kategori=?, satuan=?, hargapokok=?, hargabarang=?, gambar=? WHERE kodebarang=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nama);
                    ps.setString(2, kategori);
                    ps.setString(3, satuan);
                    ps.setInt(4, hargapokok);
                    ps.setInt(5, hargajual);
                    ps.setString(6, gambar);
                    ps.setInt(7, kode);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil diupdate!");
                    
                    editMode = false; 
                    editId = -1;
                }
                
                // reset form
                resetForm();
                
                tampilData(); // refresh tabel
                
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
    }//GEN-LAST:event_btSimpanActionPerformed

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        // TODO add your handling code here:
        resetForm();
        tblBarang.clearSelection();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        // TODO add your handling code here:
        int row = tblBarang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
                return;
            }
            
            int id = Integer.parseInt(tblBarang.getValueAt(row, 1).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin menghapus user ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = koneksi.dbKonek()) {
                    String sql = "DELETE FROM barang WHERE kodebarang=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil dihapus!");
                    tampilData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error hapus: " + ex.getMessage());
                }
            }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btGambarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGambarActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Gambar Barang");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Gambar (*.jpg, *.png, *.jpeg)", "jpg", "png", "jpeg")
        );

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // simpan path ke textfield
            tGambar.setText(selectedFile.getAbsolutePath());

            // tampilkan preview di JLabel
            ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(
                lbGambar.getWidth(), lbGambar.getHeight(), Image.SCALE_SMOOTH
            );
            lbGambar.setIcon(new ImageIcon(img));
        }

    }//GEN-LAST:event_btGambarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btGambar;
    private javax.swing.JButton btSimpan;
    private javax.swing.JComboBox<String> cbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbGambar;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tGambar;
    private javax.swing.JTextField tHargaJual;
    private javax.swing.JTextField tHargaPokok;
    private javax.swing.JTextField tKdBarang;
    private javax.swing.JTextField tNamaBarang;
    private javax.swing.JTextField tPajak;
    private javax.swing.JTextField tSatuan;
    private javax.swing.JTextField tSku;
    private javax.swing.JTable tblBarang;
    // End of variables declaration//GEN-END:variables
}
