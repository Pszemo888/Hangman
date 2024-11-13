package model;

import java.util.*;
import java.time.Duration;
import java.time.Instant;

public class HangmanModel {
    private List<String> words;
    public String wordToGuess;
    private Set<Character> guessedLetters = new HashSet<>();
    private SortedSet<Character> wrongGuesses = new TreeSet<>();
    private int attemptsLeft;
    private int maxAttempts = 6;
    private int wins = 0;
    private int losses = 0;
    private int score = 100;
    private Instant startTime;
    private int defaultgameDuration = 60;
    private int maxHints = 3; // Maksymalna liczba podpowiedzi
    private int hintCount = 0;


    public HangmanModel(List<String> words) {
        this.words = words;
        resetGame();
    }

    private void setWordToGuess() {
        if (!words.isEmpty()) {
            Random random = new Random();
            wordToGuess = words.get(random.nextInt(words.size()));
            System.out.println("słowo to: " + wordToGuess); // Debugowanie
        } else {
            wordToGuess = "programowanie";
            System.out.println("Użyto domyślnego słowa: " + wordToGuess);
        }
    }


    public void resetGame() {
        guessedLetters.clear();
        attemptsLeft = maxAttempts;
        setWordToGuess();
        wrongGuesses.clear();
        startTime = Instant.now();
        hintCount = 0;
    }

    public boolean guessLetter(char letter) {
        letter = Character.toUpperCase(letter);

        // Sprawdzenie, czy wprowadzony znak jest literą
        if (!Character.isLetter(letter)) {
            return false; // Ignorujemy nieprawidłowy znak
        }

        // Dodajemy literę do zgadywanych liter
        guessedLetters.add(letter);

        if (wordToGuess.indexOf(letter) == -1) {
            attemptsLeft--;
            wrongGuesses.add(letter);
            return false; // Zwraca false, jeśli litera jest błędna
        } else {
            score += 2;
            return true; // Zwraca true, jeśli litera jest poprawna
        }
    }

    public boolean isWordGuessed() {
        for (char letter : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(letter)) { //Sprawdzam czy litera nie została odgadnieta
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || isWordGuessed();
    }

    // Definicja metody getWordDisplay() - wyświetla stan zgadywanego słowa
    public String getWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char letter : wordToGuess.toCharArray()) {
            display.append(guessedLetters.contains(letter) ? letter + " " : "_ ");
        }
        return display.toString();
    }
    public int getRemainingTime() {
        int elapsedSeconds = (int) Duration.between(startTime, Instant.now()).getSeconds();
        return Math.max(defaultgameDuration - elapsedSeconds, 0);
    }

    public boolean isTimeUp() {
        return getRemainingTime() <= 0;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void updateStats(boolean won) {
        if (won) {
            wins++;
            score += 50;
        } else {
            losses++;
            score -= 25;
        }
    }
    public boolean processGuess(char letter) {
        if (guessLetter(letter)) { // Jeśli litera jest poprawna
            if (isWordGuessed()) {
                updateStats(true); // Gracz wygrywa
                return true; // Gra zakończona, wygrana
            }
            return false; // Gra trwa dalej
        } else {
            if (isGameOver()) {
                updateStats(false); // Gracz przegrywa
                return true; // Gra zakończona, przegrana
            }
            return false; // Gra trwa dalej
        }
    }

    public void setDifficulty(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        resetGame();
    }
    public void setGameDuration(int defaultgameDuration) {
        this.defaultgameDuration = defaultgameDuration;
        resetGame();
    }
    public String getWordToGuess() {
        return wordToGuess;
    }


    public Set<Character> getWrongGuesses() {
        return wrongGuesses;
    }

    public void updateWords(List<String> newWords) {
        if (!newWords.isEmpty()) {
            this.words = newWords;
            resetGame(); // Opcjonalnie resetuj grę po aktualizacji słów
        }
    }
    public Character useHint() {
        if (hintCount >= maxHints || attemptsLeft <= 1) {
            return null; // Brak dostępnych podpowiedzi lub prób
        }

        List<Character> unguessedLetters = new ArrayList<>();
        for (char letter : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(letter)) {
                unguessedLetters.add(letter);
            }
        }

        if (!unguessedLetters.isEmpty()) {
            Character hintLetter = unguessedLetters.get(new Random().nextInt(unguessedLetters.size()));
            guessedLetters.add(hintLetter);
            hintCount++; // Zwiększamy licznik podpowiedzi
            attemptsLeft--;
            score -= 10;
            return hintLetter;
        }
        return null;
    }
    public int getHintCount() {
        return hintCount;
    }
    public int getMaxHints() {
        return maxHints;
    }
}
