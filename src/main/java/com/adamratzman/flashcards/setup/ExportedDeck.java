package com.adamratzman.flashcards.setup;

import com.adamratzman.flashcards.models.Flashcard;

import java.util.List;

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
