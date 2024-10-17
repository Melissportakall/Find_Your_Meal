package Controller;

import Model.Tarif;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ItemController {
    @FXML
    private Button nameLabel; // Tarifi gösteren Label

    @FXML
    private Label malzemeAdiLabel;

    @FXML
    private Button malzemeSilButton;



    private Tarif tarif;

    public void setTarifData(String name) {
        nameLabel.setText(name);
    }

    // Tarif adı ve hazırlama süresi verisini ayarlamak için metot
    public void setTarifData(String tarifAdi, int hazirlamaSuresi) {
        // Hazırlama süresini dakika cinsinden ekleyin
        nameLabel.setText(tarifAdi + " - " + hazirlamaSuresi + " dakika");
    }

    // Malzeme adı ve miktarını ayarlamak için metot
    public void setMalzemeData(String malzemeAdi, float malzemeMiktari) {

        malzemeAdiLabel.setText(malzemeAdi + "-" + String.valueOf(malzemeMiktari));
    }
}
