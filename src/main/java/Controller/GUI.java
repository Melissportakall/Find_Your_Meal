package Controller;

import Model.Malzeme;
import Model.Tarif;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HyperlinkLabel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GUI implements Initializable {
    @FXML
    private Button malzemeEkleButton;

    @FXML
    private GridPane malzemeEkleGrid;

    @FXML
    private ScrollPane malzemeEkleScroll;

    @FXML
    private VBox chosenRecipeCard;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeIdLabel;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private TextField searchfield;

    @FXML
    private Button searchbutton;

    private List<Tarif> tarifler;

    private List<Malzeme> malzemeler;

    private List<Tarif> getTarifler() throws SQLException {
        return DatabaseConnection.getTarifler(); // Tüm tarifleri al
    }

    private List<Malzeme> getMalzemeler() throws SQLException {
        return DatabaseConnection.getMalzemeler();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<String> tarifAdlari = new ArrayList<>();
            List<Integer> tarifHazirlamaSuresi = new ArrayList<>();
            tarifler = getTarifler();
            Tarif tarif;

            List<String> malzemeAdlari = new ArrayList<>();
            List<Float> malzemeMiktar = new ArrayList<>();
            malzemeler = getMalzemeler();
            Malzeme malzeme;

            for (int i = 0; i < tarifler.size(); i++) {
                tarif = tarifler.get(i);
                System.out.println(tarif.getTarifAdi());
                System.out.println(tarif.getHazirlamaSuresi());
                tarifAdlari.add(tarif.getTarifAdi());
                tarifHazirlamaSuresi.add(tarif.getHazirlamaSuresi());
            }

            int col = 0;
            int row = 1;

            for (int i = 0; i < tarifAdlari.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File("/Users/melisportakal/desktop/yazlabyeni/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                // ItemController ile veriyi GUI'ye bastır
                ItemController itemController = fxmlLoader.getController();
                itemController.setTarifData(tarifAdlari.get(i), tarifHazirlamaSuresi.get(i));



                if (col == 3) {
                    col = 0;
                    row++;
                }

                grid.add(anchorPane, col++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }

            for (int i = 0; i < malzemeler.size(); i++) {
                malzeme = malzemeler.get(i);
                System.out.println(malzeme.getMalzemeAdi());
                System.out.println(malzeme.getToplamMiktar());
                malzemeAdlari.add(malzeme.getMalzemeAdi());
                malzemeMiktar.add(malzeme.getToplamMiktar());
            }

            int malzemeCol = 0;
            int malzemeRow = 1;

            for (int i = 0; i < malzemeAdlari.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File("/Users/melisportakal/desktop/yazlabyeni/src/main/resources/com/example/yazlabb/malzeme_item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setMalzemeData(malzemeAdlari.get(i), malzemeMiktar.get(i));

                malzemeEkleGrid.add(anchorPane, malzemeCol, malzemeRow++);
                GridPane.setMargin(anchorPane, new Insets(0, 0, 1, 0));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        // TextField için aksiyon tanımlaması

        /*searchfield.setOnAction(event -> {
            String arananTarif = searchfield.getText();
            Tarif bulunanTarif = DatabaseConnection.getTarifByName(arananTarif);



            if (bulunanTarif != null) {
                System.out.println(bulunanTarif.getTarifAdi());
                System.out.println("buldun");
                //showRecipeDetails(bulunanTarif);
            } else {
                //showAlert("Tarif bulunamadı!");
                System.out.println("bulamadın");
            }
        });*/

        // "Malzeme Ekle" butonuna tıklanınca formu aç
        malzemeEkleButton.setOnAction(event -> showAddMalzemeDialog());

        //Arama butonuna tıklanınca
        searchbutton.setOnAction(event -> {
            // Arama alanındaki tarifi al
            String arananTarif = searchfield.getText().trim();

            // Veri tabanından tarifi bul
            Tarif bulunanTarif = DatabaseConnection.getTarifByName(arananTarif);

            // Eğer tarif bulunduysa detayları göster, aksi takdirde uyarı mesajı ver
            if (bulunanTarif != null) {
                System.out.println(arananTarif);
                System.out.println("Tarif bulundu!");
                showRecipeDetails(bulunanTarif);
            } else {
                System.out.println("Tarif bulunamadı!");
                showAlert("Tarif bulunamadı!");
            }
        });






    }

    // Tarif detaylarını göstermek için kullanılan fonksiyon
    public static void showRecipeDetails(Tarif tarif) {
        // Tarif detaylarını konsola yazdır
        String detaylar = String.format(
                "Tarif Adı: %s\nKategori: %s\nHazırlama Süresi: %d dakika\nTalimatlar: %s",
                tarif.getTarifAdi(), tarif.getKategori(), tarif.getHazirlamaSuresi(), tarif.getTalimatlar()
        );

        // Burada konsolda yazdırmak yerine bir Alert penceresi ile gösterebilirsiniz
        showAlert(detaylar);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tarif Detayları");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Malzeme ekleme formunu açan fonksiyon
    // Malzeme ekleme formunu açan fonksiyon
    private void showAddMalzemeDialog() {
        Dialog<Malzeme> dialog = new Dialog<>();
        dialog.setTitle("Malzeme Ekle");
        dialog.setHeaderText("Yeni Malzeme Bilgilerini Girin");

        // Dialog için butonlar
        ButtonType ekleButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ekleButtonType, ButtonType.CANCEL);

        // Dialog içeriği
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField malzemeAdiField = new TextField();
        malzemeAdiField.setPromptText("Malzeme Adı");
        TextField malzemeBirimField = new TextField();
        malzemeBirimField.setPromptText("Malzeme Birimi");
        TextField malzemeMiktarField = new TextField();
        malzemeMiktarField.setPromptText("Malzeme Miktarı");
        TextField malzemeFiyatField = new TextField();
        malzemeFiyatField.setPromptText("Birim Fiyat");

        gridPane.add(new Label("Malzeme Adı:"), 0, 0);
        gridPane.add(malzemeAdiField, 1, 0);
        gridPane.add(new Label("Malzeme Birimi:"), 0, 1);
        gridPane.add(malzemeBirimField, 1, 1);
        gridPane.add(new Label("Malzeme Miktarı:"), 0, 2);
        gridPane.add(malzemeMiktarField, 1, 2);
        gridPane.add(new Label("Birim Fiyat:"), 0, 3);
        gridPane.add(malzemeFiyatField, 1, 3);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ekleButtonType) {
                String malzemeAdi = malzemeAdiField.getText();
                String malzemeBirim = malzemeBirimField.getText();
                int toplamMiktar = Integer.parseInt(malzemeMiktarField.getText());
                float birimFiyat = Float.parseFloat(malzemeFiyatField.getText());

                DatabaseConnection.addMalzeme(malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);

                //return new Malzeme(malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);
            }
            return null;
        });

        dialog.showAndWait();
    }

}


