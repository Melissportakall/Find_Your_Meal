package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static Controller.DatabaseConnection.*;
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
    private GridPane seciliTarifMalzeme;

    @FXML
    private ImageView seciliTarifImage;

    @FXML
    private AnchorPane seciliTarifRoot;

    @FXML
    private StackPane seciliTarifStackPane;

    @FXML
    private VBox seciliTariVBox;

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

                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/item.fxml");
                }

                fxmlLoader.setLocation(filePath.toURI().toURL());


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

                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/eksikmalzemedetay/src/main/resources/com/example/yazlabb/malzeme_item.fxml");
                }

                fxmlLoader.setLocation(filePath.toURI().toURL());


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
                FXMLLoader loader = new FXMLLoader();
                try {
                    File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml");

                    if (!filePath.exists()) {
                        filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/item.fxml");
                    }

                    loader.setLocation(filePath.toURI().toURL());


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

        File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\tarif_scene.fxml");

        if (!filePath.exists()) {
            filePath = new File("/Users/melisportakal/Desktop/eksikmalzemedetay/src/main/resources/com/example/yazlabb/tarif_scene.fxml");
        }

        loader.setLocation(filePath.toURI().toURL());



        Parent tarifView = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(tarifView);
        stage.setScene(scene);
        stage.show();

        GUI controller = loader.getController();
        controller.setTarifDetails(tarif);
    }

    //=================TIKLANAN TARİF DETAYLARINI GETİR=================
    public void setTarifDetails(Tarif tarif) {
        seciliTarifAdi.setText(tarif.getTarifAdi());

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(1);
        shadow.setOffsetY(1);
        shadow.setRadius(5);
        seciliTarifAdi.setEffect(shadow);

        seciliTarifSure.setText(tarif.getHazirlamaSuresi() + " dakika");
        seciliTarifTalimat.setText(tarif.getTalimatlar());

        seciliTarifMalzeme.getChildren().clear();

        List<Malzeme> malzemeList = DatabaseConnection.TarifinMalzemeleri(tarif.getTarifID());

        float toplamEksikMaliyet = 0;

        for (int i = 0; i < malzemeList.size(); i++) {
            Malzeme malzeme = malzemeList.get(i);
            String malzemeBilgisi = malzeme.getMalzemeAdi() + " - " + malzeme.getToplamMiktar() + " " + malzeme.getMalzemeBirim();
            Label malzemeLabel = new Label(malzemeBilgisi);
            malzemeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
            seciliTarifMalzeme.add(malzemeLabel, 0, i);
        }

        List<Malzeme> eksikMalzemeList = DatabaseConnection.EksikMalzemeler(tarif.getTarifID());
        if (!eksikMalzemeList.isEmpty()) {
            Label eksikMalzemeLabel = new Label("Eksik Malzemeler:");
            eksikMalzemeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");
            seciliTarifMalzeme.add(eksikMalzemeLabel, 0, malzemeList.size() + 1);

            for (int i = 0; i < eksikMalzemeList.size(); i++) {
                Malzeme eksikMalzeme = eksikMalzemeList.get(i);

                // Eksik miktar ve maliyeti hesapla
                float eksikMiktar = eksikMalzeme.getToplamMiktar();
                float birimFiyat = eksikMalzeme.getMalzemeBirimFiyat();
                float maliyet = eksikMiktar * birimFiyat;
                toplamEksikMaliyet += maliyet;

                String eksikMalzemeBilgisi = eksikMalzeme.getMalzemeAdi() + " - Eksik Miktar: " + eksikMiktar +
                        " " + eksikMalzeme.getMalzemeBirim() + " - Maliyet: " + maliyet + " TL";
                Label eksikMalzemeLabelItem = new Label(eksikMalzemeBilgisi);
                eksikMalzemeLabelItem.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");
                seciliTarifMalzeme.add(eksikMalzemeLabelItem, 0, malzemeList.size() + 2 + i);
            }

            Label toplamEksikMaliyetLabel = new Label("Toplam Eksik Maliyet: " + toplamEksikMaliyet + " TL");
            toplamEksikMaliyetLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #811818;");
            toplamEksikMaliyetLabel.setUnderline(true);
            seciliTarifMalzeme.add(toplamEksikMaliyetLabel, 0, malzemeList.size() + 2 + eksikMalzemeList.size());
        }

        File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\img\\" + tarif.getTarifID() + ".jpg");
        if (!filePath.exists()) {
            filePath = new File("/Users/melisportakal/desktop/iyilestirmelermis/img/" + tarif.getTarifID() + ".jpg");
        }

        Image image = new Image(filePath.toURI().toString());
        seciliTarifImage.setImage(image);
        seciliTarifImage.setPreserveRatio(true);
        seciliTarifImage.fitWidthProperty().bind(seciliTarifImage.getScene().widthProperty());

        seciliTarifImage.setFitHeight(image.getHeight());

        Rectangle mask = new Rectangle();
        mask.setWidth(seciliTarifImage.getFitWidth());
        mask.setHeight(seciliTarifImage.getFitHeight());
        mask.widthProperty().bind(seciliTarifImage.fitWidthProperty());
        mask.heightProperty().bind(seciliTarifImage.fitHeightProperty());
        mask.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.2, Color.TRANSPARENT), // Transparent at the top
                new Stop(0.4, Color.web("#ffe6b2")), // Approximate midpoint color
                new Stop(1, Color.web("#f5e0d2")) // Final color at the bottom
        ));


        seciliTarifStackPane.getChildren().addAll(mask);

        StackPane.setAlignment(seciliTarifImage, Pos.TOP_LEFT);
        StackPane.setAlignment(mask, Pos.TOP_LEFT);
        StackPane.setAlignment(seciliTarifMalzeme, Pos.BOTTOM_LEFT);
    }

