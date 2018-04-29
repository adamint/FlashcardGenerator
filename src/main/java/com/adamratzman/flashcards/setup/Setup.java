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
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Setup {
    private Scene scene;
    private int currentCreation = 0;
    private Hyperlink createdFlashcards;
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private Gson gson = new Gson();

    public Setup(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/setup.fxml"));

        scene = new Scene(root);
        createdFlashcards = (Hyperlink) scene.lookup("#created-flashcards");
        Pane creationPane = (Pane) scene.lookup("#creation-pane");

        scene.lookup("#create-via-program").setOnMouseClicked((MouseEvent onClickEvent) -> {
            if (currentCreation != 1) {
                currentCreation = 1;
                creationPane.getChildren().clear();
                creationPane.getChildren().addAll(getFlashcardPane().getChildrenUnmodifiable());

                TextArea questionText = (TextArea) scene.lookup("#question-text");
                TextField hintText = (TextField) scene.lookup("#hint-text");
                VBox answersVb = (VBox) scene.lookup("#answers-vb");
                TextField answerText = (TextField) scene.lookup("#answer-text");

                answerText.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (!answerText.getText().isEmpty()) {
                            if (answerText.getText().contentEquals("None!") || answerText.getText().contentEquals("Answers:"))
                                Utils.showAlert(Alert.AlertType.WARNING, "You tried to add an illegal answer");
                            else {
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

                scene.lookup("#clear-fields").setOnMouseClicked(event -> {
                    questionText.clear();
                    hintText.clear();
                    answerText.clear();
                    answersVb.getChildren().removeIf(value -> !((Label) value).getText().equals("None!")
                            && !((Label) value).getText().equals("Answers:"));
                    answersVb.getChildren().add(new Label("None!"));
                });

                scene.lookup("#add-flashcard").setOnMouseClicked(event -> {
                    if (questionText.getText().length() < 10) {
                        Utils.showAlert(Alert.AlertType.ERROR, "Questions must be at least 10 characters");
                    } else {
                        String question = questionText.getText();
                        String hint = hintText.getText();
                        String enteredAnswer = answerText.getText();
                        List<String> savedAnswers = Utils.getAnswers(answersVb);
                        if (enteredAnswer != null) savedAnswers.add(enteredAnswer);
                        if (savedAnswers.size() == 0)
                            Utils.showAlert(Alert.AlertType.ERROR, "You need at least one correct answer!");
                        else {
                            flashcards.add(new Flashcard(question, hint, savedAnswers));
                            createdFlashcards.setText("Preview, Remove, and Export Flashcards: (" + flashcards.size() + ")");
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

        createdFlashcards.setOnMouseClicked(event -> {
            if (flashcards.isEmpty()) Utils.showAlert(Alert.AlertType.INFORMATION,
                    "There are no created flashcards. Preview and export are only available one " +
                            "you have added at least 1 flashcard");
            else {
                new Preview(this, flashcards);
            }
        });

        scene.lookup("#create-via-import").setOnMouseClicked(event -> {
            if (currentCreation != 2) {
                currentCreation = 2;
                creationPane.getChildren().clear();
                creationPane.getChildren().addAll(getImportPane().getChildrenUnmodifiable());

                scene.lookup("#import-hyperlink").setOnMouseClicked(e -> {
                    FileChooser deckChooser = new FileChooser();
                    deckChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Flashcard Deck (*.fdeck)", "*.fdeck"));
                    File selected = deckChooser.showOpenDialog(stage);
                    if (selected != null) {
                        try {
                            ExportedDeck deck = gson.fromJson(new String(Files.readAllBytes(selected.toPath())), ExportedDeck.class);
                            if (deck != null && deck.getFlashcards() != null && deck.getFlashcards().size() != 0) {
                                flashcards.addAll(deck.getFlashcards());
                                Utils.showAlert(Alert.AlertType.INFORMATION, "Successfully imported " + deck.getFlashcards().size()
                                        + " flashcards!");
                                createdFlashcards.setText("Preview, Remove, and Export Flashcards: (" + flashcards.size() + ")");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });

        scene.lookup("#start-game").setOnMouseClicked(event -> {
            if (flashcards.size() == 0) Utils.showAlert(Alert.AlertType.ERROR, "You need at least one flashcard to start the game!");
            else {
                new Game(flashcards);
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

    private Parent getFlashcardPane() {
        try {
            return FXMLLoader.load(getClass().getResource("/add-flashcard.fxml")) ;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Parent getImportPane() {
        try {
            return FXMLLoader.load(getClass().getResource("/import.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
