package com.example.sanmarino;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

  // --- NAVIGATION HELPER (The Polish) ---
  private void navigateTo(ActionEvent event, String fxmlFile) {
    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root);

      // Keeps the records window maximized as requested in original code
      if (fxmlFile.contains("RECORDS")) {
        stage.setMaximized(true);
      }

      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
    }
  }

  @FXML void switchToAnnouncement(ActionEvent e) { navigateTo(e, "ANNOUNCEMENT.fxml"); }
  @FXML void switchToHOME(ActionEvent e) { navigateTo(e, "HOME.fxml"); }
  @FXML void switchToAbout(ActionEvent e) { navigateTo(e, "ABOUT.fxml"); }
  @FXML void switchToPROFILE(ActionEvent e) { navigateTo(e, "PROFILE.fxml"); }
  @FXML void switchToRecords(ActionEvent e) { navigateTo(e, "RECORDSCOPY.fxml"); }

  @FXML
  void handlePostAnnouncement(ActionEvent event) {
    String message = announcementInput.getText();
    if (message != null && !message.trim().isEmpty()) {
      sharedMessages.add(0, message);
      refreshContainers();
      announcementInput.clear(); // Only cleared once now!
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
    btnRemove.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 10px;");
    btnRemove.setOnAction(e -> {
      sharedMessages.remove(message);
      refreshContainers();
    });

    cardDiv.getChildren().addAll(content, btnRemove);
    return cardDiv;
  }

  @FXML
  public void goAnnouncement(MouseEvent event) throws IOException {
    switchScene(event, "ANNOUNCEMENT.fxml");
  }

  @FXML
  public void goHome(MouseEvent event) throws IOException {
    // Already here, so maybe just refresh or do nothing
    System.out.println("Already on the Residents page.");
  }

  @FXML
  public void goRecords(MouseEvent event) throws IOException {
    switchScene(event, "RECORDSCOPY.fxml"); // Or whatever your file is named
  }

  @FXML
  public void goAbout(MouseEvent event) throws IOException {
    switchScene(event, "ABOUT.fxml");

  }

  // Helper method to keep your code clean
  private void switchScene(MouseEvent event, String fxmlFile) throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}