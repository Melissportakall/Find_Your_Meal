package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemController {
    @FXML
    private Label nameLabel; // Tarifi gösteren Label

    public void setData(String name) {
        nameLabel.setText(name); // Tarif adını Label'a ayarla
    }
}
