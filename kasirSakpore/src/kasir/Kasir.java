/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author yaniyan
 */
public class Kasir extends javax.swing.JPanel {

    /**
     * Creates new form Kasir
     */
    public Kasir() {
        initComponents();
        loadMenu(null);
try (Connection conn = koneksi.dbKonek()) {
    txtNo.setText(generateNoTransaksi(conn));
} catch (Exception e) {
    e.printStackTrace();
    txtNo.setText("TERROR"); // fallback kalau error
}


    }
    
    private void loadMenu(String keyword) {
    pnlMenu.removeAll();
    pnlMenu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

    String sql = "SELECT kodeBarang, SKUBarang, nama, hargaBarang, stok, gambar FROM Barang";
    if (keyword != null && !keyword.isEmpty()) {
        sql += " WHERE LOWER(nama) LIKE ?";
    }

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (keyword != null && !keyword.isEmpty()) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
        }
int count = 0;
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int kodeBarang = rs.getInt("kodeBarang");
                String sku = rs.getString("SKUBarang");
                String nama = rs.getString("nama");
                double harga = rs.getDouble("hargaBarang");
                int stok = rs.getInt("stok");
                String gambarPath = rs.getString("gambar");
count++;
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setPreferredSize(new Dimension(200, 250));

                JLabel lblGambar = new JLabel();
                lblGambar.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblGambar.setPreferredSize(new Dimension(120, 120));
                lblGambar.setHorizontalAlignment(JLabel.CENTER);
                lblGambar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

                if (gambarPath != null && !gambarPath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(gambarPath);
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblGambar.setIcon(new ImageIcon(img));
                } else {
                    lblGambar.setText("No Image");
                }

                JLabel lblNama = new JLabel(nama);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblSKU = new JLabel("SKU: " + sku);
                JLabel lblHarga = new JLabel("Rp " + harga);
                JLabel lblStok = new JLabel("Stok: " + stok);
                lblSKU.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblStok.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton btnBeli = new JButton("Beli");
                btnBeli.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnBeli.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

                btnBeli.addActionListener(ev -> {
                    String jumlahStr = JOptionPane.showInputDialog(
                            this,
                            "Masukkan jumlah beli untuk " + nama,
                            "Input Jumlah",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (jumlahStr != null && !jumlahStr.isEmpty()) {
                        try {
                            int jumlah = Integer.parseInt(jumlahStr);
                            if (jumlah > 0 && jumlah <= stok) {
                                double total = jumlah * harga;

                                DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
                                model.addRow(new Object[]{sku, nama, harga, jumlah, total});
                                // Hitung ulang semua total
                                double grandTotal = 0;
                                for (int i = 0; i < model.getRowCount(); i++) {
                                    grandTotal += (double) model.getValueAt(i, 4);
                                }

                                txtTotal.setText(String.valueOf((int) grandTotal));

                                kurangiStok(kodeBarang, jumlah);
                                loadMenu(keyword);
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Jumlah tidak valid atau stok tidak cukup!");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Input harus angka!");
                        }
                    }
                });

                itemPanel.add(lblGambar);
                itemPanel.add(Box.createVerticalStrut(5));
                itemPanel.add(lblNama);
                itemPanel.add(lblSKU);
                itemPanel.add(lblHarga);
                itemPanel.add(lblStok);
                itemPanel.add(Box.createVerticalStrut(5));
                itemPanel.add(btnBeli);

                pnlMenu.add(itemPanel);
            }
        }
    int rows = (int) Math.ceil(count / 3.0); // 3 kolom
        int height = rows * 270; // tinggi per item (250 + margin)
        pnlMenu.setPreferredSize(new Dimension(662, height));

        pnlMenu.revalidate();
        pnlMenu.repaint();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal load menu: " + e.getMessage());
    }
}


