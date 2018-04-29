package com.adamratzman.flashcards.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle("Flashcard Generator");
        alert.show();
    }
    public static ArrayList<String> getAnswers(VBox vb) {
        return new ArrayList<>(vb.getChildren().stream().filter(node -> !((Label) node).getText().equals("None!") &&
                !((Label) node).getText().equals("Answers:")).map(node -> ((Label) node).getText()).collect(Collectors.toList()));
    }
}
