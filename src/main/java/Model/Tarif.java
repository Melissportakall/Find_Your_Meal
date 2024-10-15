package Model;

public class Tarif {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private String hazirlamaSuresi;
    private String talimatlar;

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

    public String getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public void setHazirlamaSuresi(String hazirlamaSuresi) {
        this.hazirlamaSuresi = hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }
}
