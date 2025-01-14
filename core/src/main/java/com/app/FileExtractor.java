package com.app;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileExtractor {
    private final Map<String, BslFileHandler> fileHandlers;

    public FileExtractor() {
        fileHandlers = Map.of("bsl", new BslFileHandler());
    }

    public void extractNStrToFile(Path pathToFile) {
        Objects.requireNonNull(pathToFile, "Path to file cannot be null.");

        String fileExtension = getFileExtension(pathToFile);
        FileHandler handler = fileHandlers.get(fileExtension);

        if (handler != null) {
            handler.handle(pathToFile);
        } else {
            throw new IllegalArgumentException("Handler not found for file extension: " + fileExtension);
        }
    }

    private String getFileExtension(Path pathToFile) {
        final String FILE_EXTENSION_SEPARATOR = ".";
        String fileName = pathToFile.getFileName().toString();
        int lastIndex = fileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        if (lastIndex != -1 && lastIndex < fileName.length() - 1) {
            return fileName.substring(lastIndex + 1);
        } else {
            throw new RuntimeException("File does not have a valid extension.");
        }
    }

    public Map<String, BslFileHandler> getFileHandlers() {
        return new HashMap<>(fileHandlers);
    }
}