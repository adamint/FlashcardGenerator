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
            //score is one less than question number
            final int[] score = {0};
            final int[] question = {1};
            //when game launches:
            //set score to 0
            ((Label) scene.lookup("#flashcard-score")).setText("Score: " + score[0]);
            //set question number to one
            ((Label) scene.lookup("#flashcard-number")).setText("Question " + question[0] + " of " + flashcards.size());

            //displays the first question
            ((TextArea) scene.lookup("#question-holder")).setText(flashcards.get(0).getQuestion()); //0 has to increase
            //at some point
            //displays the hint if there is one
            ((TextField) scene.lookup("#hint-holder")).setText(flashcards.get(0).getHint());



            //check answer to raise points
            scene.lookup("#check-answer").setOnMouseClicked(event -> {
                Flashcard current = flashcards.get(question[0] - 1);
                String userAnswer = ((TextField) scene.lookup("#answer-box")).getText();
                for (String answer : current.getAnswers()) {
                    if (answer.equalsIgnoreCase(userAnswer)) score[0]++;
                }
                if (question[0] == flashcards.size()) {
                    new Results(score[0], flashcards.size(), flashcards);
                    stage.close();
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
        // TODO implement :)
    }


    //public int updatescore(){
    //if(iscorrect==true){
    //    score++;
    // }
    //return score;
    //}
    //getter for the array list
    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    //when save for later button is clicked move the flashcards to another list
    public ArrayList<Flashcard> saved() {
        ArrayList<Flashcard> s = new ArrayList<>();
        //if(scene.lookup("#save-for-later").setOnMouseClicked()){
        for (int i = 0; i < flashcards.size(); i++) {
            Flashcard j = flashcards.get(i);
            j = s.get(i);
        }
        //}
        return s;
    }


}
