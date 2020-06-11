package gui_test;


import domein.DomeinController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class AutoFrameController extends HBox {

    private OverzichtPanelController overzichtPanel;
    private DetailPanelController detailPanelController;

    private DomeinController domeinController;


    public AutoFrameController(DomeinController domeinController) {
        this.domeinController = domeinController;
        overzichtPanel = new OverzichtPanelController(domeinController);

        detailPanelController = new DetailPanelController();
        
       // getChildren().addAll(overzichtPanel);
        getChildren().addAll(overzichtPanel, detailPanelController);


    }

}
