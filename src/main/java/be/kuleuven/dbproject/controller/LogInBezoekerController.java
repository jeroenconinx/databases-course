package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.Bezoeker;
import be.kuleuven.dbproject.repository.BezoekerRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static org.hibernate.Hibernate.getClass;

public class LogInBezoekerController {

    @FXML
    public Button btnLogin;
    @FXML
    public TextField txtEmail;
    @FXML
    public TextField textID;

    @FXML
    public Button btnRegistreer;

    SharedData sharedData = SharedData.getInstance();


    public void initialize() {
        btnLogin.setOnAction(e -> logIn());
        btnRegistreer.setOnAction(e -> registreerBezoeker());
    }

    private void registreerBezoeker() {
        try {
            Stage oldStage = (Stage) btnLogin.getScene().getWindow();
            oldStage.close();

            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("BezoekerToevoeging.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan bezoekToevoeging.fxml niet vinden", e);
        }
    }

    private void logIn(){
        var bezoekerRepository = new BezoekerRepository(sharedData.getEntityManager());
        Bezoeker bezoeker= bezoekerRepository.findById(Integer.valueOf(textID.getText().toLowerCase()));
        System.out.println(textID.getText().toLowerCase());
        if (bezoeker == null){
            showAlert("Bezoeker niet gevonden", "De opgegeven ID is niet gevonden.");
        } else if (!bezoeker.getEmailAdres().equals(txtEmail.getText().toLowerCase())) {
            showAlert("Incorrect email", "De opgegeven email is niet correct.");
        }else {
            sharedData.setBezoeker(bezoeker);
            sharedData.maakEenBezoek();
            toevoegScherm();
        }
    }

    private void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void toevoegScherm() {
        try {
            Stage oldStage = (Stage) btnLogin.getScene().getWindow();
            oldStage.close();

            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("bezoekToevoeging.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("welcome " + sharedData.getBezoeker().getNaam());
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan bezoekToevoeging.fxml niet vinden", e);
        }
    }

    //ALS OP KNOP btnLogin -> GA NAAR BEZOEKTOEVOEGING FXML
}
