package Controller;

import Model.Malzeme;
import Model.Tarif;
import Model.TarifMalzeme;
import Model.TarifinMalzemeleri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static Controller.DatabaseConnection.addMalzemeToTarif;

public class GUI implements Initializable {
    @FXML
    private Button malzemeEkleButton;

    @FXML
    private Button malzemeSilButton;

    @FXML
    private GridPane malzemeEkleGrid;

    @FXML
    private ScrollPane malzemeEkleScroll;

    @FXML
    private Button backwardButton;

    @FXML
    private VBox chosenRecipeCard;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label recipeIdLabel;

    @FXML
    private ScrollPane scroll;

    @FXML
    public GridPane grid;

    @FXML
    private TextField searchfield;

    @FXML
    private Button searchbutton;

    @FXML
    private Label seciliTarifAdi;

    @FXML
    private Label seciliTarifSure;

    @FXML
    private Label seciliTarifTalimat;


    private List<Tarif> tarifler;

    private List<Malzeme> malzemeler;

    private List<Tarif> getTarifler() throws SQLException {
        return DatabaseConnection.getTarifler();
    }

    private List<Malzeme> getMalzemeler() throws SQLException {
        return DatabaseConnection.getMalzemeler();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            tarifler = getTarifler();
            malzemeler = getMalzemeler();

            int col = 0;
            int row = 1;

            for (Tarif tarif : tarifler) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setTarifData(tarif);

                if (col == 3) {
                    col = 0;
                    row++;
                }
                if (grid != null)
                {
                    grid.add(anchorPane, col++, row);
                    GridPane.setMargin(anchorPane, new Insets(10));
                }
            }

            int malzemeCol = 0;
            int malzemeRow = 1;

            for (Malzeme malzeme : malzemeler) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/malzeme_item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setMalzemeData(malzeme);

                if (malzemeEkleGrid != null)
                {
                    malzemeEkleGrid.add(anchorPane, malzemeCol, malzemeRow++);
                    GridPane.setMargin(anchorPane, new Insets(0, 0, 1, 0));
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

//===============TARİF ARAYAN METOT================
    @FXML
    public void searchButtonOnAction() {
        String arananTarif = searchfield.getText().trim().toLowerCase();
        List<Tarif> aramaListesi;

        if (!ItemController.filtreliTariflerID.isEmpty()) {
            aramaListesi = ItemController.filtreliTariflerID;
            System.out.println("Filtreli tarif listesinde arama yapılıyor...");
        } else {
            aramaListesi = DatabaseConnection.getTarifler();
            System.out.println("Tüm tarifler üzerinde arama yapılıyor...");
        }

        List<Tarif> bulunanTarifler = new ArrayList<>();

        for (Tarif tarif : aramaListesi) {
            if (tarif.getTarifAdi().trim().toLowerCase().contains(arananTarif)) {
                bulunanTarifler.add(tarif);
            }
        }

        if (!bulunanTarifler.isEmpty()) {
            System.out.println(arananTarif);
            System.out.println("Tarif bulundu!");

            grid.getChildren().clear();

            int col = 0;
            int row = 1;

            for (Tarif bulunanTarif : bulunanTarifler) {
                FXMLLoader loader;
                try {
                    //loader.setLocation(new File("/Users/melisportakal/Desktop/geritusu/src/main/resources/com/example/yazlabb/tarif_scene.fxml").toURI().toURL());
                    loader = new FXMLLoader(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
                    AnchorPane tarifNode = loader.load();
                    ItemController controller = loader.getController();

                    controller.setTarifData(bulunanTarif);

                    if (col == 3) {
                        col = 0;
                        row++;
                    }

                    grid.add(tarifNode, col++, row);
                    GridPane.setMargin(tarifNode, new Insets(10));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Tarif bulunamadı!");
            showAlert("Tarif bulunamadı!");
        }
    }
//===================TIKLANDIĞINDA TARİFE GİDEN METOT=================
    public void showRecipeDetails(Tarif tarif, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/tarif_scene.fxml").toURI().toURL());
        loader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\tarif_scene.fxml").toURI().toURL());

        Parent tarifView = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(tarifView);
        stage.setScene(scene);
        stage.show();

        GUI controller = loader.getController();
        controller.setTarifDetails(tarif);
    }

    public void setTarifDetails(Tarif tarif) {
        seciliTarifAdi.setText(tarif.getTarifAdi());
        seciliTarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");
        seciliTarifTalimat.setText(tarif.getTalimatlar());
    }
//=====================================================================

//================TIKLANDIĞINDA ANA MENÜYE DÖNEN METOT=================
    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/deneme.fxml").toURI().toURL());
        loader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\deneme.fxml").toURI().toURL());

        Parent mainMenuView = loader.load();

        Scene scene = new Scene(mainMenuView);

        //String css = new File("/Users/melisportakal/Desktop/filtreleme/views/style.css").toURI().toURL().toExternalForm();
        String css = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\views\\style.css").toURI().toURL().toExternalForm();

        scene.getStylesheets().add(css);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

//==================BİLDİRİ GÖSTEREN METOT==================
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bildiri");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//==================MALZEME EKLEYEN METOT===================
    @FXML
    private void showAddMalzemeDialog() {
        Dialog<Malzeme> dialog = new Dialog<>();
        dialog.setTitle("Malzeme Ekle");
        dialog.setHeaderText("Yeni Malzeme Bilgilerini Girin");

        ButtonType ekleButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ekleButtonType, ButtonType.CANCEL);

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
                float toplamMiktar = Float.parseFloat(malzemeMiktarField.getText());
                float birimFiyat = Float.parseFloat(malzemeFiyatField.getText());

                boolean isValid = true;

                DatabaseConnection.addMalzeme(malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);
                showAlert("Malzeme başarıyla eklendi.");
                updateMalzemeGridPane();
            }


            return null;
        });

        dialog.showAndWait();

        //updateMalzemeGridPane();
    }

