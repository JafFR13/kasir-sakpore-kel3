/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.sql.*;
import java.text.NumberFormat;
/**
 *
 * @author yaniyan
 */
public class Admin extends javax.swing.JPanel {

    /**
     * Creates new form Admin
     */

   public Admin() {
    initComponents();

    panelChart.setLayout(new BorderLayout());
     java.time.LocalDate today = java.time.LocalDate.now();
    String startDate = today.withDayOfMonth(1).toString();
    String endDate = today.toString(); 
    loadChart(startDate, endDate);
    
warna();
  try {
            Connection conn = koneksi.dbKonek();
            updateLabels(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        
   
}
   
   private void warna(){
   applyGradient(panelPemasukkan, new Color(204, 255, 204), new Color(102, 204, 102), false);

// warna merah
applyGradient(panelPengeluaran, new Color(255, 204, 204), new Color(255, 102, 102), false);
applyGradient(pnlStok, new Color(255, 204, 204), new Color(255, 102, 102), false);

//warna orange
applyGradient(panelKeuntungan, new Color(255, 229, 204), new Color(255, 178, 102), false);
applyGradient(pnlBarang, new Color(255, 229, 204), new Color(255, 178, 102), false);

// warna ungu
applyGradient(pnlTransaksi, new Color(229, 204, 255), new Color(153, 102, 255), false);

// atas: biru muda, bawah: biru segar
applyGradient(panelKeuntunganTotal, new Color(204, 229, 255), new Color(102, 153, 255), false);
applyGradient(pnlMen, new Color(204, 229, 255), new Color(102, 153, 255), false);
   }
    private void updateLabels(Connection conn) {
        try {
            NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            // Total pemasukan hari ini
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT SUM(masuk) FROM Keuangan WHERE tanggal = CURRENT_DATE"
            );
            double totalMasuk = rs.next() ? rs.getDouble(1) : 0;
            lblMasuk.setText(rupiah.format(totalMasuk));
            rs.close();

            // Total pengeluaran hari ini
            rs = conn.createStatement().executeQuery(
                "SELECT SUM(keluar) FROM Keuangan WHERE tanggal = CURRENT_DATE"
            );
            double totalKeluar = rs.next() ? rs.getDouble(1) : 0;
            lblKeluar.setText(rupiah.format(totalKeluar));
            rs.close();

            // Keuntungan hari ini
            lblKeuntungan.setText(rupiah.format(totalMasuk - totalKeluar));

            // Keuntungan total (semua data)
            rs = conn.createStatement().executeQuery(
                "SELECT SUM(masuk) - SUM(keluar) FROM Keuangan"
            );
            double totalKeuntungan = rs.next() ? rs.getDouble(1) : 0;
            lblKeuntunganTotal.setText(rupiah.format(totalKeuntungan));
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void loadChart(String startDate, String endDate) {
    LineChart chart = new LineChart(startDate, endDate);

    JScrollPane scrollPane = new JScrollPane(chart,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    panelChart.removeAll();
    panelChart.add(scrollPane, BorderLayout.CENTER);
    panelChart.revalidate();
    panelChart.repaint();
}

/**
     * @param panel panel yang mau diwarnai
     * @param c1 warna awal
     * @param c2 warna akhir
     * @param horizontal true = kiri→kanan, false = atas→bawah
     */
    private void applyGradient(JPanel panel, Color c1, Color c2, boolean horizontal) {
        panel.setOpaque(false);
        panel.setUI(new javax.swing.plaf.PanelUI() {
            @Override
            public void update(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                int w = c.getWidth();
                int h = c.getHeight();

                GradientPaint gp;
                if (horizontal) {
                    gp = new GradientPaint(0, 0, c1, w, 0, c2); // kiri → kanan
                } else {
                    gp = new GradientPaint(0, 0, c1, 0, h, c2); // atas → bawah
                }

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                super.update(g, c);
            }
        });
    }

     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panelChart = new javax.swing.JPanel();
        jdcStart = new com.toedter.calendar.JDateChooser();
        jdcEnd = new com.toedter.calendar.JDateChooser();
        btnFilter = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        merah = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        merah1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panelKeuntunganTotal = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblKeuntunganTotal = new javax.swing.JLabel();
        panelPemasukkan = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblMasuk = new javax.swing.JLabel();
        panelPengeluaran = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblKeluar = new javax.swing.JLabel();
        panelKeuntungan = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblKeuntungan = new javax.swing.JLabel();
        pnlMen = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JLabel();
        pnlTransaksi = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblPemasukan4 = new javax.swing.JLabel();
        pnlStok = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblPemasukan6 = new javax.swing.JLabel();
        pnlBarang = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblPemasukan7 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(20000, 20000));
        setPreferredSize(new java.awt.Dimension(1740, 960));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1740, 960));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data pemasukkan dan pengeluran"));

        panelChart.setBackground(new java.awt.Color(255, 255, 255));
        panelChart.setLayout(new java.awt.BorderLayout());

        btnFilter.setBackground(new java.awt.Color(0, 153, 102));
        btnFilter.setText("FIlter");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(51, 102, 255));
        btnReset.setText("reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        merah.setBackground(new java.awt.Color(255, 51, 51));

        jLabel1.setText("Pengeluararn");

        jLabel2.setText("Pemasukkan");

        merah1.setBackground(new java.awt.Color(0, 153, 51));

        jLabel3.setText("Sampai");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(merah, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(merah1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jdcStart, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdcEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(782, 782, 782))
            .addComponent(panelChart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(merah, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(merah1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jdcStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addComponent(panelChart, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 1690, 460));

        panelKeuntunganTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Total Keuntungan");
        panelKeuntunganTotal.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeuntunganTotal.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeuntunganTotal.setText("Rp 400.000,00");
        panelKeuntunganTotal.add(lblKeuntunganTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelKeuntunganTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 610, 160));

        panelPemasukkan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("Pemasukkan Hari Ini");
        panelPemasukkan.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblMasuk.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblMasuk.setText("Rp 200.000,00");
        panelPemasukkan.add(lblMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelPemasukkan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 610, 160));

        panelPengeluaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Pengeluaran Hari ini");
        panelPengeluaran.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeluar.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeluar.setText("Rp 160.000,00");
        panelPengeluaran.add(lblKeluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 610, 160));

        panelKeuntungan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Keuntungan Hari ini");
        panelKeuntungan.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblKeuntungan.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblKeuntungan.setText("Rp 80.000,00");
        panelKeuntungan.add(lblKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 540, 90));

