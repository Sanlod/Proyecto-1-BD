package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.AdopcionesDAO;
//import com.example.bdbconsultas.models.Sesion; //hipotetico para manejo de usuario
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.sql.SQLException;
import java.time.LocalDate;


public class AdopcionesController {

    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private ComboBox<ObservableList<String>> cbMascota;
    @FXML private ComboBox<ObservableList<String>> cbAdoptante;
    @FXML private Label lblResultados;
    @FXML private TableView<ObservableList<String>> tablaAdopciones;

    @FXML private Button btnAprobar;
    @FXML private Button btnRechazar;

    private final AdopcionesDAO dao = new AdopcionesDAO();


    @FXML
    public void initialize() {
        dpDesde.setValue(LocalDate.now().withDayOfYear(1));
        dpHasta.setValue(LocalDate.now());

        cargarCombosFiltro();

        /* implementacion admin
        boolean isAdmin = Sesion.getUsuarioActual().getRol().equalsIgnoreCase("ADMIN");
        btnAprobar.setVisible(isAdmin);
        btnRechazar.setVisible(isAdmin);

        //necesita lista negra para no dar error
        tablaAdopciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                verificarListaNegra(newVal);
            }
        });

         */
    }

    private void cargarCombosFiltro() {
        try {
            ObservableList<ObservableList<String>> mascotas = FXCollections.observableArrayList();
            mascotas.add(FXCollections.observableArrayList("0", "Todos"));
            mascotas.addAll(AdopcionesDAO.getMascotas());
            cbMascota.setItems(mascotas);
            cbMascota.getSelectionModel().selectFirst();
            cbMascota.setConverter(crearConverter());

            ObservableList<ObservableList<String>> adoptantes = FXCollections.observableArrayList();
            adoptantes.add(FXCollections.observableArrayList("0", "Todos"));
            adoptantes.addAll(AdopcionesDAO.getAdoptantes());
            cbAdoptante.setItems(adoptantes);
            cbAdoptante.getSelectionModel().selectFirst();
            cbAdoptante.setConverter(crearConverter());
        } catch (Exception e) {
            mostrarAlerta("Error de carga", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void onBuscar() {
        try {
            String idM = cbMascota.getValue().get(0);
            String idA = cbAdoptante.getValue().get(0);

            AdopcionesDAO.ResultadoConsulta res = dao.consultarSolicitudes(
                    dpDesde.getValue(), dpHasta.getValue(), idM, idA);

            configurarColumnasDinamicas((ObservableList<String>) res.columnas);
            tablaAdopciones.setItems(res.filas);
            lblResultados.setText("Total registros: " + res.total); // Requerimiento 5.f

        } catch (Exception e) {
            mostrarAlerta("Error de consulta", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

   /* (igual tiene que ver con tener la clase de sesion o log in para verificar nombre del logeado)
    public void onAprobar() {
        ObservableList<String> seleccion = tablaAdopciones.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarAlerta("Atención", "Seleccione una solicitud de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean exito = dao.actualizarEstadoSolicitud(seleccion.get(0), "APROBADA", Sesion.getUsuarioActual().getNombre());
            if (exito) {
                mostrarAlerta("Éxito", "Adopción aprobada. El estado de la mascota ha cambiado.", Alert.AlertType.INFORMATION);
                onBuscar();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo procesar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    */

    private void verificarListaNegra(ObservableList<String> fila) {
        //necesita dao de lista negra
    }

    private void configurarColumnasDinamicas(ObservableList<String> nombresColumnas) {
        tablaAdopciones.getColumns().clear();
        for (int i = 0; i < nombresColumnas.size(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(nombresColumnas.get(i));
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
            tablaAdopciones.getColumns().add(col);
        }
    }

    private StringConverter<ObservableList<String>> crearConverter() {
        return new StringConverter<>() {
            @Override public String toString(ObservableList<String> f) { return f != null ? f.get(1) : ""; }
            @Override public ObservableList<String> fromString(String s) { return null; }
        };
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(msg);
        a.showAndWait();
    }
}