private void kurangiStok(int kodeBarang, int jumlah) {
    String sql = "UPDATE Barang SET stok = stok - ? WHERE kodeBarang = ?";

    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, jumlah);
        ps.setInt(2, kodeBarang);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal update stok: " + e.getMessage());
    }
}
private int getStokBySKU(String sku) {
    String sql = "SELECT stok FROM Barang WHERE SKUBarang = ?";
    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("stok");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

private void updateStokBySKU(String sku, int jumlah) {
    String sql = "UPDATE Barang SET stok = stok + ? WHERE SKUBarang = ?";
    try (Connection conn = koneksi.dbKonek();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, jumlah);
        ps.setString(2, sku);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal update stok: " + e.getMessage());
    }
}
private void showPopupPembayaran() {
    // Ambil parent dari JPanel
    Window parentWindow = SwingUtilities.getWindowAncestor(this);

    // === POPUP DIALOG ===
    JDialog dialog = new JDialog(parentWindow, "Pembayaran", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setSize(700, 700);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout(10, 10));

   // === Panel Atas: Ringkasan Barang ===
DefaultTableModel modelKasir = (DefaultTableModel) tblKasir.getModel();
JTable tblRingkasan = new JTable(modelKasir);

// Batasi tinggi tabel (misal 150px)
JScrollPane scrollRingkasan = new JScrollPane(tblRingkasan);
scrollRingkasan.setPreferredSize(new Dimension(580, 150));
scrollRingkasan.setBorder(BorderFactory.createTitledBorder("Barang yang dibeli"));

    // === Panel Tengah: Form Input ===
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBorder(BorderFactory.createTitledBorder("Detail Pembayaran"));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Buat semua textfield
    JTextField txtSubtotal   = new JTextField(txtTotal.getText(), 15);
    JTextField txtDiskon     = new JTextField("0", 15);
    JTextField txtGrandTotal = new JTextField(txtTotal.getText(), 15);
    JTextField txtTunai      = new JTextField(txtTotal.getText(), 15);
    JTextField txtKredit     = new JTextField("0", 15);
    JTextField txtKembalian  = new JTextField("0", 15);
    JTextArea  txtKeterangan = new JTextArea(3, 20);

    txtSubtotal.setEditable(false);
    txtGrandTotal.setEditable(false);
    txtKembalian.setEditable(false);

    // Tambahkan ke panel dengan GridBagLayout
    int row = 0;
    formPanelAdd(formPanel, gbc, row++, "Subtotal:", txtSubtotal);
    formPanelAdd(formPanel, gbc, row++, "Diskon:", txtDiskon);
    formPanelAdd(formPanel, gbc, row++, "Grand Total:", txtGrandTotal);
    formPanelAdd(formPanel, gbc, row++, "Tunai:", txtTunai);
    formPanelAdd(formPanel, gbc, row++, "Kredit:", txtKredit);
    formPanelAdd(formPanel, gbc, row++, "Kembalian:", txtKembalian);

    gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
    formPanel.add(new JLabel("Keterangan:"), gbc);
    gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.BOTH;
    formPanel.add(new JScrollPane(txtKeterangan), gbc);

    // === Panel Bawah: Tombol ===
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSelesai = new JButton("Selesai");
    JButton btnKembali = new JButton("Kembali");
    buttonPanel.add(btnKembali);
    buttonPanel.add(btnSelesai);

    // === Event Listener Perhitungan ===
    txtDiskon.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateGrandTotal(txtSubtotal, txtDiskon, txtGrandTotal)
    ));

    txtTunai.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateKembalian(txtGrandTotal, txtTunai, txtKredit, txtKembalian)
    ));

    txtKredit.getDocument().addDocumentListener(new SimpleDocumentListener(() ->
        updateKembalian(txtGrandTotal, txtTunai, txtKredit, txtKembalian)
    ));

    // === Event Tombol ===
    btnKembali.addActionListener(ev -> dialog.dispose());
