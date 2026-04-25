package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.AssociationDAO;
import com.example.bdbconsultas.DAOs.MascotasDAO;
import com.example.bdbconsultas.DAOs.RecompensasDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RecompensasController implements Initializable {

    @FXML private ComboBox<String> cmbMascota;
    @FXML private TableView<ObservableList<String>> tblRecompensas;
    @FXML private TableColumn<ObservableList<String>, String> colId;
    @FXML private TableColumn<ObservableList<String>, String> colMonto;
    @FXML private TableColumn<ObservableList<String>, String> colMoneda;
    @FXML private TableColumn<ObservableList<String>, String> colMascota;
    @FXML private Label lblTotal;

    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cmbMoneda;

    @FXML private ComboBox<String> cmbAsociacion;
    @FXML private ComboBox<String> cmbRecompensa;

    @FXML private Button btnVolver;

    private ObservableList<ObservableList<String>> datosMascotas;
    private ObservableList<ObservableList<String>> datosMonedas;
    private ObservableList<ObservableList<String>> datosAsociaciones;
    private ObservableList<ObservableList<String>> datosRecompensas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarMascotas();
        cargarMonedas();
        cargarAsociaciones();
        limpiarFormulario();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(0)));
        colMonto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(1)));
        colMoneda.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(2)));
        colMascota.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(3)));
    }

    private void cargarMascotas() {
        try {
            datosMascotas = MascotasDAO.getMascotas();
            ObservableList<String> nombres = FXCollections.observableArrayList();
            for (ObservableList<String> fila : datosMascotas) {
                nombres.add(fila.get(1));
            }
            cmbMascota.setItems(nombres);
        } catch (Exception e) {
            mostrarError("Error al cargar mascotas: " + e.getMessage());
        }
    }

    private void cargarMonedas() {
        try {
            datosMonedas = MascotasDAO.getMonedas();
            ObservableList<String> nombres = FXCollections.observableArrayList();
            for (ObservableList<String> fila : datosMonedas) {
                nombres.add(fila.get(1));
            }
            cmbMoneda.setItems(nombres);
        } catch (Exception e) {
            mostrarError("Error al cargar monedas: " + e.getMessage());
        }
    }

    private void cargarAsociaciones() {
        try {
            datosAsociaciones = AssociationDAO.getAsociaciones();
            ObservableList<String> nombres = FXCollections.observableArrayList();
            for (ObservableList<String> fila : datosAsociaciones) {
                nombres.add(fila.get(1));
            }
            cmbAsociacion.setItems(nombres);
        } catch (Exception e) {
            mostrarError("Error al cargar asociaciones: " + e.getMessage());
        }
    }

    @FXML
    private void onConsultar() {
        try {
            String idPet = null;
            int idx = cmbMascota.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                idPet = datosMascotas.get(idx).get(0);
            }

            RecompensasDAO.ResultadoConsulta resultado =
                    RecompensasDAO.consultarRecompensas(idPet);

            tblRecompensas.setItems(resultado.filas);
            lblTotal.setText("Total: " + resultado.total);

            datosRecompensas = resultado.filas;
            ObservableList<String> opciones = FXCollections.observableArrayList();
            if (datosRecompensas != null) {
                for (ObservableList<String> fila : datosRecompensas) {
                    opciones.add("ID: " + fila.get(0) + " - " + fila.get(3) + " (" + fila.get(1) + ")");
                }
            }
            cmbRecompensa.setItems(opciones);

        } catch (Exception e) {
            mostrarError("Error al consultar recompensas: " + e.getMessage());
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            if (txtMonto.getText().isEmpty()) {
                mostrarError("El monto es requerido.");
                return;
            }
            if (cmbMascota.getSelectionModel().getSelectedIndex() < 0) {
                mostrarError("Seleccione una mascota.");
                return;
            }
            if (cmbMoneda.getSelectionModel().getSelectedIndex() < 0) {
                mostrarError("Seleccione una moneda.");
                return;
            }

            String monto = txtMonto.getText().trim();
            String idPet = datosMascotas.get(cmbMascota.getSelectionModel().getSelectedIndex()).get(0);
            String idMoneda = datosMonedas.get(cmbMoneda.getSelectionModel().getSelectedIndex()).get(0);

            RecompensasDAO.registrarRecompensa(monto, idPet, idMoneda);

            mostrarInfo("Recompensa registrada correctamente.");
            limpiarRegistro();
            onConsultar();

        } catch (NumberFormatException e) {
            mostrarError("El monto debe ser un número válido.");
        } catch (Exception e) {
            mostrarError("Error al registrar recompensa: " + e.getMessage());
        }
    }

    @FXML
    private void onDonar() {
        try {
            int idxRec = cmbRecompensa.getSelectionModel().getSelectedIndex();
            int idxAsc = cmbAsociacion.getSelectionModel().getSelectedIndex();

            if (idxRec < 0) {
                mostrarError("Seleccione una recompensa.");
                return;
            }
            if (idxAsc < 0) {
                mostrarError("Seleccione una asociación.");
                return;
            }
            if (datosRecompensas == null || datosRecompensas.isEmpty()) {
                mostrarError("No hay recompensas disponibles para donar.");
                return;
            }

            String idRecompensa = datosRecompensas.get(idxRec).get(0);
            String idAsociacion = datosAsociaciones.get(idxAsc).get(0);
            String modifiedBy = "SYSTEM";

            RecompensasDAO.donarRecompensa(idRecompensa, idAsociacion, modifiedBy);

            mostrarInfo("Recompensa donada correctamente.");
            onConsultar();
            cmbRecompensa.setValue(null);

        } catch (Exception e) {
            mostrarError("Error al donar recompensa: " + e.getMessage());
        }
    }

    private void limpiarRegistro() {
        txtMonto.clear();
        cmbMascota.setValue(null);
        cmbMoneda.setValue(null);
    }

    private void limpiarFormulario() {
        limpiarRegistro();
        cmbAsociacion.setValue(null);
        cmbRecompensa.setValue(null);
        tblRecompensas.setItems(null);
        lblTotal.setText("Total: 0 resultados");
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
}