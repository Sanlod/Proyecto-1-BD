package com.example.bdbconsultas.DAOs;
import com.example.bdbconsultas.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class EstadisticasMatchesDAO {

    public ObservableList<ObservableList<String>> getMatchStats() throws SQLException, ClassNotFoundException {

        ObservableList<ObservableList<String>> results = FXCollections.observableArrayList();
        String sql = "{ call SP_STATS_MATCHES(?) }";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(rs.getString("match_status"));  // [0]
                    row.add(rs.getString("total"));         // [1]
                    row.add(rs.getString("avg_similarity")); // [2]
                    results.add(row);
                }
            }
        }
        return results;
    }
}