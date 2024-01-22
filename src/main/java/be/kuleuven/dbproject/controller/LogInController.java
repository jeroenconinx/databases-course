package be.kuleuven.dbproject.controller;
import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Medewerker;
import be.kuleuven.dbproject.repository.MedewerkerRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogInController {


    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        btnLogin.setOnAction(e -> logIn());
    }

    private void logIn(){
        var medewerkersRepository = new MedewerkerRepository(sharedData.getEntityManager());
        Medewerker medewerker= medewerkersRepository.findByEmailAdres(txtEmail.getText().toLowerCase());
        System.out.println(txtEmail.getText().toLowerCase());
        if (medewerker == null){
            showAlert("Medewerker niet gevonden", "De opgegeven mailAdres is niet gevonden.");
        } else if (!medewerker.getWachtwoord().equals(txtPassword.getText())) {
            showAlert("Incorrect password", "De opgegeven password is niet correct.");
        }else{
            sharedData.setLoggedInMedewerker(medewerker);
            showWerkingsscherm();
        }
    }
    private void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWerkingsscherm(){
        try {
            Stage oldStage = (Stage) btnLogin.getScene().getWindow();
            oldStage.close();

            var stage = new Stage();
            var root = (TabPane) FXMLLoader.load(getClass().getClassLoader().getResource("museaMedewerker.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("welcome " + sharedData.getLoggedInMedewerker().getNaam());
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan museaMedewerker.fxml niet vinden", e);
        }
    }
}
