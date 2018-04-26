package com.adamratzman.flashcards.utils;

import javafx.scene.control.Alert;

public class Utils {
    public static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
