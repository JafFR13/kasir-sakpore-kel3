/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasir;
import java.sql.*;
import javax.swing.*;


public class koneksi {
    private static Connection conn;
    public static Connection dbKonek() throws SQLException{
        if(conn == null){
            try{
        String db = "jdbc:mysql://localhost:3306/kasir";
        String user = "root";
        String pass = "";
        conn = (Connection)DriverManager.getConnection(db,user,pass);
    }catch(Exception e){
         JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
                e.printStackTrace();
    }
        }return conn;
    }}