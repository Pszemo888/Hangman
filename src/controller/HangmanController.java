package controller;

import model.HangmanModel;
import view.TerminalView;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HangmanController {
    private final HangmanModel model;
    private final TerminalView view;
    private final FileController fileController;
    private final MenuController menuController; // Dodajemy MenuController

    private Timer displayTimer;

    public HangmanController(HangmanModel model, TerminalView view) {
        this.model = model;
        this.view = view;
        this.fileController = new FileController(model, view, "words.txt");
        this.menuController = new MenuController(model, view, fileController); // Inicjalizacja MenuController
    }

    public void startGame() {
        try {
            boolean exit = false;

            while (!exit) {
                int selectedOption = menuController.displayMainMenu();

                switch (selectedOption) {
                    case 0 -> startNewGame();
                    case 1 -> menuController.displaySettingsMenu();
                    case 2 -> displayStats();
                    case 3 -> menuController.displayAchievements();
                    case 4 -> view.displayRules(view.getScreen());
                    case 5 -> exit = true;
                }
            }

            view.closeScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startNewGame() throws IOException {
        model.resetGame();
        startDisplayTimer();

        while (!model.isGameOver() && !model.isTimeUp()) {

            char guessedLetter = view.getInput();

            if (guessedLetter != '\0') {
                // Sprawdzenie, czy gracz nacisnął spację, aby użyć podpowiedzi
                if (guessedLetter == ' ') {
                    Character hintLetter = model.useHint();
                    if (hintLetter != null) {
                        //view.displayHintMessage("Odkryta litera: " + hintLetter + " (pozostałe próby: " + model.getAttemptsLeft() + ")");
                    } else {
                        view.displayMessage("No available hints or no attempts left.");
                    }
                    continue; // Pomijamy dalsze sprawdzanie w tej iteracji
                }

                // Kontynuuj normalne przetwarzanie zgadywania litery
                boolean gameEnded = model.processGuess(guessedLetter);
                if (gameEnded) { //po upłynieciu czasu konczy gre, ale nie
                    if (model.isWordGuessed()) {
                        view.displayMessage("You won! The word was: " + model.getWordToGuess());
                    } else {
                        view.displayMessage("You lost! The word was: " + model.getWordToGuess());
                    }
                    break;
                }
            }

        }

        stopDisplayTimer();
    }


    private void startDisplayTimer() {
        displayTimer = new Timer();
        displayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                view.displayGameState(
                        model.getWordDisplay(),
                        model.getAttemptsLeft(),
                        model.getScore(),
                        model.getWrongGuesses(),
                        model.getRemainingTime(),
                        model.getHintCount(),   // Dodajemy liczbę użytych podpowiedzi
                        model.getMaxHints()
                );
            }
        }, 0, 1000);
    }

    private void stopDisplayTimer() {
        if (displayTimer != null) {
            displayTimer.cancel();
            displayTimer = null;
        }
    }

    private void displayStats() throws IOException {
        int wins = model.getWins();
        int losses = model.getLosses();
        int score = model.getScore();

        view.displayStats(view.getScreen(), wins, losses, score);
    }
}
