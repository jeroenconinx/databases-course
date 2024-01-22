package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Boek;
import be.kuleuven.dbproject.model.Museum;
import be.kuleuven.dbproject.repository.BoekRepository;
import be.kuleuven.dbproject.repository.MuseumRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.Persistence;
import java.util.regex.Pattern;

public class BoekToevoegingController {

    @FXML
    private TextField txtNaam;
    @FXML
    public TextField naamField;
    @FXML
    public TextField auteurField;
    @FXML
    public Button btnVoegToe;
    @FXML
    public TextField jaarField;
    @FXML
    public TextField uitgeverField;
    @FXML
    public TextField waardeField;

    @FXML
    private void initialize() {
        // Voeg een actie toe aan de knop "Voeg Toe" bij initialisatie
        btnVoegToe.setOnAction(event -> voegBoekToe());
    }

    SharedData sharedData = SharedData.getInstance();

    private void voegBoekToe() {
        String naam = naamField.getText();
        String auteur = auteurField.getText();
        String jaarText = jaarField.getText();
        String uitgever = uitgeverField.getText();
        String waardeText = waardeField.getText();

        if (isValidInput(naam, auteur, jaarText, uitgever, waardeText)) {
            try {
                Integer publicatiejaar = Integer.parseInt(jaarText);
                Double waarde = Double.parseDouble(waardeText);

                MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());

                Boek boek = new Boek(naam, auteur, publicatiejaar, uitgever, sharedData.getMuseum(), waarde);
                museumRepository.update(sharedData.getMuseum());

                showAlert("Het boek is toegevoegd", "Het is toegevoegd aan de geselecteerde musea");
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
    private boolean isValidInput(String naam, String auteur, String jaar, String uitgever, String waarde) {
        // Controleer of de tekstvelden niet leeg zijn
        if (naam.isEmpty() || auteur.isEmpty() || jaar.isEmpty() || uitgever.isEmpty() || waarde.isEmpty()) {
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
            showAlert("Ongeldige waarde", "Voer een geldige waarde in voor het boek.");
            return false;
        }

        return true;
    }

}
