package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.MascotasDAO;
import com.example.bdbconsultas.DAOs.AssociationDAO;
import com.example.bdbconsultas.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CatalogosController implements Initializable {

    @FXML private ComboBox<String> cmbEntidad;
    @FXML private TableView<ObservableList<String>> tblDatos;
    @FXML private TableColumn<ObservableList<String>, String> colC1;
    @FXML private TableColumn<ObservableList<String>, String> colC2;
    @FXML private Spinner<Integer> spnIdEditar;
    @FXML private TextField txtNuevoValor;
    @FXML private TextField txtValorAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnAgregar;
    @FXML private Button btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarSpinner();
        cargarEntidades();
        configurarTabla();
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, 1);
        spnIdEditar.setValueFactory(valueFactory);
        spnIdEditar.setEditable(true);
    }

    private void cargarEntidades() {
        ObservableList<String> entidades = FXCollections.observableArrayList(
                "Asociación", "Color", "Raza", "Tipo Mascota", "Estado",
                "Severidad", "Nivel Energía", "Moneda", "Enfermedad",
                "Tratamiento", "Medicamento", "Provincia"
        );
        cmbEntidad.setItems(entidades);
    }

    private void configurarTabla() {
        colC1.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(0)));
        colC2.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(1)));
    }

    @FXML
    private void onEntidadSeleccionada() {
        String entidad = cmbEntidad.getValue();
        if (entidad != null) {
            cargarDatos(entidad);
        }
    }

    private void cargarDatos(String entidad) {
        try {
            ObservableList<ObservableList<String>> datos = null;

            switch(entidad) {
                case "Asociación":
                    datos = AssociationDAO.getAsociaciones();
                    break;
                case "Color":
                    datos = MascotasDAO.getColores();
                    break;
                case "Raza":
                    datos = MascotasDAO.getRazas();
                    break;
                case "Tipo Mascota":
                    datos = MascotasDAO.getTiposMascotas();
                    break;
                case "Estado":
                    datos = MascotasDAO.getEstados();
                    break;
                case "Severidad":
                    datos = MascotasDAO.getSeveridades();
                    break;
                case "Nivel Energía":
                    datos = MascotasDAO.getNivEnergia();
                    break;
                case "Moneda":
                    datos = MascotasDAO.getMonedas();
                    break;
                case "Enfermedad":
                    datos = MascotasDAO.getEnfermedades();
                    break;
                case "Tratamiento":
                    datos = MascotasDAO.getTratamientos();
                    break;
                case "Medicamento":
                    datos = MascotasDAO.getMedicamentos();
                    break;
                case "Provincia":
                    datos = MascotasDAO.getProvincias();
                    break;
            }

            if (datos != null) {
                tblDatos.setItems(datos);
            }

        } catch (SQLException | ClassNotFoundException e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        Integer id = spnIdEditar.getValue();
        String nuevoValor = txtNuevoValor.getText();
        String entidad = cmbEntidad.getValue();

        if (id == null) {
            mostrarError("El ID a editar no puede estar vacío");
            return;
        }
        if (!validarCampo(nuevoValor, "Nuevo valor")) return;
        if (!validarCampo(entidad, "Entidad")) return;

        try {
            String tabla = obtenerTabla(entidad);
            String sql = "UPDATE " + tabla +
                    " SET name = ?, modifiedBy = USER, modifiedAt = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nuevoValor);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

            cargarDatos(entidad);
            limpiarCamposEdicion();
            mostrarInfo("Registro actualizado correctamente");

        } catch (SQLException | ClassNotFoundException e) {
            mostrarError("Error al editar: " + e.getMessage());
        }
    }

    @FXML
    private void onAgregar() {
        String valor = txtValorAgregar.getText();
        String entidad = cmbEntidad.getValue();

        if (!validarCampo(valor, "Valor a agregar")) return;
        if (!validarCampo(entidad, "Entidad")) return;

        try {
            String tabla = obtenerTabla(entidad);
            String sql = "INSERT INTO " + tabla +
                    " (id, name, createdBy, createdAt, modifiedBy, modifiedAt) " +
                    "VALUES (SEQ_" + tabla.toUpperCase() + ".NEXTVAL, ?, USER, CURRENT_TIMESTAMP, USER, CURRENT_TIMESTAMP)";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, valor);
                ps.executeUpdate();
            }

            cargarDatos(entidad);
            txtValorAgregar.clear();
            mostrarInfo("Registro agregado correctamente");

        } catch (SQLException | ClassNotFoundException e) {
            mostrarError("Error al agregar: " + e.getMessage());
        }
    }

    private String obtenerTabla(String entidad) {
        switch(entidad) {
            case "Asociación": return "Association";
            case "Color": return "Colour";
            case "Raza": return "Breed";
            case "Tipo Mascota": return "PetType";
            case "Estado": return "Status";
            case "Severidad": return "Severity";
            case "Nivel Energía": return "EnergyLevel";
            case "Moneda": return "Currency";
            case "Enfermedad": return "Disease";
            case "Tratamiento": return "Treatment";
            case "Medicamento": return "Medication";
            case "Provincia": return "Province";
            default: return null;
        }
    }

    private boolean validarCampo(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            mostrarError("El campo '" + nombreCampo + "' no puede estar vacío");
            return false;
        }
        return true;
    }

    private void limpiarCamposEdicion() {
        spnIdEditar.getValueFactory().setValue(1);
        txtNuevoValor.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
}