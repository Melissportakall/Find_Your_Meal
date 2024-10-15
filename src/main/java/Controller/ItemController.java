package Controller;

import Model.Tarif;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemController
{
    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    private Tarif tarif;

    public void setData(Tarif tarif)
    {
        this.tarif = tarif;
        nameLabel.setText(tarif.getTarifAdi());
        idLabel.setText(Integer.toString(tarif.getTarifID()));
    }

}
