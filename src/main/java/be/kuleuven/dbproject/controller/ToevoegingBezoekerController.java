package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Bezoeker;
import be.kuleuven.dbproject.repository.BezoekerRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToevoegingBezoekerController {


    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtadres;

    @FXML
    private DatePicker dpGeboortedatum;

    @FXML
    private Button btnRegistreer;


    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        btnRegistreer.setOnAction(event -> registreerBezoeker());
    }

    private void registreerBezoeker() {
        String naam = txtNaam.getText();
        String email = txtEmail.getText().toLowerCase();
        LocalDate geboortedatum = dpGeboortedatum.getValue();
        String adres = txtadres.getText();
        BezoekerRepository bezoekerRepository = new BezoekerRepository(sharedData.getEntityManager());

        if(naam.isEmpty()){
            showAlert("Vul uw naam in aub", "Er is geen naam ingegeven, vul aub de naam in");
        }
        else if (!isValidEmail(email)) {
            showAlert("Ongeldig mailAdres", "het ingegeven mailAdres is ongeldig, gelieve een geldige mail adres in te geven");
        }
        else if (bezoekerRepository.findByEmailAdres(email) != null) {
            showAlert("Al gebruikte mailAdres", "het ingegeven mailAdres is al in gebruik, gelieve een andere mail adres in te geven");
        }
        else if((LocalDate.now().getYear() - geboortedatum.getYear() < 12)) {
            showAlert("Te jonge leeftijd", "De bezoeker moet minimaal 12 jaar oud zijn.");
        }else if(adres.isEmpty()){
            showAlert("Vul uw adres in aub", "Er is geen adres ingegeven, vul aub het adres in");
        } else {
            Bezoeker bezoeker = new Bezoeker(naam,adres,email,geboortedatum);
            bezoekerRepository.save(bezoeker);
            showAlert("De bezoeker is geregistreerd", " U bent geregistreerd als bezoeker, u ID is " + bezoeker.getBezoekerID() +", ondhoud het" +
                    "goed, je het nodig om later in te loggen");
            sharedData.setBezoeker(bezoeker);
            sharedData.maakEenBezoek();
            Stage currentStage = (Stage) btnRegistreer.getScene().getWindow();
            currentStage.close();
            try {
                var stage = new Stage();
                var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("bezoekToevoeging.fxml"));
                var scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Welkom " + sharedData.getBezoeker().getNaam());
                stage.initOwner(ProjectMain.getRootStage());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException("Kan logInBezoeker.fxml niet vinden", e);
            }
        }
    }

    public static boolean isValidEmail(String email) {
        // Define a regular expression pattern for a simple email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a matcher object
        Matcher matcher = pattern.matcher(email);

        // Return true if the email matches the pattern, otherwise false
        return matcher.matches();
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
