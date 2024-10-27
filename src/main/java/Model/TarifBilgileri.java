package Model;

import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;


public class TarifBilgileri {
    private List<Malzeme> eksikMalzemeler;
    private int tarifId;
    private int malzemeSayisi;

    // Constructor
    public TarifBilgileri(int tarifId, int malzemeSayisi, List<Malzeme> eksikMalzemeler) {
        this.tarifId = tarifId;
        this.malzemeSayisi = malzemeSayisi;
        this.eksikMalzemeler = eksikMalzemeler;
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

    public List<Malzeme> getEksikMalzemeler() {
        return eksikMalzemeler;
    }

    public void setEksikMalzemeler(List<Malzeme> malzemeler) {
        this.eksikMalzemeler = malzemeler;
    }

}
