package search.searchlogic.filesearch;

import search.SearchInException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
    private List<String> fileLines;
    private final String filePath;
    private final String term;
    private final int termLength;
    private int termCount = 0;
    private int lineCount = 1;
    private List<SearchTermHit> searchTermHits;

    public FileSearcher(String path, String term) {
        this.filePath = path;
        this.term = term;
        this.termLength = term.length();
        this.searchTermHits = new ArrayList<>();
    }

    /**
     * Searches for a Token in a File and returns the result as String
     * @return result as FileResult that contains all information about term findings in that file
     * @throws SearchInException when a file can not be read and converted into a list of Strings
     */
    public FileResult searchTerm() throws SearchInException {
        this.fileLines = fileToList();
        for (String line : fileLines) {
            searchTermInLine(line, 0);
            lineCount++;
        }
        return new FileResult(filePath, term, termCount, searchTermHits);
    }

    /*
     * Returns all findings of the search term within the line as string in the same format as searchTerm().
     */
    private void searchTermInLine(String lineString, int subStringIndex) {
        // Part of the line that is being evaluated
        String checkString = lineString.substring(subStringIndex);
        if (checkString.contains(term)) {
            int startIndex = checkString.indexOf(term) + subStringIndex;
            searchTermHits.add(new SearchTermHit(term, lineString, lineCount, startIndex));
            termCount++;
            searchTermInLine(lineString, startIndex + termLength);
        }
    }

    private List<String> fileToList() throws SearchInException {
        List<String> list = new ArrayList<String>();
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
        }
        catch(IOException e) {
            throw new SearchInException("Could not read the file: " + filePath, e);
        }
        return list;
    }

    public List<String> getFileLines() {
        return fileLines;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTerm() {
        return term;
    }

    public int getTermCount() {
        return termCount;
    }

    public int getLineCount() {
        return lineCount;
    }
}
