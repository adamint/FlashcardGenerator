package com.adamratzman.flashcards.models;

public class Flashcard {
    private String question;
    private String hint;
    private String[] answers;
    private FlashcardType type;

    public Flashcard(String question, String hint, String[] answers, FlashcardType type) {
        this.question = question;
        this.hint = hint;
        this.answers = answers;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public FlashcardType getType() {
        return type;
    }

    public String getHint() {
        return hint;
    }

    private enum FlashcardType {
        MULTIPLE_CHOICE, ENTER_ANSWER
    }
}
