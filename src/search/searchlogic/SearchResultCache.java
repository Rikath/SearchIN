package search.searchlogic;

import search.searchlogic.filesearch.FileResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Caches intermediate data while a search task is being executed
 */

public class SearchResultCache {
    private List<String> ignorePaths;
    private List<FileResult> fileResults;
    private int totalTermCount;
    private boolean wasInterrupted;

    public SearchResultCache() {
        ignorePaths = new ArrayList<>();
        fileResults = new ArrayList<>();
        totalTermCount = 0;
        wasInterrupted = false;
    }

    /**
     * Adds the String of a path to the ignored Path list
     * @param path String that should be added
     */
    public void addIgnoredPath(String path) {
        ignorePaths.add(path);
    }

    /**
     * Adds a FileResult to the list and increments the term hit count by the FileResult's term count
     * @param result FileResult that should be added
     */
    public void addFileResult(FileResult result) {
        fileResults.add(result);
        totalTermCount += result.getTermCount();
    }

    public void setInterrupted () {
        wasInterrupted = true;
    }

    public List<String> getIgnorePaths() {
        return ignorePaths;
    }

    public List<FileResult> getFileResults() {
        return fileResults;
    }

    public int getTotalTermCount() {
        return totalTermCount;
    }

    public boolean isWasInterrupted() {
        return wasInterrupted;
    }
}
