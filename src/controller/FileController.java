package controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import model.HangmanModel;
import utils.FileLoader;
import view.TerminalView;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileController {
    private final HangmanModel model;
    private final TerminalView view;
    private final FileLoader fileLoader;

    public FileController(HangmanModel model, TerminalView view, String defaultFileName) {
        this.model = model;
        this.view = view;
        this.fileLoader = new FileLoader(defaultFileName);
    }

    // Wczytanie słów z pliku podanego przez użytkownika
    public void loadWordsFromFile() throws IOException {
        String fileName = promptForTextInput("Enter the file name (e.g., words.txt): ");
        if (fileName == null) {
            view.displayMessage("Operation canceled.");
            return; // Zakończenie operacji, gdy użytkownik naciśnie ESC
        }

        try {
            List<String> words = fileLoader.loadWordsFromFile(fileName);
            model.updateWords(words); // Aktualizacja słów w modelu
            view.displayMessage("Loaded " + words.size() + " words from " + fileName);
        } catch (IOException e) {
            view.displayMessage("Error loading file. Please check the file name and try again.");
        }
    }

    // Wybór tematu na podstawie pliku
    public void setSubject(String subjectFileName) {
        try {
            List<String> subjectWords = fileLoader.loadWordsFromFile(subjectFileName);
            model.updateWords(subjectWords); // Aktualizacja słów w modelu
           // view.displayMessage("Loaded subject words from " + subjectFileName + ": " + subjectWords.size() + " words.");
        } catch (IOException e) {
            view.displayMessage("Error loading subject. Please check the file and try again.");
        }
    }

    // Dodanie niestandardowych słów do pliku
    public void addCustomWords() throws IOException {
        String input = promptForTextInput("Enter words separated by commas: ");
        if (input == null) {
            view.displayMessage("Operation canceled.");
            return; // Zakończenie operacji, gdy użytkownik naciśnie ESC
        }

        List<String> customWords = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        try {
            fileLoader.saveCustomWords(customWords, "custom_words.txt");
            model.updateWords(customWords);
            view.displayMessage("Added custom words: " + customWords);
        } catch (IOException e) {
            view.displayMessage("Error saving custom words. Please try again.");
        }
    }

    // Tworzenie nowego pliku ze słowami podanymi przez użytkownika
    public void createNewWordFile() throws IOException {
        // Krok 1: Pobierz nazwę pliku od użytkownika
        view.displayInlineInput("Enter the name for the new file (e.g., new_words.txt): ", "");
        String fileName = promptForTextInput("Enter the name for the new file (e.g., new_words.txt): ");

        // Sprawdź, czy użytkownik anulował lub wpisał pustą nazwę
        if (fileName == null || fileName.trim().isEmpty()) {
            view.displayMessage("File creation canceled.");
            return; // Anulowanie, jeśli nazwa pliku jest pusta
        }

        // Krok 2: Pobierz słowa od użytkownika
        view.displayInlineInput("Enter words separated by commas: ", "");
        String input = promptForTextInput("Enter words separated by commas: ");

        // Przetwarzaj wprowadzone słowa
        List<String> newWords = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        // Próba zapisania słów do nowego pliku
        try {
            fileLoader.saveCustomWords(newWords, fileName);
            view.displayMessage("Created file " + fileName + " with words: " + newWords);
        } catch (IOException e) {
            view.displayMessage("Error creating file. Please try again.");
        }
    }

    // Pobieranie tekstu od użytkownika
    public String promptForTextInput(String prompt) throws IOException {
        StringBuilder input = new StringBuilder();
        view.displayInlineInput(prompt, ""); // refresh the screen
        while (true) {
            var keyStroke = view.getScreen().readInput();
            if (keyStroke.getKeyType() == KeyType.Enter) {
                break;
            } else if (keyStroke.getKeyType() == KeyType.Escape) {
                // Gdy naciśnięto ESC, zwracamy wartość null, aby wskazać anulowanie
                return null;
            } else if (keyStroke.getKeyType() == KeyType.Backspace && input.length() > 0) {
                // Usuń ostatni znak z `input` przy naciśnięciu Backspace
                input.deleteCharAt(input.length() - 1);
                view.displayInlineInput(prompt, input.toString());
            } else if (keyStroke.getKeyType() == KeyType.Character) {
                input.append(keyStroke.getCharacter());
                view.displayInlineInput(prompt, input.toString());
            }
        }
        return input.toString();
    }
}
