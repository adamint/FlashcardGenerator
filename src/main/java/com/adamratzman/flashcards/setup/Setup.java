package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.game.Game;
import com.adamratzman.flashcards.models.Flashcard;
import com.adamratzman.flashcards.utils.Utils;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Facilitates the opening and management of game setup, including adding and exporting new flashcards.
 */
public class Setup {
    private Scene scene;
    // The id of the action happening. 0 is nothing, 1 is creating via program, and 2 is importing via external file.
    // Important in order to avoid regenerating the same templates
    private int currentCreation = 0;
    private Hyperlink createdFlashcards;
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    // Allows for serialization of the ExportedDeck.
    private Gson gson = new Gson();

    public Setup(Stage stage) throws IOException {
        // Load the fxml file using the classloader.
        Parent root = FXMLLoader.load(getClass().getResource("/setup.fxml"));

        scene = new Scene(root);
        // Assign values to the instance fields to be used later
        createdFlashcards = (Hyperlink) scene.lookup("#created-flashcards");
        Pane creationPane = (Pane) scene.lookup("#creation-pane");

        // Handle when the create-via-program button is clicked.
        scene.lookup("#create-via-program").setOnMouseClicked((MouseEvent onClickEvent) -> {
            if (currentCreation != 1) {
                // If the action is not already create via program, then set it.
                currentCreation = 1;
                // Remove everything inside the display pane to make way for the template.
                creationPane.getChildren().clear();
                creationPane.getChildren().addAll(getFlashcardPane().getChildrenUnmodifiable());

                TextArea questionText = (TextArea) scene.lookup("#question-text");
                TextField hintText = (TextField) scene.lookup("#hint-text");
                VBox answersVb = (VBox) scene.lookup("#answers-vb");
                TextField answerText = (TextField) scene.lookup("#answer-text");

                // New answers are added when the enter key is pressed thus we need to listen for that
                answerText.setOnKeyPressed(event -> {
                    // if the key pressed was enter
                    if (event.getCode() == KeyCode.ENTER) {
                        // if there's real text
                        if (!answerText.getText().isEmpty()) {
                            // if it's an illegal answer (one that's used by the program or shown when no answers present)
                            if (answerText.getText().contentEquals("None!") || answerText.getText().contentEquals("Answers:"))
                                Utils.showAlert(Alert.AlertType.WARNING, "You tried to add an illegal answer");
                            else {
                                /*
                                 * A little complicated. If no answers have been added, remove the placeholder saying None!.
                                 * If an equivalent answer is already added, remove the answer. Thus to remove answers you type
                                 *them again. If it's not already entered, clear answer box and add the answer to the vbox.
                                 */
                                Label nonePlaceholder = (Label) scene.lookup("#no-answers");
                                if (nonePlaceholder != null) answersVb.getChildren().remove(nonePlaceholder);
                                boolean removeIfEnteredAgain = answersVb.getChildren()
                                        .removeIf(node -> ((Label) node).getText().equalsIgnoreCase(answerText.getText()));
                                if (!removeIfEnteredAgain) {
                                    answersVb.getChildren().add(new Label(answerText.getText()));
                                    answerText.clear();
                                }
                            }
                        }
                    }
                });

                /*
                 * When the clear-fields button is pressed, clears all fields and adds back the default answer
                 * display
                 */
                scene.lookup("#clear-fields").setOnMouseClicked(event -> {
                    questionText.clear();
                    hintText.clear();
                    answerText.clear();
                    answersVb.getChildren().removeIf(value -> !((Label) value).getText().equals("None!")
                            && !((Label) value).getText().equals("Answers:"));
                    answersVb.getChildren().add(new Label("None!"));
                });

                // when the add flashcard button is pressed, we have to check conditions before adding flashcard
                scene.lookup("#add-flashcard").setOnMouseClicked(event -> {
                    // if question length less than 10 likely not a question
                    if (questionText.getText().length() < 10) {
                        Utils.showAlert(Alert.AlertType.ERROR, "Questions must be at least 10 characters");
                    } else {
                        String question = questionText.getText();
                        String hint = hintText.getText();
                        String enteredAnswer = answerText.getText();
                        List<String> savedAnswers = Utils.getAnswers(answersVb);
                        if (enteredAnswer != null) savedAnswers.add(enteredAnswer);

                        // make sure there's an answer
                        if (savedAnswers.size() == 0)
                            Utils.showAlert(Alert.AlertType.ERROR, "You need at least one correct answer!");
                        else {
                            flashcards.add(new Flashcard(question, hint, savedAnswers));
                            createdFlashcards.setText("Preview, Remove, and Export Flashcards: (" + flashcards.size() + ")");
                            // clear all fields for new flashcard to be added, and reset the answer box
                            questionText.clear();
                            hintText.clear();
                            answerText.clear();
                            answersVb.getChildren().removeIf(value -> !((Label) value).getText().equals("None!")
                                    && !((Label) value).getText().equals("Answers:"));
                            if (answersVb.getChildren().size() == 1) answersVb.getChildren().add(new Label("None!"));
                        }
                    }
                });
            }
        });

        // If the preview Hyperlink is pressed, check if there are any to preview, and if so, create a new preview object
        createdFlashcards.setOnMouseClicked(event -> {
            if (flashcards.isEmpty()) Utils.showAlert(Alert.AlertType.INFORMATION,
                    "There are no created flashcards. Preview and export are only available one " +
                            "you have added at least 1 flashcard");
            else {
                new Preview(this, flashcards);
            }
        });

        // When the create-via-import hyperlink is pressed, handle event
        scene.lookup("#create-via-import").setOnMouseClicked(event -> {
            if (currentCreation != 2) {
                currentCreation = 2;
                // clear pane for template, load template
                creationPane.getChildren().clear();
                creationPane.getChildren().addAll(getImportPane().getChildrenUnmodifiable());

                // Listen for when the import hyperlink is pressed. When it is, let the user select a fdeck file!
                scene.lookup("#import-hyperlink").setOnMouseClicked(e -> {
                    // allow choosing file
                    FileChooser deckChooser = new FileChooser();
                    // extension of fdeck is required.
                    deckChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Flashcard Deck (*.fdeck)", "*.fdeck"));
                    File selected = deckChooser.showOpenDialog(stage);
                    if (selected != null) {
                        try {
                            // read the file, serialize the text to ExportedDeck object, and add the flashcards to the list
                            ExportedDeck deck = gson.fromJson(new String(Files.readAllBytes(selected.toPath())), ExportedDeck.class);
                            if (deck != null && deck.getFlashcards() != null && deck.getFlashcards().size() != 0) {
                                flashcards.addAll(deck.getFlashcards());
                                Utils.showAlert(Alert.AlertType.INFORMATION, "Successfully imported " + deck.getFlashcards().size()
                                        + " flashcards!");
                                // new flashcard size, so re-set text
                                createdFlashcards.setText("Preview, Remove, and Export Flashcards: (" + flashcards.size() + ")");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });

        // Start game AND close window if more than one flashcard is present
        scene.lookup("#start-game").setOnMouseClicked(event -> {
            if (flashcards.size() == 0)
                Utils.showAlert(Alert.AlertType.ERROR, "You need at least one flashcard to start the game!");
            else {
                new Game(flashcards);
                stage.close();
            }
        });


        stage.setScene(scene);
        stage.show();
    }

    Gson getGson() {
        return gson;
    }

    Hyperlink getCreatedFlashcards() {
        return createdFlashcards;
    }

    /**
     * Load the flashcard creation template. Must be loaded each time button is clicked.
     */
    private Parent getFlashcardPane() {
        try {
            return FXMLLoader.load(getClass().getResource("/add-flashcard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load the flashcard creation template. Must be loaded each time button is clicked.
     */
    private Parent getImportPane() {
        try {
            return FXMLLoader.load(getClass().getResource("/import.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
