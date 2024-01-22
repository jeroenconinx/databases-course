package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.*;
import be.kuleuven.dbproject.repository.BoekRepository;
import be.kuleuven.dbproject.repository.MuseumRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.Persistence;
import java.util.regex.Pattern;

public class GameToevoegingController {

    @FXML
    private TextField txtNaam;
    @FXML
    public TextField naamField;
    @FXML
    public TextField developerField;
    @FXML
    public Button btnVoegToe;
    @FXML
    public TextField jaarField;
    @FXML
    public TextField consoleField;
    @FXML
    public TextField waardeField;
    @FXML
    private ComboBox selecteerConsole;

    @FXML
    private void initialize() {
        ObservableList<Console> consoleList = FXCollections.observableArrayList(Console.values());

        selecteerConsole.setItems(consoleList);

        btnVoegToe.setOnAction(event -> voegGameToe());
    }

    SharedData sharedData = SharedData.getInstance();

    public void selecteerConsole(ActionEvent actionEvent) {
        String selected = selecteerConsole.getSelectionModel().getSelectedItem().toString();
        Console console = Console.valueOf(selected);
    }

    private void voegGameToe() {
        String naam = naamField.getText();
        String developer = developerField.getText();
        String jaarText = jaarField.getText();
        Console console = (Console) selecteerConsole.getSelectionModel().getSelectedItem();
        String waardeText = waardeField.getText();

        if (isValidInput(naam, developer, jaarText, console, waardeText)) {
            try {
                Integer publicatiejaar = Integer.parseInt(jaarText);
                Double waarde = Double.parseDouble(waardeText);

                MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());

                Game game = new Game(sharedData.getMuseum(), developer, publicatiejaar, waarde, naam, console);

                sharedData.getMuseum().voegGameToe(game);

                museumRepository.update(sharedData.getMuseum());

                showAlert("De game is toegevoegd.", "Het is toegevoegd aan het geselecteerde museum");
                Stage currentStage = (Stage) btnVoegToe.getScene().getWindow();
                currentStage.close();

            } catch (NumberFormatException e) {
                showAlert("Ongeldige invoer", "Controleer de waarden voor publicatiejaar en waarde.");
            }
        }
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean isValidInput(String naam, String auteur, String jaar, Console console, String waarde) {
        // Controleer of de tekstvelden niet leeg zijn
        if (naam.isEmpty() || auteur.isEmpty() || jaar.isEmpty() || waarde.isEmpty()) {
            return false;
        }

        // Controleer of het publicatiejaar een integer van exact vier cijfers is
        if (!Pattern.matches("\\d{4}", jaar)) {
            showAlert("Ongeldig publicatiejaar", "Voer een geldig publicatiejaar in (exact vier cijfers).");
            return false;
        }

        // Controleer of de waarde een geldige Double is
        try {
            Double.parseDouble(waarde);
        } catch (NumberFormatException e) {
            showAlert("Ongeldige waarde", "Voer een geldige waarde in voor de game.");
            return false;
        }

        return true;
    }

}
