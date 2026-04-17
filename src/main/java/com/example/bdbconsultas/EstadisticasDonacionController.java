package com.example.bdbconsultas;
import com.example.bdbconsultas.DAOs.EstadisticasDonacionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadisticasDonacionController {

    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    @FXML private TableView<ObservableList<String>>           tblStats;
    @FXML private TableColumn<ObservableList<String>, String> colName;
    @FXML private TableColumn<ObservableList<String>, String> colType;
    @FXML private TableColumn<ObservableList<String>, String> colTotal;
    @FXML private TableColumn<ObservableList<String>, String> colPercent;

    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis            xAxis;
    @FXML private NumberAxis              yAxis;

    @FXML private Label lblGrandTotal;
    @FXML private Label lblAssociationTotal;
    @FXML private Label lblAssociationPct;
    @FXML private Label lblRescuerTotal;
    @FXML private Label lblRescuerPct;

    private final EstadisticasDonacionDAO dao = new EstadisticasDonacionDAO();

    @FXML
    public void initialize() {
        dpStartDate.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        dpEndDate.setValue(LocalDate.now());
        setupTableColumns();
        loadStats();
    }

    public void switchVolver(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/bdbconsultas/Usuario.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void setupTableColumns() {
        colName.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(0)));
        colType.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(1)));
        colTotal.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(2)));
        colPercent.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(3)));
    }

    @FXML
    private void onFilter() {
        loadStats();
    }

    private void loadStats() {
        LocalDate start = dpStartDate.getValue();
        LocalDate end   = dpEndDate.getValue();

        try {
            ObservableList<ObservableList<String>> rows =
                    dao.getStatsByEntityAndDate(start, end);

            // Total general
            double grandTotal = rows.stream()
                    .mapToDouble(r -> Double.parseDouble(r.get(2)))
                    .sum();

            lblGrandTotal.setText(String.format("%.2f", grandTotal));

            // Totales por tipo
            double assocTotal   = rows.stream()
                    .filter(r -> r.get(1).equals("Asociación"))
                    .mapToDouble(r -> Double.parseDouble(r.get(2)))
                    .sum();

            double rescuerTotal = rows.stream()
                    .filter(r -> r.get(1).equals("Rescatista"))
                    .mapToDouble(r -> Double.parseDouble(r.get(2)))
                    .sum();

            lblAssociationTotal.setText(String.format("%.2f", assocTotal));
            lblAssociationPct.setText(String.format("%.1f%% del total",
                    grandTotal == 0 ? 0 : assocTotal * 100.0 / grandTotal));

            lblRescuerTotal.setText(String.format("%.2f", rescuerTotal));
            lblRescuerPct.setText(String.format("%.1f%% del total",
                    grandTotal == 0 ? 0 : rescuerTotal * 100.0 / grandTotal));

            // Agregar porcentaje como [3]
            rows.forEach(r -> {
                if (r.size() == 3) {
                    double val = Double.parseDouble(r.get(2));
                    double pct = grandTotal == 0 ? 0 : (val * 100.0 / grandTotal);
                    r.add(String.format("%.1f%%", pct));
                }
            });

            tblStats.setItems(rows);
            buildBarChart(rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buildBarChart(ObservableList<ObservableList<String>> rows) {
        barChart.getData().clear();

        Map<String, XYChart.Series<String, Number>> seriesMap = new LinkedHashMap<>();

        for (ObservableList<String> row : rows) {
            String name  = row.get(0);
            String type  = row.get(1);
            double total = Double.parseDouble(row.get(2));

            seriesMap.computeIfAbsent(type, t -> {
                XYChart.Series<String, Number> s = new XYChart.Series<>();
                s.setName(t);
                return s;
            }).getData().add(new XYChart.Data<>(name, total));
        }

        barChart.getData().addAll(seriesMap.values());
    }
}