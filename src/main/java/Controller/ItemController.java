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

    @FXML
    private Label malzemeListeleBirim;

    private Image img;

    private Tarif tarif;

    public static final List<Tarif> filtreliTariflerID = new ArrayList<>();

    public static final List<Malzeme> seciliMalzemeler = new ArrayList<>();

    public void setTarifData(Tarif tarif) throws MalformedURLException {
        tarifLabel.setText(tarif.getTarifAdi());
        tarifKategori.setText(tarif.getToplamMaliyet() + " TL");
        tarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");


        File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\img\\" + tarif.getTarifID() + ".jpg");

        if (!filePath.exists()) {
            filePath = new File("/Users/melisportakal/desktop/iyilestirmelermis/img/" + tarif.getTarifID() + ".jpg");
        }

        img = new Image(filePath.toURI().toURL().toExternalForm());

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
        malzemeListeleBirim.setText(malzeme.getMalzemeBirim());
        malzemeListeleBirim.setId(Integer.toString(malzeme.getMazemeID()));
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

            Malzeme malzeme = new Malzeme();
            malzeme.setMalzemeID(Integer.parseInt(malzemeListeleID.getId()));
            malzeme.setMalzemeAdi(malzemeListeleLabel.getText());
            malzeme.setMalzemeBirim(malzemeListeleBirim.getText());
            malzeme.setMalzemeBirimFiyat(Integer.parseInt(malzemeListeleBirim.getId()));
            malzeme.setToplamMiktar(Float.parseFloat(malzemeListeleText.getText()));

            seciliMalzemeler.add(malzeme);
            System.out.print("Seçilen malzemeler: ");
            for (Malzeme malzeme1 : seciliMalzemeler) {
                System.out.println(malzeme1.getMalzemeAdi() + " " + malzeme1.getToplamMiktar() + " " + malzeme1.getMalzemeBirim() + " ");
            }
        } else {
            int malzemeID = Integer.parseInt(malzemeListeleID.getId());
            seciliMalzemeler.removeIf(malz -> malz.getMazemeID() == malzemeID);
        }
    }


}
