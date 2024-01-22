package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.BetaalMethode;
import be.kuleuven.dbproject.model.Boek;
import be.kuleuven.dbproject.model.Museum;
import be.kuleuven.dbproject.repository.BoekRepository;
import be.kuleuven.dbproject.repository.MuseumRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ToonBoekenController{

    @FXML
    public TableView boekTableView;
    @FXML
    public TableColumn idBookTableColumn;
    @FXML
    public TableColumn naamBookTableColumn;
    @FXML
    public TableColumn auteurBookTableColumn;
    @FXML
    public TableColumn publicatiejaarBookTableColumn;
    @FXML
    public TableColumn uitgeverBookTableColumn;
    @FXML
    public TableColumn uitgeleendBookTableColumn;
    @FXML
    public Button btnAddBook;
    @FXML
    public Button btnDeleteBook;

    @FXML
    private TextField zoekBoekTextBar;

    SharedData sharedData = SharedData.getInstance();

    public void initialize() {

        btnAddBook.setOnAction(e -> addBook());

        btnDeleteBook.setOnAction(e -> {
            IsOneRowSelected();
            deleteCurrentRow();
        });

        initTable(sharedData.getMuseum().getBoeken());
        BoekRepository boekRepository = new BoekRepository(sharedData.getEntityManager());
        zoekBoekTextBar.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.isEmpty() || newValue.isBlank() || newValue == null ){
                initTable(sharedData.getMuseum().getBoeken());
            }
            else{
                initTable(boekRepository.findByPartialNameInMuseum(newValue, sharedData.getMuseum()));
            }
        });
    }

    private void deleteCurrentRow() {
        ObservableList<String> selectedRow = (ObservableList<String>) boekTableView.getSelectionModel().getSelectedItem();
        ButtonType nietVerwijderenButton = new ButtonType("Niet verwijderen", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Vraag om bevestiging met twee knoppen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering");
        alert.setHeaderText("Ben je zeker dat je het geselcteerde boek wilt verwijderen?");
        alert.setContentText("Deze actie kan niet ongedaan worden gemaakt.");
        alert.getButtonTypes().setAll(ButtonType.OK, nietVerwijderenButton);

        // Haal het resultaat op (OK of Niet verwijderen)
        var result = alert.showAndWait();
        int boekID = Integer.parseInt(selectedRow.get(0));
        BoekRepository boekRepository = new BoekRepository(sharedData.getEntityManager());
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        Boek boek = boekRepository.findById(boekID);

        if(boek.isUitgeleend()){
            showAlert("Boek is uitgeleend", "Dit boek kan niet verwijderd worden omdat het nog steeds uitgeleend is");
        }
        else if(result.isPresent() && result.get() == ButtonType.OK) { //haalt rij uit tabel
            Museum museum = boek.getMuseum();
            museum.getBoeken().remove(boek);
            boekRepository.delete(boek);
            museumRepository.update(boek.getMuseum());
            initTable(sharedData.getMuseum().getBoeken());
        }
    }


    private void addBook() {
        try {
            /**
            Stage oldStage = (Stage) btnAddBook.getScene().getWindow();
            oldStage.close();
             **/
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("voegBoekToe.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
            Stage currentStage = (Stage) btnAddBook.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            throw new RuntimeException("Kan voegBoekToe.fxml niet vinden", e);
        }
    }

    private boolean IsOneRowSelected() {
        if(boekTableView.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Fout!", "Selecteer een boek om te verwijderen.");
        }
        return !(boekTableView.getSelectionModel().getSelectedCells().size() == 0);
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void initTable(List<Boek> boeken) {

        boekTableView.getItems().clear();
        boekTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        boekTableView.getColumns().clear();

        int colIndex = 0;
        for(var colName : new String[]{"ID", "Naam", "Auteur", "Jaar", "Uitgever", "Uitgeleend"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            boekTableView.getColumns().add(col);
            colIndex++;
        }

        for(int i = 0; i <= boeken.size()-1; i++) {
            boekTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(boeken.get(i).getBoekID()), boeken.get(i).getNaam(),
                    boeken.get(i).getAuteur(), String.valueOf(boeken.get(i).getPublicatiejaar()), boeken.get(i).getUitgever(),
                    String.valueOf(boeken.get(i).isUitgeleend())));
        }
    }

}

