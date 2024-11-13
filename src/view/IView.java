package view;

import java.util.Set;

public interface IView {
    void displayGameState(String wordDisplay, int attemptsLeft, int score, Set<Character> wrongGuesses, int remainingTime, int hintCount, int maxHints);
    char getInput();
    void displayMessage(String message);
}
