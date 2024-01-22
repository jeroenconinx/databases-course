package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Medewerker;
import be.kuleuven.dbproject.model.Museum;
import be.kuleuven.dbproject.repository.MedewerkerRepository;
import be.kuleuven.dbproject.repository.MuseumRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MedewerkersDataController {

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;

    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        if (!sharedData.getLoggedInMedewerker().isAdmin()) {
            btnDelete.setDisable(true);
        }

        initTable(); //tabel laten zien

        btnDelete.setOnAction(e -> {
            IsOneRowSelected();
            deleteCurrentRow();
        });

        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable() {

        tblConfigs.getItems().clear();
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;
        for(var colName : new String[]{"medewerkerID", "Naam", "Adres"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        MedewerkerRepository medewerkerRepository = new MedewerkerRepository((sharedData.getEntityManager()));
        List<Medewerker> medewerkerLijst = new ArrayList<>();
        medewerkerLijst = medewerkerRepository.findAll();
        for(int i = 0; i <= medewerkerLijst.size()-1; i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(String.valueOf(medewerkerLijst.get(i).getMedewerkerID()), medewerkerLijst.get(i).getNaam(), medewerkerLijst.get(i).getEmailAdres() ));
        }
    }


    private void deleteCurrentRow() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();

        ButtonType nietVerwijderenButton = new ButtonType("Niet verwijderen", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Vraag om bevestiging met twee knoppen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering");
        alert.setHeaderText("Ben je zeker dat je de geselecteerde meedewerker wil verwijderen?");
        alert.setContentText("Deze actie kan niet ongedaan worden gemaakt.");
        alert.getButtonTypes().setAll(ButtonType.OK, nietVerwijderenButton);

        // Haal het resultaat op (OK of Niet verwijderen)
        var result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) { //haalt rij uit tabel
            int medewerkerID = Integer.parseInt(selectedRow.get(0));
            MuseumRepository museumRepository =new MuseumRepository(sharedData.getEntityManager());
            MedewerkerRepository medewerkerRepository = new MedewerkerRepository(sharedData.getEntityManager());
            Medewerker medewerker = medewerkerRepository.findById(medewerkerID);
            List<Museum> musea = museumRepository.findByMedewerker(medewerker);
            for (Museum muse : musea) {
                muse.getMedewerkers().remove(medewerker);
                museumRepository.update(muse);
            }
            medewerkerRepository.delete(medewerker);


        }
        initTable();
    }


    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean IsOneRowSelected() {
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
        return !(tblConfigs.getSelectionModel().getSelectedCells().size() == 0);
    }

}
