package controller;

import model.HangmanModel;
import view.TerminalView;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;

import view.Theme;

public class MenuController {
    private final HangmanModel model;
    private final TerminalView view;
    private final FileController fileController;

    public MenuController(HangmanModel model, TerminalView view, FileController fileController) {
        this.model = model;
        this.view = view;
        this.fileController = fileController;
    }

    public int displayMainMenu() throws IOException {
        String[] options = {"Start Game", "Settings", "Statistics","Achievements","Rules", "Exit"};
        return navigateMenu(options);
    }

    public void displaySettingsMenu() throws IOException {
        String[] settingsOptions = {"Difficulty", "Game Duration", "Word Source","Subject","Theme", "Back"};
        int selectedOption = navigateMenu(settingsOptions);

        switch (selectedOption) {
            case 0 -> chooseDifficulty();
            case 1 -> setGameDuration();
            case 2 -> setWordSource();
            case 3 -> chooseSubject();
            case 4 -> toggleTheme();
            case 5 -> { return; }
        }
    }

    private void chooseDifficulty() throws IOException {
        String[] difficultyOptions = {"Easy", "Medium", "Hard","EXTREME"};
        int selectedOption = navigateMenu(difficultyOptions);

        switch (selectedOption) {
            case 0 -> model.setDifficulty(8);
            case 1 -> model.setDifficulty(6);
            case 2 -> model.setDifficulty(4);
            case 3 -> model.setDifficulty(1);
        }

        view.displayMessage("Selected difficulty: " + difficultyOptions[selectedOption]);
        view.getScreen().refresh();
    }

    private void setGameDuration() throws IOException {
        String[] durationOptions = {"30 seconds", "60 seconds", "90 seconds", "Custom", "Back"};
        int selectedOption = navigateMenu(durationOptions);

        switch (selectedOption) {
            case 0 -> model.setGameDuration(30);
            case 1 -> model.setGameDuration(60);
            case 2 -> model.setGameDuration(90);
            case 3 -> {
                int customDuration = promptForCustomDuration();
                if (customDuration > 0) {
                    model.setGameDuration(customDuration);
                    view.displayMessage("Game duration set to " + customDuration + " seconds.");
                } else {
                    //view.displayMessage("Invalid duration. Please enter a positive number.");
                }
            }
            case 4 -> { return; }
        }
    }

    private void setWordSource() throws IOException {
        String[] sourceOptions = {"Load from File", "Add Custom Words", "Create new File", "Back"};
        int selectedOption = navigateMenu(sourceOptions);

        switch (selectedOption) {
            case 0 -> fileController.loadWordsFromFile();
            case 1 -> fileController.addCustomWords();
            case 2 -> fileController.createNewWordFile();
            case 3 -> { return; }
        }
    }

    private void chooseSubject() throws IOException {
        String[] subjectOptions = {"Technology", "Countries", "Animals", "Back"};
        int selectedOption = navigateMenu(subjectOptions);

        switch (selectedOption) {
            case 0 -> fileController.setSubject("technology.txt");
            case 1 -> fileController.setSubject("countries.txt");
            case 2 -> fileController.setSubject("animals.txt");
            case 3 -> { return; }
        }
        view.displayMessage("Selected subject: " + subjectOptions[selectedOption]);
    }



    private void toggleTheme() throws IOException {
        String[] themeOptions = {"Dark", "Light", "Back"};
        int selectedOption = navigateMenu(themeOptions);

        switch (selectedOption) {
            case 0 -> {
                view.setTheme(Theme.DARK);  // Ustawia motyw ciemny
                toggleTheme();               // Rekurencyjnie wywołujemy menu, aby pozostać w wyborze motywu
            }
            case 1 -> {
                view.setTheme(Theme.LIGHT); // Ustawia motyw jasny
                toggleTheme();               // Rekurencyjnie wywołujemy menu, aby pozostać w wyborze motywu
            }
            case 2 -> {
                // Wybieramy "Back" - metoda kończy się bez ponownego wywołania
                return;
            }
        }
    }



    private int navigateMenu(String[] options) throws IOException {
        int selectedOption = 0;

        while (true) {
            view.drawMenu(view.getScreen(), options, selectedOption);
            var keyType = getInputKeyType();

            if (keyType == KeyType.ArrowDown) {
                selectedOption = (selectedOption + 1) % options.length;
            } else if (keyType == KeyType.ArrowUp) {
                selectedOption = (selectedOption - 1 + options.length) % options.length;
            } else if (keyType == KeyType.Enter) {
                return selectedOption;
            }
        }
    }

    private KeyType getInputKeyType() {
        try {
            var keyStroke = view.getScreen().readInput();
            return keyStroke != null ? keyStroke.getKeyType() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int promptForCustomDuration() throws IOException {
        String promptMessage = "Enter custom time: ";
        String input = fileController.promptForTextInput(promptMessage);

        // Check if input is null (indicating Esc key was pressed)
        if (input == null) {
            view.displayMessage("Custom duration setting canceled.");
            return -1; // Indicates cancellation
        }

        // Only proceed with parsing if the input is not empty or null
        try {
            int duration = Integer.parseInt(input);
            return duration > 0 ? duration : -1; // Valid positive duration or -1 if zero or negative
        } catch (NumberFormatException e) {
            view.displayMessage("Invalid duration. Please enter a positive number.");
            return -1;
        }
    }

    public void displayAchievements(){
        view.displayMessage("Soon... ");
    }
}
