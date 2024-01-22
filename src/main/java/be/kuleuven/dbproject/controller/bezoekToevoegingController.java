package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.ProjectMain;
import be.kuleuven.dbproject.model.*;
import be.kuleuven.dbproject.repository.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.NoResultException;
import java.util.List;

public class bezoekToevoegingController{

    @FXML
    public TableView boekTableView;
    @FXML
    public TableView gameTableView;

    @FXML
    public TableView winkelwagenTableView;

    @FXML
    private ComboBox selecteerBetaalmethode;

    @FXML
    private TextField zoekBoekTextBar;

    @FXML
    private TextField zoekGameTextBar;
    @FXML
    private TextField teBetalenBedrag;

    @FXML
    private Button btnSlaBezoekOp;

    @FXML
    private Button btnVoegBoek;
    @FXML
    private Button btnVoegGame;

    @FXML
    private Button btnReturnBoek;
    @FXML
    private Button btnReturnGame;
    @FXML
    private Button btnLogUit;

    @FXML
    private Button btnDeleteItem;





    SharedData sharedData = SharedData.getInstance();




    public void initialize() {
        ObservableList<BetaalMethode> list = FXCollections.observableArrayList(BetaalMethode.values());

        selecteerBetaalmethode.setItems(list);

        initTableBoeken(sharedData.getMuseum().getBoeken());
        initTableGames(sharedData.getMuseum().getGames());

        GameRepository gameRepository = new GameRepository(sharedData.getEntityManager());
        zoekGameTextBar.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.isEmpty() || newValue.isBlank() || newValue == null ){
                initTableGames(sharedData.getMuseum().getGames());
            }
            else{
                initTableGames(gameRepository.findByPartialNameInMuseum(newValue, sharedData.getMuseum()));
            }
        });

        BoekRepository boekRepository = new BoekRepository(sharedData.getEntityManager());
        zoekBoekTextBar.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.isEmpty() || newValue.isBlank() || newValue == null ){
                initTableBoeken(sharedData.getMuseum().getBoeken());
            }
            else{
                initTableBoeken(boekRepository.findByPartialNameInMuseum(newValue, sharedData.getMuseum()));
            }
        });

        btnVoegGame.setOnAction(e -> {
            if(isOneGameSelected()){
                VoegGameToeAanWinkelwagen();
            }
        });

        btnVoegBoek.setOnAction(e -> {
            if(isOneBoekSelected()){
                VoegBoekToeAanWinkelwagen();
            }
        });
        btnSlaBezoekOp.setOnAction(e -> {
            slaBezoekOp();
        });
        btnReturnGame.setOnAction(e -> {
            if(isOneGameSelected()){
                ObservableList<String> selectedRow = (ObservableList<String>) gameTableView.getSelectionModel().getSelectedItem();
                int gameID = Integer.parseInt(selectedRow.get(0));
                Game game = gameRepository.findById(gameID);
                if(game.isUitgeleend()) {
                    game.setUitgeleend(false);
                    gameRepository.update(game);
                    initTableGames(sharedData.getMuseum().getGames());
                }else{
                    showAlert("De game is al teruggebracht", "U probeert een game terug te brengen die al teruggebracht is");
                }
            }
        });
        btnReturnBoek.setOnAction(e -> {
            if(isOneBoekSelected()){
                ObservableList<String> selectedRow = (ObservableList<String>) boekTableView.getSelectionModel().getSelectedItem();
                int boekID = Integer.parseInt(selectedRow.get(0));
                Boek boek = boekRepository.findById(boekID);
                if(boek.isUitgeleend()) {
                    boek.setUitgeleend(false);
                    boekRepository.update(boek);
                    initTableBoeken(sharedData.getMuseum().getBoeken());
                }else{
                    showAlert("Het boek is al teruggebracht", "U probeert een boek terug te brengen die al teruggebracht is");
                }

            }
        });

        btnLogUit.setOnAction(e -> {
            logUit();
        });

        btnDeleteItem.setOnAction(e -> {
            if(isOneItemSelected()){
                ObservableList<String> selectedRow = (ObservableList<String>) winkelwagenTableView.getSelectionModel().getSelectedItem();
                int id = Integer.parseInt(selectedRow.get(0));
                try {
                    Game game = gameRepository.findById(id);
                    sharedData.getBezoek().getGeleendeGames().remove(game);
                } catch (NoResultException e1){
                    Boek boek = boekRepository.findById(id);
                    sharedData.getBezoek().getGeleendeBoeken().remove(boek);
                }
                initTableWinkelwagen();
            }
        });




    }

    private void logUit() {
        try {
            Stage currentStage = (Stage) btnLogUit.getScene().getWindow();
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

    private void slaBezoekOp() {
        try {

            Double bedrag = Double.valueOf(teBetalenBedrag.getText());

            BetaalMethode betaalMethode = (BetaalMethode) selecteerBetaalmethode.getValue();


            if (betaalMethode == null) {
                showAlert("Fout", "Selecteer een betaalmethode");
                return;
            }

            if (bedrag <= 0) {
                showAlert("Fout", "Voer een geldig bedrag in");
                return;
            }

            Bijdrage bijdrage = new Bijdrage(bedrag,betaalMethode);

            sharedData.getBezoek().kenBijdragesToe(List.of(bijdrage));

            BezoekerRepository bezoekerRepository = new BezoekerRepository(sharedData.getEntityManager());
            BezoekRepository bezoekRepository = new BezoekRepository(sharedData.getEntityManager());
            BijdrageRepository bijdrageRepository = new BijdrageRepository(sharedData.getEntityManager());
            BoekRepository boekRepository = new BoekRepository(sharedData.getEntityManager());
            GameRepository gameRepository = new GameRepository(sharedData.getEntityManager());
            MedewerkerRepository medewerkerRepository = new MedewerkerRepository(sharedData.getEntityManager());
            MuseumRepository museumRepository = new MuseumRepository(sharedData.getEntityManager());

            bezoekRepository.save(sharedData.getBezoek());
            for(Boek boek: sharedData.getBezoek().getGeleendeBoeken() ){
                boek.setUitgeleend(true);
                boekRepository.update(boek);
            }
            for(Game game: sharedData.getBezoek().getGeleendeGames() ){
                game.setUitgeleend(true);
                gameRepository.update(game);
            }

            bezoekerRepository.update(sharedData.getBezoek().getBezoeker());
            museumRepository.update(sharedData.getMuseum());

            for(Bijdrage donatie: sharedData.getBezoek().getBijdragesTijdensBezoek() ){
                bijdrageRepository.update(donatie);
            }
            showAlert("Succes", "Betaling geslaagd");
            logUit();

        } catch (NumberFormatException e) {
            showAlert("Fout", "Voer een geldig bedrag in");
        }

    }

    private void VoegBoekToeAanWinkelwagen() {
        ObservableList<String> selectedRow = (ObservableList<String>) boekTableView.getSelectionModel().getSelectedItem();


            int boekID = Integer.parseInt(selectedRow.get(0));
            BoekRepository boekRepository =new BoekRepository(sharedData.getEntityManager());
            Boek boek = boekRepository.findById(boekID);
            if(sharedData.getBezoek().getGeleendeBoeken().contains(boek)){
                showAlert("boek is al in uw winkelwagen", "het geselecteerde boek ia al in uw winkelwagen");
            }
            else if(boek.isUitgeleend()){
                showAlert("boek is uitgeleend", "het geselecteerde boek ia al uitgeleend");
            }else {
                sharedData.getBezoek().kenBoekenToe(List.of(boek));
                initTableWinkelwagen();
            }

    }

    private void VoegGameToeAanWinkelwagen() {

        ObservableList<String> selectedRow = (ObservableList<String>) gameTableView.getSelectionModel().getSelectedItem();


        int gameID = Integer.parseInt(selectedRow.get(0));
        GameRepository gameRepository =new GameRepository(sharedData.getEntityManager());
        Game game = gameRepository.findById(gameID);
        if(sharedData.getBezoek().getGeleendeBoeken().contains(game)){
            showAlert("game is al in uw winkelwagen", "het geselecteerde game ia al in uw winkelwagen");
        }
        else if(game.isUitgeleend()){
            showAlert("game is uitgeleend", "het geselecteerde game ia al uitgeleend");
        }else {
            sharedData.getBezoek().kenGamesToe(List.of(game));
            initTableWinkelwagen();
        }
    }


    public void selecteerBetaalmethode(ActionEvent actionEvent) {
        String selected = selecteerBetaalmethode.getSelectionModel().getSelectedItem().toString();
        BetaalMethode betaalMethode = BetaalMethode.valueOf(selected);
    }

    private void initTableBoeken(List<Boek> boeken) {

        boekTableView.getItems().clear();
        boekTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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

    private void initTableGames(List<Game> games) {

        gameTableView.getItems().clear();
        gameTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gameTableView.getColumns().clear();

        int colIndex = 0;
        for (var colName : new String[]{"ID", "Naam", "Developer", "Jaar", "Console", "Uitgeleend"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            gameTableView.getColumns().add(col);
            colIndex++;
        }

        for (int i = 0; i <= games.size() - 1; i++) {
            gameTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(games.get(i).getGameID()), games.get(i).getNaam(),
                    games.get(i).getDeveloper(), String.valueOf(games.get(i).getPublicatiejaar()), String.valueOf(games.get(i).getConsole()),
                    String.valueOf(games.get(i).isUitgeleend())));
        }
    }

    private void initTableWinkelwagen(){

        winkelwagenTableView.getItems().clear();
        winkelwagenTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        winkelwagenTableView.getColumns().clear();

        int colIndex = 0;
        for (var colName : new String[]{"ID", "Naam", "Type"}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            winkelwagenTableView.getColumns().add(col);
            colIndex++;
        }
        List<Game> games= sharedData.getBezoek().getGeleendeGames();
        List<Boek> boeken = sharedData.getBezoek().getGeleendeBoeken();
        int aantalGames  = sharedData.getBezoek().getGeleendeGames().size();
        int aantalBoeken = sharedData.getBezoek().getGeleendeBoeken().size();

        for (int i = 0; i <= aantalGames - 1; i++) {
            winkelwagenTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(games.get(i).getGameID()), games.get(i).getNaam(), "Game"));
        }
        for (int i = 0; i <= aantalBoeken - 1; i++) {
            winkelwagenTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(boeken.get(i).getBoekID()), boeken.get(i).getNaam(), "Boek"));
        }
    }

    private boolean isOneGameSelected() {
        if(gameTableView.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Fout!", "Selecteer een game");
        }
        return !(gameTableView.getSelectionModel().getSelectedCells().size() == 0);
    }

    private boolean isOneItemSelected() {
        if(winkelwagenTableView.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Fout!", "Selecteer een item vanuit uw winkelwagen");
        }
        return !(winkelwagenTableView.getSelectionModel().getSelectedCells().size() == 0);
    }
    private boolean isOneBoekSelected() {
        if(boekTableView.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Fout!", "Selecteer een boek");
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


}

