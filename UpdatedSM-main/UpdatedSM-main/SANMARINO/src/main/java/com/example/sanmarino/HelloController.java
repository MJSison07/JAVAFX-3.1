package com.example.sanmarino;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import java.time.LocalDate;
import java.util.Comparator;

public class HelloController {

    @FXML private TableView<Resident> table;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterBox;

    @FXML private TableColumn<Resident, String> idCol, lastCol, middleCol,
            firstCol, suffixCol, contactCol, emailCol,
            houseCol, streetCol, civilCol, birthCol;

    private final ObservableList<Resident> list = FXCollections.observableArrayList();
    private FilteredList<Resident> filtered;

    @FXML
    public void initialize() {

        idCol.setCellValueFactory(d -> d.getValue().idProperty());
        lastCol.setCellValueFactory(d -> d.getValue().lastProperty());
        middleCol.setCellValueFactory(d -> d.getValue().middleProperty());
        firstCol.setCellValueFactory(d -> d.getValue().firstProperty());
        suffixCol.setCellValueFactory(d -> d.getValue().suffixProperty());
        contactCol.setCellValueFactory(d -> d.getValue().contactProperty());
        emailCol.setCellValueFactory(d -> d.getValue().emailProperty());
        houseCol.setCellValueFactory(d -> d.getValue().houseProperty());
        streetCol.setCellValueFactory(d -> d.getValue().streetProperty());
        civilCol.setCellValueFactory(d -> d.getValue().civilProperty());
        birthCol.setCellValueFactory(d -> d.getValue().birthProperty());

        filterBox.getItems().addAll("ID", "Last Name", "First Name");
        filterBox.setValue("Last Name");

        filtered = new FilteredList<>(list, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        filterBox.valueProperty().addListener((obs, o, n) -> {
            applyFilter();
            applySort();
        });

        table.setItems(filtered);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        applySort();
    }

    private void applyFilter() {
        String keyword = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String filter = filterBox.getValue();

        filtered.setPredicate(resident -> {
            if (keyword.isEmpty()) return true;

            return switch (filter) {
                case "ID"         -> resident.getId().toLowerCase().contains(keyword);
                case "Last Name"  -> resident.getLast().toLowerCase().contains(keyword);
                case "First Name" -> resident.getFirst().toLowerCase().contains(keyword);
                default           -> true;
            };
        });
    }

    private void applySort() {
        String filter = filterBox.getValue();
        if (filter == null) return;

        Comparator<Resident> comparator = switch (filter) {
            case "ID" -> Comparator.comparing(r -> {
                try { return Integer.parseInt(r.getId()); }
                catch (NumberFormatException e) { return Integer.MAX_VALUE; }
            });
            case "Last Name"  -> Comparator.comparing(r -> r.getLast().toLowerCase());
            case "First Name" -> Comparator.comparing(r -> r.getFirst().toLowerCase());
            default -> null;
        };

        if (comparator != null) {
            FXCollections.sort(list, comparator);
        }
    }

    @FXML
    public void openAddPopup() {
        showForm(null);
    }

    @FXML
    public void openEditPopup() {
        Resident selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a resident first.");
            return;
        }
        showForm(selected);
    }

    private void showForm(Resident existing) {

        Stage popup = new Stage();
        popup.setTitle(existing == null ? "Add Resident" : "Edit Resident");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        double w = 180;

        TextField id          = new TextField();
        TextField last        = new TextField();
        TextField middle      = new TextField();
        TextField first       = new TextField();
        TextField suffix      = new TextField();
        TextField contact     = new TextField();
        TextField email       = new TextField();
        TextField houseNumber = new TextField();
        TextField street      = new TextField();

        ComboBox<String> civil = new ComboBox<>();
        civil.getItems().addAll("Single", "Married", "Widowed");

        DatePicker birth = new DatePicker();

        TextField[] fields = { id, last, middle, first, suffix, contact, email, houseNumber, street };
        for (TextField f : fields) f.setPrefWidth(w);
        civil.setPrefWidth(w);
        birth.setPrefWidth(w);

        if (existing != null) {
            id.setText(existing.getId());
            last.setText(existing.getLast());
            middle.setText(existing.getMiddle());
            first.setText(existing.getFirst());
            suffix.setText(existing.getSuffix());
            contact.setText(existing.getContact());
            email.setText(existing.getEmail());
            houseNumber.setText(existing.getHouse());
            street.setText(existing.getStreet());
            civil.setValue(existing.getCivil());

            if (existing.getBirth() != null && !existing.getBirth().isEmpty()) {
                try {
                    birth.setValue(LocalDate.parse(existing.getBirth()));
                } catch (Exception e) {
                    birth.setValue(null);
                }
            }
        }

        grid.addRow(0,  new Label("ID"),           id);
        grid.addRow(1,  new Label("Last Name"),     last);
        grid.addRow(2,  new Label("Middle Name"),   middle);
        grid.addRow(3,  new Label("First Name"),    first);
        grid.addRow(4,  new Label("Suffix"),        suffix);
        grid.addRow(5,  new Label("Contact"),       contact);
        grid.addRow(6,  new Label("Email"),         email);
        grid.addRow(7,  new Label("Civil Status"),  civil);
        grid.addRow(8,  new Label("House Number"),  houseNumber);
        grid.addRow(9,  new Label("Street"),        street);
        grid.addRow(10, new Label("Date of Birth"), birth);

        Button save = new Button("Save");
        save.setPrefWidth(120);

        save.setOnAction(e -> {
            if (id.getText().isEmpty() || first.getText().isEmpty()) {
                showAlert("ID and First Name are required.");
                return;
            }

            String birthValue = birth.getValue() == null ? "" : birth.getValue().toString();

            if (existing == null) {
                list.add(new Resident(
                        id.getText(), last.getText(), middle.getText(),
                        first.getText(), suffix.getText(), contact.getText(),
                        email.getText(), houseNumber.getText(), street.getText(),
                        civil.getValue(), birthValue
                ));
            } else {
                existing.setId(id.getText());
                existing.setLast(last.getText());
                existing.setMiddle(middle.getText());
                existing.setFirst(first.getText());
                existing.setSuffix(suffix.getText());
                existing.setContact(contact.getText());
                existing.setEmail(email.getText());
                existing.setHouse(houseNumber.getText());
                existing.setStreet(street.getText());
                existing.setCivil(civil.getValue());
                existing.setBirth(birthValue);
            }

            applySort();
            popup.close();
        });

        VBox layout = new VBox(15, grid, save);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        popup.setScene(new Scene(layout, 360, 520));
        popup.show();
    }

    @FXML
    public void deleteResident() {
        Resident selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Select a resident first.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this resident?");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no  = new ButtonType("No",  ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(response -> {
            if (response == yes) list.remove(selected);
        });
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText(msg);
        a.show();
    }

    @FXML
    public void goAnnouncement(MouseEvent event) throws IOException {
        switchScene(event, "ANNOUNCEMENT.fxml");
    }

    @FXML
    public void goHome(MouseEvent event) throws IOException {
        switchScene(event, "HOME.fxml");
    }

    @FXML
    public void goRecords(MouseEvent event) throws IOException {
        switchScene(event, "RECORDSCOPY.fxml"); // Or whatever your file is named

    }

    @FXML
    public void goAbout(MouseEvent event)  throws IOException {
        switchScene(event, "RECORDSCOPY.fxml");

    }

    // Helper method to keep your code clean
    private void switchScene(MouseEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
