package com.app.v2;

import com.app.util.PathManager;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class FileExtractor {
    private final Map<String, BslFileHandler> fileHandlers;

    public FileExtractor() {
        fileHandlers = Map.of("bsl", new BslFileHandler());
    }

    public void extractNStrToFile(Path pathToFile) {
        Optional<String> optExtension = PathManager.getFileExtensionSafe(pathToFile);
        optExtension.ifPresentOrElse(
                extension -> {
                    FileHandler handler = fileHandlers.get(extension);
                    if (handler != null) {
                        handler.handle(pathToFile);
                    } else {
                        throw new IllegalArgumentException("Handler not found for file extension: " + extension);
                    }
                },
                () -> System.out.println("File does not have a valid extension.")
        );
    }
}