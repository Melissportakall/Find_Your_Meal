package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Controller.DatabaseConnection.getConnection;

public class Tarif {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;
    private float toplamMaliyet = 0;

    public Tarif(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        this.tarifID = tarifID;
        this.tarifAdi = tarifAdi;
        this.kategori = kategori;
        this.hazirlamaSuresi = hazirlamaSuresi;
        this.talimatlar = talimatlar;
    }

    public Tarif() { }

    // Getter ve Setter metotları
    public int getTarifID() {
        return tarifID;
    }

    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public void setHazirlamaSuresi(int hazirlamaSuresi) {
        this.hazirlamaSuresi = hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }

    // equals() ve hashCode() metotları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarif tarif = (Tarif) o;
        return tarifID == tarif.tarifID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tarifID);
    }

    public float getToplamMaliyet() {
        return toplamMaliyet;
    }

    public void setToplamMaliyet(int tarifID) {
        String sql = "SELECT tm.MalzemeID, tm.MalzemeMiktar " +
                "FROM MalzemeTarif tm " +
                "WHERE tm.TarifID = ?";

        List<Malzeme> malzemeList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, tarifID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int malzemeID = resultSet.getInt("MalzemeID");
                float malzemeMiktar = resultSet.getFloat("MalzemeMiktar");

                // Malzeme tablosundan malzeme bilgilerini al
                Malzeme malzeme = getMalzemeById(malzemeID);
                if (malzeme != null) {
                    malzeme.setToplamMiktar(malzemeMiktar);
                    malzemeList.add(malzeme);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Malzeme malzeme : malzemeList) {
            this.toplamMaliyet += (malzeme.getMalzemeBirimFiyat() * malzeme.getToplamMiktar());
        }
    }

    // Malzeme bilgilerini ID'sine göre döndüren yardımcı metot
    private Malzeme getMalzemeById(int malzemeID) {
        Malzeme malzeme = null;
        String sql = "SELECT m.MalzemeID, m.MalzemeAdi, m.MalzemeBirim, m.BirimFiyat " +
                "FROM Malzemeler m WHERE m.MalzemeID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, malzemeID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                malzeme = new Malzeme();
                malzeme.setMalzemeID(resultSet.getInt("MalzemeID"));
                malzeme.setMalzemeAdi(resultSet.getString("MalzemeAdi"));
                malzeme.setMalzemeBirim(resultSet.getString("MalzemeBirim"));
                malzeme.setMalzemeBirimFiyat(resultSet.getInt("BirimFiyat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return malzeme;
    }

}

