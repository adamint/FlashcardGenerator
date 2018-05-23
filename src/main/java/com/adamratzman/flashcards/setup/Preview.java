package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;
import com.adamratzman.flashcards.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Preview {
    private ArrayList<Flashcard> flashcards;
    private Stage stage = new Stage();
    private Label totalFlashcards;
    private ListView<Pane> flashcardList;
    private Setup setup;

    public Preview(Setup setup, ArrayList<Flashcard> flashcards) {
        try {
            this.setup = setup;
            this.flashcards = flashcards;
            Parent root = FXMLLoader.load(getClass().getResource("/preview.fxml"));
            totalFlashcards = (Label) root.lookup("#total-flashcards");
            totalFlashcards.setText("Total Flashcards (" + flashcards.size() + ")");
            flashcardList = (ListView<Pane>) root.lookup("#flashcard-list");
            Scene scene = new Scene(root);

            render();

            scene.lookup("#export").setOnMouseClicked(event -> {
                if (flashcards.size() == 0)
                    Utils.showAlert(Alert.AlertType.ERROR, "You need at least one flashcard to export");
                else {
                    FileChooser exportFileChooser = new FileChooser();
                    exportFileChooser.getExtensionFilters()
                            .add(new FileChooser.ExtensionFilter("Flashcard Deck File (fdeck)", "*.fdeck"));
                    File chosen = exportFileChooser.showSaveDialog(stage);
                    if (chosen != null) export(chosen);
                }
            });

            scene.lookup("#preview").requestFocus();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void export(File file) {
        try {
            if (file.exists()) file.delete();
            file.createNewFile();

            OutputStreamWriter outputWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputWriter.write(setup.getGson().toJson(new ExportedDeck(System.currentTimeMillis(), flashcards)));
            outputWriter.close();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Successfully exported flashcard deck to " + file.getPath());
        } catch (Exception e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Unable to export flashcard deck here. Please check permissions");
        }
    }

    ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    Label getFlashcardsLabel() {
        return totalFlashcards;
    }

    Setup getSetup() {
        return setup;
    }

    void render() {
        flashcardList.getItems().clear();
        for (int i = 0; i < flashcards.size(); i++) {
            Pane flashcard = FlashcardPreview.getPreview(flashcards.get(i), i, this);
            flashcardList.getItems().add(flashcard);
        }
    }
}
