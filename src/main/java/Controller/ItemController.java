package Controller;

import Model.Malzeme;
import Model.Tarif;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


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
    private AnchorPane malzemeID;

    @FXML
    private CheckBox malzemeCheckBox;

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
        malzemeID.setId(Integer.toString(malzeme.getMazemeID()));
    }

    @FXML
    public void tarifOnAction(ActionEvent event){
        try {
            GUI gui = new GUI();
            gui.showRecipeDetails(tarif, event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void CheckBoxAction() {
        if (malzemeCheckBox.isSelected()) {
            System.out.println("CheckBox işaretlendi, malzemeID: " + malzemeID.getId());
            GUI.updateFilteredTarifler(Integer.parseInt(malzemeID.getId()));
        } else {
            System.out.println("CheckBox işareti kaldırıldı, malzemeID: " + malzemeID.getId());
        }
    }
}
