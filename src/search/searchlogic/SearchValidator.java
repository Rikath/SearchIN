package search.searchlogic;

import search.SearchInException;

import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Validates search task inputs to ensure that the paths exist and can be accessed and that the ignore file is a valid file type
 */

public final class SearchValidator {

    // possible file extensions for the ignore file
    private static final String[] ignoreFileExtensions = {"txt", "md", "rtf"};

    public static void validateRootFolder(String path) throws SearchInException {
        if (path == null) {
            throw new SearchInException("The root folder can not be null.");
        }
        try {
            Path folderPath = Paths.get(path);
            if (!Files.isDirectory(folderPath)) {
                throw new SearchInException("The root folder has to be a directory.");
            }
            if (!Files.exists(folderPath)) {
                throw new SearchInException("The entered root folder does not exist.");
            }
        } catch (SecurityException e) {
            throw new SearchInException("The entered root folder could not be accessed.", e);
        } catch (IllegalArgumentException e) {
            throw new SearchInException("The format of the RootFolder path can not be read.", e);
        } catch (FileSystemNotFoundException e) {
            throw new SearchInException("The path that should lead to the RootFolder, does not lead to a file.", e);
        }
    }

    public static void validateIgnoreFile(String path) throws SearchInException {
        if (path == null) {
            throw new SearchInException("The ignore file path can not be null.");
        }
        try {
            Path ignoreFile = Paths.get(path);
            if (Files.notExists(ignoreFile)) {
                throw new SearchInException("The ignore file does not exist");
            }
        } catch (SecurityException e) {
            throw new SearchInException("The ignore file can not be accessed, because the permission is denied.", e);
        } catch (IllegalArgumentException e) {
            throw new SearchInException("The format of the ignore file path can not be read.", e);
        } catch (FileSystemNotFoundException e) {
            throw new SearchInException(
                    "The path entered for the ignore file does not lead to a file.", e);
        }
        if (!hasIgnoreFileExtension(path)) {
            throw new SearchInException("The selected file must end with a valid extension: " + ignoreFileExtensions.toString());
        }
    }

    public static void validateFileExtension(String extension) throws SearchInException {
        if (extension == (null)) {
            throw new SearchInException("The file extension can not be null.");
        }
        if (!extension.startsWith(".")) {
            throw new SearchInException("The file extension needs to start with a '.' [e.g. '.txt'].");
        }
    }

    public static void validateTerm(String term) throws SearchInException {
        if (term == null || term.isEmpty()) {
            throw new SearchInException("The search term can not be empty.");
        }
    }

    public static void validateResultFile(String path) throws SearchInException {
        if (path == null) {
            throw new SearchInException("The given String for the result file can not be empty.");
        }
        try {
            Path resultPath = Paths.get(path);
            if (!Files.exists(resultPath)) {
                throw new SearchInException("The result file does not exist. ");
            }
            if (!Files.isWritable(resultPath)) {
                throw new SearchInException("Unable to write into the result file");
            }
        } catch (SecurityException e) {
            throw new SearchInException("The result file can not be accessed, because the permission is denied.", e);
        } catch (IllegalArgumentException e) {
            throw new SearchInException("The format of the path to the result file can not be read.", e);
        } catch (FileSystemNotFoundException e) {
            throw new SearchInException(
                    "The path that should lead to the result file, does not lead to a file at all.", e);
        }
    }

    /**
     * Checks if a given file is of an accepted file type
     *
     * @param filePath File path of the given file
     * @return true if the extension is acceptable
     */
    public static boolean hasIgnoreFileExtension(String filePath) {
        for (String extension : ignoreFileExtensions) {
            if (filePath.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getIgnoreFileExtensions() {
        return ignoreFileExtensions;
    }
}
