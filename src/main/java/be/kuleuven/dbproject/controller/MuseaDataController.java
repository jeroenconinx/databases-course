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
import java.util.Optional;

public class MuseaDataController {

    @FXML
    public Button btnToonGames;
    @FXML
    public Button btnToonBoeken;
    @FXML
    public Button btnToonBezoeken;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;
    @FXML
    private Button btnVoegMedewerkerToe;
    @FXML
    private Button btnToonMedewerkers;
    @FXML
    private Button btnRegistreerBezoek;


    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        if(!sharedData.getLoggedInMedewerker().isAdmin()){
            btnDelete.setDisable(true);
            btnAdd.setDisable(true);
            btnModify.setDisable(true);
        }


        initTable();

        btnAdd.setOnAction(e -> addNewRow());


        btnToonBoeken.setOnAction(e -> {
            IsOneRowSelected();
            toonBoeken();
        });

        btnToonBezoeken.setOnAction(e -> {
            IsOneRowSelected();
            toonBezoeken();
        });

        btnToonGames.setOnAction(e -> {
            IsOneRowSelected();
            toonGames();
        });

        btnToonMedewerkers.setOnAction(e -> {
            if (IsOneRowSelected()) {

                btnToonMedewerkers();

            }
            });


        btnVoegMedewerkerToe.setOnAction(e -> {
            if (IsOneRowSelected()) {
                VoegMedewerkerToeAanMuseum();
            }
        });

        btnDelete.setOnAction(e -> {
            IsOneRowSelected();
            deleteCurrentRow();
        });

