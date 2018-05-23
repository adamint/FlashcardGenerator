package com.adamratzman.flashcards.game;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
/**
 * The game is launched when user pushes start. A new stage and scene is created for the game.
 * <br>
 * It starts with question 1 and a score of 0.
 * <br>
 * It sets the stage to having the first question and its possible hint and a text box for the answer.
 * <br>
 * When check-answer button is clicked it compares the answer to the flashcard answer and increases score and advance to next question
 * or end game.
 */
public class Game {
    private ArrayList<Flashcard> flashcards;
    private Stage stage;
    private Scene scene;
    public Game(ArrayList<Flashcard> flashcards) {
        this.flashcards = flashcards;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/game-gui.fxml"));
            stage = new Stage();
            scene = new Scene(root);
            //when game launches:
            //set score to 0
            //score is one less than question number
            final int[] score = {0};
            final int[] question = {1};
            //set score tex to zero
            ((Label) scene.lookup("#flashcard-score")).setText("Score: " + score[0]);
            //set question number text to one
            ((Label) scene.lookup("#flashcard-number")).setText("Question " + question[0] + " of " + flashcards.size());
            //displays the first question
            ((TextArea) scene.lookup("#question-holder")).setText(flashcards.get(0).getQuestion());
            //displays the first hint if there is one or it stays blank
            ((TextField) scene.lookup("#hint-holder")).setText(flashcards.get(0).getHint());



            //check answer to raise points and advance to next question or end game if at last question
            scene.lookup("#check-answer").setOnMouseClicked(event -> {
                Flashcard current = flashcards.get(question[0] - 1);
                String userAnswer = ((TextField) scene.lookup("#answer-box")).getText();
                for (String answer : current.getAnswers()) {
                    if (answer.equalsIgnoreCase(userAnswer)) score[0]++;
                }
                //if at the last question end the game and show the results from the game
                if (question[0] == flashcards.size()) {
                    new Results(score[0], flashcards.size(), flashcards);
                    stage.close();
                    //otherwise move to the next question
                } else {
                    question[0]++;
                    ((Label) scene.lookup("#flashcard-number")).setText("Question " + question[0] + " of " + flashcards.size());
                    ((TextArea) scene.lookup("#question-holder")).setText(flashcards.get(question[0] - 1).getQuestion());
                    ((TextField) scene.lookup("#hint-holder")).setText(flashcards.get(question[0] - 1).getHint());
                    ((Label) scene.lookup("#flashcard-score")).setText("Score: " + score[0]);
                    ((TextField) scene.lookup("#answer-box")).clear();
                }
            });

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

}
