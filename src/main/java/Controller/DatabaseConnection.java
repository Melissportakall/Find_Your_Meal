package Controller;

import Model.Tarif;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ekmek_sef";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Veritabanına bağlantı kurmak için metot
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Tarif tablosundaki tarifID alanını al
    public static int getTarifID_DBC(int id) {
        String query = "SELECT tarifID FROM tarifler WHERE tarifID = " + id;
        return getIntValue(query);
    }

    // Tarif tablosundaki tarifAdi alanını al
    public static String getTarifAdi_DBC(int id) {
        String query = "SELECT tarifAdi FROM tarifler WHERE tarifID = " + id;
        return getStringValue(query);
    }

    // Tarif tablosundaki tarifKategori alanını al
    public static String getTarifKategori_DBC(int id) {
        String query = "SELECT tarifKategori FROM tarifler WHERE tarifID = " + id;
        return getStringValue(query);
    }

    // Tarif tablosundaki tarifSure alanını al
    public static int getTarifSure_DBC(int id) {
        String query = "SELECT tarifSure FROM tarifler WHERE tarifID = " + id;
        return getIntValue(query);
    }

    // Tarif tablosundaki tarifTalimat alanını al
    public static String getTarifTalimat_DBC(int id) {
        String query = "SELECT tarifTalimat FROM tarifler WHERE tarifID = " + id;
        return getStringValue(query);
    }

    // Yardımcı metotlar: String ve int değerlerini veritabanından almak için genel metotlar
    private static String getStringValue(String query) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String value = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getString(1); // Sütun 1'deki değeri alıyoruz
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    private static int getIntValue(String query) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int value = 0;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getInt(1); // Sütun 1'deki değeri alıyoruz
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public static void getTarifler() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(); // Bağlantıyı alıyoruz
            statement = connection.createStatement();

            // SQL sorgusu
            String sql = "SELECT * FROM tarifler";

            // Sorguyu çalıştır ve sonuçları al
            resultSet = statement.executeQuery(sql);

            // Sonuçları yazdır
            while (resultSet.next()) {
                int id = resultSet.getInt("TarifID"); // Örneğin id sütununu al
                String tarifAdi = resultSet.getString("TarifAdi"); // Örneğin tarif_adi sütununu al

                // Terminalde sonuçları yazdır
                System.out.println("ID: " + id + ", Tarif Adı: " + tarifAdi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Kaynakları kapat
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
