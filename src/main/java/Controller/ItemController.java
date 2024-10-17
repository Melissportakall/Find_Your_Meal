package Controller;

import Model.Malzeme;
import Model.Tarif;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ItemController {
    @FXML
    private Button tarifLabel;

    @FXML
    private Button tarifKategori;

    @FXML
    private Button tarifSure;

    @FXML
    private Label malzemeAdiLabel;

    @FXML
    private Label malzemeMiktarLabel;

    @FXML
    private Button malzemeSilButton;

    @FXML
    private Button malzemeEkleButton;

    private Tarif tarif;

    public void setTarifData(Tarif tarif) {
        tarifLabel.setText(tarif.getTarifAdi());
        tarifKategori.setText(tarif.getKategori());
        tarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");

        this.tarif = tarif;
    }

    public void setMalzemeData(Malzeme malzeme) {
        malzemeAdiLabel.setText(malzeme.getMalzemeAdi());
        malzemeMiktarLabel.setText(malzeme.getToplamMiktar() + " " + malzeme.getMalzemeBirim());
    }

    public void tarifOnAction()
    {
        GUI.showRecipeDetails(tarif);
    }
}
