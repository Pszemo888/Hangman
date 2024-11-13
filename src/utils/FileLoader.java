package utils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader {

    private final String filePath;
   // private static final String RESOURCES_DIRECTORY = "src/resources/";

    public FileLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> loadWords() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("Plik " + filePath + " nie został znaleziony w zasobach!");
                return List.of(); // Zwróć pustą listę, jeśli plik nie został znaleziony
            }

            System.out.println("Plik " + filePath + " został znaleziony, rozpoczynam wczytywanie...");

            // Wczytujemy linie i konwertujemy każdą na wielkie litery
            List<String> words = reader.lines()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());

            System.out.println("Wczytano słowa: " + words);
            return words;

        } catch (Exception e) {
            System.out.println("Błąd podczas wczytywania pliku " + filePath + ": " + e.getMessage());
            e.printStackTrace(); // Zapisuje pełen ślad błędu
            return List.of(); // Zwróć pustą listę, jeśli wystąpi błąd
        }
    }

    public List<String> loadWordsFromFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Plik nie znaleziony: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
        }
    }

    public void saveCustomWords(List<String> customWords, String fileName) throws IOException {
        // Otwieramy plik do zapisu
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (String word : customWords) {
                // Sprawdzamy, czy słowo nie jest puste ani nie składa się wyłącznie z białych znaków
                if (word != null && !word.trim().isEmpty()) {
                    writer.write(word.toUpperCase());
                    writer.newLine();
                } else {
                    System.out.println("Puste słowo pominięte przy zapisie do pliku: " + fileName);
                }
            }
            System.out.println("Dodano niestandardowe słowa do pliku: " + fileName);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania niestandardowych słów do pliku: " + fileName);
            e.printStackTrace();
        }
    }



}