btnSelesai.addActionListener(ev -> {
    try (Connection conn = koneksi.dbKonek()) {
        conn.setAutoCommit(false); // biar atomic semua insert

        // === 1. Insert Transaksi ===
        String sqlTransaksi = "INSERT INTO Transaksi (noTransaksi, namaPengguna, tgl_transaksi, subtotal, diskon, grand_total, metodePembayaran) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idTransaksi";
        PreparedStatement pstTrans = conn.prepareStatement(sqlTransaksi);
        pstTrans.setString(1, txtNo.getText());
        pstTrans.setString(2, txtPengguna.getText());
        pstTrans.setTimestamp(3, new java.sql.Timestamp(jdcTanggal.getDate().getTime()));
        pstTrans.setBigDecimal(4, new java.math.BigDecimal(txtSubtotal.getText()));
        pstTrans.setBigDecimal(5, new java.math.BigDecimal(txtDiskon.getText()));
        pstTrans.setBigDecimal(6, new java.math.BigDecimal(txtGrandTotal.getText()));

        String metodePembayaran = Double.parseDouble(txtKredit.getText()) > 0 ? "Kredit" : "Tunai";
        pstTrans.setString(7, metodePembayaran);

        ResultSet rs = pstTrans.executeQuery();
        int idTransaksi = 0;
        if (rs.next()) {
            idTransaksi = rs.getInt("idTransaksi");
        }
        rs.close();
        pstTrans.close();

        // === 2. Insert DetailTransaksi ===
        DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();
        String sqlDetail = "INSERT INTO DetailTransaksi (kodeBarang, idTransaksi, namaBarang, jumlah, harga, keterangan, subtotal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

        for (int i = 0; i < model.getRowCount(); i++) {
            String sku = model.getValueAt(i, 0).toString();
            String namaBarang = model.getValueAt(i, 1).toString();
            double harga = Double.parseDouble(model.getValueAt(i, 2).toString());
            int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString());
            double total = Double.parseDouble(model.getValueAt(i, 4).toString());

            // Ambil kodeBarang dari tabel Barang berdasarkan SKU
            int kodeBarang = 0;
            PreparedStatement pstCek = conn.prepareStatement("SELECT kodeBarang FROM Barang WHERE SKUBarang = ?");
            pstCek.setString(1, sku);
            ResultSet rsCek = pstCek.executeQuery();
            if (rsCek.next()) {
                kodeBarang = rsCek.getInt("kodeBarang");
            }
            rsCek.close();
            pstCek.close();

            pstDetail.setInt(1, kodeBarang);
            pstDetail.setInt(2, idTransaksi);
            pstDetail.setString(3, namaBarang);
            pstDetail.setInt(4, jumlah);
            pstDetail.setBigDecimal(5, new java.math.BigDecimal(harga));
            pstDetail.setString(6, txtKeterangan.getText());
            pstDetail.setBigDecimal(7, new java.math.BigDecimal(total));
            pstDetail.addBatch();
        }
        pstDetail.executeBatch();
        pstDetail.close();

        // === 3. Insert Keuangan ===
        String sqlKeu = "INSERT INTO Keuangan (idAsal, jenisKeuangan, masuk, keluar, tanggal) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstKeu = conn.prepareStatement(sqlKeu);
        pstKeu.setInt(1, idTransaksi);
        pstKeu.setString(2, "Penjualan Barang");
        pstKeu.setBigDecimal(3, new java.math.BigDecimal(txtGrandTotal.getText()));
        pstKeu.setBigDecimal(4, new java.math.BigDecimal("0"));
        pstKeu.setDate(5, new java.sql.Date(jdcTanggal.getDate().getTime()));
        pstKeu.executeUpdate();
        pstKeu.close();

        conn.commit(); // simpan transaksi

        JOptionPane.showMessageDialog(dialog, "Transaksi berhasil disimpan!");
        dialog.dispose();

        // Kosongkan keranjang
        ((DefaultTableModel) tblKasir.getModel()).setRowCount(0);
        txtTotal.setText("0");
txtNo.setText(generateNoTransaksi(conn));

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(dialog, "Gagal simpan transaksi: " + ex.getMessage());
    }
});


    // === Susun Layout ===
    dialog.add(scrollRingkasan, BorderLayout.NORTH);
    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setVisible(true);
}

// Helper untuk tambah field
private void formPanelAdd(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
    gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
    panel.add(new JLabel(label), gbc);

    gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.7;
    panel.add(field, gbc);
}

// Listener sederhana
class SimpleDocumentListener implements javax.swing.event.DocumentListener {
    private Runnable callback;
    public SimpleDocumentListener(Runnable callback) {
        this.callback = callback;
    }
    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
}

// Helper parsing aman
private double parseDoubleSafe(String text) {
    try {
        return Double.parseDouble(text);
    } catch (Exception e) {
        return 0;
    }
}

// Perhitungan
private void updateGrandTotal(JTextField txtSubtotal, JTextField txtDiskon, JTextField txtGrandTotal) {
    double subtotal = parseDoubleSafe(txtSubtotal.getText());
    double diskon   = parseDoubleSafe(txtDiskon.getText());
    txtGrandTotal.setText(String.valueOf(subtotal - diskon));
}

