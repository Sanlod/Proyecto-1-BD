package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.MascotasDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class SaludController  implements Initializable {

    public TextField nombre;
    public TextField chip;
    public ComboBox<ObservableList<String>> buscMedicina;
    public ComboBox<ObservableList<String>> buscTratamiento;
    public ComboBox<ObservableList<String>> enfermedad;
    public TableView<ObservableList<String>> tablaMascotas;
    public ComboBox<ObservableList<String>> seleccTratamiento;
    public ComboBox<ObservableList<String>> seleccMedicina;
    public TextField dosis;
    private MascotasDAO mascotasDAO = MascotasDAO.getMascotasDAO();

    public void switchVolver(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/bdbconsultas/Usuario.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<ObservableList<String>> enfermedades = MascotasDAO.getEnfermedades();
            enfermedad.setItems(enfermedades);
            enfermedad.setConverter(new StringConverter<ObservableList<String>>() {
                @Override
                public String toString(ObservableList<String> fila) {
                    return fila != null ? fila.get(1) : "";
                }

                @Override
                public ObservableList<String> fromString(String s) {
                    return null;
                }
            });

            ObservableList<ObservableList<String>> tratamientos = MascotasDAO.getTratamientos();
            buscTratamiento.setItems(tratamientos);
            buscTratamiento.setConverter(new StringConverter<ObservableList<String>>() {
                @Override
                public String toString(ObservableList<String> fila) {
                    return fila != null ? fila.get(1) : "";
                }

                @Override
                public ObservableList<String> fromString(String s) {
                    return null;
                }
            });

            seleccTratamiento.setItems(tratamientos);
            buscTratamiento.setConverter(new StringConverter<ObservableList<String>>() {
                @Override
                public String toString(ObservableList<String> fila) {
                    return fila != null ? fila.get(1) : "";
                }

                @Override
                public ObservableList<String> fromString(String s) {
                    return null;
                }
            });

            ObservableList<ObservableList<String>> medicinas = MascotasDAO.getMedicamentos();
            buscMedicina.setItems(medicinas);
            buscMedicina.setConverter(new StringConverter<ObservableList<String>>() {
                @Override
                public String toString(ObservableList<String> fila) {
                    return fila != null ? fila.get(1) : "";
                }

                @Override
                public ObservableList<String> fromString(String s) {
                    return null;
                }
            });

            seleccMedicina.setItems(medicinas);
            seleccMedicina.setConverter(new StringConverter<ObservableList<String>>() {
                @Override
                public String toString(ObservableList<String> fila) {
                    return fila != null ? fila.get(1) : "";
                }

                @Override
                public ObservableList<String> fromString(String s) {
                    return null;
                }
            });

        }catch (Exception e){
            mostrarAlerta("Error inesperado al abrir la ventana, por favor vuelva e intente más tarde");
        }
    }

//crear y cambiar por buscarMascotaSalud que reciba y busque por nombre, chip, enfermedad, medicamento y tratamiento
    public void buscar() throws SQLException, ClassNotFoundException {
        String nombreStr;
        String chipStr;
        String medicina;
        String tratamiento;
        String idEnfermedad;
        if(!nombre.getText().isEmpty()){
            nombreStr = nombre.getText();
        }else{nombreStr = null;}

        if(!chip.getText().isEmpty()){
            chipStr = chip.getText();
        }else{chipStr = null;}

        if(!buscMedicina.getValue().toString().isEmpty()){
            medicina = buscMedicina.getValue().getFirst();
        }else{medicina = null;}

        if(!buscTratamiento.getValue().toString().isEmpty()){
            tratamiento = buscTratamiento.getValue().getFirst();
        }else{tratamiento = null;}

        if(!enfermedad.getValue().toString().isEmpty()){
            idEnfermedad = enfermedad.getValue().getFirst();
        }else{idEnfermedad = null;}

        MascotasDAO.ResultadoConsulta mascotas = mascotasDAO.consultarMascotas(null, null, nombreStr, chipStr, null, null,
                null, null, null, null, null, null, null);
    }

    public void asignarMedicina() throws SQLException, ClassNotFoundException {
        if(tablaMascotas.getSelectionModel().getSelectedItem() == null){
            mostrarAlerta("Por favor seleccione una mascota");
            return;
        }
        if(seleccMedicina.getValue().toString().isEmpty()){
            mostrarAlerta("Por favor seleccione una medicina");
        }
        MascotasDAO.registrarMedicamentoMascota(tablaMascotas.getSelectionModel().getSelectedItem().getFirst(),seleccMedicina.getSelectionModel().getSelectedItem().getFirst(),
                dosis.getText(), LocalDate.now(),LogInController.loggedUser);
    }

    public void asignarTratamiento() throws SQLException, ClassNotFoundException {
        if(tablaMascotas.getSelectionModel().getSelectedItem() == null){
            mostrarAlerta("Por favor seleccione una mascota");
            return;
        }
        if(seleccTratamiento.getValue().toString().isEmpty()){
            mostrarAlerta("Por favor seleccione un tratamiento");
        }
        MascotasDAO.registrarTratamientoMascota(tablaMascotas.getSelectionModel().getSelectedItem().getFirst(),seleccTratamiento.getSelectionModel().getSelectedItem().getFirst()
                ,LocalDate.now(),LogInController.loggedUser);
    }


    public void mostrarAlerta(String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Problema con entradas de usuario");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
