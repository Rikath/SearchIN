package search.searchlogic.filesearch;

import java.util.List;

public class FileResult {
    private final String path;
    private final String term;
    private final int termCount;
    private final List<SearchTermHit> results;

    public FileResult (String path, String term, int termCount, List<SearchTermHit> results) {
        this.path = path;
        this.term = term;
        this.termCount = termCount;
        this.results = results;
    }

    public String toString() {
        // possibly add file path before each result
        return "File: " + path + " contains **" + term + "** " + termCount + " times: \n\n" + resultsToString();
    }

    private String resultsToString() {
        String resultString = "";
        for (SearchTermHit result : results) {
            resultString += result.toString();
        }
        return resultString;
    }

    public boolean includesTerm() {
        return termCount > 0;
    }

    public String getPath() {
        return path;
    }

    public String getTerm() {
        return term;
    }

    public int getTermCount() {
        return termCount;
    }

    public List<SearchTermHit> getResults() {
        return results;
    }
}
