package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.CasaCunaDAO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CasaCunaController implements Initializable {

    @FXML private TextField txtIdPerson;
    @FXML private CheckBox checkRequiresFood;
    @FXML private ListView<String> listPetTypes;
    @FXML private ListView<String> listPetSizes;
    @FXML private ListView<String> listEnergyLevels;
    @FXML private ComboBox<String> comboDistrict;

    private final CasaCunaDAO casaCunaDAO = CasaCunaDAO.getInstance();

    // Mapas para rastrear qué elementos están marcados con el CheckBox
    private final Map<String, BooleanProperty> seleccionTipos = new HashMap<>();
    private final Map<String, BooleanProperty> seleccionTamanos = new HashMap<>();
    private final Map<String, BooleanProperty> seleccionEnergia = new HashMap<>();

    private ObservableList<ObservableList<String>> datosTipos;
    private ObservableList<ObservableList<String>> datosNivelesEnergia;
    private ObservableList<ObservableList<String>> datosDistritos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListViews();
        cargarDatos();
    }

    private void configurarListViews() {
        // Meter los checkboxes dentro de los Listview
        listPetTypes.setCellFactory(CheckBoxListCell.forListView(item ->
                seleccionTipos.computeIfAbsent(item, k -> new SimpleBooleanProperty(false))));

        listPetSizes.setCellFactory(CheckBoxListCell.forListView(item ->
                seleccionTamanos.computeIfAbsent(item, k -> new SimpleBooleanProperty(false))));

        listEnergyLevels.setCellFactory(CheckBoxListCell.forListView(item ->
                seleccionEnergia.computeIfAbsent(item, k -> new SimpleBooleanProperty(false))));
    }

    private void cargarDatos() {
        try {
            // Cargar datos desde el DAO
            datosTipos = casaCunaDAO.getTiposMascotas();
            listPetTypes.setItems(datosTipos.stream().map(row -> row.get(1))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));

            datosNivelesEnergia = casaCunaDAO.getNivelesEnergia();
            listEnergyLevels.setItems(datosNivelesEnergia.stream().map(row -> row.get(1))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));

            datosDistritos = casaCunaDAO.getDistritos();
            comboDistrict.setItems(datosDistritos.stream().map(row -> row.get(1))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));

            // Tamaños fijos
            listPetSizes.setItems(FXCollections.observableArrayList("Pequeño", "Mediano", "Grande"));

        } catch (Exception e) {
            mostrarError("Error al cargar catálogos: " + e.getMessage());
        }
    }

    @FXML
    private void guardarCasaCuna() {
        try {
            if (!validarCampos()) return;

            int idPersona = Integer.parseInt(txtIdPerson.getText());
            int requiereComida = checkRequiresFood.isSelected() ? 1 : 0;

            String idDistritoStr = obtenerIdSeleccionado(comboDistrict, datosDistritos);
            int idDistrito = Integer.parseInt(idDistritoStr);

            // Obtener strings separados por coma de los mapas de selección
            String tipos = obtenerSeleccionados(seleccionTipos);
            String tamanios = obtenerSeleccionados(seleccionTamanos);
            String nivelesEnergia = obtenerSeleccionados(seleccionEnergia);

            // Llamada al DAO
            int resultadoId = casaCunaDAO.CasaCuna(
                    idPersona, requiereComida, tipos, tamanios, idDistrito, nivelesEnergia
            );

            if (resultadoId != -1) {
                mostrarInfo("Casa Cuna registrada correctamente. ID: " + resultadoId);
                limpiarFormulario();
            } else {
                mostrarError("No se pudo completar el registro en la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarError("El ID de la persona debe ser un número válido.");
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    // Filtra el mapa para obtener solo los nombres que tienen el check marcado
    private String obtenerSeleccionados(Map<String, BooleanProperty> mapa) {
        return mapa.entrySet().stream()
                .filter(entry -> entry.getValue().get())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private String obtenerIdSeleccionado(ComboBox<String> cmb, ObservableList<ObservableList<String>> datos) {
        if (cmb.getValue() == null || datos == null) return null;
        String nombre = cmb.getValue();
        for (ObservableList<String> fila : datos) {
            if (fila.get(1).equals(nombre)) return fila.get(0);
        }
        return null;
    }

    private boolean validarCampos() {
        if (txtIdPerson.getText().isEmpty()) { mostrarError("ID de persona obligatorio."); return false; }
        if (obtenerSeleccionados(seleccionTipos).isEmpty()) { mostrarError("Seleccione al menos un tipo."); return false; }
        if (obtenerSeleccionados(seleccionTamanos).isEmpty()) { mostrarError("Seleccione al menos un tamaño."); return false; }
        if (comboDistrict.getValue() == null) { mostrarError("Distrito obligatorio."); return false; }
        return true;
    }

    private void limpiarFormulario() {
        txtIdPerson.clear();
        checkRequiresFood.setSelected(false);
        comboDistrict.setValue(null);

        // Desmarcar todos los CheckBoxes
        seleccionTipos.values().forEach(p -> p.set(false));
        seleccionTamanos.values().forEach(p -> p.set(false));
        seleccionEnergia.values().forEach(p -> p.set(false));
    }

    @FXML
    private void cancelar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/bdbconsultas/Admin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}