        jPanel2.add(panelKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 610, 160));

        pnlMen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("Total Menu tersedia");
        pnlMen.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        pnlMenu.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        pnlMenu.setText("100");
        pnlMen.add(pnlMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 100, 90));

        jPanel2.add(pnlMen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 190, 180, 160));

        pnlTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("Total Transaksi Hari ini");
        pnlTransaksi.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblPemasukan4.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblPemasukan4.setText("99");
        pnlTransaksi.add(lblPemasukan4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 100, 90));

        jPanel2.add(pnlTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 10, 180, 160));

        pnlStok.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Jumlah Stok < 5");
        pnlStok.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblPemasukan6.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblPemasukan6.setText("10");
        pnlStok.add(lblPemasukan6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 100, 90));

        jPanel2.add(pnlStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 10, 180, 160));

        pnlBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("Barang Terjual hari ini");
        pnlBarang.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

        lblPemasukan7.setFont(new java.awt.Font("Segoe UI", 0, 60)); // NOI18N
        lblPemasukan7.setText("40");
        pnlBarang.add(lblPemasukan7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 100, 90));

        jPanel2.add(pnlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 190, 180, 160));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1740, 960));
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
 java.util.Date start = jdcStart.getDate();
    java.util.Date end = jdcEnd.getDate();

    if (start != null && end != null) {
        // Cek jika tanggal awal lebih besar dari tanggal akhir
        if (start.after(end)) {
            JOptionPane.showMessageDialog(
                null,
                "Tanggal awal tidak boleh lebih besar dari tanggal akhir!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // hentikan proses
        }

        String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(start);
        String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(end);

        loadChart(startDate, endDate);
    } else {
        JOptionPane.showMessageDialog(
            null, 
            "Pilih tanggal awal dan akhir dulu!"
        );
    }     // TODO add your handling code here:
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
    // ambil tanggal awal bulan
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
    String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

    // ambil tanggal hari ini
    String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    // load ulang chart
    loadChart(startDate, endDate);        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnReset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private com.toedter.calendar.JDateChooser jdcEnd;
    private com.toedter.calendar.JDateChooser jdcStart;
    private javax.swing.JLabel lblKeluar;
    private javax.swing.JLabel lblKeuntungan;
    private javax.swing.JLabel lblKeuntunganTotal;
    private javax.swing.JLabel lblMasuk;
    private javax.swing.JLabel lblPemasukan4;
    private javax.swing.JLabel lblPemasukan6;
    private javax.swing.JLabel lblPemasukan7;
    private javax.swing.JPanel merah;
    private javax.swing.JPanel merah1;
    private javax.swing.JPanel panelChart;
    private javax.swing.JPanel panelKeuntungan;
    private javax.swing.JPanel panelKeuntunganTotal;
    private javax.swing.JPanel panelPemasukkan;
    private javax.swing.JPanel panelPengeluaran;
    private javax.swing.JPanel pnlBarang;
    private javax.swing.JPanel pnlMen;
    private javax.swing.JLabel pnlMenu;
    private javax.swing.JPanel pnlStok;
    private javax.swing.JPanel pnlTransaksi;
    // End of variables declaration//GEN-END:variables
}
