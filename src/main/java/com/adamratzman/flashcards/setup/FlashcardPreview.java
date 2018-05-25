package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
/*
 * When the preview object is called it creatas a new preview object specifically for a flashcard using the preview class.
 * <br>
 * It sets the question,hint, and deletes a question and canadd answers to a getAnswer list.
 * <br>
 * When the user clicks the deleteQuestion button the question will be deleted.
 */
class FlashcardPreview {
    static Pane getPreview(Flashcard flashcard, int index, Preview preview) {
        try {
            Pane root = FXMLLoader.load(preview.getClass().getResource("/flashcard-display-preview.fxml"));
            Label questionNumber = (Label) root.lookup("#question-number");//question the flashcard is on
            TextArea questionText = (TextArea) root.lookup("#question-text");//question  number the flashcard is on
            TextField hintText = (TextField) root.lookup("#hint-text");//hint the flashcard is on
            ComboBox<String> answers = (ComboBox<String>) root.lookup("#answers");//answer the flashcard is on
            Hyperlink deleteQuestion = (Hyperlink) root.lookup("#delete");//pushed to delete a card
            //sets question number out of all
            questionNumber.setText("Question " + (index + 1));
            questionNumber.setId("#question-" + index + "-number");
            //set to display the question the flashcard in on
            questionText.setText(flashcard.getQuestion());
            questionText.setId("#question-" + index + "-text");
            //show the text to the card that is being previewed if there is one
            hintText.setText(flashcard.getHint() == null ? "There is no hint available" : flashcard.getHint());
            hintText.setId("#hint-" + index + "-text");
            //shows the answer to the card that is being previewed
            answers.setId("#answers-" + index);
            answers.getItems().addAll(flashcard.getAnswers());
            //used to delete a flashcard from the list
            deleteQuestion.setId("#delete-" + index);
            deleteQuestion.setOnMouseClicked(event -> {
                preview.getFlashcards().removeIf(comp -> comp.getQuestion().equals(questionText.getText()));
                preview.getFlashcardsLabel().setText("Total Flashcards (" + preview.getFlashcards().size() + ")");
                preview.getSetup().getCreatedFlashcards().setText("Preview, Remove, and Export Flashcards: (" + preview.getFlashcards().size() + ")");
                preview.render();
            });
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
