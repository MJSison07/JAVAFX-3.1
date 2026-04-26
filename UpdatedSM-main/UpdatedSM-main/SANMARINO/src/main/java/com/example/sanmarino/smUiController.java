package com.example.sanmarino;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class smUiController {

    private Stage stage;
    private Scene scene;
    private static final ObservableList<String> sharedMessages = FXCollections.observableArrayList();

    @FXML private TextField announcementInput;
    @FXML private VBox announcementContainer;
    @FXML private VBox homeAnnouncementContainer;

    @FXML
    public void initialize() {
        refreshContainers();
    }

    @FXML private TableView<?> table;
    @FXML private TextField searchField;

    @FXML
    void openAddPopup(ActionEvent event) {
        System.out.println("Add Resident Clicked");
    }

    @FXML
    void openEditPopup(ActionEvent event) {
        System.out.println("Edit Resident Clicked");
    }

    @FXML
    void deleteResident(ActionEvent event) {
        System.out.println("Delete Resident Clicked");
    }

    
    private void navigateTo(Event event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            if (fxmlFile.equalsIgnoreCase("RECORDSCOPY.fxml")) {
                stage.setMaximized(true);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML void switchToAnnouncement(javafx.scene.input.MouseEvent event) { navigateTo(event, "ANNOUNCEMENT.fxml"); }
    @FXML void switchToHOME(Event e) { navigateTo(e, "HOME.fxml"); }
    @FXML void switchToAbout(Event e) { navigateTo(e, "ABOUT.fxml"); }
    @FXML void switchToPROFILE(Event e) { navigateTo(e, "PROFILE.fxml"); }
    @FXML void switchToRecords(Event e) { navigateTo(e, "RECORDSCOPY.fxml"); }
    

    @FXML public void goAnnouncement(MouseEvent event) { navigateTo(event, "ANNOUNCEMENT.fxml"); }
    @FXML public void goHome(MouseEvent event) { navigateTo(event, "HOME.fxml"); }
    @FXML public void goRecords(MouseEvent event) { navigateTo(event, "RECORDSCOPY.fxml"); }
    @FXML public void goAbout(MouseEvent event) { navigateTo(event, "F.fxml"); }

    @FXML
    void handlePostAnnouncement(Event event) {
        String message = announcementInput.getText();
        if (message != null && !message.trim().isEmpty()) {
            sharedMessages.add(0, message);
            refreshContainers();
            announcementInput.clear();
        }
    }

    private void refreshContainers() {
        if (announcementContainer != null) {
            announcementContainer.getChildren().clear();
            sharedMessages.forEach(msg -> announcementContainer.getChildren().add(createAnnouncementCard(msg)));
        }
        if (homeAnnouncementContainer != null) {
            homeAnnouncementContainer.getChildren().clear();
            sharedMessages.forEach(msg -> homeAnnouncementContainer.getChildren().add(createAnnouncementCard(msg)));
        }
    }

    private VBox createAnnouncementCard(String message) {
    VBox cardDiv = new VBox();
    
    
    cardDiv.getStyleClass().add("announcement-div"); 
    cardDiv.setSpacing(10);

    Label content = new Label("📌 " + message);
    content.getStyleClass().add("announcement-text");
    content.setWrapText(true);

    Button btnRemove = new Button("Remove");
    
    btnRemove.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 10px; -fx-background-radius: 5;");
    
    btnRemove.setOnAction(e -> {
        sharedMessages.remove(message);
        refreshContainers();
    });

    cardDiv.getChildren().addAll(content, btnRemove);
    return cardDiv;
    }


    
}