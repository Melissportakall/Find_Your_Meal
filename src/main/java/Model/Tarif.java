package Model;

import java.util.Objects;

public class Tarif {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;

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
        return tarifID == tarif.tarifID; // Eşitlik kontrolü tarifID üzerinden yapılıyor
    }

    @Override
    public int hashCode() {
        return Objects.hash(tarifID); // hashCode hesaplaması da tarifID üzerinden yapılıyor
    }
}

