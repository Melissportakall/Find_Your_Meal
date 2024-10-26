package Controller;

import Model.Malzeme;
import Model.Tarif;
import Model.TarifinMalzemeleri;
import Model.TarifMalzeme;
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

                // Tüm gerekli verileri al
                malzeme.setMazemeID(resultSet.getInt("MalzemeID"));
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
        // Malzemenin var olup olmadığını kontrol eden SQL sorgusu
        String checkSql = "SELECT COUNT(*) FROM Malzemeler WHERE MalzemeAdi = ?";
        // Malzeme eklemek için kullanılan SQL sorgusu
        String insertSql = "INSERT INTO Malzemeler (MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            // İlk olarak, malzeme adını kontrol ediyoruz
            checkStmt.setString(1, MalzemeAdi);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Eğer malzeme zaten varsa, uyarı göster
                showAlert("Malzeme zaten mevcut!", "Bu malzeme zaten veritabanında bulunuyor.");
                return 0; // İşlem iptal
            }

            // Eğer malzeme yoksa, ekleme işlemini yap
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, MalzemeAdi);
                pstmt.setFloat(2, ToplamMiktar);
                pstmt.setString(3, MalzemeBirim);
                pstmt.setFloat(4, BirimFiyat);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Malzeme başarıyla eklendi!");

                    // Otomatik üretilen anahtarları al
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

    //ANLAMADIM
    public static int TarifinMalzemesiniEkle(String MalzemeAdiT,float MalzemeMiktarT,String MalzemeBirimT,int MalzemeBirimFiyatT) {
        String sql = "INSERT INTO TarifinMalzemeleri (MalzemeAdiT, MalzemeMiktarT, MalzemeBirimT,MalzemeBirimFiyatT) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, MalzemeAdiT);
            pstmt.setFloat(2, MalzemeMiktarT);
            pstmt.setString(3, MalzemeBirimT);
            pstmt.setFloat(4, MalzemeBirimFiyatT);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Malzeme başarıyla eklendi! TarifinMalzemelerine");

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Malzeme eklenirken bir hata oluştu, anahtar döndürülmedi.");
                    }
                }

            } else {
                System.out.println("Malzeme ekleme başarısız oldu!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
        // tarifID'ye göre malzeme sayısını almak için SQL sorgusu
        String sql = "SELECT COUNT(malzemeID) AS malzemeSayisi FROM MalzemeTarif WHERE tarifID = ?";

        int malzemeSayisi = 0; // Sonucu tutacak değişken

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // tarifID parametresini ayarlama
            preparedStatement.setInt(1, tarifID);

            // Sorguyu çalıştırma ve sonuç alma
            ResultSet resultSet = preparedStatement.executeQuery();

            // Sonuç setinden malzeme sayısını alma
            if (resultSet.next()) {
                malzemeSayisi = resultSet.getInt("malzemeSayisi");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hata ayıklama için hata mesajını yazdır
        }

        return malzemeSayisi; // Sonucu döndür
    }



    public static Map<Integer, String> EksikMalzemeler(int tarifID) {
        String sql = "SELECT m.malzemeID, m.malzemeAdi " +
                "FROM MalzemeTarif tm " +
                "JOIN Malzemeler m ON tm.malzemeID = m.malzemeID " +
                "WHERE tm.tarifID = ? AND m.ToplamMiktar = 0";

        Map<Integer, String> eksikMalzemeler = new HashMap<>();

        // JDBC bağlantısını oluştur
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            System.out.println(tarifID);
            // tarifID parametresini ayarlama
            preparedStatement.setInt(1, tarifID);

            // Sorguyu çalıştırma ve sonuç alma
            ResultSet resultSet = preparedStatement.executeQuery();

            // Sonuç setinden eksik malzemeleri alma
            while (resultSet.next()) {
                int malzemeID = resultSet.getInt("malzemeID"); // Malzeme ID'si
                if(malzemeID == 0)
                {
                    malzemeID = 0;
                }
                String malzemeAdi = resultSet.getString("malzemeAdi"); // Malzeme adı
                if(malzemeAdi == null)
                {
                    malzemeAdi = "";
                }
                eksikMalzemeler.put(malzemeID, malzemeAdi); // Map'e ekle

            }

            // Sorgu sonuçlarını yazdır
            if (eksikMalzemeler.isEmpty()) {
                System.out.println("Hiç eksik malzeme bulunamadı.");
            } else {
                System.out.println("Bulunan eksik malzemeler: " + eksikMalzemeler);
            }

            // Eksik malzemeleri yazdırma
           /* for (Map.Entry<Integer, String> entry : eksikMalzemeler.entrySet()) {
                Integer malzemeID = entry.getKey(); // Malzeme ID'si
                String malzemeAdi = entry.getValue(); // Malzeme adı
                System.out.println("Malzeme ID: " + malzemeID + ", Malzeme Adı: " + malzemeAdi);
            }*/
            //System.out.println("Tarif ID: " + tarifID + ", Eksik Malzeme Sayısı:db " + eksikMalzemeler.size());

        } catch (SQLException e) {
            e.printStackTrace(); // Hata ayıklama için hata mesajını yazdır
        }

        return eksikMalzemeler; // Eksik malzemeleri döndür
    }

    public static List<Tarif> KategoriBul(String Kategori)
    {
        String sql = "SELECT * From Tarifler  Where LOWER(Kategori) = LOWER(?)";
        List<Tarif> tarifListesi = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Parametreyi ayarla
            preparedStatement.setString(1, Kategori);

            // Sorguyu çalıştır
            ResultSet resultSet = preparedStatement.executeQuery();

            // Sonuçları işleyerek listeye ekle
            while (resultSet.next()) {
                int tarifID = resultSet.getInt("TarifID"); // Tarif ID'sini al
                String tarifAdi = resultSet.getString("TarifAdi"); // Tarif adını al
                String kategoriSonuc = resultSet.getString("Kategori"); // Kategoriyi al
                int HazirlamaSuresi = resultSet.getInt("HazırlamaSuresi"); // Malzemeleri al
                String Talimatlar = resultSet.getString("Talimatlar");


                // Tarif nesnesi oluştur ve listeye ekle
                Tarif tarif = new Tarif(tarifID, tarifAdi, kategoriSonuc,HazirlamaSuresi, Talimatlar);
                tarifListesi.add(tarif);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Hata durumunda hata ayıklama mesajı yazdır
        }

        return tarifListesi;


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


            // Güncelleme işlemini gerçekleştir
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Güncelleme başarısız oldu, hiçbir kayıt etkilenmedi.");
            }
        }
    }
}

