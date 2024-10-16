package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ItemController {
    @FXML
    private Button nameLabel; // Tarifi gösteren Label

    @FXML
    private Label malzemeAdiLabel;

    @FXML
    private Button malzemeSilButton;

    public void setTarifData(String name) {
        nameLabel.setText(name);
    }

    public void setMalzemeData(String name/*, int id*/) {
        malzemeAdiLabel.setText(name);
        //malzemeSilButton.setId(Integer.toString(id));
    }
}
