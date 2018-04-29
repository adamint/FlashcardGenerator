package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

class FlashcardPreview {
    static Pane getPreview(Flashcard flashcard, int index, Preview preview) {
        try {
            Pane root = FXMLLoader.load(preview.getClass().getResource("/flashcard-display-preview.fxml"));
            Label questionNumber = (Label) root.lookup("#question-number");
            TextArea questionText = (TextArea) root.lookup("#question-text");
            TextField hintText = (TextField) root.lookup("#hint-text");
            ComboBox<String> answers = (ComboBox<String>) root.lookup("#answers");
            Hyperlink deleteQuestion = (Hyperlink) root.lookup("#delete");

            questionNumber.setText("Question " + (index + 1));
            questionNumber.setId("#question-" + index + "-number");

            questionText.setText(flashcard.getQuestion());
            questionText.setId("#question-" + index + "-text");

            hintText.setText(flashcard.getHint() == null ? "There is no hint available" : flashcard.getHint());
            hintText.setId("#hint-" + index + "-text");

            answers.setId("#answers-" + index);
            answers.getItems().addAll(flashcard.getAnswers());

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
