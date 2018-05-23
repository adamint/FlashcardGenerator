package com.adamratzman.flashcards.models;

import java.util.List;

/**
 * Flashcard is defined by user during game setup and is used during the game and is shown in the game gui.
 * <br>
 * {@code hint} is a read only field that could be null.
 */
public class Flashcard {
    private String question;
    private String hint;
    private List<String> answers;

    public Flashcard(String question, String hint, List<String> answers) {
        this.question = question;
        this.hint = hint;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getHint() {
        return hint;
    }
}
