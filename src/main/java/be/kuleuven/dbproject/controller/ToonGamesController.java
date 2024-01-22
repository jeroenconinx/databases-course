package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.BetaalMethode;
import be.kuleuven.dbproject.model.Boek;
import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Museum;
import be.kuleuven.dbproject.repository.BoekRepository;
import be.kuleuven.dbproject.repository.GameRepository;
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

public class ToonGamesController{

    @FXML
    public TableView gameTableView;

    @FXML
    private TextField zoekGameTextBar;

    @FXML
            public Button btnAddGame;

    @FXML
            public Button btnDeleteGame;

    SharedData sharedData = SharedData.getInstance();

    public void initialize() {


        btnAddGame.setOnAction(e -> addGame());

        btnDeleteGame.setOnAction(e -> {
            IsOneRowSelected();
            deleteCurrentRow();
        });

        initTable(sharedData.getMuseum().getGames());

        GameRepository gameRepository = new GameRepository(sharedData.getEntityManager());
        zoekGameTextBar.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.isEmpty() || newValue.isBlank() || newValue == null ){
                initTable(sharedData.getMuseum().getGames());
            }
            else{
                initTable(gameRepository.findByPartialNameInMuseum(newValue, sharedData.getMuseum()));
            }
        });
    }

    private boolean IsOneRowSelected() {
        if(gameTableView.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Fout!", "Selecteer een game om te verwijderen.");
        }
        return !(gameTableView.getSelectionModel().getSelectedCells().size() == 0);
    }

    private void showAlert(String s, String s1) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(s);
        alert.setHeaderText(s);
        alert.setContentText(s1);
        alert.showAndWait();
    }

    private void addGame() {
        try {
            /**
             Stage oldStage = (Stage) btnAddBook.getScene().getWindow();
             oldStage.close();
             **/
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("voegGameToe.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
            Stage currentStage = (Stage) btnAddGame.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            throw new RuntimeException("Kan voegGameToe.fxml niet vinden", e);
        }
    }

    private void deleteCurrentRow() {
        ObservableList<String> selectedRow = (ObservableList<String>) gameTableView.getSelectionModel().getSelectedItem();
        ButtonType nietVerwijderenButton = new ButtonType("Niet verwijderen", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Vraag om bevestiging met twee knoppen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering");
        alert.setHeaderText("Ben je zeker dat je de geslecteerde game wilt verwijderen?");
        alert.setContentText("Deze actie kan niet ongedaan worden gemaakt.");
        alert.getButtonTypes().setAll(ButtonType.OK, nietVerwijderenButton);


        var result = alert.showAndWait();
        int gameID = Integer.parseInt(selectedRow.get(0));
        GameRepository gameRepository = new GameRepository(sharedData.getEntityManager());
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        Game game = gameRepository.findById(gameID);
        if(game.isUitgeleend()){
            showAlert("Game is uitgeleend", "Deze game kan niet verwijderd worden omdat het nog steeds uitgeleend is");
        }
        else if(result.isPresent() && result.get() == ButtonType.OK) { //haalt rij uit tabel
            Museum museum = game.getMuseum();
            museum.getGames().remove(game);
            gameRepository.delete(game);
            museumRepository.update(game.getMuseum());
            initTable(sharedData.getMuseum().getGames());
        }
    }

    private void initTable(List<Game> games) {

        gameTableView.getItems().clear();
        gameTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        gameTableView.getColumns().clear();

        int colIndex = 0;
        for(var colName : new String[]{"ID", "Naam", "Developer", "Jaar", "Console", "Uitgeleend"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            gameTableView.getColumns().add(col);
            colIndex++;
        }

        for(int i = 0; i <= games.size()-1; i++) {
            gameTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(games.get(i).getGameID()), games.get(i).getNaam(),
                    games.get(i).getDeveloper(), String.valueOf(games.get(i).getPublicatiejaar()), String.valueOf(games.get(i).getConsole()),
                    String.valueOf(games.get(i).isUitgeleend())));
        }
    }

}

