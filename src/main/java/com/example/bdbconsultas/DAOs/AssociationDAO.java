package com.example.bdbconsultas.DAOs;
import java.sql.*;

import com.example.bdbconsultas.DBConnection;
import com.example.bdbconsultas.Entidades.Association;
import javafx.collections.*;

public class AssociationDAO {
    public static ObservableList<Association> getAssociations(){
        ObservableList<Association> associations = FXCollections.observableArrayList();
        try{
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM association";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                associations.add(new Association(
                        rs.getInt("ID"),
                        rs.getString("NOMBRE")
                ));
            }
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return associations;
    }

}
