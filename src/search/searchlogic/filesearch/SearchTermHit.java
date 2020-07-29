package search.searchlogic.filesearch;

/**
 * Holds Information about the finding place of the search term within a file
 */
public class SearchTermHit {
    private final String term;
    private final String line;
    private final int lineNumber;
    private final int linePosition;

    public SearchTermHit(String term, String line, int lineNumber, int linePosition) {
        this.term = term;
        this.line = line;
        this. lineNumber = lineNumber;
        this. linePosition = linePosition;
    }

    public String toString() {
        String termInStars = line.substring(0, linePosition) + "**" + term + "**" + line.substring(linePosition + term.length());
        return "Line " + lineNumber + ", Position " + linePosition + "> " + termInStars + "\n";
    }

    public String getTerm() {
        return term;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public String getLine() {
        return line;
    }
}
