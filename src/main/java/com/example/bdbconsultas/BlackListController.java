package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.PersonaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class BlackListController implements Initializable {

    @FXML private TableView<ObservableList<String>> tblDatos;
    @FXML private TableColumn<ObservableList<String>, String> colId;
    @FXML private TableColumn<ObservableList<String>, String> colNombreCompleto;

    @FXML private TextField txtCalificacion;
    @FXML private TextArea txtNotas;

    @FXML private Button btnVolver;

    private final PersonaDAO dao = new PersonaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
        limpiarFormulario();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(0)));
        colNombreCompleto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(1)));

        tblDatos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarSeleccionEnFormulario(newVal);
            }
        });
    }

    private void cargarDatos() {
        try {
            ObservableList<ObservableList<String>> datos = PersonaDAO.getPersonasListaNegra();
            tblDatos.setItems(datos);
        } catch (Exception e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    private void cargarSeleccionEnFormulario(ObservableList<String> fila) {
        String idPersona = fila.get(0);
        txtNotas.setText(fila.get(2) != null ? fila.get(2) : "");

        try {
            String calificacion = PersonaDAO.obtenerCalificacionPersona(idPersona);
            txtCalificacion.setText(calificacion != null ? calificacion + " estrellas" : "Sin calificación");
        } catch (Exception e) {
            txtCalificacion.setText("Error al cargar calificación");
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtNotas.clear();
        txtCalificacion.clear();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }


    public void switchVolver(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/bdbconsultas/Usuario.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}