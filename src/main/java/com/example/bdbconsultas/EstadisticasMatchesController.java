package com.example.bdbconsultas;

import com.example.bdbconsultas.DAOs.EstadisticasMatchesDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.*;
import java.sql.SQLException;

public class EstadisticasMatchesController {

    @FXML private TableView<ObservableList<String>>           tblStats;
    @FXML private TableColumn<ObservableList<String>, String> colStatus;
    @FXML private TableColumn<ObservableList<String>, String> colTotal;
    @FXML private TableColumn<ObservableList<String>, String> colAvgSimilarity;
    @FXML private TableColumn<ObservableList<String>, String> colPercent;

    @FXML private PieChart pieChart;

    @FXML private Label lblTotalRecords;
    @FXML private Label lblOverallAvg;

    private final EstadisticasMatchesDAO dao = new EstadisticasMatchesDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadStats();
    }

    private void setupTableColumns() {
        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(0)));
        colTotal.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(1)));
        colAvgSimilarity.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(2) + "%"));
        colPercent.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(3)));
    }

    private void loadStats() {
        try {
            ObservableList<ObservableList<String>> rows = dao.getMatchStats();

            int grandTotal = rows.stream()
                    .mapToInt(r -> Integer.parseInt(r.get(1)))
                    .sum();

            double overallAvg = rows.stream()
                    .mapToDouble(r -> Double.parseDouble(r.get(2)))
                    .average()
                    .orElse(0);

            lblTotalRecords.setText(String.valueOf(grandTotal));
            lblOverallAvg.setText(String.format("%.1f%%", overallAvg));

            // Agregar porcentaje como [3]
            rows.forEach(r -> {
                if (r.size() == 3) {
                    int val = Integer.parseInt(r.get(1));
                    double pct = grandTotal == 0 ? 0 : (val * 100.0 / grandTotal);
                    r.add(String.format("%.1f%%", pct));
                }
            });

            tblStats.setItems(rows);
            buildPieChart(rows);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void buildPieChart(ObservableList<ObservableList<String>> rows) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        rows.forEach(r -> data.add(
                new PieChart.Data(r.get(0) + " (" + r.get(3) + ")", Integer.parseInt(r.get(1)))
        ));
        pieChart.setData(data);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
    }
}