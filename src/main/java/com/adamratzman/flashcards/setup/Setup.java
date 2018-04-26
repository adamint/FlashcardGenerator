package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;
import com.adamratzman.flashcards.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Setup {
    private Scene scene;
    private int currentCreation = 0;

    private ArrayList<Flashcard> flashcards = new ArrayList<>();

    public Setup(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/setup.fxml"));
        Parent addFlashcardsPane = FXMLLoader.load(getClass().getResource("/add-flashcard.fxml"));

        scene = new Scene(root);

        Pane creationPane = (Pane) scene.lookup("#creation-pane");

        scene.lookup("#create-via-program").setOnMouseClicked(onClickEvent -> {
            if (currentCreation != 1) {
                currentCreation = 1;
                creationPane.getChildren().clear();
                creationPane.getChildren().addAll(addFlashcardsPane.getChildrenUnmodifiable());

                TextArea questionText = (TextArea) scene.lookup("#question-text");
                TextField hintText = (TextField) scene.lookup("#hint-test");
                VBox answersVb = (VBox) scene.lookup("#answers-vb");
                TextField answerText = (TextField) scene.lookup("#answer-text");

                answerText.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (!answerText.getText().isEmpty()) {
                            Label nonePlaceholder = (Label) scene.lookup("#no-answers");
                            if (nonePlaceholder != null) answersVb.getChildren().remove(nonePlaceholder);
                            boolean removeIfEnteredAgain = answersVb.getChildren()
                                    .removeIf(node -> ((Label) node).getText().equalsIgnoreCase(answerText.getText()));
                            if (!removeIfEnteredAgain) answersVb.getChildren().add(new Label(answerText.getText()));
                        }
                    }
                });

                scene.lookup("#clear-fields").setOnMouseClicked(event -> {
                    // Implement clearing of all fields. All inputs are provided above. Don't forget to remove all
                    // answers in the answersVb and add a label with id #no-answers that says None!
                });

                scene.lookup("#add-flashcard").setOnMouseClicked(event -> {
                    // Implement parsing fields to check if the user can add the inputs as a flashcard.
                    // Cannot IF: question text < 10 characters, or there is no added answer in the answer input
                    // and there are no saved answers in the answersVb

                    // then add it to the flashcards arraylist and update the created flashcards hyperlink
                });
            }
        });

        scene.lookup("#created-flashcards").setOnMouseClicked(event -> {
            if (flashcards.isEmpty()) Utils.showAlert(Alert.AlertType.INFORMATION,
                    "There are no created flashcards. Preview and export are only available one " +
                            "you have added at least 1 flashcard");
            else {
                /* TODO launch preview stage where you can export flashcards as a text file (.flashcards).
                 We will all importing that later  */
            }
        });

        scene.lookup("#create-via-import").setOnMouseClicked(event -> {
            // TODO allow import of properly-formatted flashcard text files
        });


        stage.setScene(scene);
        stage.show();
    }
}
