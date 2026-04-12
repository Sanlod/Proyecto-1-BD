package com.example.bdbconsultas;
import com.example.bdbconsultas.DAOs.EstadisticasAdoptionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class EstadisticasAdoptionController {

    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    @FXML private ComboBox<String> cbPetType;
    @FXML private ComboBox<String> cbBreed;

    @FXML private TableView<ObservableList<String>>           tblStats;
    @FXML private TableColumn<ObservableList<String>, String> colStatus;
    @FXML private TableColumn<ObservableList<String>, String> colTotal;
    @FXML private TableColumn<ObservableList<String>, String> colPercent;

    @FXML private PieChart pieChart;

    @FXML private Label lblSuccessTotal;
    @FXML private Label lblSuccessPct;
    @FXML private Label lblWaitingTotal;
    @FXML private Label lblWaitingPct;
    @FXML private Label lblGrandTotal;

    private final EstadisticasAdoptionDAO dao = new EstadisticasAdoptionDAO();

    // Guarda los ids correspondientes a cada opción del ComboBox
    private ObservableList<ObservableList<String>> petTypes;
    private ObservableList<ObservableList<String>> breeds;

    @FXML
    public void initialize() {
        dpStartDate.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        dpEndDate.setValue(LocalDate.now());
        setupTableColumns();
        loadComboBoxes();
    }

    private void setupTableColumns() {
        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(0)));
        colTotal.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(1)));
        colPercent.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().get(2)));
    }

    private void loadComboBoxes() {
        try {
            petTypes = dao.getPetTypes();
            ObservableList<String> typeNames = FXCollections.observableArrayList();
            petTypes.forEach(r -> typeNames.add(r.get(1)));
            cbPetType.setItems(typeNames);
            if (!typeNames.isEmpty()) cbPetType.getSelectionModel().selectFirst();

            breeds = dao.getBreeds();
            ObservableList<String> breedNames = FXCollections.observableArrayList();
            breeds.forEach(r -> breedNames.add(r.get(1)));
            cbBreed.setItems(breedNames);
            if (!breedNames.isEmpty()) cbBreed.getSelectionModel().selectFirst();

            loadStats();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onFilter() {
        loadStats();
    }

    private void loadStats() {
        int typeIndex  = cbPetType.getSelectionModel().getSelectedIndex();
        int breedIndex = cbBreed.getSelectionModel().getSelectedIndex();

        if (typeIndex < 0 || breedIndex < 0) return;

        int idPetType = Integer.parseInt(petTypes.get(typeIndex).get(0));
        int idBreed   = Integer.parseInt(breeds.get(breedIndex).get(0));

        LocalDate start = dpStartDate.getValue();
        LocalDate end   = dpEndDate.getValue();

        try {
            ObservableList<ObservableList<String>> rows =
                    dao.getAdoptionStats(start, end, idPetType, idBreed);

            int grandTotal = rows.stream()
                    .mapToInt(r -> Integer.parseInt(r.get(1)))
                    .sum();

            lblGrandTotal.setText(String.valueOf(grandTotal));

            // Agregar porcentaje como [2]
            rows.forEach(r -> {
                if (r.size() == 2) {
                    int val = Integer.parseInt(r.get(1));
                    double pct = grandTotal == 0 ? 0 : (val * 100.0 / grandTotal);
                    r.add(String.format("%.1f%%", pct));
                }
            });

            // Llenar tarjetas
            rows.forEach(r -> {
                if (r.get(0).equals("Exitosa")) {
                    lblSuccessTotal.setText(r.get(1));
                    lblSuccessPct.setText(r.get(2) + " del total");
                } else if (r.get(0).equals("En espera")) {
                    lblWaitingTotal.setText(r.get(1));
                    lblWaitingPct.setText(r.get(2) + " del total");
                }
            });

            tblStats.setItems(rows);
            buildPieChart(rows, grandTotal);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buildPieChart(ObservableList<ObservableList<String>> rows, int grandTotal) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        rows.forEach(r -> {
            int val = Integer.parseInt(r.get(1));
            data.add(new PieChart.Data(r.get(0), val));
        });
        pieChart.setData(data);
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
    }
}