package Controller;

import Model.*;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                tarif.setToplamMaliyet(resultSet.getInt("TarifID"));

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
                Malzeme malzeme = new Malzeme();

                malzeme.setMalzemeID(resultSet.getInt("MalzemeID"));
                malzeme.setMalzemeAdi(resultSet.getString("MalzemeAdi"));
                malzeme.setToplamMiktar(resultSet.getFloat("ToplamMiktar"));
                malzeme.setMalzemeBirim(resultSet.getString("MalzemeBirim"));
                malzeme.setMalzemeBirimFiyat(resultSet.getInt("BirimFiyat"));

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

        Tarif tarif = new Tarif();

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
    public static int addMalzeme(String MalzemeAdi, float ToplamMiktar, String MalzemeBirim, int BirimFiyat) {
        String checkSql = "SELECT COUNT(*) FROM Malzemeler WHERE MalzemeAdi = ?";
        String insertSql = "INSERT INTO Malzemeler (MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, MalzemeAdi);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                showAlert("Malzeme zaten mevcut!", "Bu malzeme zaten veritabanında bulunuyor.");
                return 0;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, MalzemeAdi);
                pstmt.setFloat(2, ToplamMiktar);
                pstmt.setString(3, MalzemeBirim);
                pstmt.setFloat(4, BirimFiyat);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Malzeme başarıyla eklendi!");

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1); // Burada malzemeID döndürülür
                        } else {
                            throw new SQLException("Malzeme eklenirken bir hata oluştu, anahtar döndürülmedi.");
                        }
                    }

                } else {
                    System.out.println("Malzeme ekleme başarısız oldu!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //UYARI
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //MALZEME SİLME
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
    public static List<Tarif> MalzemeyeGoreTarif(int malzemeID) throws SQLException {
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
                "    m.MalzemeID = ?";

        List<Tarif> tarifListesi = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, malzemeID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tarif tarif = new Tarif();

                    tarif.setTarifID(rs.getInt("TarifID"));
                    tarif.setTarifAdi(rs.getString("TarifAdi"));
                    tarif.setKategori(rs.getString("Kategori"));
                    tarif.setHazirlamaSuresi(rs.getInt("HazirlamaSuresi"));
                    tarif.setTalimatlar(rs.getString("Talimatlar"));

                    tarifListesi.add(tarif);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return tarifListesi;
    }

    //TARİF EKLE
    public static int addTarif(String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        String checkSql = "SELECT COUNT(*) FROM tarifler WHERE TarifAdi = ?";
        String insertSql = "INSERT INTO tarifler (TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?)";
        int tarifID = -1;

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, tarifAdi);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                System.out.println("Tarif zaten mevcut! Ekleme işlemi durduruldu.");
                return tarifID;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, tarifAdi);
                pstmt.setString(2, kategori);
                pstmt.setInt(3, hazirlamaSuresi);
                pstmt.setString(4, talimatlar);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            tarifID = generatedKeys.getInt(1); // İlk sütun ID
                        }
                    }
                    System.out.println("Tarif başarıyla eklendi! ID: " + tarifID);
                } else {
                    System.out.println("Tarif ekleme başarısız oldu!");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifID;
    }

    //TARİF SİL
    public static void deleteTarif(String tarifAdi) {
        String sql1 = "DELETE FROM tarifler WHERE TarifAdi = ?";
        String sql2 = "DELETE FROM MalzemeTarif WHERE TarifAdi = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
                pstmt1.setString(1, tarifAdi);
                int rowsAffected1 = pstmt1.executeUpdate();

                if (rowsAffected1 > 0) {
                    System.out.println("Tarifler tablosundan tarif başarıyla silindi!");
                } else {
                    System.out.println("Tarifler tablosundan tarif silme başarısız oldu!");
                }
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setString(1, tarifAdi);
                int rowsAffected2 = pstmt2.executeUpdate();

                if (rowsAffected2 > 0) {
                    System.out.println("Diger_tablo tablosundan tarif başarıyla silindi!");
                } else {
                    System.out.println("Diger_tablo tablosundan tarif silme başarısız oldu!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TARİFTE EKLENEN MALZEMEYİ MALZEMELERE EKLE
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

    public static void addMalzemeToTarif2(int tarifID, List<Malzeme> malzemeler) {
        if (malzemeler.isEmpty()) {
            showAlert("Hata", "Malzeme seçiniz!");
            return;
        }

        String sql = "INSERT INTO MalzemeTarif (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            if (conn == null) {
                showAlert("Hata", "Bağlantı sağlanamadı!");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Malzeme malzeme : malzemeler) {
                    pstmt.setInt(1, tarifID);
                    pstmt.setInt(2, malzeme.getMazemeID());
                    pstmt.setFloat(3, malzeme.getToplamMiktar());

                    pstmt.addBatch();
                }

                int[] result = pstmt.executeBatch();

                showAlert("Başarılı", result.length + " malzeme başarıyla eklendi.");
            }
        } catch (SQLException e) {
            showAlert("Hata", "Malzeme eklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }


    //ANLAMADIM
    public static List<Malzeme> TarifinMalzemeleri(int tarifID) {
        List<Malzeme> malzemeler = new ArrayList<>();
        String sql = "SELECT m.MalzemeID, m.MalzemeAdi, mt.MalzemeMiktar, m.MalzemeBirim, m.BirimFiyat " +
                "FROM MalzemeTarif mt " +
                "JOIN Malzemeler m ON mt.MalzemeID = m.MalzemeID " +
                "WHERE mt.TarifID = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tarifID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Malzeme malzeme = new Malzeme();
                malzeme.setMalzemeID(rs.getInt("MalzemeID"));
                malzeme.setMalzemeAdi(rs.getString("MalzemeAdi"));
                malzeme.setToplamMiktar(rs.getFloat("MalzemeMiktar"));
                malzeme.setMalzemeBirim(rs.getString("MalzemeBirim"));
                malzeme.setMalzemeBirimFiyat(rs.getInt("BirimFiyat"));
                malzemeler.add(malzeme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return malzemeler;
    }




    //TARİFİ MALZEME SAYISINA GÖRE SIRALAMAK İÇİN
   /* public List<TarifMalzemeSayisi> getTarifMalzemeSayilari() throws SQLException {
        List<TarifMalzemeSayisi> tarifMalzemeSayilari = new ArrayList<>();
        String sql = "SELECT t.TarifID, COUNT(mt.MalzemeID) AS MalzemeSayisi FROM Tarif t LEFT JOIN MalzemeTarif mt ON t.TarifID = mt.TarifID GROUP BY t.TarifID";

        try (Connection connection = DriverManager.getConnection("your_database_url", "username", "password");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int tarifId = resultSet.getInt("TarifID");
                int malzemeSayisi = resultSet.getInt("MalzemeSayisi");
                tarifMalzemeSayilari.add(new TarifMalzemeSayisi(tarifId, malzemeSayisi));
            }
        }

        return tarifMalzemeSayilari;
    }*/


    public static int toplamMalzemeSayisi(int tarifID) {
        String sql = "SELECT COUNT(malzemeID) AS malzemeSayisi FROM MalzemeTarif WHERE tarifID = ?";

        int malzemeSayisi = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, tarifID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                malzemeSayisi = resultSet.getInt("malzemeSayisi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return malzemeSayisi;
    }



    public static List<Malzeme> EksikMalzemeler(int tarifID) {
        String sql = "SELECT m.malzemeID, m.malzemeAdi, m.malzemeBirim, m.birimFiyat, " +
                "tm.MalzemeMiktar - COALESCE(m.ToplamMiktar, 0) AS EksikMiktar " +
                "FROM MalzemeTarif tm " +
                "JOIN Malzemeler m ON tm.malzemeID = m.malzemeID " +
                "WHERE tm.tarifID = ? AND (m.ToplamMiktar IS NULL OR m.ToplamMiktar < tm.MalzemeMiktar)";

        List<Malzeme> eksikMalzemeler = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, tarifID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Malzeme eksikMalzeme = new Malzeme();
                eksikMalzeme.setMalzemeID(resultSet.getInt("malzemeID"));
                eksikMalzeme.setMalzemeAdi(resultSet.getString("malzemeAdi"));
                eksikMalzeme.setMalzemeBirim(resultSet.getString("malzemeBirim"));
                eksikMalzeme.setMalzemeBirimFiyat(resultSet.getInt("birimFiyat"));
                eksikMalzeme.setToplamMiktar(resultSet.getFloat("EksikMiktar"));

                eksikMalzemeler.add(eksikMalzeme);
            }

            if (eksikMalzemeler.isEmpty()) {
                System.out.println("Hiç eksik malzeme bulunamadı.");
            } else {
                System.out.print("Bulunan eksik malzemeler: ");
                for (Malzeme malzeme : eksikMalzemeler) {
                    System.out.println(malzeme.getMalzemeAdi() + " Eksik miktar: " + malzeme.getToplamMiktar());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eksikMalzemeler;
    }

    public static List<Tarif> KategoriBul(String Kategori)
    {
        String sql = "SELECT * From Tarifler  Where LOWER(Kategori) = LOWER(?)";
        List<Tarif> tarifListesi = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, Kategori);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int tarifID = resultSet.getInt("TarifID"); // Tarif ID'sini al
                String tarifAdi = resultSet.getString("TarifAdi"); // Tarif adını al
                String kategoriSonuc = resultSet.getString("Kategori"); // Kategoriyi al
                int HazirlamaSuresi = resultSet.getInt("HazırlamaSuresi"); // Malzemeleri al
                String Talimatlar = resultSet.getString("Talimatlar");

                Tarif tarif = new Tarif(tarifID, tarifAdi, kategoriSonuc,HazirlamaSuresi, Talimatlar);
                tarifListesi.add(tarif);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifListesi;
    }

    //DUPLİCATE KONTROLÜ KODU
    public static boolean tarifVarMi(String tarifAdi)throws SQLException{
        String sql = "SELECT COUNT(*) FROM Tarifler WHERE TarifAdi = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1,tarifAdi);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    //TARİF GÜNCELLEMEK İÇİN
    public static void updateTarif(Tarif tarif) throws SQLException {
        String sql = "UPDATE tarifler SET Kategori = ?, HazirlamaSuresi = ?, Talimatlar = ? WHERE TarifID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tarif.getKategori());           // 1. parametre: kategori
            pstmt.setInt(2, tarif.getHazirlamaSuresi());       // 2. parametre: hazirlama_suresi
            pstmt.setString(3, tarif.getTalimatlar());         // 3. parametre: talimatlar
            pstmt.setInt(4, tarif.getTarifID());               // 4. parametre: id

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Güncelleme başarısız oldu, hiçbir kayıt etkilenmedi.");
            }
        }
    }
}

