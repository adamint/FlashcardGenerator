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

/**
 * This class gives the user a preview of their flashcard object they created.
 * <br>
 * It creates a new scene that contains a label (total-flashcards) with the total number of flashcards in the arrayList.
 * <br>
 * If the export button is clicked it will want a {@code file} for exporting.
 * <br>
 * This is used after the user has already created a set of flashcards and does not allow an empty list.
 */
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

            render();//render the initial flashcards before any changes are made
            //when the export hyperlink is pressed allow user to select where to export the deck
            scene.lookup("#export").setOnMouseClicked(event -> {
                //check for flashcards to export if 0 export is not completed
                if (flashcards.size() == 0)
                    Utils.showAlert(Alert.AlertType.ERROR, "You need at least one flashcard to export");
                else {
                    //let users select file or create their own
                    FileChooser exportFileChooser = new FileChooser();
                    exportFileChooser.getExtensionFilters()
                            .add(new FileChooser.ExtensionFilter("Flashcard Deck File (fdeck)", "*.fdeck"));
                    //get the selected file
                    File chosen = exportFileChooser.showSaveDialog(stage);
                    //if selected a file, export
                    if (chosen != null) export(chosen);
                }
            });
            //no individual flashcard should be selected
            scene.lookup("#preview").requestFocus();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method exports the deck to the specified file.
     * <br>
     * @param file is the file to be exported to
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void export(File file) {
        try {
            //file already exists delete it
            if (file.exists()) file.delete();
            //create new file
            file.createNewFile();
            //converts the Exporteddeck to a text using method in setup.java
            OutputStreamWriter outputWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputWriter.write(setup.getGson().toJson(new ExportedDeck(System.currentTimeMillis(), flashcards)));
            outputWriter.close();
            //shows if the deck was exported successfully
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

    /**
     * This method renders the {@code flashcards} after a change is made by the user.
     */
    void render() {
        flashcardList.getItems().clear();
        for (int i = 0; i < flashcards.size(); i++) {
            Pane flashcard = FlashcardPreview.getPreview(flashcards.get(i), i, this);
            flashcardList.getItems().add(flashcard);
        }
    }
}
