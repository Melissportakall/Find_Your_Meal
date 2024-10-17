package Model;

public class Malzeme
{
    private int mazemeID;

    private String malzemeAdi;

    private float toplamMiktar;

    private String malzemeBirim;

    private float malzemeBirimFiyat;


    public int getMazemeID() {
        return mazemeID;
    }

    public void setMazemeID(int mazemeID) {
        this.mazemeID = mazemeID;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public float getToplamMiktar() {
        return toplamMiktar;
    }

    public void setToplamMiktar(float toplamMiktar) {
        this.toplamMiktar = toplamMiktar;
    }

    public String getMalzemeBirim() {
        return malzemeBirim;
    }

    public void setMalzemeBirim(String malzemeBirim) {
        this.malzemeBirim = malzemeBirim;
    }

    public float getMalzemeBirimFiyat() {
        return malzemeBirimFiyat;
    }

    public void setMalzemeBirimFiyat(float malzemeBirimFiyat) {
        this.malzemeBirimFiyat = malzemeBirimFiyat;
    }
}
