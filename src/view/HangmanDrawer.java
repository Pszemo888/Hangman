package view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;


public class HangmanDrawer {

        // Dodaj offsetX, aby przesunąć rysunek w poziomie
        public void drawHangman(TextGraphics graphics, int attemptsLeft, TextColor hangmanColor) {
            graphics.setForegroundColor(hangmanColor);

            int offsetX = 35;

            // Szubienica
            graphics.putString(offsetX + 5, 2, "+-------+");   // Górna belka
            graphics.putString(offsetX + 5, 3, "|       |");
            graphics.putString(offsetX + 5, 4, "|       |");
            graphics.putString(offsetX + 5, 5, "|");           // Pionowa belka szubienicy
            graphics.putString(offsetX + 5, 6, "|");
            graphics.putString(offsetX + 5, 7, "|");
            graphics.putString(offsetX + 5, 8, "|");
            graphics.putString(offsetX + 5, 9, "|");
            graphics.putString(offsetX + 5, 10, "|");
            graphics.putString(offsetX + 5, 11, "|");

            // Podstawa szubienicy
            graphics.putString(offsetX + 3, 12, "=========");

            // Rysowanie wisielca w zależności od liczby pozostałych prób
            if (attemptsLeft <= 5) {
                // Głowa
                graphics.putString(offsetX + 12, 5, " O ");
            }
            if (attemptsLeft <= 4) {
                // Tułów
                graphics.putString(offsetX + 12, 6, " | ");
            }
            if (attemptsLeft <= 3) {
                // Lewe ramię
                graphics.putString(offsetX + 12, 6, "/| ");
            }
            if (attemptsLeft <= 2) {
                // Prawe ramię
                graphics.putString(offsetX + 12, 6, "/|\\");
            }
            if (attemptsLeft <= 1) {
                // Lewa noga
                graphics.putString(offsetX + 12, 7, "/  ");
            }
            if (attemptsLeft == 0) {
                // Prawa noga
                graphics.putString(offsetX + 12, 7, "/ \\");
            }
        }
    }