package be.kuleuven.dbproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class museaMedewerkerController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab museauTab;

    @FXML
    private Tab medewerkersTab;

    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        medewerkersTab.setDisable(!sharedData.getLoggedInMedewerker().isAdmin());
    }
}
