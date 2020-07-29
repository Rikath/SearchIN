package search;

public class SearchTask {

    private final String folderPath;
    private final String ignoreFile;
    private final String fileExtension;
    private final String searchTerm;
    private final String resultFile;

    public SearchTask(String folderPath, String ignoreFile, String fileExtension, String searchTerm, String resultFile) {
        this.folderPath = folderPath;
        this.ignoreFile = ignoreFile;
        this.fileExtension = fileExtension;
        this.searchTerm = searchTerm;
        this.resultFile = resultFile;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getIgnoreFile() {
        return ignoreFile;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getResultFile() {
        return resultFile;    }

    public String toString () {
        String full = "Root Folder: " + folderPath + "\n";
        if (ignoreFile.isEmpty()) {
            full += "Ignore File: none\n";
        } else {
            full += "Ignore File: " + ignoreFile + "\n";
        }
        if (fileExtension.isEmpty()) {
            full += "File Extension: none\n";
        } else {
            full += "File Extension: " + fileExtension + "\n";
        }
        full += "Search Term: " + searchTerm + "\n";
        full += "Result File: " + resultFile;
        return full;
    }
}
