package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static Controller.DatabaseConnection.addMalzemeToTarif;

public class GUI implements Initializable {
    @FXML
    private Button malzemeEkleButton;

    @FXML
    private AnchorPane rootpane;

    @FXML
    private Button malzemeSilButton;

    @FXML
    private BorderPane borderpane;

    @FXML
    private HBox hbox;

    @FXML
    private VBox vbox;

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

    @FXML
    private ComboBox<String> ComboBox;



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
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());

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
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/malzeme_item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml").toURI().toURL());

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

        List<String> KategoriListesi = new ArrayList<>();
        KategoriListesi.add("Tatlı");
        KategoriListesi.add("Çorba");
        KategoriListesi.add("Ana Yemek");
        KategoriListesi.add("Salata");
        KategoriListesi.add("Kahvaltılık");

        if (!ItemController.filtreliTariflerID.isEmpty()) {
            aramaListesi = ItemController.filtreliTariflerID;
            System.out.println("Filtreli tarif listesinde arama yapılıyor...");
        }

        else{
            aramaListesi = DatabaseConnection.getTarifler();
            System.out.println("Tüm tarifler üzerinde arama yapılıyor...");

            // Kategoride arama kontrolü
            if (KategoriListesi.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(arananTarif)) {
                System.out.println("Kategori araması yapılıyor: " + arananTarif);
                aramaListesi = DatabaseConnection.KategoriBul(arananTarif); // Kategoriyi sorgula
            }

        }

        List<Tarif> bulunanTarifler = new ArrayList<>();

        // Eğer kategori bulunmuşsa bu listeyi direk ekle
        if (aramaListesi != null && !aramaListesi.isEmpty()) {
            if (KategoriListesi.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(arananTarif)) {
                // Kategoriye göre arama yaptıysanız tüm sonuçları direk ekleyin
                bulunanTarifler.addAll(aramaListesi);
            } else {
                // Aksi halde tarif adı üzerinden arama yapın
                for (Tarif tarif : aramaListesi) {
                    if (tarif.getTarifAdi().trim().toLowerCase().contains(arananTarif)) {
                        bulunanTarifler.add(tarif);
                    }
                }
            }
        }

        if (!bulunanTarifler.isEmpty()) {
            System.out.println(arananTarif);
            System.out.println("Tarif bulundu!");

            grid.getChildren().clear();

            int col = 0;
            int row = 1;

            for (Tarif bulunanTarif : bulunanTarifler) {
                //FXMLLoader loader = null;
                FXMLLoader loader = new FXMLLoader();
                try {
                    //loader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                    loader = new FXMLLoader(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
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
        //loader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/tarif_scene.fxml").toURI().toURL());
        loader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\tarif_scene.fxml").toURI().toURL());

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
        //loader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/deneme.fxml").toURI().toURL());
        loader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\deneme.fxml").toURI().toURL());

        Parent mainMenuView = loader.load();

        Scene scene = new Scene(mainMenuView);

        //String css = new File("/Users/melisportakal/Desktop/resimli/views/style.css").toURI().toURL().toExternalForm();
        String css = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\views\\style.css").toURI().toURL().toExternalForm();

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
                int birimFiyat = Integer.parseInt(malzemeFiyatField.getText());

                boolean isValid = true;

                DatabaseConnection.addMalzeme(malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);
                showAlert("Malzeme başarıyla eklendi.");
                updateMalzemeGridPane();
            }

            return null;
        });

        dialog.showAndWait();
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
        gridPane.setPadding(new Insets(20, 40, 10, 10));

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

        GridPane malzemeListele = new GridPane();
        malzemeListele.setHgap(10);
        malzemeListele.setVgap(10);
        malzemeListele.setPadding(new Insets(20, 20, 20, 20));

        List<Malzeme> malzemeler = getMalzemeler();

        int malzemeCol = 0;
        int malzemeRow = 1;

        for (Malzeme malzeme : malzemeler) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item_tarif_ekleme.fxml").toURI().toURL());

            AnchorPane anchorPane = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setMalzemeListeleData(malzeme);

            if (malzemeListele != null) {
                malzemeListele.add(anchorPane, malzemeCol, malzemeRow++);
                GridPane.setMargin(anchorPane, new Insets(0, 0, 1, 0));
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(malzemeListele);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setPrefWidth(250);

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(gridPane, scrollPane);
        hbox.setPadding(new Insets(10, 10, 10, 10));

        dialog.getDialogPane().setContent(hbox);

        List<Malzeme> malzemeListesi = new ArrayList<>();

        dialog.getDialogPane().lookupButton(malzemeEkleButtonType).addEventFilter(ActionEvent.ACTION, event -> {
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
                    Malzeme yeniMalzeme = new Malzeme();

                    yeniMalzeme.setMalzemeAdi(malzemeAdiField.getText());
                    yeniMalzeme.setToplamMiktar(Float.parseFloat(toplamMiktarField.getText()));
                    yeniMalzeme.setMalzemeBirim(malzemeBirimField.getText());
                    yeniMalzeme.setMalzemeBirimFiyat(Integer.parseInt(malzemeBirimFiyatField.getText()));

                    int mazemeIDT = DatabaseConnection.addMalzeme(yeniMalzeme.getMalzemeAdi(), 0, yeniMalzeme.getMalzemeBirim(), yeniMalzeme.getMalzemeBirimFiyat());
                    if (mazemeIDT != -1) {
                        yeniMalzeme.setMazemeID(mazemeIDT);
                        malzemeListesi.add(yeniMalzeme);
                        showAlert("Malzeme başarıyla eklendi.");
                        updateMalzemeGridPane();
                    } else {
                        showAlert("Malzeme eklenirken hata oluştu.");
                    }
                }
                return null;
            });

            malzemeDialog.showAndWait();
            event.consume();
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ekleButtonType) {
                String tarifAdi = tarifAdiField.getText();
                int hazirlanisSuresi = Integer.parseInt(hazirlanisSuresiField.getText());
                String kategori = kategoriField.getText();
                String talimatlar = talimatlarField.getText();

                int tarifID = DatabaseConnection.addTarif(tarifAdi, kategori, hazirlanisSuresi, talimatlar);
                if (tarifID != -1) {
                    for (Malzeme TarifMalzeme : malzemeListesi) {
                        int malzemeID = TarifMalzeme.getMazemeID();
                        float malzemetoplammiktar = TarifMalzeme.getToplamMiktar();
                        addMalzemeToTarif(tarifID, malzemeID, malzemetoplammiktar);
                    }
                    showAlert("Tarif ve malzemeler başarıyla eklendi.");
                } else {
                    showAlert("Bu tarif zaten mevcut.");
                }
            }
            return null;
        });

        dialog.showAndWait();
        mainMenu();
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
                //loader = new FXMLLoader(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/malzeme_item.fxml").toURI().toURL());
                loader = new FXMLLoader(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml").toURI().toURL());

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
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
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

    @FXML
    public void mainMenu(List<Tarif> tarifler) throws SQLException, IOException {
        grid.getChildren().clear();

        int col = 0;
        int row = 1;

        for (int i = 0; i < tarifler.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                //fxmlLoader.setLocation(new File("/Users/melisportakal/Desktop/resimli/src/main/resources/com/example/yazlabb/item.fxml").toURI().toURL());
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
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

//=====================SEÇENEKLE SIRALAMA===========================
    @FXML
    public void ComboBoxSort() throws SQLException, IOException {
        List<Tarif> tarifler;

        if (!ItemController.filtreliTariflerID.isEmpty()) {
            tarifler = ItemController.filtreliTariflerID;
        } else {
            tarifler = getTarifler();
        }

        String selectedOption = ComboBox.getSelectionModel().getSelectedItem();

        //ÇOKTAN AZA MALİYET
        if("Çoktan aza maliyet".equals(selectedOption)) {
            System.out.println("a");

        //AZDAN ÇOKA MALİYET
        } else if ("Azdan çoka maliyet".equals(selectedOption)) {
            System.out.println("b");

        //ÇOKTAN AZA SÜRE
        } else if ("Çoktan aza süre".equals(selectedOption)) {
            System.out.println("c");
            tarifler.sort(Comparator.comparingInt(Tarif::getHazirlamaSuresi).reversed());
            System.out.println("Tarifler süreye göre çoktan aza sıralandı.");
            for (Tarif tarif : tarifler) {
                System.out.println(tarif.getTarifAdi() + " süresi: " + tarif.getHazirlamaSuresi());
            }
            mainMenu(tarifler);

        //AZDAN ÇOKA SÜRE
        } else if ("Azdan çoka süre".equals(selectedOption)) {
            System.out.println("d");
            tarifler.sort(Comparator.comparingInt(Tarif::getHazirlamaSuresi));
            System.out.println("Tarifler süreye göre azdan çoka sıralandı.");
            for (Tarif tarif : tarifler) {
                System.out.println(tarif.getTarifAdi() + " süresi: " + tarif.getHazirlamaSuresi());
            }
            mainMenu(tarifler);
        }

        //MALZEME ORANINA GÖRE
        else if ("Malzeme oranına göre".equals(selectedOption)) {
            List<TarifBilgileri> tarifbilgileri = new ArrayList<>();

            for (int i = 0; i < tarifler.size(); i++) {
                int tarifID = tarifler.get(i).getTarifID();

                int malzemeSayisi = DatabaseConnection.toplamMalzemeSayisi(tarifID);
                Map<Integer, String> eksikMalzemeler = DatabaseConnection.EksikMalzemeler(tarifID);

                TarifBilgileri tarifBilgi = new TarifBilgileri(tarifID, malzemeSayisi, eksikMalzemeler);
                tarifbilgileri.add(tarifBilgi);
            }

            // Sıralama: önce eksik malzeme olmayanları, sonra malzeme sayısını göz önünde bulundurarak
            tarifbilgileri.sort(Comparator.comparingInt((TarifBilgileri t) -> {
                Map<Integer, String> eksikMalzemeMap = t.getEksikMalzemeler();
                return (eksikMalzemeMap == null || eksikMalzemeMap.isEmpty()) ? 0 : 1;
            }).thenComparingInt(TarifBilgileri::getMalzemeSayisi));


            //==========ANA MENÜYE LİSTEYİ YAZDIRMAK İÇİN BU
            // Sıralanan tarif bilgilerine göre tarifler listesini güncelleme
            List<Tarif> sortedTarifler = new ArrayList<>();
            for (TarifBilgileri tarifBilgi : tarifbilgileri) {
                // Her tarifBilgisi için tarifID kullanarak ilgili tarifi bul
                for (Tarif tarif : tarifler) {
                    if (tarif.getTarifID() == tarifBilgi.getTarifId()) {
                        sortedTarifler.add(tarif);
                        break; // Tarif bulundu, döngüden çık
                    }
                }
            }

            // Sıralanmış tarifleri yazdırma
            for (Tarif tarif : sortedTarifler) {
                System.out.println("Tarif ID: " + tarif.getTarifID() +
                        ", Tarif Adı: " + tarif.getTarifAdi() + // Tarif adı da yazdırılıyor, varsayılan bir alan ekledim
                        ", Malzeme Sayısı: " + DatabaseConnection.toplamMalzemeSayisi(tarif.getTarifID())); // Malzeme sayısı tekrar alınıyor


                //SORTEDTARİFLER HEM MALZEME SAYISINA GÖRE HEM DE KAÇ MALZEME EKSİK OLDUĞUNA GÖRE SIRALANIYOR
                //BU LİSTEYİ EKRANA BASTIRCAZ Bİ DE EKSİK MALZEMELERİ TARİFE YAZDIRCAZ KIRMIZI OLCAK
            /*
            for (TarifBilgileri tarifBilgi : tarifbilgileri) {
                System.out.println("Tarif ID: " + tarifBilgi.getTarifId() +
                        ", Malzeme Sayısı: " + tarifBilgi.getMalzemeSayisi() +
                        ", Eksik Malzemeler: ");

                Map<Integer, String> eksikMalzemeMap = tarifBilgi.getEksikMalzemeler();
                if (eksikMalzemeMap != null && !eksikMalzemeMap.isEmpty()) {
                    for (Map.Entry<Integer, String> entry : eksikMalzemeMap.entrySet()) {
                        int malzemeID = entry.getKey();
                        String malzemeAdi = entry.getValue();
                        System.out.println("    Malzeme ID: " + malzemeID + ", Malzeme Adı: " + malzemeAdi);
                    }
                } else {
                    System.out.println("    Hiç eksik malzeme bulunamadı.");
                }*/
            }

            mainMenu(sortedTarifler);
        }



        //SEÇENEK SEÇİLMEDİ
        else if (selectedOption == null) {
            System.out.println("Hiçbir seçenek seçilmedi.");
        }
    }
}