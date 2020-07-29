package search.searchlogic;

import search.SearchTask;

/**
 * Caches the search task data to possibly reuse it and thereby reduce input for the second search
 */
public class SearchTaskCache {
    private String rootFolder;
    private String ignoreFile;
    private String fileExtension;
    private String searchTerm;
    private String resultFile;
    private boolean hasCompleteInformation;
    private final String resultFileDefault = System.getProperty("user.dir") + "\\resources\\resultFile.txt";
    private final String ignoreFileDefault = System.getProperty("user.dir") + "\\resources\\ignoreFile.txt";


    public SearchTaskCache() {
        this.rootFolder = "";
        this.ignoreFile = "";
        this.fileExtension = "";
        this.searchTerm = "";
        this.resultFile = resultFileDefault;
        this.hasCompleteInformation = false;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
        if (informationCompleted()) {
            setCompleted();
        }
    }

    public String getIgnoreFile() {
        return ignoreFile;
    }

    public void setIgnoreFile(String ignoreFile) {
        this.ignoreFile = ignoreFile;
        if (informationCompleted()) {
            setCompleted();
        }
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        if (informationCompleted()) {
            setCompleted();
        }
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        if (informationCompleted()) {
            setCompleted();
        }
    }

    public String getResultFile() {
        return resultFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
        if (informationCompleted()) {
            setCompleted();
        }
    }

    public boolean hasCompleteInformation() {
        return hasCompleteInformation;
    }

    public void setCompleted() {
        hasCompleteInformation = true;
    }

    public String getResultFileDefault() {
        return resultFileDefault;
    }

    public String getIgnoreFileDefault() {
        return ignoreFileDefault;
    }

    public void setAll(String rootFolder, String ignoreFile, String fileExtension, String searchTerm, String resultFile) {
        this.rootFolder = rootFolder;
        this.ignoreFile = ignoreFile;
        this.fileExtension = fileExtension;
        this.searchTerm = searchTerm;
        this.resultFile = resultFile;
        this.hasCompleteInformation = true;
    }

    public void clear() {
        this.rootFolder = "";
        this.ignoreFile = "";
        this.fileExtension = "";
        this.searchTerm = "";
        this.resultFile = resultFileDefault;
        this.hasCompleteInformation = false;
    }

    public SearchTask toSearchTask() {
        return new SearchTask(rootFolder, ignoreFile, fileExtension, searchTerm, resultFile);
    }

    public String toString() {
        String full = "Root Folder: " + rootFolder + "\n";
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

    private boolean informationCompleted() {
        return !(rootFolder.isEmpty() || searchTerm.isEmpty() || resultFile.isEmpty());
    }
}
