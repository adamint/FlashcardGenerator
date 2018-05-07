package com.adamratzman.flashcards.models;

public class GameStatistic {
    private int questionsTotal;
    private int questionsCorrect;

    public GameStatistic() {
        questionsTotal = 0;
        questionsCorrect = 0;
    }

    public void addQuestion() {
        questionsTotal++;
    }

    public void addQuestionCorrect() {
        questionsCorrect++;
    }

    public int getQuestionsCorrect() {
        return questionsCorrect;
    }

    public int getQuestionsTotal() {
        return questionsTotal;
    }
}
