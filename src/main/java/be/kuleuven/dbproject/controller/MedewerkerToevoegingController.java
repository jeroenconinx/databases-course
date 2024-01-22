package be.kuleuven.dbproject.controller;


import be.kuleuven.dbproject.model.Medewerker;
import be.kuleuven.dbproject.model.Museum;
import be.kuleuven.dbproject.repository.MedewerkerRepository;
import be.kuleuven.dbproject.repository.MuseumRepository;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MedewerkerToevoegingController {

    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtWachtwoord;

    @FXML
    private DatePicker dpGeboortedatum;

    @FXML
    private CheckBox chkAdmin;

    @FXML
    private Button btnVoegToe;

    private Museum museum;

    SharedData sharedData = SharedData.getInstance();

    @FXML
    private void initialize() {
            btnVoegToe.setOnAction(e -> handleVoegToeButton());
    }

    @FXML
    private void handleVoegToeButton() {
        // Handle the logic for adding a new medewerker
        String naam = txtNaam.getText();
        String email = txtEmail.getText().toLowerCase();
        String wachtwoord = txtWachtwoord.getText();
        LocalDate geboortedatum = dpGeboortedatum.getValue();
        boolean isAdmin = chkAdmin.isSelected();

        if(naam.isEmpty()){
            showAlert("Vul De naam van de medewerker in aub", "Er is geen naam ingegeven, vul aub de naam in");
        } else if (!isValidEmail(email)) {
            showAlert("Ongeldig mailAdres", "het ingegeven mailAdres is ongeldig, gelieve een geldige mail adres in te geven");
        } else if (wachtwoord.length() < 6) {
            showAlert("Korte wachtwoord", "het ingegeven wachtwoord moet minimaal 6 karakters bevatten");
        } else if((LocalDate.now().getYear() - geboortedatum.getYear() < 16)){
            showAlert("Te jonge leeftijd", "De medewerker moet minimaal 16 jaar oud zijn.");
        } else {
            MedewerkerRepository medewerkerRepository = new MedewerkerRepository(sharedData.getEntityManager());
            MuseumRepository    museumRepository = new MuseumRepository(sharedData.getEntityManager());
            Medewerker medewerker = new Medewerker(naam,email,wachtwoord,geboortedatum,isAdmin);
            medewerkerRepository.save(medewerker);
            museum.voegMedewerkerToe(medewerker);
            museumRepository.update(museum);

            showAlert("De medewerker is toegevoegd", " de medewerker is toegevoegd aan de geselecteerde musea");
            Stage currentStage = (Stage) btnVoegToe.getScene().getWindow();
            currentStage.close();
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

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }
}
