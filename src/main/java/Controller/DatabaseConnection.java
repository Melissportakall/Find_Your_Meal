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
    /*private static List<Tarif> getTariflerFromDB(String query) {
        List<Tarif> tarifler = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                Tarif tarif = new Tarif(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar);

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
    }*/

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
                // ResultSet'ten değerleri al
                int tarifID = resultSet.getInt("TarifID");
                String tarifAdi = resultSet.getString("TarifAdi");
                String kategori = resultSet.getString("Kategori");
                int hazirlamaSuresi = resultSet.getInt("HazirlamaSuresi");
                String talimatlar = resultSet.getString("Talimatlar");

                // Yeni Tarif nesnesini oluştur
                Tarif tarif = new Tarif(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar);

                // Listeye ekle
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


    // MALZEMELERİ VERİTABANINDAN ALIP LİSTE OLARAK DÖNDÜR
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
                // Tüm gerekli verileri al
                int malzemeID = resultSet.getInt("MalzemeID");
                String malzemeAdi = resultSet.getString("MalzemeAdi");
                float toplamMiktar = resultSet.getFloat("ToplamMiktar");
                String malzemeBirim = resultSet.getString("MalzemeBirim");
                float malzemeBirimFiyat = resultSet.getFloat("BirimFiyat");

                // Malzeme nesnesini oluştur
                Malzeme malzeme = new Malzeme(malzemeID, malzemeAdi, toplamMiktar, malzemeBirim, malzemeBirimFiyat);

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
        int tarifID = tarif.getTarifID();
        String kategori = tarif.getKategori();
        int hazirlamaSuresi = tarif.getHazirlamaSuresi();
        String talimatlar = tarif.getTalimatlar();

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {

                tarif = new Tarif(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar);

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
    public static int addMalzeme(String MalzemeAdi, float ToplamMiktar, String MalzemeBirim, float BirimFiyat) {
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

            // Otomatik üretilen anahtarları al
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Burada malzemeID döndürülür
            } else {
                throw new SQLException("Malzeme eklenirken bir hata oluştu, anahtar döndürülmedi.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
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

    public static int addTarif(String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        String sql = "INSERT INTO tarifler (TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?)";
        int tarifID = -1; // Başarısız durum için varsayılan değer

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tarifAdi);
            pstmt.setString(2, kategori);
            pstmt.setInt(3, hazirlamaSuresi);
            pstmt.setString(4, talimatlar);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Eklenen tarifin ID'sini al
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tarifID = generatedKeys.getInt(1); // İlk sütun ID
                    }
                }
                System.out.println("Tarif başarıyla eklendi! ID: " + tarifID);
            } else {
                System.out.println("Tarif ekleme başarısız oldu!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifID; // Eklenen tarifin ID'sini döndür
    }


    public static void deleteTarif(String tarifAdi) {
        String sql = "DELETE FROM tarifler WHERE TarifAdi = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tarifAdi);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Tarif başarıyla silindi!");
            } else {
                System.out.println("Tarif ekleme başarısız oldu!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMalzemeToTarif(int tarifID, int malzemeID, float malzemeMiktar) {
        if (malzemeID <= 0) {
            System.out.println("Geçersiz MalzemeID: " + malzemeID);
            return;
        }

        String sql = "INSERT INTO MalzemeTarif (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";
        int id = -1;

        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Veritabanı bağlantısı sağlanamadı!");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, tarifID);
                pstmt.setInt(2, malzemeID);
                pstmt.setFloat(3, malzemeMiktar);

                System.out.println("TarifID: " + tarifID + ", MalzemeID: " + malzemeID + ", MalzemeMiktar: " + malzemeMiktar);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            id = generatedKeys.getInt(1);
                        }
                    }
                    System.out.println("Malzeme tarifine başarıyla eklendi! ID: " + id);
                } else {
                    System.out.println("Malzeme ekleme başarısız oldu! Etkilenen satır sayısı: " + rowsAffected);
                }

            } catch (SQLException e) {
                System.err.println("SQL hatası: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }
}