package Model;

import Model.Tarif;

public class TarifMalzeme {

    private int tarifId;
    private int malzemeId;
    private int malzemeMiktar;


    public int getTarifId() {
        return tarifId;
    }

    public void setTarifId(int tarifId) {
        this.tarifId = tarifId;
    }


    public int getMalzemeId() {
        return malzemeId;
    }

    public void setMalzemeId(int malzemeId) {
        this.malzemeId = malzemeId;
    }


    public int getMalzemeMiktar() {
        return malzemeMiktar;
    }

    public void setMalzemeMiktar(int malzemeMiktar) {
        this.malzemeMiktar = malzemeMiktar;
    }
}
