package com.app;

import com.app.util.PathManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public final class PathManagerTest {

    @Test
    void whenPathIsNullThenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> PathManager.getFileExtensionSafe(null));
    }

    @Test
    void whenFileHasExtensionThenReturnExtension() {
        Path pathToFile = Paths.get("src/main/java/com/app/PathManager.java");
        Optional<String> fileExtension = PathManager.getFileExtensionSafe(pathToFile);
        assertTrue(fileExtension.isPresent());
        assertEquals("java", fileExtension.get());
    }

    @Test
    void whenFileHasNoExtensionThenReturnEmpty() {
        Path pathToFile = Paths.get("README");
        Optional<String> fileExtension = PathManager.getFileExtensionSafe(pathToFile);
        assertTrue(fileExtension.isEmpty());
    }

    @Test
    void whenFileIsHiddenThenReturnEmpty() {
        Path pathToFile = Paths.get(".env");
        Optional<String> fileExtension = PathManager.getFileExtensionSafe(pathToFile);
        assertTrue(fileExtension.isEmpty());
    }

    @Test
    void whenFileHasMultipleDotsInNameThenReturnLastExtension() {
        Path pathToFile = Paths.get("archive.tar.gz");
        Optional<String> fileExtension = PathManager.getFileExtensionSafe(pathToFile);
        assertTrue(fileExtension.isPresent());
        assertEquals("gz", fileExtension.get());
    }
}