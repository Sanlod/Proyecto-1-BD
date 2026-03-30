package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.AssociationDAO;
import com.example.bdbconsultas.Entidades.Association;
import javafx.application.Application;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MainController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TableView<Association> tableView = new TableView();

        TableColumn<Association, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(Integer.toString(data.getValue().getId())));

        TableColumn<Association, String> nomCol = new TableColumn<>("NOME");
        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));

        tableView.getColumns().addAll(idCol, nomCol);
        tableView.setItems(AssociationDAO.getAssociations());

    }
    public static void main(String[] args) {
        launch();
    }
}
