package be.kuleuven.dbproject.controller;

import be.kuleuven.dbproject.model.Bezoek;
import be.kuleuven.dbproject.model.Game;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ToonBezoekenController {

    @FXML
    public TableView bezoekTableView;

    SharedData sharedData = SharedData.getInstance();

    public void initialize() {
        initTable(sharedData.getMuseum().getBezoeken());
    }

    private void initTable(List<Bezoek> bezoeken) {

        bezoekTableView.getItems().clear();
        bezoekTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        bezoekTableView.getColumns().clear();

        int colIndex = 0;
        for(var colName : new String[]{"ID", "Naam bezoeker",  "Museum", "Adres","Bijdrage",}) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(colName);
            final int finalColIndex = colIndex;
            col.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().get(finalColIndex)));
            bezoekTableView.getColumns().add(col);
            colIndex++;
        }

        for(int i = 0; i <= bezoeken.size()-1; i++) {
            bezoekTableView.getItems().add(FXCollections.observableArrayList(String.valueOf(bezoeken.get(i).getBezoekID()), bezoeken.get(i).getBezoeker().getNaam(),String.valueOf(bezoeken.get(i).getMuseum().getNaam()),
                    String.valueOf(bezoeken.get(i).getMuseum().getAdres()),String.valueOf(bezoeken.get(i).getTotaleBedrag())));
        }
    }
}
