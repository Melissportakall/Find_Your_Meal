package Controller;

import Model.Malzeme;
import Model.Tarif;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GUI implements Initializable
{
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

    private List<Tarif> tarifler;

    private List<Malzeme> malzemeler;

    private List<Tarif> getTarifler() throws SQLException {
        return DatabaseConnection.getTarifler(); // Tüm tarifleri al
    }

    private List<Malzeme> getMalzemeler() throws SQLException {
        return DatabaseConnection.getMalzemeler();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            List<String> tarifAdlari = new ArrayList<>();
            tarifler = getTarifler();
            Tarif tarif;

            List<String> malzemeAdlari = new ArrayList<>();
            malzemeler = getMalzemeler();
            Malzeme malzeme;

            for(int i = 0; i < tarifler.size(); i++)
            {
                tarif = tarifler.get(i);
                System.out.println(tarif.getTarifAdi());
                tarifAdlari.add(tarif.getTarifAdi());
            }

            int col = 0;
            int row = 1;

            for (int i = 0; i < tarifAdlari.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                //ItemController ile veriyi GUI'ye bastır
                ItemController itemController = fxmlLoader.getController();
                itemController.setTarifData(tarifAdlari.get(i));

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
                malzemeAdlari.add(malzeme.getMalzemeAdi());
            }

            int malzemeCol = 0;
            int malzemeRow = 1;

            for (int i = 0; i < malzemeAdlari.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\Find_Your_Meal\\src\\main\\resources\\com\\example\\yazlabb\\malzeme_item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setMalzemeData(malzemeAdlari.get(i));

                malzemeEkleGrid.add(anchorPane, malzemeCol, malzemeRow++);
                GridPane.setMargin(anchorPane, new Insets(0,0,1,0));
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