    //TARİF EKLEMEYİ GĞNCEKKEDİM
    @FXML
    private void showAddTarifDialog() throws SQLException, IOException {
        Dialog<Tarif> dialog = new Dialog<>();
        dialog.setTitle("Tarif Ekle");
        dialog.setHeaderText("Yeni Tarif Bilgilerini Girin");

        ButtonType ekleButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        ButtonType malzemeEkleButtonType = new ButtonType("Malzeme Ekle", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(ekleButtonType, malzemeEkleButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField tarifAdiField = new TextField();
        tarifAdiField.setPromptText("Tarif Adı");
        TextField hazirlanisSuresiField = new TextField();
        hazirlanisSuresiField.setPromptText("Hazırlanış Süresi (dakika)");
        TextField kategoriField = new TextField();
        kategoriField.setPromptText("Kategori");
        TextArea talimatlarField = new TextArea();
        talimatlarField.setPromptText("Talimatlar");
        talimatlarField.setWrapText(true);

        gridPane.add(new Label("Tarif Adı:"), 0, 0);
        gridPane.add(tarifAdiField, 1, 0);
        gridPane.add(new Label("Hazırlanış Süresi:"), 0, 1);
        gridPane.add(hazirlanisSuresiField, 1, 1);
        gridPane.add(new Label("Kategori:"), 0, 2);
        gridPane.add(kategoriField, 1, 2);
        gridPane.add(new Label("Talimatlar:"), 0, 3);
        gridPane.add(talimatlarField, 1, 3);

        dialog.getDialogPane().setContent(gridPane);

        // Malzemeleri tutacak bir liste oluştur
        List<TarifinMalzemeleri> malzemeListesi = new ArrayList<>();

        // Malzeme ekleme butonunun işlevi
        dialog.getDialogPane().lookupButton(malzemeEkleButtonType).addEventFilter(ActionEvent.ACTION, event -> {
            // Malzeme ekleme diyalogu aç
            Dialog<Malzeme> malzemeDialog = new Dialog<>();
            malzemeDialog.setTitle("Malzeme Ekle");
            malzemeDialog.setHeaderText("Malzeme Bilgilerini Girin");

            ButtonType malzemeEkleButton = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
            malzemeDialog.getDialogPane().getButtonTypes().addAll(malzemeEkleButton, ButtonType.CANCEL);

            GridPane malzemeGridPane = new GridPane();
            malzemeGridPane.setHgap(10);
            malzemeGridPane.setVgap(10);
            malzemeGridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField malzemeAdiField = new TextField();
            malzemeAdiField.setPromptText("Malzeme Adı");
            TextField toplamMiktarField = new TextField();
            toplamMiktarField.setPromptText("Miktar");
            TextField malzemeBirimField = new TextField();
            malzemeBirimField.setPromptText("Birim");
            TextField malzemeBirimFiyatField = new TextField();
            malzemeBirimFiyatField.setPromptText("Birim Fiyatı");

            malzemeGridPane.add(new Label("Malzeme Adı:"), 0, 0);
            malzemeGridPane.add(malzemeAdiField, 1, 0);
            malzemeGridPane.add(new Label("Miktar:"), 0, 1);
            malzemeGridPane.add(toplamMiktarField, 1, 1);
            malzemeGridPane.add(new Label("Birim:"), 0, 2);
            malzemeGridPane.add(malzemeBirimField, 1, 2);
            malzemeGridPane.add(new Label("Birim Fiyatı:"), 0, 3);
            malzemeGridPane.add(malzemeBirimFiyatField, 1, 3);

            malzemeDialog.getDialogPane().setContent(malzemeGridPane);

            malzemeDialog.setResultConverter(malzemeDialogButton -> {
                if (malzemeDialogButton == malzemeEkleButton) {
                    TarifinMalzemeleri yeniMalzeme = new TarifinMalzemeleri();

                    yeniMalzeme.setMalzemeAdiT(malzemeAdiField.getText());
                    yeniMalzeme.setMalzemeMiktarT(Float.parseFloat(toplamMiktarField.getText()));
                    yeniMalzeme.setMalzemeBirimT(malzemeBirimField.getText());
                    yeniMalzeme.setMalzemeBirimFiyatT(Integer.parseInt(malzemeBirimFiyatField.getText()));

                    // Malzemeyi veritabanına ekle ve geri dönen ID'yi al
                    int malzemeIDT = DatabaseConnection.TarifinMalzemesiniEkle(yeniMalzeme.getMalzemeAdiT(), yeniMalzeme.getMalzemeMiktarT(), yeniMalzeme.getMalzemeBirimT(), yeniMalzeme.getMalzemeBirimFiyatT());
                    if (malzemeIDT != -1) { // Hata yoksa
                        // Malzemeyi listeye ekle
                    yeniMalzeme.setMalzemeIdT(malzemeIDT);
                        malzemeListesi.add(yeniMalzeme);
                        showAlert("Malzeme başarıyla eklendi.");
                    } else {
                        showAlert("Malzeme eklenirken hata oluştu.");
                    }

                }
                return null;
            });

            malzemeDialog.showAndWait();
            event.consume(); // Butonun varsayılan davranışını durdur
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ekleButtonType) {
                String tarifAdi = tarifAdiField.getText();
                int hazirlanisSuresi = Integer.parseInt(hazirlanisSuresiField.getText());
                String kategori = kategoriField.getText();
                String talimatlar = talimatlarField.getText();

                // Tarif ekleme
                int tarifID = DatabaseConnection.addTarif(tarifAdi, kategori, hazirlanisSuresi, talimatlar);
                if (tarifID != -1) { // Tarif ekleme başarılıysa
                    // Malzemeleri ekle
                    for (TarifinMalzemeleri tarifinMalzemeleri : malzemeListesi) {
                        int malzemeID = tarifinMalzemeleri.getMalzemeIdT();
                        float malzemetoplammiktar =tarifinMalzemeleri.getMalzemeMiktarT();
                        addMalzemeToTarif(tarifID, malzemeID,malzemetoplammiktar);
                    }


                    showAlert("Tarif ve malzemeler başarıyla eklendi.");
                } else {
                    showAlert("Tarif eklenirken hata oluştu.");
                }
            }
            return null;
        });

        dialog.showAndWait();
        mainMenu(); //ana menüyü güncelle
    }





    //================TARİF SİLEN METOT=====================
    @FXML
    private void showRemoveTarifDialog() throws SQLException, IOException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Tarif Sil");
        dialog.setHeaderText("Silmek İstediğiniz Tarifin Bilgilerini Girin");

        ButtonType tarifSilButton = new ButtonType("Sil", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(tarifSilButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField tarifAdiField = new TextField();
        tarifAdiField.setPromptText("Tarif Adı");

        gridPane.add(new Label("Tarif Adı:"), 0, 0);
        gridPane.add(tarifAdiField, 1, 0);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == tarifSilButton) {
                String tarifAdi = tarifAdiField.getText();

                if (tarifAdi.isEmpty()) {
                    //showAlert("Hata", "Lütfen tarif adını girin.");
                    System.out.println("Hata: Tarif adı boş bırakılmamalı.");
                    return null;
                }

                try {
                    DatabaseConnection.deleteTarif(tarifAdi);
                    System.out.println("Tarif silindi.");
                    showAlert("Tarif başarıyla silindi.");
                } catch (Exception e) {
                    System.out.println("Hata: Tarif silinirken bir sorun oluştu.");
                }
            }
            return null;
        });

