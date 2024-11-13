package view;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*; //do fontu
import java.io.IOException;
import java.util.Set;

public class TerminalView implements IView {
    private  Screen screen;
    private Theme currentTheme = Theme.DARK;
    private final HangmanDrawer hangmanDrawer = new HangmanDrawer();

    public TerminalView() {
        initializeTerminal(); // Wywołujemy metodę inicjalizującą terminal w konstruktorze
    }
    private void initializeTerminal() {
        try {
           Font font = new Font("Monospaced", Font.BOLD, 18);
           SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(font);

            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setTerminalEmulatorTitle("Hangman")
                    .setInitialTerminalSize(new TerminalSize(80, 25))
                    .setTerminalEmulatorFontConfiguration(fontConfig);
            screen = terminalFactory.createScreen();
            screen.startScreen(); // Rozpoczęcie ekranu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Screen getScreen() {
        return screen;
    }

    public void closeScreen() {
        try {
            if (screen != null) {
                screen.stopScreen(); // Zatrzymanie ekranu
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawMenu(Screen screen, String[] options, int selectedOption) throws IOException {
        // Ustaw kolory na podstawie `currentTheme`
        TextColor titleColor = currentTheme == Theme.DARK ? TextColor.ANSI.CYAN : TextColor.ANSI.BLUE;
        TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
        TextColor footerColor = currentTheme == Theme.DARK ? TextColor.ANSI.YELLOW : TextColor.ANSI.MAGENTA;
        TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;

        TextGraphics graphics = screen.newTextGraphics();
        screen.clear();

        // Ustaw tło dla całego terminala
        graphics.setBackgroundColor(backgroundColor);
        graphics.fill(' '); // Wypełnia cały ekran kolorem tła

        // Ustawienie kolorów tytułu i tekstu w zależności od motywu
        graphics.setForegroundColor(titleColor);
        putCenteredText(graphics, 2, "==============================");
        putCenteredText(graphics, 3, "            Hangman           ");
        putCenteredText(graphics, 4, "==============================");

        // Wyświetlenie opcji menu
        for (int i = 0; i < options.length; i++) {
            graphics.setForegroundColor(i == selectedOption ? footerColor : textColor);
            putCenteredText(graphics, 6 + i * 2, (i == selectedOption ? "> " : "  ") + options[i]);
        }

        screen.refresh();
    }


    @Override
    public void displayGameState(String wordDisplay, int attemptsLeft, int score, Set<Character> wrongGuesses, int remainingTime,int hintCount, int maxHints) {
        try {
            // Definiowanie kolorów w zależności od motywu
            TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
            TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
            TextColor hangmanColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLUE_BRIGHT;
            TextColor highlightColor = currentTheme == Theme.DARK ? TextColor.ANSI.RED : TextColor.ANSI.BLUE;
            TextColor timerColor = currentTheme == Theme.DARK ? TextColor.ANSI.GREEN : TextColor.ANSI.RED;

            screen.clear();
            TextGraphics graphics = screen.newTextGraphics();

            // Ustaw tło całego ekranu na podstawie motywu
            graphics.setBackgroundColor(backgroundColor);
            graphics.fill(' ');

            // Wyświetlenie timera z kolorem zależnym od motywu
            graphics.setForegroundColor(timerColor);
            String timerText = "Time: " + remainingTime + "s";
            int xPosition = screen.getTerminalSize().getColumns() - timerText.length() - 3;
            graphics.putString(xPosition, 2, timerText);

            // Wyświetlenie szubienicy
            hangmanDrawer.drawHangman(graphics, attemptsLeft, hangmanColor);

            // Wyświetlenie podstawowych informacji o grze
            graphics.setForegroundColor(textColor);
            graphics.putString(3, 4, "Attempts Left: " + attemptsLeft);
            graphics.putString(3, 2, "Score: " + score);
            graphics.putString(2, 8, "Wrong Letters: ");
            graphics.putString(2, 10, "Hints Used: " + hintCount + "/" + maxHints);
            graphics.putString(2, 19, "Press 'Tab' for hint (-10 points)");
            // Wyświetlenie błędnych liter z kolorem podświetlenia
            int XPosition = 18;
            for (Character letter : wrongGuesses) {
                graphics.setForegroundColor(highlightColor);
                graphics.putString(XPosition, 8, letter.toString());
                XPosition += 2;
            }

            // Wycentrowane wyświetlenie zgadywanego słowa na dole ekranu
            graphics.setForegroundColor(textColor);
            String wordText = wordDisplay;
            int wordTextX = (screen.getTerminalSize().getColumns() - wordText.length()) / 2;
            int wordTextY = screen.getTerminalSize().getRows() - 10; // Pozycja u dołu ekranu
            graphics.putString(wordTextX, wordTextY, wordText);

            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public char getInput() {
        try {
            KeyStroke keyStroke = screen.readInput();
            if (keyStroke.getKeyType() == KeyType.Character) {
                return keyStroke.getCharacter();
            } else if (keyStroke.getKeyType() == KeyType.Tab) {
                return ' '; // Zwracamy spację jako znak specjalny
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return '\0';
    }

    public void displayMessage(String message) {
        try {
            screen.clear();
            TextGraphics graphics = screen.newTextGraphics();

            // Ustaw kolory w zależności od motywu
            TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
            TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;

            // Ustawienie tła na cały ekran, by wiadomość była czytelna
            graphics.setBackgroundColor(backgroundColor);
            graphics.setForegroundColor(textColor);
            graphics.fill(' ');

            // Wyśrodkowane wyświetlenie wiadomości
            putCenteredText(graphics, 6, message);
            screen.refresh();
            Thread.sleep(2000);
            // Wyświetl wiadomość przez 2 sekundy
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayInlineInput(String promptMessage, String inputText) {

        try {
            screen.clear();
            TextGraphics graphics = screen.newTextGraphics();

            // Ustaw kolory w zależności od motywu
            TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
            TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
            TextColor highlightColor = currentTheme == Theme.DARK ? TextColor.ANSI.YELLOW : TextColor.ANSI.MAGENTA;

            // Wypełnienie ekranu kolorem tła
            graphics.setBackgroundColor(backgroundColor);
            graphics.setForegroundColor(textColor);
            graphics.fill(' ');

            // Wyświetlenie dynamicznego komunikatu (np. „Enter file name:”)
            putCenteredText(graphics, 7, promptMessage + inputText);
            graphics.setForegroundColor(highlightColor);
            putCenteredText(graphics, 9, "Press Enter to confirm, or Esc to cancel");

            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayStats(Screen screen, int wins, int losses, int score) throws IOException {
        TextGraphics graphics = screen.newTextGraphics();
        screen.clear();

        TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
        TextColor titleColor = currentTheme == Theme.DARK ? TextColor.ANSI.CYAN : TextColor.ANSI.BLUE;
        TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
        TextColor highlightColor = currentTheme == Theme.DARK ? TextColor.ANSI.YELLOW : TextColor.ANSI.MAGENTA;

        graphics.setBackgroundColor(backgroundColor);
        graphics.fill(' ');
        graphics.setForegroundColor(titleColor);

        putCenteredText(graphics, 5, "--- Hangman Stats ---");
        graphics.setForegroundColor(textColor);
        putCenteredText(graphics, 7, "Wins:" + wins);
        putCenteredText(graphics, 8, "Losses:" + losses);
        putCenteredText(graphics, 9, "Score: " + score);
        graphics.setForegroundColor(highlightColor);
        putCenteredText(graphics, 11, "Press any key to return to the menu");
        screen.refresh();
        screen.readInput();
    }
    public void displayRules(Screen screen) throws IOException{
        String[] rules = {
                "",
                "Celem gry jest odgadnięcie ukrytego słowa, literka po literce.",
                "Masz ograniczoną liczbę prób, aby zgadnąć całe słowo.",
                "Za każdą błędną próbę tracisz jedną próbę i część wizerunku wisielca.",
                "Używając podpowiedzi, odkrywasz jedną literę,",
                        "ale tracisz dodatkową próbę i punkty.",
                "Gra kończy się, gdy odgadniesz słowo lub stracisz wszystkie próby.",
                "",
                "--- Punktacja ---",
                "2. Zaczynasz grę mając 100 punktów",
                "2. Poprawna litera dodaje 2 punkty do twojego wyniku.",
                "3. Odgadnięcie całego słowa daje dodatkowe 50 punktów.",
                "4. Każde użycie podpowiedzi odejmuje 10 punktów.",
                "5. Każda błędna litera zmniejsza liczbę prób, ale nie wpływa na punkty.",
                "6. Nieodgadnięcie całego słowa odejmuje 25 punktów od wyniku.",
                "",
                "Keep gambling and have fun!"
        };
        TextGraphics graphics = screen.newTextGraphics();
        screen.clear();

        TextColor backgroundColor = currentTheme == Theme.DARK ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
        TextColor titleColor = currentTheme == Theme.DARK ? TextColor.ANSI.CYAN : TextColor.ANSI.BLUE;
        TextColor textColor = currentTheme == Theme.DARK ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
        TextColor highlightColor = currentTheme == Theme.DARK ? TextColor.ANSI.YELLOW : TextColor.ANSI.MAGENTA;
        graphics.setBackgroundColor(backgroundColor);
        graphics.fill(' ');
        graphics.setForegroundColor(titleColor);
        putCenteredText(graphics, 2,  "--- Zasady Gry Hangman ---");
        graphics.setForegroundColor(textColor);
        int yPosition = 4;
        for (String line : rules) {
            putCenteredText(graphics, yPosition, line);
            yPosition++;
        }
        graphics.setForegroundColor(highlightColor);
        putCenteredText(graphics, 23, "Press any key to return to the menu");
        screen.refresh();
        screen.readInput();
    }
    // Wyśrodkowanie tekstu w terminalu
    private void putCenteredText(TextGraphics graphics, int y, String text) {
        int terminalWidth = 80; // Szerokość terminala (dostosuj w zależności od potrzeb)
        int x = (terminalWidth - text.length()) / 2; // Oblicz, aby wyśrodkować tekst
        graphics.putString(x, y, text);
    }


    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }

}

