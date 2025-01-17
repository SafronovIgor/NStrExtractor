package com.app.v2.file.extractor;

import com.app.file.extractor.BslFileMethodArgumentExtractor;
import com.app.file.extractor.MethodName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BslFileMethodArgumentExtractorTest {

    @Test
    void whenNStrHasMultipleArgumentsInOneLineThenExtractCorrectly() throws IOException {
        File tempFile = File.createTempFile("test", ".bsl");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("MessageString = NStr(\"en=Hello;ru=Привет\", CommonUseClientServer.MainLanguageCode());");
        }

        MethodName[] methods = {MethodName.N_STR};
        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(tempFile, methods);

        Map<MethodName, List<String>> result = extractor.extractArguments();

        assertNotNull(result);
        assertTrue(result.containsKey(MethodName.N_STR));
        List<String> arguments = result.get(MethodName.N_STR);
        assertEquals(2, arguments.size());
        assertEquals("1: en : Hello", arguments.get(0));
        assertEquals("1: ru : Привет", arguments.get(1));
    }

    @Test
    void whenNStrContainsValidEntriesThenExtractArgumentsCorrectly() throws IOException {
        File tempFile = File.createTempFile("test", ".bsl");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("NStr(\"en=Hello;ru=Привет\");");
        }

        MethodName[] methods = {MethodName.N_STR};
        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(tempFile, methods);

        Map<MethodName, List<String>> result = extractor.extractArguments();

        assertNotNull(result);
        assertTrue(result.containsKey(MethodName.N_STR));
        List<String> arguments = result.get(MethodName.N_STR);
        assertEquals(2, arguments.size());
        assertEquals("1: en : Hello", arguments.get(0));
        assertEquals("1: ru : Привет", arguments.get(1));
    }

    @Test
    void whenFileContainsCommentsAndEmptyLinesThenSkipThem() throws IOException {
        File tempFile = File.createTempFile("test", ".bsl");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("// Comment line\n");
            writer.write("\n");
            writer.write("NStr(\"en=Hi\");\n");
        }

        MethodName[] methods = {MethodName.N_STR};
        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(tempFile, methods);

        Map<MethodName, List<String>> result = extractor.extractArguments();

        assertNotNull(result);
        assertTrue(result.containsKey(MethodName.N_STR));
        List<String> arguments = result.get(MethodName.N_STR);
        assertEquals(1, arguments.size());
        assertEquals("3: en : Hi", arguments.get(0));
    }

    @Test
    void whenFileContainsMultipleNStrEntriesThenExtractAllArguments() throws IOException {
        File tempFile = File.createTempFile("test", ".bsl");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("NStr(\"en=Hello;ru=Привет\");\n");
            writer.write("NStr(\"es=Hola;fr=Bonjour\");\n");
        }

        MethodName[] methods = {MethodName.N_STR};
        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(tempFile, methods);

        Map<MethodName, List<String>> result = extractor.extractArguments();

        assertNotNull(result);
        assertTrue(result.containsKey(MethodName.N_STR));
        List<String> arguments = result.get(MethodName.N_STR);
        assertEquals(4, arguments.size());
        assertEquals("1: en : Hello", arguments.get(0));
        assertEquals("1: ru : Привет", arguments.get(1));
        assertEquals("2: es : Hola", arguments.get(2));
        assertEquals("2: fr : Bonjour", arguments.get(3));
    }

    @Test
    void whenFileIsEmptyOrInvalidThenReturnEmptyResult() throws IOException {
        File tempFile = File.createTempFile("test", ".bsl");

        MethodName[] methods = {MethodName.N_STR};
        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(tempFile, methods);

        Map<MethodName, List<String>> result = extractor.extractArguments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void whenFileDoesNotExistThenThrowException() {
        File nonExistentFile = new File("non-existent-file.bsl");

        MethodName[] methods = {MethodName.N_STR};

        BslFileMethodArgumentExtractor extractor = new BslFileMethodArgumentExtractor(nonExistentFile, methods);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                extractor::extractArguments
        );

        assertTrue(exception.getMessage().contains("Failed to extract arguments"));
    }
}