        btnRegistreerBezoek.setOnAction(e -> {
            if(IsOneRowSelected()) {
                registreerBezoek();
            }
        });
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void toonBezoeken() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        sharedData.setMuseum(museumRepository.findById(museumID));
        try {
            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toonBezoeken.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Weergave van bezoeken");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan toonBezoeken.fxml niet vinden", e);
        }
    }

    private void toonBoeken() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        sharedData.setMuseum(museumRepository.findById(museumID));
        try {
            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toonBoeken.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("De boeken van dit museum zijn:");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan toonBoeken.fxml niet vinden", e);
        }
    }

    private void toonGames() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        sharedData.setMuseum(museumRepository.findById(museumID));
        try {
            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toonGames.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("De games van dit museum zijn:");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan toonGames.fxml niet vinden", e);
        }
    }



    /**
     * Maak een bezoekaan een museum aan
     * Hier kan men een bezoek registreren, een bijdrage toevoegen (betaalmethode), boek/spel geleend?
     */
    private void registreerBezoek() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        sharedData.setMuseum(museumRepository.findById(museumID));
        try {
            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            currentStage.close();
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("logInBezoeker.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Log in");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan logInBezoeker.fxml niet vinden", e);
        }

    }

    private void btnToonMedewerkers() {
        try {
            ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
            int museumID = Integer.parseInt(selectedRow.get(0));
            MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
            sharedData.setMuseum(museumRepository.findById(museumID));
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toonMedewerkers.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("De medewerkers van dit museum zijn:");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan toonMedewerkers.fxml niet vinden", e);
        }
    }

    private void voegEenNieuweMedewerkerToe() {

        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        Museum museum = museumRepository.findById(museumID);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/medewerkerToevoeging.fxml"));
            Parent root = loader.load();

            MedewerkerToevoegingController medewerkerToevoegingController = loader.getController();

            medewerkerToevoegingController.setMuseum(museum);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            throw new RuntimeException("Kan medewerkerToevoeging.fxml niet vinden", e);
        }
    }


    private void initTable() { //tabel maken
        tblConfigs.getItems().clear();
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        int colIndex = 0;
        for(var colName : new String[]{"museumID", "Naam", "Adres"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            tblConfigs.getColumns().add(col);
            colIndex++;
        }

        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        List<Museum> musea = new ArrayList<>();
        if (sharedData.getLoggedInMedewerker().isAdmin()){
            musea = museumRepository.findAll();
        }else{
            musea = museumRepository.findByMedewerker(sharedData.getLoggedInMedewerker());
        }

        for(int i = 0; i <= musea.size()-1; i++) {
            tblConfigs.getItems().add(FXCollections.observableArrayList(String.valueOf(musea.get(i).getMuseumID()), musea.get(i).getNaam(), musea.get(i).getAdres() ));
        }
    }

    private void addNewRow() {
        try {
            Stage currentStage = (Stage) btnAdd.getScene().getWindow();
            currentStage.close();
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("museumToevoeging.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Toevoeging van een museum");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan museumToevoeging.fxml niet vinden", e);
        }
    }

    private void VoegEenBestaandeMedewerkerToe() {
        String mail = vraagEmailAdres();
        MedewerkerRepository medewerkerRepository= new MedewerkerRepository(sharedData.getEntityManager());
        Medewerker medewerker = medewerkerRepository.findByEmailAdres(mail);
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();
        int museumID = Integer.parseInt(selectedRow.get(0));
        MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
        Museum museum = museumRepository.findById(museumID);

        if(medewerker == null){
            if(!mail.isEmpty()) {
                showAlert("EmailAdres niet gevonden", "Het emailAdres van het bestaande medewerker is niet gevonden");
            }
        } else if (museum.getMedewerkers().contains(medewerker)) {
            showAlert("medewerker werkt al in deze museum", "U probeert een medewerker toe te voegen aan een museum waar hij al werkt!");
        } else{
            museum.voegMedewerkerToe(medewerker);
            museumRepository.update(museum);
        }
    }
    private void deleteCurrentRow() {
        ObservableList<String> selectedRow = (ObservableList<String>) tblConfigs.getSelectionModel().getSelectedItem();

        ButtonType nietVerwijderenButton = new ButtonType("Niet verwijderen", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Vraag om bevestiging met twee knoppen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering");
        alert.setHeaderText("Ben je zeker dat je de geselecteerde musea wilt verwijderen?");
        alert.setContentText("Deze actie kan niet ongedaan worden gemaakt.");
        alert.getButtonTypes().setAll(ButtonType.OK, nietVerwijderenButton);

        // Haal het resultaat op (OK of Niet verwijderen)
        var result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) { //haalt rij uit tabel
                int museumID = Integer.parseInt(selectedRow.get(0));
                MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());
                museumRepository.deleteByID(museumID);
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
            showAlert("Fout!", "Selecteer een museum");
        }
        return !(tblConfigs.getSelectionModel().getSelectedCells().size() == 0);
    }

    private void VoegMedewerkerToeAanMuseum() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Keuze medewerker");
        alert.setHeaderText("Voeg een nieuwe of bestaande medewerker toe?");
        alert.setContentText("Kies een optie:");

        // Voeg knoppen toe aan het dialoogvenster
        ButtonType nieuweMedewerkerButton = new ButtonType("Nieuwe medewerker");
        ButtonType bestaandeMedewerkerButton = new ButtonType("Bestaande medewerker");
        ButtonType cancelButton = new ButtonType("Annuleren");

        alert.getButtonTypes().setAll(nieuweMedewerkerButton, bestaandeMedewerkerButton, cancelButton);

        // Toon het dialoogvenster en wacht op de reactie van de gebruiker

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == nieuweMedewerkerButton) {
                // Handle the "Nieuwe medewerker" case
                voegEenNieuweMedewerkerToe();
            } else if (result.get() == bestaandeMedewerkerButton) {
                VoegEenBestaandeMedewerkerToe();
            } else if (result.get() == cancelButton) {
                // Handle the "Annuleren" case (close the window)
            }
        }
    }
    private String vraagEmailAdres() {
        // Maak een nieuw dialoogvenster aan met een TextField om het e-mailadres in te voeren
        TextField emailField = new TextField();
        emailField.setPromptText("Voer het e-mailadres in");

        GridPane grid = new GridPane();
        grid.add(emailField, 0, 0);

        Alert emailDialog = new Alert(Alert.AlertType.CONFIRMATION);
        emailDialog.setTitle("Voer e-mailadres in");
        emailDialog.setHeaderText(null);
        emailDialog.getDialogPane().setContent(grid);

        // Voeg OK en Annuleren knoppen toe
        emailDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Toon het dialoogvenster en wacht op de reactie van de gebruiker
        emailDialog.showAndWait();

        // Controleer of de gebruiker op OK heeft geklikt en retourneer het ingevoerde e-mailadres
        if (emailDialog.getResult() == ButtonType.OK) {
            return emailField.getText().toLowerCase();
        } else {
            // Gebruiker heeft geannuleerd of venster gesloten, retourneer een lege string
            return "";
        }
    }
}
