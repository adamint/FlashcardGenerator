package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

class FlashcardPreview {
    /**
     * Represents the preview pane of the current flashcard set, where user can delete or export flashcards.
     * Given a preview, returns pane for a singular flashcard
     *
     * @param flashcard the flashcard to get preview for
     * @param index the index in the flashcard list of this flashcard
     * @param preview the Preview object to display in
     * @return the preview pane for this singular flashcard
     */
    static Pane getPreview(Flashcard flashcard, int index, Preview preview) {
        try {
            Pane root = FXMLLoader.load(preview.getClass().getResource("/flashcard-display-preview.fxml"));
            Label questionNumber = (Label) root.lookup("#question-number");
            TextArea questionText = (TextArea) root.lookup("#question-text");
            TextField hintText = (TextField) root.lookup("#hint-text");
            ComboBox<String> answers = (ComboBox<String>) root.lookup("#answers");
            Hyperlink deleteQuestion = (Hyperlink) root.lookup("#delete");

            // Ids must be unique so they are set to the question index. This allows for simple retrieval as well

            questionNumber.setText("Question " + (index + 1));
            questionNumber.setId("#question-" + index + "-number");

            questionText.setText(flashcard.getQuestion());
            questionText.setId("#question-" + index + "-text");

            hintText.setText(flashcard.getHint() == null ? "There is no hint available" : flashcard.getHint());
            hintText.setId("#hint-" + index + "-text");

            answers.setId("#answers-" + index);
            answers.getItems().addAll(flashcard.getAnswers());

            deleteQuestion.setId("#delete-" + index);

            // Update text boxes where the flashcard size is included AND re-render the preview scene to reflect the deletion
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
