package search;

import search.gui.ConsoleView;
import search.gui.SearchInGui;
import search.searchlogic.*;
import search.searchlogic.filesearch.FileResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

// TODO concurrent search

/**
 *  Controls the processing of information based on user input.
 *  Contains the process logic for the search function.
 */
public class MainController {
    private SearchInGui mainView;
    private final String exitMessage;
    private SearchTask searchTask;
    private SearchTaskCache taskCache;
    private SearchResultCache searchResultCache;

    public MainController() {
        mainView = new ConsoleView();
        taskCache = new SearchTaskCache();
        exitMessage = mainView.getExitMessage();
    }

    /**
     * Starts the main controller by showing the main menu
     */
    public void start() {
        mainMenu();
    }

    public void mainMenu() {
        // TODO adapt to action enum
        String action = mainView.mainMenu();
        if (action.equals("1")) {
            initSearch();
        } else if (action.equals("0")) {
            exit();
        } else {
            mainView.printError("Invalid input, please enter 1 or 0");
            mainMenu();
        }
    }

    private void initSearch() {
        updateSearchInfo();
        searchTask = taskCache.toSearchTask();
        search(searchTask);
    }

    // updates the search information
    // if valid information is still in the cache, only changing the search term of the previous search is offered as an option
    private void updateSearchInfo() {
        if (taskCache.hasCompleteInformation()) {
            // TODO options to change one variable (term, result path, root folder path)
            String answer = mainView.getInput("Enter (1) to use previous settings and only change search term. Any other key to enter new information.");
            if (answer.equals("1")) {
                mainView.printMessage("Using cached data.");
                taskCache.setSearchTerm(askTerm());
            } else {
                askTask();
            }
        } else {
            askTask();
        }
    }

    // asks for necessary information to start a search task
    private void askTask() {
        // TODO allow for exit when exitMessage is entered
        String rootFolder;
        String ignoreFile;
        String fileExtension;
        String searchTerm;
        String resultFile;

        mainView.printMessage("----\tInput necessary information\t----");
        rootFolder = askRootFolder();
        searchTerm = askTerm();
        ignoreFile = askIgnore();
        fileExtension = askExtension();
        resultFile = askResultFile();
        taskCache.setAll(rootFolder, ignoreFile, fileExtension, searchTerm, resultFile);
    }

    /**
     * Searches through all files specified in the search task
     * @param searchTask task that includes all search information
     */
    public void search(SearchTask searchTask) {
        mainView.printMessage("Search Task starting for: " + searchTask.toString() + "\n...\n");
        try {
            List<String> ignoreList = ignoreFileToList(searchTask.getIgnoreFile());
            DirectoryParser directoryParser = new DirectoryParser(searchTask, ignoreList, this);
            searchResultCache = new SearchResultCache();
            directoryParser.searchDirectory(Paths.get(searchTask.getFolderPath()));
            printSearchSummary();
            writeResultToFile();
        } catch (SearchInException e) {
            mainView.printError("Search Task could not be executed: " + e.getMessage());
        }
        mainMenu();
    }

    public void ignoreDirectory(Path directory) {
        mainView.printMessage(directory.toString() + " directory was ignored." + "\n");
        searchResultCache.addIgnoredPath(directory.toString());
    }

    public void ignoreFile(Path file) {
        mainView.printError(file.toString() + " could not be read and was ignored.\n");
    }

    public void addSearchedFile(FileResult fileResult) {
        mainView.printMessage(fileResult.toString());
        searchResultCache.addFileResult(fileResult);
    }

    private void exit() {
        mainView.printMessage("Have a nice day!");
    }

    private void printSearchSummary() {
        mainView.printMessage("The folder includes **" + searchTask.getSearchTerm() + "** " + searchResultCache.getTotalTermCount() + " times.");
        System.out.println("\n\n*** SUMMARY ***\n\n");
        System.out.println(searchTask.toString() + "\n\n");
        for (FileResult fileResult : searchResultCache.getFileResults()) {
            System.out.println(fileResult.toString());
        }
    }

    // writes summary to the designated result file
    private void writeResultToFile() {
        Path resultFile = Paths.get(searchTask.getResultFile());
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
            writer.write("Search Summary for:\n" + searchTask.toString() + "\n\n");
            for (FileResult fileResult : searchResultCache.getFileResults()) {
                writer.write(fileResult.toString() + "\n\n");
                writer.newLine();
            }
        }
        catch (IOException e) {
            mainView.printError("Could not write the result to a file.");
        }
        mainView.printMessage("\nSearch result was saved in " + searchTask.getResultFile());
    }

    // reads ignore file and converts entries into a list
    private List<String> ignoreFileToList (String ignoreFile) throws SearchInException {
        List<String> ignoreList = new ArrayList<>();
        if (ignoreFile.isEmpty()) {
            return ignoreList;
        }
        Path path = Paths.get(ignoreFile);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while (line != null) {
                ignoreList.add(line);
                line = reader.readLine();
            }
        } catch(IOException e) {
            throw new SearchInException("An error has occurred while reading the ignore File. Make sure the given file exists.", e);
        }
        return ignoreList;
    }

    /*
        helper functions to update and verify search task information
     */

    // asks for the folder path that should be searched
    private String askRootFolder() {
        String rootFolder = mainView.askRootFolder();
        try {
            SearchValidator.validateRootFolder(rootFolder);
        } catch (SearchInException e) {
            mainView.printError(e.getMessage());
            rootFolder = askRootFolder();
        }
        return rootFolder;
    }

    // asks where an ignore file is located
    private String askIgnore() {
        String ignoreFile = mainView.askIgnore();
        if (ignoreFile.isEmpty()) {
            ignoreFile = ""; // treat differently!!
        } else if (ignoreFile.equals("0")) {
            return taskCache.getIgnoreFileDefault();
        } else {
            try {
                SearchValidator.validateIgnoreFile(ignoreFile);
            } catch (SearchInException e) {
                mainView.printError(e.getMessage());
                ignoreFile = askIgnore();
            }
        }
        return ignoreFile;
    }

    // asks for the file extension that files should have, if none is entered, all files will be attempted to search
    private String askExtension() {
        return mainView.askExtension();
    }

    // asks for a search term and validates that it's not empty
    private String askTerm() {
        String term = mainView.askTerm();
        try {
            SearchValidator.validateTerm(term);
        } catch (SearchInException e) {
            mainView.printError(e.getMessage());
            term = askTerm();
        }
        return term;
    }

    // asks for the path where a result file should be saved and validates its existence
    private String askResultFile() {
        String resultPath = mainView.askResultFile();
        if (resultPath.isEmpty()) {
            resultPath = taskCache.getResultFileDefault();
        } else {
            try {
                SearchValidator.validateResultFile(resultPath);
            } catch (SearchInException e) {
                mainView.printError(e.getMessage());
                resultPath = askResultFile();
            }
        }
        return resultPath;
    }
}
