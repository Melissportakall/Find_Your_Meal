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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    @FXML
    private CheckBox malzemeListeleCheckBox;

    @FXML
    private AnchorPane malzemeListeleID;

    @FXML
    private Label malzemeListeleLabel;

    @FXML
    private TextField malzemeListeleText;

    private Image img;

    private Tarif tarif;

    public static final List<Tarif> filtreliTariflerID = new ArrayList<>();

    public void setTarifData(Tarif tarif) throws MalformedURLException {
        tarifLabel.setText(tarif.getTarifAdi());
        tarifKategori.setText(tarif.getKategori());
        tarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");


            img = new Image(String.valueOf(new File("/Users/melisportakal/desktop/iyilestirmelermis/img" + tarif.getTarifID() +".jpg").toURI().toURL()));


        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(100);
        imgView.setFitWidth(100);
        imgView.setPreserveRatio(true);

        System.out.println(tarifLabel.getHeight() + " " + tarifLabel.getWidth());

        tarifLabel.setGraphic(imgView);

        this.tarif = tarif;
    }

    public void setMalzemeData(Malzeme malzeme) {
        malzemeAdiLabel.setText(malzeme.getMalzemeAdi());
        malzemeMiktarLabel.setText(malzeme.getToplamMiktar() + " " + malzeme.getMalzemeBirim());
        malzemeID.setId(String.valueOf(malzeme.getMazemeID()));
    }

    public void setMalzemeListeleData(Malzeme malzeme) {
        malzemeListeleLabel.setText(malzeme.getMalzemeAdi());
        malzemeListeleID.setId(String.valueOf(malzeme.getMazemeID()));
        malzemeListeleText.setVisible(false);
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

    @FXML
    private void MalzemeListeleCheckBoxAction() {
        if (malzemeListeleCheckBox.isSelected()) {
            malzemeListeleText.setVisible(true);
        } else {
            malzemeListeleText.setVisible(false);
        }
    }


}
