package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/yazlab"; // Veritabanı URL
    private static final String USER = "root"; // Kullanıcı adı
    private static final String PASSWORD = ""; // Parola

    // Veritabanına bağlantı kurmak için metot
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Tarif tablosundaki tüm tarif adlarını al
    public static List<String> getTarifler() {
        String query = "SELECT tarifAdi FROM tarifler"; // Tüm tarif adlarını seçen sorgu
        return getTariflerFromDB(query);
    }

    // Tarifleri veritabanından al ve liste olarak döndür
    private static List<String> getTariflerFromDB(String query) {
        List<String> tarifler = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                tarifler.add(resultSet.getString("tarifAdi")); // Tarif adını listeye ekle
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

        return tarifler; // Tarif listesini döndür
    }
}