//=====================================================================

//================TIKLANDIĞINDA ANA MENÜYE DÖNEN METOT=================
    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\deneme.fxml");

        if (!filePath.exists()) {
            filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/deneme.fxml");
        }

        loader.setLocation(filePath.toURI().toURL());


        Parent mainMenuView = loader.load();

        Scene scene = new Scene(mainMenuView);

        String css;

        File cssFile = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\views\\style.css");

        if (!cssFile.exists()) {
            cssFile = new File("/Users/melisportakal/Desktop/sonins/views/style.css");
        }

        css = cssFile.toURI().toURL().toExternalForm();


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
        alert.getDialogPane().setStyle("-fx-background-color: #f1c15b;"); // Arka plan rengini turuncu yapar
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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

        dialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");

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

    //TARİF EKLEYEN METOT
    @FXML
    private void showAddTarifDialog() throws SQLException, IOException {
        Dialog<Tarif> dialog = new Dialog<>();
        dialog.setTitle("Tarif Ekle");
        dialog.setHeaderText("Yeni Tarif Bilgilerini Girin");

        ButtonType ekleButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
        ButtonType malzemeEkleButtonType = new ButtonType("Malzeme Ekle", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(ekleButtonType, malzemeEkleButtonType, ButtonType.CANCEL);

        dialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");

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

            File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item_tarif_ekleme.fxml");

            if (!filePath.exists()) {
                filePath = new File("/Users/melisportakal/Desktop/olmusheralde/src/main/resources/com/example/yazlabb/malzeme_item_tarif_ekleme.fxml");
            }

            fxmlLoader.setLocation(filePath.toURI().toURL());

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

            malzemeDialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");

            malzemeDialog.setResultConverter(malzemeDialogButton -> {
                if (malzemeDialogButton == malzemeEkleButton) {
                    Malzeme yeniMalzeme = new Malzeme();

                    yeniMalzeme.setMalzemeAdi(malzemeAdiField.getText());
                    yeniMalzeme.setToplamMiktar(Float.parseFloat(toplamMiktarField.getText()));
                    yeniMalzeme.setMalzemeBirim(malzemeBirimField.getText());
                    yeniMalzeme.setMalzemeBirimFiyat(Integer.parseInt(malzemeBirimFiyatField.getText()));

                    int mazemeIDT = DatabaseConnection.addMalzeme(yeniMalzeme.getMalzemeAdi(), 0, yeniMalzeme.getMalzemeBirim(), yeniMalzeme.getMalzemeBirimFiyat());
                    if (mazemeIDT != -1) {
                        yeniMalzeme.setMalzemeID(mazemeIDT);
                        malzemeListesi.add(yeniMalzeme);
                        showAlert("Malzeme başarıyla eklendi.");

                        updateMalzemeGridPane();

                        try {
                            updateMalzemeListele(malzemeListele);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        showAlert("Malzeme eklenirken hata oluştu.");
                    }
                }
                return null;
            });

            malzemeDialog.showAndWait();
            event.consume();
        });

        TextField resimPathField = new TextField();
        resimPathField.setPromptText("Resim Yolu");

        Button resimSecButton = new Button("Resim Seç");
        resimSecButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                resimPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        gridPane.add(new Label("Resim Yolu:"), 0, 4);
        gridPane.add(resimPathField, 1, 4);
        gridPane.add(resimSecButton, 2, 4);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ekleButtonType) {
                String tarifAdi = tarifAdiField.getText();
                int hazirlanisSuresi = Integer.parseInt(hazirlanisSuresiField.getText());
                String kategori = kategoriField.getText();
                String talimatlar = talimatlarField.getText();
                String resimYolu = resimPathField.getText();

                try {
                    if (DatabaseConnection.tarifVarMi(tarifAdi)) {
                        showAlert("Bu tarif zaten mevcut.");
                    } else {
                        int tarifID = DatabaseConnection.addTarif(tarifAdi, kategori, hazirlanisSuresi, talimatlar);
                        for (Malzeme malzeme : malzemeListesi) {
                            int malzemeID = malzeme.getMazemeID();
                            float malzememiktar = malzeme.getToplamMiktar();
                            addMalzemeToTarif(tarifID, malzemeID, malzememiktar);
                        }

                        if (tarifID != -1) {
                            addMalzemeToTarif2(tarifID, ItemController.seciliMalzemeler);
                            saveImage(resimYolu, tarifID);
                            showAlert("Tarif ve malzemeler başarıyla eklendi.");
                        } else {
                            showAlert("Tarif eklenirken hata oluştu.");
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        dialog.showAndWait();
        mainMenu();
        ItemController.seciliMalzemeler.clear();
    }

    private void saveImage(String resimYolu, int tarifID) {
        try {
            File sourceFile = new File(resimYolu);

            File destinationFile = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\img\\" + tarifID + ".jpg");
            if (!destinationFile.exists()) {
                destinationFile = new File("/Users/melisportakal/desktop/iyilestirmelermis/img/" + tarifID + ".jpg");
            }

            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            showAlert("Resim kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    //================TARİF SİLEN METOT=====================
    @FXML
    private void showRemoveTarifDialog() throws SQLException, IOException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Tarif Sil");
        dialog.setHeaderText("Silmek İstediğiniz Tarifin Bilgilerini Girin");

        dialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");


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

        dialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");


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
    }

//================MALZEME EKLEYİP SİLDİKTEN SONRA PANELİ GÜNCELLEYEN METOT====================
    public void updateMalzemeGridPane() {
        malzemeEkleGrid.getChildren().clear();

        List<Malzeme> malzemeList = DatabaseConnection.getMalzemeler();

        for (Malzeme malzeme : malzemeList) {
            FXMLLoader loader = new FXMLLoader();
            try {
                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/malzeme_item.fxml");
                }

                loader.setLocation(filePath.toURI().toURL());


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
                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/item.fxml");
                }

                fxmlLoader.setLocation(filePath.toURI().toURL());

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
    public void mainMenu(List<Tarif> tarifler) throws IOException {
        grid.getChildren().clear();

        int col = 0;
        int row = 1;

        for (int i = 0; i < tarifler.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/item.fxml");
                }

                fxmlLoader.setLocation(filePath.toURI().toURL());


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
    public void mainMenu(List<Tarif> tarifler, Map<Tarif, List<Malzeme>> eksikTarifler) throws IOException {
        grid.getChildren().clear();

        int col = 0;
        int row = 1;

        for (int i = 0; i < tarifler.size(); i++) {
            Tarif currentTarif = tarifler.get(i);

            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml");

                if (!filePath.exists()) {
                    filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/item.fxml");
                }

                fxmlLoader.setLocation(filePath.toURI().toURL());

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            AnchorPane anchorPane = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setTarifData(currentTarif);

            if (eksikTarifler.containsKey(currentTarif)) {
                itemController.getTarifLabel().setStyle("-fx-text-fill: red;");
                itemController.getTarifKategori().setStyle("-fx-text-fill: red;");
                itemController.getTarifSure().setStyle("-fx-text-fill: red;");
                itemController.getTarifOran().setStyle("-fx-text-fill: red");
            } else {
                itemController.getTarifLabel().setStyle("-fx-text-fill: green;");
                itemController.getTarifKategori().setStyle("-fx-text-fill: green;");
                itemController.getTarifSure().setStyle("-fx-text-fill: green;");
                itemController.getTarifOran().setStyle("-fx-text-fill: green");
            }

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

        //FİLTRELİ OLUP OLMADIĞINI KONTROL ET
        if (!ItemController.filtreliTariflerID.isEmpty()) {
            tarifler = ItemController.filtreliTariflerID;
        } else {
            tarifler = getTarifler();
        }

        String selectedOption = ComboBox.getSelectionModel().getSelectedItem();

        //ÇOKTAN AZA MALİYET
        if("Çoktan aza maliyet".equals(selectedOption)) {
            tarifler.sort((tarif1, tarif2) -> Double.compare(tarif2.getToplamMaliyet(), tarif1.getToplamMaliyet()));
            mainMenu(tarifler);

        //AZDAN ÇOKA MALİYET
        } else if ("Azdan çoka maliyet".equals(selectedOption)) {
            tarifler.sort((tarif1, tarif2) -> Double.compare(tarif1.getToplamMaliyet(), tarif2.getToplamMaliyet()));
            mainMenu(tarifler);

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
            Map<Tarif, List<Malzeme>> eksikTarifler = new HashMap<>();

            for (Tarif tarif : tarifler) {
                int tarifID = tarif.getTarifID();

                int malzemeSayisi = DatabaseConnection.toplamMalzemeSayisi(tarifID);
                List<Malzeme> eksikMalzemeler = DatabaseConnection.EksikMalzemeler(tarifID);

                TarifBilgileri tarifBilgi = new TarifBilgileri(tarifID, malzemeSayisi, eksikMalzemeler);
                tarifbilgileri.add(tarifBilgi);

                if (!eksikMalzemeler.isEmpty()) {
                    eksikTarifler.put(tarif, eksikMalzemeler);
                }
            }

            tarifbilgileri.sort(Comparator.comparingInt((TarifBilgileri t) -> {
                List<Malzeme> eksikMalzemeList = t.getEksikMalzemeler();
                return (eksikMalzemeList == null || eksikMalzemeList.isEmpty()) ? 0 : 1;
            }).thenComparingInt(TarifBilgileri::getMalzemeSayisi));


            //==========ANA MENÜYE LİSTEYİ YAZDIRMAK İÇİN BU
            List<Tarif> sortedTarifler = new ArrayList<>();
            for (TarifBilgileri tarifBilgi : tarifbilgileri) {
                for (Tarif tarif : tarifler) {
                    if (tarif.getTarifID() == tarifBilgi.getTarifId()) {
                        sortedTarifler.add(tarif);
                        break;
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

            mainMenu(sortedTarifler, eksikTarifler);
        }

        //SEÇENEK SEÇİLMEDİ
        else if (selectedOption == null) {
            System.out.println("Hiçbir seçenek seçilmedi.");
        }
    }


    //MALZEME LİSTELEMEYİ GÜNCELLE
    public void updateMalzemeListele(GridPane malzemeListele) throws SQLException, IOException {
        malzemeListele.getChildren().clear();

        List<Malzeme> malzemeler = getMalzemeler();

        int malzemeCol = 0;
        int malzemeRow = 1;

        for (Malzeme malzeme : malzemeler) {
            FXMLLoader fxmlLoader = new FXMLLoader();

            File filePath = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item_tarif_ekleme.fxml");

            if (!filePath.exists()) {
                filePath = new File("/Users/melisportakal/Desktop/sonins/src/main/resources/com/example/yazlabb/malzeme_item_tarif_ekleme.fxml");
            }

            fxmlLoader.setLocation(filePath.toURI().toURL());


            AnchorPane anchorPane = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setMalzemeListeleData(malzeme);

            if (malzemeListele != null) {
                malzemeListele.add(anchorPane, malzemeCol, malzemeRow++);
                GridPane.setMargin(anchorPane, new Insets(0, 0, 1, 0));
            }
        }
    }

    //TARİF DÜZENLE EKRANI
    @FXML
    public void TarifGuncelle() {
        Dialog<Tarif> dialog = new Dialog<>();
        dialog.setTitle("Tarif Düzenleme");
        dialog.setHeaderText("Düzenlemek İstediğiniz Tarifin Adını Giriniz:");

        dialog.getDialogPane().setStyle("-fx-background-color: FFE6B2FF;");


        TextField tarifAdiField = new TextField();
        tarifAdiField.setPromptText("Tarif Adı");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Tarif Adı:"), 0, 0);
        grid.add(tarifAdiField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType guncelleButtonType = new ButtonType("Güncelle", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guncelleButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guncelleButtonType) {
                return getTarifByName(tarifAdiField.getText());
            }
            return null;
        });

        Optional<Tarif> result = dialog.showAndWait();
        result.ifPresent(secilenTarif -> {

            if (secilenTarif != null) {
                try {
                    showTarifDuzenleDialog(secilenTarif);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setHeaderText(null);
                alert.setContentText("Belirtilen isimde bir tarif bulunamadı.");
                alert.showAndWait();

            }
        });
    }

    //TARİF DÜZENLE
    @FXML
    private void showTarifDuzenleDialog(Tarif tarif) throws SQLException, IOException {
        Dialog<Void> duzenleDialog = new Dialog<>();
        duzenleDialog.setTitle("Tarif Düzenleme");
        duzenleDialog.setHeaderText("Tarifi Düzenle: " + tarif.getTarifAdi());

        TextField maliyetField = new TextField(String.valueOf(tarif.getKategori()));
        TextField sureField = new TextField(String.valueOf(tarif.getHazirlamaSuresi()));
        TextArea talimatlarArea = new TextArea(tarif.getTalimatlar());
        talimatlarArea.setWrapText(true);
        talimatlarArea.setPrefWidth(300);
        talimatlarArea.setPrefHeight(100);

        GridPane grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 40, 10));

        grid.add(new Label("Maliyet:"), 0, 0);
        grid.add(maliyetField, 1, 0);
        grid.add(new Label("Süre:"), 0, 1);
        grid.add(sureField, 1, 1);
        grid.add(new Label("Talimatlar:"), 0, 2);
        grid.add(talimatlarArea, 1, 2);

        duzenleDialog.getDialogPane().setContent(grid);

        ButtonType kaydetButtonType = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        duzenleDialog.getDialogPane().getButtonTypes().addAll(kaydetButtonType, ButtonType.CANCEL);

        duzenleDialog.setResultConverter(dialogButton -> {
            if (dialogButton == kaydetButtonType) {

                tarif.setKategori((maliyetField.getText()));
                tarif.setHazirlamaSuresi(Integer.parseInt(sureField.getText()));
                tarif.setTalimatlar((talimatlarArea.getText()));

                try {
                    DatabaseConnection.updateTarif(tarif);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText(null);
                alert.setContentText("Tarif başarıyla güncellendi.");
                alert.showAndWait();

            }
            return null;
        });

        duzenleDialog.showAndWait();
        mainMenu();
    }
}