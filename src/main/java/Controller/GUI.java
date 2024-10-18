package Controller;

import Model.Malzeme;
import Model.Tarif;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.HyperlinkLabel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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
    private GridPane grid;

    @FXML
    private TextField searchfield;

    @FXML
    private Button searchbutton;

    @FXML
    private Label seciliTarifAdi;

    @FXML
    private Label seciliTarifSure;


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

    @FXML
    public void searchButtonOnAction() {
        String arananTarif = searchfield.getText().trim();

        Tarif bulunanTarif = DatabaseConnection.getTarifByName(arananTarif);

        if (bulunanTarif != null) {
            System.out.println(arananTarif);
            System.out.println("Tarif bulundu!");

            grid.getChildren().clear();

            List<Tarif> tarifList = DatabaseConnection.getTarifler();

            for (Tarif tarif : tarifList) {
                FXMLLoader loader = null;
                try {
                    loader = new FXMLLoader(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                if(Objects.equals(tarif.getTarifAdi(), bulunanTarif.getTarifAdi()))
                {
                    try {
                        Node tarifNode = loader.load();
                        ItemController controller = loader.getController();
                        controller.setTarifData(tarif);
                        grid.add(tarifNode, 0, grid.getRowCount());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Tarif bulunamadı!");
            showAlert("Tarif bulunamadı!");
        }
    }

    public void showRecipeDetails(Tarif tarif, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
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
    }

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\deneme.fxml").toURI().toURL());

        Parent mainMenuView = loader.load();

        Scene scene = new Scene(mainMenuView);

        String css = new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\views\\style.css").toURI().toURL().toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bildiri");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

                // Kontrol için toplamMiktar ve birimFiyat stringlerini parse edelim
                boolean isValid = true;

                DatabaseConnection.addMalzeme(malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);
                showAlert("Malzeme başarıyla eklendi.");
            }


            return null;
        });

        dialog.showAndWait();

        updateMalzemeGridPane();
    }
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
            }
            return null;
        });

        dialog.showAndWait();
        updateMalzemeGridPane();
    }

    public void updateMalzemeGridPane() {
        malzemeEkleGrid.getChildren().clear();

        List<Malzeme> malzemeList = DatabaseConnection.getMalzemeler();

        for (Malzeme malzeme : malzemeList) {
            FXMLLoader loader = null;
            try {
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

    @FXML
    public void mainMenu() throws SQLException, IOException {
        grid.getChildren().clear();

        List<Tarif> tarifler = getTarifler();

        int col = 0;
        int row = 1;

        for (int i = 0; i < tarifler.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
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
}


