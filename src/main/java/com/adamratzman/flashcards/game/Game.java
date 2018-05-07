package com.adamratzman.flashcards.game;

import com.adamratzman.flashcards.models.Flashcard;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;

public class Game {
    private ArrayList<Flashcard> flashcards;
    public Game(ArrayList<Flashcard> flashcards) {
        this.flashcards = flashcards;
        
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
            for(int i=0;i<flashcards.size();i++){
                Flashcard j=flashcards.get(i);
                j=s.get(i);
            }
        //}
        return s;
    }


}
