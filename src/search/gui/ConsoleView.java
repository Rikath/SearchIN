package search.gui;

import java.util.Scanner;

/**
 * Console view for SearchIN that asks and takes desired actions via text input
 */

public class ConsoleView implements SearchInGui {

    private final Scanner scanner;
    private final String exitMessage = "0";

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.out.println("Error: " + message);
    }

    public String getInput(String question) {
        System.out.println(question);
        return scanner.nextLine();
    }

    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * Asks for the desired action and returns the answer as String
     * @return the action input, 1 for searching and 2 to exit the program
     */
    public String mainMenu() {
        System.out.println("What do you want to do?");
        // TODO make enum for possible actions
        System.out.println("(1) Search");
        System.out.println("(0) Exit");
        return scanner.nextLine();
    }

    // asks for the root folder String
    // repeats process if input folder path does not exist
    public String askRootFolder() {
        printMessage("Input folder path: (enter " + exitMessage + " to abort)");
        return scanner.nextLine();
    }

    // asks where an ignore file is located
    public String askIgnore() {
        this.printMessage("Input ignore file path. Leave empty to not skip any folders. Input (1) to use default location: ");
        return scanner.nextLine();
    }

    // asks for the file extension that files should have, if none is entered, all files will be attempted to search
    public String askExtension() {
        this.printMessage("Which file type (e.g.'.txt') should be searched through? (no entry means all possible files will be searched): ");
        return scanner.nextLine().trim().toLowerCase();
    }

    // asks for a search term and validates that it's not empty
    public String askTerm() {
        this.printMessage("What is your search term: (enter " + exitMessage + " to abort)");
        return scanner.nextLine();
    }

    // asks for the path where a result file should be saved and validates its existence
    public String askResultFile() {
        this.printMessage("Where should the result file be saved? (no entry if default path should be used)");
        return scanner.nextLine();
    }

    public String getExitMessage() {
        return exitMessage;
    }
}
