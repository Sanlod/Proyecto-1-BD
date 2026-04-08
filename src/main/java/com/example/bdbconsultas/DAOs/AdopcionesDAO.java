package com.example.bdbconsultas.DAOs;

import com.example.bdbconsultas.DBConnection;
import javafx.collections.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdopcionesDAO {

    public ResultadoConsulta consultarSolicitudes(String idM, String idA) {
        return null;
    }

    public static class ResultadoConsulta {
        public final List<String> columnas;
        public final ObservableList<ObservableList<String>> filas;
        public final int total;

        public ResultadoConsulta(List<String> columnas,
                                 ObservableList<ObservableList<String>> filas,
                                 int total) {
            this.columnas = columnas;
            this.filas    = filas;
            this.total    = total;
        }
    }


    public static ObservableList<ObservableList<String>> getMascotas()
            throws SQLException, ClassNotFoundException {
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_LISTAR_MASCOTAS(?) }")) {
            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();
            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                int numCols = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    ObservableList<String> fila = FXCollections.observableArrayList();
                    for (int i = 1; i <= numCols; i++) {
                        Object val = rs.getObject(i);
                        fila.add(val != null ? val.toString() : "");
                    }
                    filas.add(fila);
                }
            }
        }
        return filas;
    }

    public static ObservableList<ObservableList<String>> getAdoptantes()
            throws SQLException, ClassNotFoundException {
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_LISTAR_ADOPTANTES(?) }")) {
            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();
            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                int numCols = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    ObservableList<String> fila = FXCollections.observableArrayList();
                    for (int i = 1; i <= numCols; i++) {
                        Object val = rs.getObject(i);
                        fila.add(val != null ? val.toString() : "");
                    }
                    filas.add(fila);
                }
            }
        }
        return filas;
    }

    public ResultadoConsulta consultarSolicitudes(
            LocalDate desde, LocalDate hasta,
            String idMascota, String idAdoptante) throws SQLException, ClassNotFoundException {

        List<String> columnas = new ArrayList<>();
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();
        int total = 0;

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_CONSULTAR_SOLICITUDES(?,?,?,?,?,?) }")) {

            // Manejo de fechas nulas o vacías para evitar errores en Oracle
            cs.setObject(1, desde != null ? Date.valueOf(desde) : null);
            cs.setObject(2, hasta != null ? Date.valueOf(hasta) : null);
            cs.setString(3, (idMascota == null || idMascota.equals("0")) ? null : idMascota);
            cs.setString(4, (idAdoptante == null || idAdoptante.equals("0")) ? null : idAdoptante);

            cs.registerOutParameter(5, Types.REF_CURSOR);
            cs.registerOutParameter(6, Types.NUMERIC);

            cs.execute();
            total = cs.getInt(6);

            try (ResultSet rs = (ResultSet) cs.getObject(5)) {
                ResultSetMetaData meta = rs.getMetaData();
                int numCols = meta.getColumnCount();

                for (int i = 1; i <= numCols; i++) columnas.add(meta.getColumnLabel(i));

                while (rs.next()) {
                    ObservableList<String> fila = FXCollections.observableArrayList();
                    for (int i = 1; i <= numCols; i++) {
                        Object val = rs.getObject(i);
                        fila.add(val != null ? val.toString() : "");
                    }
                    filas.add(fila);
                }
            }
        }
        return new ResultadoConsulta(columnas, filas, total);
    }


    public boolean actualizarEstadoSolicitud(String idSolicitud, String nuevoEstado, String usuarioAdmin)
            throws SQLException, ClassNotFoundException {

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_GESTIONAR_SOLICITUD(?,?,?,?) }")) {

            cs.setString(1, idSolicitud);
            cs.setString(2, nuevoEstado);
            cs.setString(3, usuarioAdmin);
            cs.registerOutParameter(4, Types.NUMERIC);

            cs.execute();
            return cs.getInt(4) == 0;
        }
    }

    public boolean registrarAdopcion(String idMascota, String idAdoptante, int calificacion, String notas)
            throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_REGISTRAR_ADOPCION(?,?,?,?,?) }")) {
            cs.setString(1, idMascota);
            cs.setString(2, idAdoptante);
            cs.setInt(3, calificacion);
            cs.setString(4, notas);
            cs.registerOutParameter(5, Types.NUMERIC);
            cs.execute();
            return cs.getInt(5) == 0;
        }
    }
}