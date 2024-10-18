package Controller;

import Model.Malzeme;
import Model.Tarif;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/yazlab";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Tarif> getTarifler() {
        String query = "SELECT * FROM tarifler";
        return getTariflerFromDB(query);
    }

    public static List<Malzeme> getMalzemeler() {
        String query = "SELECT * FROM malzemeler";
        return getMalzemelerFromDB(query);
    }

    //TARİFLERİ VERİTABANINDAN AL VE LİSTE OLARAK DÖNDÜR
    private static List<Tarif> getTariflerFromDB(String query) {
        List<Tarif> tarifler = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Tarif tarif = new Tarif();

                tarif.setTarifID(resultSet.getInt("TarifID"));
                tarif.setTarifAdi(resultSet.getString("TarifAdi"));
                tarif.setKategori(resultSet.getString("Kategori"));
                tarif.setHazirlamaSuresi(resultSet.getInt("HazirlamaSuresi"));
                tarif.setTalimatlar(resultSet.getString("Talimatlar"));

                tarifler.add(tarif);
                System.out.println(tarifler.size());
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

        return tarifler;
    }

    //MALZEMELERİ VERİTABANINDAN ALIP LİSTE OLARAK DÖNDÜR
    private static List<Malzeme> getMalzemelerFromDB(String query) {
        List<Malzeme> malzemeler = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Malzeme malzeme = new Malzeme();

                malzeme.setMazemeID(resultSet.getInt("MalzemeID"));
                malzeme.setMalzemeAdi(resultSet.getString("MalzemeAdi"));
                malzeme.setToplamMiktar(resultSet.getFloat("ToplamMiktar"));
                malzeme.setMalzemeBirim(resultSet.getString("MalzemeBirim"));
                malzeme.setMalzemeBirimFiyat(resultSet.getFloat("BirimFiyat"));

                malzemeler.add(malzeme);
                System.out.println(malzeme.getToplamMiktar());
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

        return malzemeler;
    }

    //TARİF ARAMAK İÇİN
    public static Tarif getTarifByName(String tarifAdi) {
        String query = "SELECT * FROM tarifler WHERE TarifAdi = '" + tarifAdi + "'";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Tarif tarif = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                tarif = new Tarif();

                tarif.setTarifID(resultSet.getInt("TarifID"));
                tarif.setTarifAdi(resultSet.getString("TarifAdi"));
                tarif.setKategori(resultSet.getString("Kategori"));
                tarif.setHazirlamaSuresi(resultSet.getInt("HazirlamaSuresi"));
                tarif.setTalimatlar(resultSet.getString("Talimatlar"));
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

        return tarif;
    }

    //MALZEME EKLE
    public static void addMalzeme(String MalzemeAdi, float ToplamMiktar, String MalzemeBirim, float BirimFiyat) {
        String sql = "INSERT INTO malzemeler (MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, MalzemeAdi);
            pstmt.setFloat(2, ToplamMiktar);
            pstmt.setString(3, MalzemeBirim);
            pstmt.setFloat(4, BirimFiyat);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Malzeme başarıyla eklendi!");
            } else {
                System.out.println("Malzeme ekleme başarısız oldu!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMalzeme(String MalzemeAdi, float ToplamMiktar, String MalzemeBirim) {
        String sql = "DELETE FROM malzemeler WHERE MalzemeAdi = ? ";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, MalzemeAdi);


            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Malzeme başarıyla silindi!");
            } else {
                System.out.println("Malzeme silme başarısız oldu! Hiçbir kayıt bulunamadı.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //SEÇİLEN MALZEMEYE GÖRE TARİF ARAMA
    public static void MalzemeyeGoreTarif(String MalzemeAdi) {
      /*
        String sql = "SELECT " +
                "    t.TarifID, " +
                "    t.TarifAdi, " +
                "    t.Kategori, " +
                "    t.HazirlamaSuresi, " +
                "    t.Talimatlar " +
                "FROM " +
                "    Malzemeler m " +
                "JOIN " +
                "    MalzemeTarif mt ON m.MalzemeID = mt.MalzemeID " +
                "JOIN " +
                "    Tarifler t ON mt.TarifID = t.TarifID " +
                "WHERE " +
                "    m.MalzemeAdi = ?"; // Parametre yer tutucusu


        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, MalzemeAdi);
        }
    }
    */




    }

    public static void addTarif(String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        String sql = "INSERT INTO tarifler (TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tarifAdi);
            pstmt.setInt(2, kategori);
            pstmt.setString(3,hazirlamaSuresi);
            pstmt.setString(4, talimatlar);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Tarif başarıyla eklendi!");
            } else {
                System.out.println("Tarif ekleme başarısız oldu!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
