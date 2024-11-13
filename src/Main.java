//import com.googlecode.lanterna.screen.Screen;
//import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import controller.HangmanController;
import model.HangmanModel;
import utils.FileLoader;
import view.TerminalView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {

            FileLoader fileLoader = new FileLoader("words.txt");
            List<String> words = fileLoader.loadWords();

            HangmanModel model = new HangmanModel(words);
            TerminalView view = new TerminalView();
            HangmanController controller = new HangmanController(model, view);

            controller.startGame();
            view.closeScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
