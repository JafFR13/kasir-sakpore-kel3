/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasir;

/**
 *
 * @author yaniyan
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineChartPanel extends JPanel {
    private List<Integer> dataMasuk = new ArrayList<>();
    private List<Integer> dataKeluar = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private List<Point> pointsMasuk = new ArrayList<>();
    private List<Point> pointsKeluar = new ArrayList<>();
    private JLabel infoLabel;

    private String filter = "bulan"; // default filter

    public LineChartPanel(String filter) {
        this.filter = filter;
        setPreferredSize(new Dimension(1000, 300));
        setBackground(Color.WHITE);

        infoLabel = new JLabel("Klik titik untuk detail");
        this.add(infoLabel);

        loadDataFromDatabase();

        // Event klik titik
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < labels.size(); i++) {
                    if (i < pointsMasuk.size()) {
                        Point pMasuk = pointsMasuk.get(i);
                        if (pMasuk.distance(e.getPoint()) < 7) {
                            String msg = "Tanggal: " + labels.get(i) +
                                         "\nPemasukan: Rp " + dataMasuk.get(i);
                            infoLabel.setText("Tanggal: " + labels.get(i) +
                                              " | Pemasukan: Rp " + dataMasuk.get(i));
                            JOptionPane.showMessageDialog(LineChartPanel.this, msg,
                                    "Detail Pemasukan", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    if (i < pointsKeluar.size()) {
                        Point pKeluar = pointsKeluar.get(i);
                        if (pKeluar.distance(e.getPoint()) < 7) {
                            String msg = "Tanggal: " + labels.get(i) +
                                         "\nPengeluaran: Rp " + dataKeluar.get(i);
                            infoLabel.setText("Tanggal: " + labels.get(i) +
                                              " | Pengeluaran: Rp " + dataKeluar.get(i));
                            JOptionPane.showMessageDialog(LineChartPanel.this, msg,
                                    "Detail Pengeluaran", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        labels.clear();
        dataMasuk.clear();
        dataKeluar.clear();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/db_resto", "postgres", "1234")) {

            String sql = "";

            if (filter.equals("hari")) {
                sql = "SELECT tanggal, SUM(masuk) AS pemasukan, SUM(keluar) AS pengeluaran " +
                      "FROM Keuangan WHERE tanggal = CURRENT_DATE GROUP BY tanggal ORDER BY tanggal";
            } else if (filter.equals("minggu")) {
                sql = "SELECT tanggal, SUM(masuk) AS pemasukan, SUM(keluar) AS pengeluaran " +
                      "FROM Keuangan WHERE tanggal >= date_trunc('week', CURRENT_DATE) " +
                      "AND tanggal <= CURRENT_DATE GROUP BY tanggal ORDER BY tanggal";
            } else { // bulan
                sql = "SELECT tanggal, SUM(masuk) AS pemasukan, SUM(keluar) AS pengeluaran " +
                      "FROM Keuangan WHERE tanggal >= date_trunc('month', CURRENT_DATE) " +
                      "AND tanggal <= CURRENT_DATE GROUP BY tanggal ORDER BY tanggal";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                labels.add(rs.getDate("tanggal").toString());
                dataMasuk.add(rs.getInt("pemasukan"));
                dataKeluar.add(rs.getInt("pengeluaran"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dataMasuk.isEmpty() && dataKeluar.isEmpty()) {
            g.drawString("Tidak ada data", getWidth() / 2 - 30, getHeight() / 2);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int margin = 60;

        // Axis
        g.setColor(Color.BLACK);
        g.drawLine(margin, height - margin, width - margin, height - margin); // X
        g.drawLine(margin, margin, margin, height - margin); // Y

        // Cari max value
        int maxData = 1;
        if (!dataMasuk.isEmpty()) maxData = Math.max(maxData, dataMasuk.stream().max(Integer::compare).orElse(1));
        if (!dataKeluar.isEmpty()) maxData = Math.max(maxData, dataKeluar.stream().max(Integer::compare).orElse(1));

        int[] yScales = {20000, 50000, 100000, 200000, 500000, 1000000};
        int maxY = yScales[yScales.length - 1];
        for (int s : yScales) {
            if (s >= maxData) {
                maxY = s;
                break;
            }
        }

        int xStep = labels.size() > 1 ? (width - 2 * margin) / (labels.size() - 1) : 1;

        pointsMasuk.clear();
        pointsKeluar.clear();

        // Grid Y + label
        for (int y : yScales) {
            if (y > maxY) break;
            int yPos = height - margin - (y * (height - 2 * margin) / maxY);

            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(margin, yPos, width - margin, yPos);

            g.setColor(Color.BLACK);
            g.drawString("Rp " + y, 5, yPos + 5);
        }

        // Gambar garis pemasukan
        g.setColor(Color.GREEN);
        for (int i = 0; i < dataMasuk.size() - 1; i++) {
            int x1 = margin + i * xStep;
            int y1 = height - margin - (dataMasuk.get(i) * (height - 2 * margin) / maxY);
            int x2 = margin + (i + 1) * xStep;
            int y2 = height - margin - (dataMasuk.get(i + 1) * (height - 2 * margin) / maxY);

            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x1 - 4, y1 - 4, 8, 8);
            pointsMasuk.add(new Point(x1, y1));

            g.setColor(Color.BLACK);
            g.drawString(labels.get(i), x1 - 20, height - margin + 20);
            g.setColor(Color.GREEN);
        }
        if (!dataMasuk.isEmpty()) {
            int lastX = margin + (dataMasuk.size() - 1) * xStep;
            int lastY = height - margin - (dataMasuk.get(dataMasuk.size() - 1) * (height - 2 * margin) / maxY);
            g.fillOval(lastX - 4, lastY - 4, 8, 8);
            pointsMasuk.add(new Point(lastX, lastY));
            g.setColor(Color.BLACK);
            g.drawString(labels.get(dataMasuk.size() - 1), lastX - 20, height - margin + 20);
        }

        // Gambar garis pengeluaran
        g.setColor(Color.RED);
        for (int i = 0; i < dataKeluar.size() - 1; i++) {
            int x1 = margin + i * xStep;
            int y1 = height - margin - (dataKeluar.get(i) * (height - 2 * margin) / maxY);
            int x2 = margin + (i + 1) * xStep;
            int y2 = height - margin - (dataKeluar.get(i + 1) * (height - 2 * margin) / maxY);

            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x1 - 4, y1 - 4, 8, 8);
            pointsKeluar.add(new Point(x1, y1));

            g.setColor(Color.RED);
        }
        if (!dataKeluar.isEmpty()) {
            int lastX = margin + (dataKeluar.size() - 1) * xStep;
            int lastY = height - margin - (dataKeluar.get(dataKeluar.size() - 1) * (height - 2 * margin) / maxY);
            g.fillOval(lastX - 4, lastY - 4, 8, 8);
            pointsKeluar.add(new Point(lastX, lastY));
        }
    }
}
