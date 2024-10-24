package Model;

import java.util.Map;


public class TarifBilgileri {
    private Map<Integer, String> eksikMalzemeMap;
    private int tarifId;
    private int malzemeSayisi;
    private Map<Integer, String> eksikMalzemeler;

    // Constructor
    public TarifBilgileri(int tarifId, int malzemeSayisi, Map<Integer, String> eksikMalzemeler) {
        this.tarifId = tarifId;
        this.malzemeSayisi = malzemeSayisi;
        this.eksikMalzemeMap = eksikMalzemeler;
    }

    // Getter ve Setter metotları
    public int getTarifId() {
        return tarifId;
    }

    public void setTarifId(int tarifId) {
        this.tarifId = tarifId;
    }

    public int getMalzemeSayisi() {
        return malzemeSayisi;
    }

    public void setMalzemeSayisi(int malzemeSayisi) {
        this.malzemeSayisi = malzemeSayisi;
    }

    public Map<Integer, String> getEksikMalzemeler() {
        return eksikMalzemeler;
    }

    public void setEksikMalzemeler(Map<Integer, String> eksikMalzemeler) {
        this.eksikMalzemeMap = eksikMalzemeler;
    }

}
