package search.searchlogic;

import search.MainController;
import search.SearchInException;
import search.SearchTask;
import search.searchlogic.filesearch.FileResult;
import search.searchlogic.filesearch.FileSearcher;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Parses a folder (including subfolders) and starts a term search for every found file that fits the search task requirement
 */
public class DirectoryParser {
    private final SearchTask task;
    private List<String> ignoreList;
    private final MainController controller;

    public DirectoryParser(SearchTask task, List<String> ignoreList, MainController controller) {
        this.task = task;
        this.ignoreList = ignoreList;
        this.controller = controller;
    }

    /**
     * Searches the directory for viable files and starts a file search when one is found
     */
    public void searchDirectory(Path directoryPath) throws SearchInException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path entry : directoryStream) {
                // ignore directory if applicable, otherwise search it by calling this method recursively
                if (Files.isDirectory(entry)) {
                    if (isContainedInIgnoreFile(entry)) {
                        controller.ignoreDirectory(entry);
                    } else {
                        this.searchDirectory(entry);
                    }
                } else if (Files.isRegularFile(entry)) {
                    if (hasMatchingEnding(entry)) {
                        FileSearcher fileSearcher = new FileSearcher(entry.toString(), task.getSearchTerm());
                        try {
                            FileResult fileResult = fileSearcher.searchTerm();
                            controller.addSearchedFile(fileResult);
                        } catch (SearchInException e) {
                            controller.ignoreFile(entry);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new SearchInException("An error has occurred while streaming the directory.", e);
        }
    }

    // checks whether a file has the matching file extension as given in the search task
    private boolean hasMatchingEnding(Path entry) {
        String FileExtensions = this.task.getFileExtension();
        return entry.toString().endsWith(FileExtensions);
    }

    // Checks if a directory given by the Path entry is on the List<String> ignore if true
    // return true else return false
    private boolean isContainedInIgnoreFile(Path entry) {
        for (String line : this.ignoreList) {
            if (entry.endsWith(line)) {
                return true;
            }
        }
        return false;
    }
}
