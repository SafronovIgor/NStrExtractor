package com.app.util;

import java.nio.file.Path;
import java.util.Optional;

public final class PathManager {

    private PathManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Optional<String> getFileExtensionSafe(Path pathToFile) {
        if (pathToFile == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        String fileName = pathToFile.getFileName().toString();

        if (fileName.startsWith(".")) {
            return Optional.empty();
        }

        int lastIndex = fileName.lastIndexOf('.');

        if (lastIndex != -1 && lastIndex < fileName.length() - 1) {
            return Optional.of(fileName.substring(lastIndex + 1));
        }

        return Optional.empty();
    }
}