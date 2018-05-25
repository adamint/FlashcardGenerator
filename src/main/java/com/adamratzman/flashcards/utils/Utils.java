package com.adamratzman.flashcards.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Utils {
    /**
     * Display an alert with the specified type.
     *
     * @param alertType The seriousness of the alert.
     * @param message   What is to be displayed.
     */
    public static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle("Flashcard Generator");
        alert.show();
    }

    /**
     * Given a VBox, construct a list of answers.
     *
     * @param vb the JavaFX VBox containing answers.
     * @return List of answers at this moment.
     */
    public static ArrayList<String> getAnswers(VBox vb) {
        // Uses the stream api to remove unnecessary LOCs. None! and Answers: are not valid answers (what's displayed)
        // so disregard them, then collect what's left into an arraylist
        return vb.getChildren().stream().filter(node -> !((Label) node).getText().equals("None!") &&
                !((Label) node).getText().equals("Answers:")).map(node -> ((Label) node).getText())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
