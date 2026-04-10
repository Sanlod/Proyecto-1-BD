package com.example.bdbconsultas.DAOs;

import com.example.bdbconsultas.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MatchesDAO {
    public static class ResultadoConsulta {
        public final List<String> columnas;
        public final ObservableList<ObservableList<String>> filas;
        public final int total;

        public ResultadoConsulta(List<String> columnas,
                                 ObservableList<ObservableList<String>> filas,
                                 int total) {
            this.columnas = columnas;
            this.filas = filas;
            this.total = total;
        }
    }

    public static ObservableList<ObservableList<String>> listadosCatalogo(String nomSP)
            throws SQLException, ClassNotFoundException {
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL " + nomSP + " (?) }")) {
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

    public static ObservableList<ObservableList<String>> getMascostasPerdidas()
            throws SQLException, ClassNotFoundException {
        return listadosCatalogo("SP_LISTAR_MASCOTASPERDIDAS");
    }
    public static ObservableList<ObservableList<String>> getMascotasHalladas()
            throws SQLException, ClassNotFoundException {
        return listadosCatalogo("SP_LISTAR_MASCOTASHALLADAS");
    }
    public static ObservableList<ObservableList<String>> getEstadosMatch()
            throws SQLException, ClassNotFoundException {
        return listadosCatalogo("SP_LISTAR_ESTADOSMATCH");
    }
    public static void cambiarEstadoMatch(
            String idMatch,
            String idEstado,
            String modifiedBy) throws SQLException, ClassNotFoundException {

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SP_CAMBIAR_ESTADOMATCH(?,?,?) }")) {

            cs.setString(1, idMatch);
            cs.setString(2, idEstado);
            cs.setString(3, modifiedBy);

            cs.execute();
        }
    }

    public MatchesDAO.ResultadoConsulta consultarMatches(
            String idMascotaPerdida,
            String idTipoMascota,
            String idRaza,
            String nombre,
            String idEstado,
            String idProvincia,
            String idCanton,
            String idDistrito,
            String idAsociacion) throws SQLException, ClassNotFoundException {



        List<String> columnas = new ArrayList<>();
        ObservableList<ObservableList<String>> filas = FXCollections.observableArrayList();
        int total = 0;

        try (Connection conn = DBConnection.getConnection()) {
            try (CallableStatement cs = conn.prepareCall(
                    "{ CALL SP_CONSULTAR_MATCHES(?,?,?,?,?,?,?,?,?,?,?) }")) {

                // Parámetros IN — si viene vacío manda NULL

                cs.setString(1, idMascotaPerdida.isEmpty()    ? null : idMascotaPerdida);
                cs.setString(2, idTipoMascota.isEmpty()  ? null : idTipoMascota);
                cs.setString(3, idRaza.isEmpty()  ? null : idRaza);
                cs.setString(4, nombre.isEmpty()  ? null : nombre);
                cs.setString(5, idEstado.isEmpty()  ? null : idEstado);
                cs.setString(6, idProvincia.isEmpty()  ? null : idProvincia);
                cs.setString(7, idCanton.isEmpty()  ? null : idCanton);
                cs.setString(8, idDistrito.isEmpty()  ? null : idDistrito);
                cs.setString(9, idAsociacion.isEmpty() ? null : idAsociacion);


                // Parámetros OUT
                cs.registerOutParameter(10, Types.REF_CURSOR);
                cs.registerOutParameter(11, Types.NUMERIC);

                cs.execute();

                total = cs.getInt(11);

                try (ResultSet rs = (ResultSet) cs.getObject(10)) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int numCols = meta.getColumnCount();

                    // Nombres de columnas
                    for (int i = 1; i <= numCols; i++) {
                        columnas.add(meta.getColumnLabel(i));
                    }

                    // Filas como strings
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
        }
        return new MatchesDAO.ResultadoConsulta(columnas, filas, total);
    }
}
