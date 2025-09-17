package kasir;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;

public class LineChart extends JPanel {

    private java.util.List<DataPoint> dataList = new ArrayList<>();
    private java.util.List<PointData> pointList = new ArrayList<>(); // untuk simpan koordinat titik

    public LineChart() {
        loadData();
setBackground(Color.WHITE); // di constructor LineChart()

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (PointData pd : pointList) {
                    if (pd.contains(e.getPoint())) {
                        JOptionPane.showMessageDialog(LineChart.this,
                                "Tanggal: " + pd.data.tanggal +
                                "\nPemasukan: " + pd.data.masuk +
                                "\nPengeluaran: " + pd.data.keluar,
                                "Detail Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        });
    }

    // ambil data dari database
    private void loadData() {
        String sql = "SELECT tanggal, SUM(masuk) as total_masuk, SUM(keluar) as total_keluar "
                   + "FROM keuangan GROUP BY tanggal ORDER BY tanggal ASC";

        try (Connection conn = koneksi.dbKonek();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String tgl = rs.getString("tanggal");
                double masuk = rs.getDouble("total_masuk");
                double keluar = rs.getDouble("total_keluar");

                dataList.add(new DataPoint(tgl, masuk, keluar));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (dataList.isEmpty()) return;

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w = getWidth();
    int h = getHeight();

    // Background gradien
GradientPaint gp = new GradientPaint(
    0, 0, Color.white,   // biru muda (lightblue)
    0, h, new Color(135, 206, 250)        // biru tua (darkblue)
);

    g2.setPaint(gp);
    g2.fillRect(0, 0, w, h);

    int padding = 50;
    int labelPadding = 40;


        // cari min & max dari masuk/keluar
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (DataPoint dp : dataList) {
            minValue = Math.min(minValue, Math.min(dp.masuk, dp.keluar));
            maxValue = Math.max(maxValue, Math.max(dp.masuk, dp.keluar));
        }

        if (maxValue == minValue) maxValue += 10;

        // gambar sumbu
        g2.drawLine(padding, h - padding, w - padding, h - padding); // X
        g2.drawLine(padding, h - padding, padding, padding);         // Y

        // skala Y
        int numberYDivisions = 10;
        for (int i = 0; i <= numberYDivisions; i++) {
            int y = h - padding - (i * (h - 2 * padding) / numberYDivisions);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(padding + 1, y, w - padding, y);
            g2.setColor(Color.BLACK);
            double value = minValue + (i * (maxValue - minValue) / numberYDivisions);
            g2.drawString(String.format("%.0f", value), 10, y + 5);
        }

        // titik koordinat
        double xScale = ((double) (w - 2 * padding - labelPadding)) / (dataList.size() - 1);
        double yScale = ((double) (h - 2 * padding)) / (maxValue - minValue);

        java.util.List<Point> pemasukanPoints = new ArrayList<>();
        java.util.List<Point> pengeluaranPoints = new ArrayList<>();
        pointList.clear();

        for (int i = 0; i < dataList.size(); i++) {
            int x = (int) (i * xScale + padding + labelPadding);

            int yMasuk = (int) ((maxValue - dataList.get(i).masuk) * yScale + padding);
            int yKeluar = (int) ((maxValue - dataList.get(i).keluar) * yScale + padding);

            pemasukanPoints.add(new Point(x, yMasuk));
            pengeluaranPoints.add(new Point(x, yKeluar));

            // simpan koordinat titik untuk interaksi mouse
            pointList.add(new PointData(new Point(x, yMasuk), dataList.get(i)));
            pointList.add(new PointData(new Point(x, yKeluar), dataList.get(i)));
        }

        // gambar garis pemasukan (biru)
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < pemasukanPoints.size() - 1; i++) {
            g2.drawLine(pemasukanPoints.get(i).x, pemasukanPoints.get(i).y,
                        pemasukanPoints.get(i+1).x, pemasukanPoints.get(i+1).y);
        }

        // gambar garis pengeluaran (merah)
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < pengeluaranPoints.size() - 1; i++) {
            g2.drawLine(pengeluaranPoints.get(i).x, pengeluaranPoints.get(i).y,
                        pengeluaranPoints.get(i+1).x, pengeluaranPoints.get(i+1).y);
        }

        // titik pemasukan (biru)
g2.setColor(new Color(0, 100, 0));  // Dark Green
        for (Point p : pemasukanPoints) {
            g2.fillOval(p.x - 3, p.y - 3, 6, 6);
        }

        // titik pengeluaran (merah)
        g2.setColor(Color.RED);
        for (Point p : pengeluaranPoints) {
            g2.fillOval(p.x - 3, p.y - 3, 6, 6);
        }

        // label tanggal di sumbu X
        g2.setColor(Color.BLACK);
        for (int i = 0; i < dataList.size(); i++) {
            int x = (int) (i * xScale + padding + labelPadding);
            int y = h - padding + 20;
            g2.drawString(dataList.get(i).tanggal, x - 20, y);
        }
    }

    // inner class untuk data
    private static class DataPoint {
        String tanggal;
        double masuk;
        double keluar;

        public DataPoint(String tanggal, double masuk, double keluar) {
            this.tanggal = tanggal;
            this.masuk = masuk;
            this.keluar = keluar;
        }
    }

    // inner class untuk simpan titik + data
    private static class PointData {
        Point point;
        DataPoint data;

        public PointData(Point point, DataPoint data) {
            this.point = point;
            this.data = data;
        }

        // cek apakah klik dekat dengan titik (radius 5px)
        public boolean contains(Point p) {
            return p.distance(point) < 6;
        }
    }
}
