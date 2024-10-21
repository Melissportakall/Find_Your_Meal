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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    public static final List<Tarif> filtreliTariflerID = new ArrayList<>();

    public void setTarifData(Tarif tarif) {
        tarifLabel.setText(tarif.getTarifAdi());
        tarifKategori.setText(tarif.getKategori());
        tarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");

        this.tarif = tarif;
    }

    public void setMalzemeData(Malzeme malzeme) {
        malzemeAdiLabel.setText(malzeme.getMalzemeAdi());
        malzemeMiktarLabel.setText(malzeme.getToplamMiktar() + " " + malzeme.getMalzemeBirim());
        malzemeID.setId(String.valueOf(malzeme.getMazemeID()));
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
    public void CheckBoxAction() throws SQLException {
        if (malzemeCheckBox.isSelected()) {
            System.out.println("CheckBox işaretlendi, malzemeID: " + malzemeID.getId());

            List<Tarif> tarifler = DatabaseConnection.MalzemeyeGoreTarif(Integer.parseInt(malzemeID.getId()));

            for (Tarif tarif : tarifler) {
                if (!filtreliTariflerID.contains(tarif)) {
                    filtreliTariflerID.add(tarif);
                    System.out.println(tarif.getTarifAdi() + " listeye eklendi.");
                }
            }

        } else {
            System.out.println("CheckBox işareti kaldırıldı, malzemeID: " + malzemeID.getId());

            List<Tarif> tarifler = DatabaseConnection.MalzemeyeGoreTarif(Integer.parseInt(malzemeID.getId()));

            for (Tarif tarif : tarifler) {
                if (filtreliTariflerID.contains(tarif)) {
                    filtreliTariflerID.remove(tarif);
                    System.out.println(tarif.getTarifAdi() + " listeden çıkarıldı.");
                }
            }
        }

        System.out.println("Güncel filtrelenen tarifler: ");
        for (Tarif tarif : filtreliTariflerID) {
            System.out.println(tarif.getTarifAdi());
        }
    }
}