        dialog.showAndWait();
        mainMenu();
    }





    //================MALZEME SİLEN METOT=====================
    @FXML
    private void showRemoveMalzemeDialog() {
        Dialog<Malzeme> dialog = new Dialog<>();
        dialog.setTitle("Malzeme Sil");
        dialog.setHeaderText("Silmek İstediğiniz Malzemenin Bilgilerini Girin");

        ButtonType malzemeSilButton = new ButtonType("Sil", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(malzemeSilButton, ButtonType.CANCEL);

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


        gridPane.add(new Label("Malzeme Adı:"), 0, 0);
        gridPane.add(malzemeAdiField, 1, 0);
        gridPane.add(new Label("Malzeme Birimi:"), 0, 1);
        gridPane.add(malzemeBirimField, 1, 1);
        gridPane.add(new Label("Malzeme Miktarı:"), 0, 2);
        gridPane.add(malzemeMiktarField, 1, 2);


        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == malzemeSilButton) {
                String malzemeAdi = malzemeAdiField.getText();
                String malzemeBirim = malzemeBirimField.getText();
                String miktarText = malzemeMiktarField.getText();


                if (malzemeAdi.isEmpty() || malzemeBirim.isEmpty() || miktarText.isEmpty()) {
                    //showAlert("Hata", "Lütfen tüm alanları doldurun.");
                    System.out.println("hataa bos bırakma");
                    return null;
                }

                try {
                    float toplamMiktar = Float.parseFloat(miktarText);

                    DatabaseConnection.deleteMalzeme(malzemeAdi, toplamMiktar, malzemeBirim);
                    System.out.println("sildin");
                    showAlert("Malzeme başarıyla silindi.");
                } catch (NumberFormatException e) {
                    System.out.println("hataa geldin");
                }

                updateMalzemeGridPane();
            }
            return null;
        });

        dialog.showAndWait();
        //updateMalzemeGridPane();
    }

