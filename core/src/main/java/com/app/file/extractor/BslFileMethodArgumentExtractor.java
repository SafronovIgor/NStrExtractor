package com.app.file.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BslFileMethodArgumentExtractor implements FileMethodArgumentExtractor {
    private static final Pattern SEMICOLON_SPLIT_PATTERN = Pattern.compile(";");
    private static final Pattern EQUAL_SIGN_SPLIT_PATTERN = Pattern.compile("=");
    private final File sourceFile;
    private final MethodName[] methods;
    private final Map<MethodName, List<String>> extractedArgumentsByMethod = new HashMap<>();

    public BslFileMethodArgumentExtractor(File sourceFile, MethodName[] methods) {
        this.sourceFile = Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    @Override
    public Map<MethodName, List<String>> extractArguments() {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            String currentLine;
            int currentLineNumber = 0;

            while ((currentLine = reader.readLine()) != null) {
                currentLineNumber++;
                if (shouldProcessLine(currentLine)) {
                    processSourceLine(currentLine, currentLineNumber);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Failed to extract arguments from file: %s", sourceFile.getAbsolutePath()),
                    e
            );
        }
        return Collections.unmodifiableMap(extractedArgumentsByMethod);
    }

    private void processSourceLine(String line, int lineNumber) {
        for (MethodName method : methods) {
            List<String> extractedArguments = extractedArgumentsByMethod
                    .computeIfAbsent(method, k -> new ArrayList<>());

            extractMatches(line, method, extractedArguments, lineNumber);
        }
    }

    private void extractMatches(String line, MethodName method, List<String> extractedArguments, int lineNumber) {
        Matcher matcher = method.getPattern().matcher(line);

        while (matcher.find()) {
            String match = matcher.group(1);

            if (method == MethodName.N_STR && match != null) {
                processNStr(match, extractedArguments, lineNumber);
            } else if (match != null) {
                extractedArguments.add(match);
            }
        }
    }

    private void processNStr(String nstrContent, List<String> extractedArguments, int lineNumber) {
        SEMICOLON_SPLIT_PATTERN.splitAsStream(nstrContent)
                .map(entry -> EQUAL_SIGN_SPLIT_PATTERN.split(entry, 2))
                .filter(parts -> parts.length == 2)
                .map(parts -> {
                    String languageCode = parts[0].trim();
                    String value = parts[1].trim().replace("'", "");
                    return String.format("%d: %s : %s", lineNumber, languageCode, value);
                })
                .forEach(extractedArguments::add);
    }

    public boolean shouldProcessLine(String line) {
        return line != null && !line.isEmpty() && !line.startsWith("//") && !line.startsWith("Var");
    }
}