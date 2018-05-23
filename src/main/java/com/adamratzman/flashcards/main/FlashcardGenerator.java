package com.adamratzman.flashcards.main;

import com.adamratzman.flashcards.setup.Setup;
import javafx.application.Application;
import javafx.stage.Stage;

public class FlashcardGenerator extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Setup(primaryStage);
    }

}