//================MALZEME EKLEYİP SİLDİKTEN SONRA PANELİ GÜNCELLEYEN METOT====================
    public void updateMalzemeGridPane() {
        malzemeEkleGrid.getChildren().clear();

        List<Malzeme> malzemeList = DatabaseConnection.getMalzemeler();

        for (Malzeme malzeme : malzemeList) {
            FXMLLoader loader = null;
            try {
                loader = new FXMLLoader(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/malzeme_item.fxml").toURI().toURL());
                //loader = new FXMLLoader(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml").toURI().toURL());

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                Node malzemeNode = loader.load();
                ItemController controller = loader.getController();
                controller.setMalzemeData(malzeme);
                malzemeEkleGrid.add(malzemeNode, 0, malzemeEkleGrid.getRowCount());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//===================ANA MENÜYE GERİ GÖTÜREN METOT=====================
    @FXML
    public void mainMenu() throws SQLException, IOException {
        grid.getChildren().clear();

        List<Tarif> tarifler = getTarifler();

        int col = 0;
        int row = 1;

        for (int i = 0; i < tarifler.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/filtreleme/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\findoyurmeal\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            AnchorPane anchorPane = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setTarifData(tarifler.get(i));

            if (col == 3) {
                col = 0;
                row++;
            }

            grid.add(anchorPane, col++, row);
            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }
}