private void updateKembalian(JTextField txtGrandTotal, JTextField txtTunai, JTextField txtKredit, JTextField txtKembalian) {
    double grand   = parseDoubleSafe(txtGrandTotal.getText());
    double tunai   = parseDoubleSafe(txtTunai.getText());
    double kredit  = parseDoubleSafe(txtKredit.getText());
    txtKembalian.setText(String.valueOf((tunai + kredit) - grand));
}
private String generateNoTransaksi(Connection conn) throws SQLException {
    java.util.Date now = new java.util.Date();
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMdd"); 
    String hariBulan = sdf.format(now); // contoh: 0921

    // Ambil transaksi terakhir hari ini
    String sql = "SELECT noTransaksi FROM Transaksi " +
                 "WHERE to_char(tgl_transaksi, 'MMdd') = ? " +
                 "ORDER BY idTransaksi DESC LIMIT 1";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, hariBulan);

    ResultSet rs = pst.executeQuery();
    int urutan = 1;
    if (rs.next()) {
        String lastNo = rs.getString("noTransaksi"); 
        System.out.println("Last NoTransaksi = " + lastNo);

        // Ambil angka di belakang kode tanggal
        String angka = lastNo.replaceAll("\\D+", ""); // hanya ambil angka
        if (angka.length() > 4) { // setelah "0921"
            urutan = Integer.parseInt(angka.substring(4)) + 1;
        }
    }
    rs.close();
    pst.close();

    return "T" + hariBulan + urutan; 
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNo = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        jdcTanggal = new com.toedter.calendar.JDateChooser();
        btnDelete = new javax.swing.JButton();
        txtSku = new javax.swing.JTextField();
        txtPengguna = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKasir = new javax.swing.JTable();
        btnPembayaran = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlMenu = new javax.swing.JPanel();
        txtSku1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 94, 281, 51));

        txtTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 200, 301, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("No transaksi");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 57, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("CARI");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 70, -1, -1));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel1.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 312, 97, 50));
        jPanel1.add(jdcTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 312, 281, 50));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 312, 90, 50));

        txtSku.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtSku, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 94, 301, 51));

        txtPengguna.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtPengguna, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 200, 281, 51));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tanggal");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 269, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nama Pengguna");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 163, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("SKU barang");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 57, -1, -1));

        btnBatal.setBackground(new java.awt.Color(153, 153, 153));
        btnBatal.setText("BATAL");
        jPanel1.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 760, 150, 60));

        tblKasir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KodeSKU", "Nama", "Harga", "Jumlah", "Total"
            }
        ));
        tblKasir.setRowHeight(30);
        jScrollPane1.setViewportView(tblKasir);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 930, 310));

        btnPembayaran.setBackground(new java.awt.Color(0, 204, 102));
        btnPembayaran.setText("PEMBAYARAN");
        btnPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPembayaranActionPerformed(evt);
            }
        });
        jPanel1.add(btnPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 760, 240, 60));

        pnlMenu.setBackground(new java.awt.Color(255, 255, 255));
        pnlMenu.setPreferredSize(new java.awt.Dimension(662, 858));
        jScrollPane2.setViewportView(pnlMenu);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 120, -1, 704));

        txtSku1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSku1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSku1KeyReleased(evt);
            }
        });
        jPanel1.add(txtSku1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1161, 60, 500, 51));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("TOTAL");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 163, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
int row = tblKasir.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih dulu item yang mau dihapus!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

    String sku = model.getValueAt(row, 0).toString();
    int jumlah = (int) model.getValueAt(row, 3);

    // Kembalikan stok ke DB
    updateStokBySKU(sku, jumlah);

    // Hapus baris dari JTable
    model.removeRow(row);

    // Refresh menu
    loadMenu(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    int row = tblKasir.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih dulu item yang mau diedit!");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblKasir.getModel();

    String sku = model.getValueAt(row, 0).toString();
    String nama = model.getValueAt(row, 1).toString();
    double harga = (double) model.getValueAt(row, 2);
    int jumlahLama = (int) model.getValueAt(row, 3);

    // Pop-up input jumlah baru
    String jumlahStr = JOptionPane.showInputDialog(
        this, 
        "Edit jumlah untuk " + nama, 
        jumlahLama
    );

    if (jumlahStr != null && !jumlahStr.isEmpty()) {
        try {
            int jumlahBaru = Integer.parseInt(jumlahStr);

            // Cari stok di DB
            int stokDb = getStokBySKU(sku);

            // Hitung perubahan stok
            int selisih = jumlahBaru - jumlahLama;

            if (jumlahBaru > 0 && selisih <= stokDb) {
                double totalBaru = jumlahBaru * harga;

                // Update JTable
                model.setValueAt(jumlahBaru, row, 3);
                model.setValueAt(totalBaru, row, 4);

                // Update stok di DB
                updateStokBySKU(sku, -selisih);

                // Refresh menu
                loadMenu(null);
            } else {
                JOptionPane.showMessageDialog(this, "Jumlah tidak valid atau stok tidak cukup!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input harus angka!");
        }
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed
private javax.swing.Timer searchTimer;

    private void txtSku1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSku1KeyReleased
    if (searchTimer != null && searchTimer.isRunning()) {
        searchTimer.stop();
    }

    searchTimer = new javax.swing.Timer(300, e -> {
        String keyword = txtSku1.getText().trim();
        loadMenu(keyword);
    });
    searchTimer.setRepeats(false); // cuma sekali jalan
    searchTimer.start();

    }//GEN-LAST:event_txtSku1KeyReleased

    private void btnPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPembayaranActionPerformed
showPopupPembayaran();        // TODO add your handling code here:
    }//GEN-LAST:event_btnPembayaranActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnPembayaran;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcTanggal;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JTable tblKasir;
    private javax.swing.JTextField txtNo;
    private javax.swing.JTextField txtPengguna;
    private javax.swing.JTextField txtSku;
    private javax.swing.JTextField txtSku1;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
