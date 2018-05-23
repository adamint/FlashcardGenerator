package com.adamratzman.flashcards.game;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates a new stage and scene after the game has ended.
 * <br>
 * The results stage gives the game statistics at the end of the game.
 * <br>
 * Has a button play-again that allows user to restart the game.
 */
public class Results {
    public Results(int correct, int total, ArrayList<Flashcard> flashcards) {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/results.fxml")));

            ((Label) scene.lookup("#correct-label")).setText("You got " + correct + " of "
                    + total + " correct! (" + (correct * 100) / ((float) total) + "%)");

            ((Label) scene.lookup("#confidence-interval")).setText("I am 95% confident that " +
                    "your true knowledge on this flashcard set is between " + (correct - getME(correct, total))
                    + " and " + (correct + getME(correct, total)) + " questions");

            scene.lookup("#play-again").setOnMouseClicked(event -> new Game(flashcards));

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the margin of error.
     * <br>
     * @param correct comes from contructor and keeps track of how many correct answers there are.
     * @param total comes from the amount of flashcards in the ArrayList.
     * @return margin of error.
     */
    private float getME(int correct, int total) {
        float mean = correct / ((float) total);
        float deviations = 0f;
        deviations += correct * Math.pow(1.0 - mean, 2.0);
        deviations += (total - correct) * Math.pow(0.0 - mean, 2.0);

        float stDev = (float) Math.sqrt(deviations / ((double) total - 1));
        return stDev * 1.96f;
    }
}
