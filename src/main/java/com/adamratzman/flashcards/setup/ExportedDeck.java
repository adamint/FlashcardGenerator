package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;

import java.util.List;

/**
 * {@code exportTime} calculates the amount of time it takes to export flashcards.
 * <br>
 * {@code flashcards} is an ArrayList that holds a set of flashcards to be exported.
 */
public class ExportedDeck {
    private long exportTime;
    private List<Flashcard> flashcards;

    public ExportedDeck(long exportTime, List<Flashcard> flashcards) {
        this.exportTime = exportTime;
        this.flashcards = flashcards;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public long getExportTime() {
        return exportTime;
    }
}
