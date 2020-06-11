package gui_test;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DetailPanelController extends GridPane {

    @FXML
    private TextField txtNummerplaat, txtMerk, txtModel;

    public DetailPanelController() {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("DetailPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }




}
