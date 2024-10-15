package Controller;

import Model.Tarif;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import java.util.List;
import java.util.ResourceBundle;

public class GUI implements Initializable
{
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

    private List<Tarif> tarifler = new ArrayList<Tarif>();

    private List<Tarif> getData()
    {
        List<Tarif> tarifler = new ArrayList<Tarif>();
        Tarif tarif;

        try {
            DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 20; i++)
        {
            tarif = new Tarif();
            tarif.setTarifID(DatabaseConnection.getTarifID_DBC(1));
            tarif.setTarifAdi(DatabaseConnection.getTarifAdi_DBC(1));
            tarif.setTarifKategori(DatabaseConnection.getTarifKategori_DBC(1));
            tarif.setTarifSure(DatabaseConnection.getTarifSure_DBC(1));
            tarif.setTarifTalimat(DatabaseConnection.getTarifTalimat_DBC(1));
            tarifler.add(tarif);
        }

        return tarifler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tarifler.addAll(getData());
        int col = 0;
        int row = 1;

        try
        {
            for(int i = 0; i < tarifler.size(); i++)
            {
                FXMLLoader fxmlLoader = new FXMLLoader();

                //                                         BU KISMA item.fxml NİN MUTLAK ADRESİNİ YAZ
                fxmlLoader.setLocation(new File("C:\\Users\\Acer\\OneDrive\\Masaüstü\\YazLab\\YazLab 1\\1\\FindYourMeal\\src\\main\\resources\\com\\example\\yazlabb\\item.fxml").toURI().toURL());

                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(tarifler.get(i));

                if(col == 3)
                {
                    col = 0;
                    row++;
                }

                grid.add(anchorPane, col++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }

        catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }
}
