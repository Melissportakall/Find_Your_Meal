package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class FillComboBox implements Initializable{
    @FXML
    private ComboBox<String> Sort;
    ObservableList<String> SortList = FXCollections
            .observableArrayList("Çoktan aza maliyet", "Azdan çoka maliyet", "Çoktan aza süre", "Azdan çoka süre", "Malzeme oranına göre");

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Sort.setItems(SortList);
    }
}