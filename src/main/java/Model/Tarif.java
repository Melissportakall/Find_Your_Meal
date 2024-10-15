package Model;

public class Tarif
{
    private int tarifID;
    private String tarifAdi;
    private String tarifKategori;
    private int tarifSure;
    private String tarifTalimat;


    public int getTarifID() {
        return tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public String getTarifKategori() {
        return tarifKategori;
    }

    public int getTarifSure() {
        return tarifSure;
    }

    public String getTarifTalimat() {
        return tarifTalimat;
    }

    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    public void setTarifKategori(String tarifKategori) {
        this.tarifKategori = tarifKategori;
    }

    public void setTarifSure(int tarifSure) {
        this.tarifSure = tarifSure;
    }

    public void setTarifTalimat(String tarifTalimat) {
        this.tarifTalimat = tarifTalimat;
    }
}
