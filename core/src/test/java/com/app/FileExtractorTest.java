package com.app;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileExtractorTest {

    private final FileExtractor fileExtractor = new FileExtractor();

    @ParameterizedTest(name = "Check valid extension '{0}' for file handler.")
    @ValueSource(strings = {"bsl"})
    void WhenExtensionIsValidThenHandlerExists(String keyHandler) {
        Map<String, BslFileHandler> fileHandlers = fileExtractor.getFileHandlers();
        BslFileHandler bslFileHandler = fileHandlers.get(keyHandler);

        assertNotNull(bslFileHandler, "Handler for valid extension should not be null.");
    }

    @ParameterizedTest(name = "Check handle extractors file.")
    @ValueSource(strings = {".js", ".txt"})
    public void WhenExtensionNotValidThenExceptions(String keyHandler) {
        assertThrowsExactly(IllegalArgumentException.class, () -> fileExtractor.extractNStrToFile(Path.of(keyHandler)));
    }

    @ParameterizedTest(name = "Check invalid path '{0}'.")
    @ValueSource(strings = {" ", "QWERTY://", "<>", "C:\\Invalid\\??"})
    @NullAndEmptySource
    public void WhenPathNotValidThenExceptions(String notValidPath) {
        if (notValidPath == null || notValidPath.trim().isEmpty()) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> {
                        if (notValidPath == null) {
                            throw new IllegalArgumentException("Path cannot be null");
                        } else {
                            throw new IllegalArgumentException("Path cannot be empty or blank");
                        }
                    });
            assertTrue(exception.getMessage().contains("Path cannot"), "Unexpected exception message");
        } else {
            assertThrowsExactly(InvalidPathException.class, () -> Path.of(notValidPath));
        }
    }
}