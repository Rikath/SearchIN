package search.gui;

/**
 * Interface for SearchIN GUIs to allow for other views later
 */
public interface SearchInGui {

    void printMessage(String message);
    void printError(String error);
    String mainMenu();
    String getInput();
    String getInput(String question);
    String askRootFolder();
    String askIgnore();
    String askExtension();
    String askTerm();
    String askResultFile();
    String getExitMessage();
}
