package com.app.v2.file.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public abstract class AbstractFileMethodArgumentExtractor {
    private final File sourceFile;
    private final MethodName[] methods;
    private Map<MethodName, List<String>> extractedArgumentsByMethod;

    public AbstractFileMethodArgumentExtractor(File sourceFile, MethodName[] methods) {
        this.sourceFile = Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    public final Map<MethodName, List<String>> extract() {
        try {
            preProcessFile();
            readAndProcessFile();
            postProcessExtractedArguments();
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Failed to extract arguments from file: %s", sourceFile.getAbsolutePath()),
                    e
            );
        }
        return Collections.unmodifiableMap(getExtractedArguments());
    }

    private void readAndProcessFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                processLine(line, lineNumber);
            }
        }
    }

    private void processLine(String line, int lineNumber) {
        if (!preProcessLine(line)) {
            return;
        }

        for (MethodName method : methods) {
            List<String> extractedArguments = getExtractedArguments().computeIfAbsent(method, k -> new ArrayList<>());
            MatchExtractionParams matchParams = MatchExtractionParams.builder()
                    .line(line)
                    .pattern(method.getPattern())
                    .methodName(method)
                    .build();

            extractMatches(matchParams, extractedArguments, lineNumber);
        }
    }

    private void extractMatches(MatchExtractionParams params, List<String> extractedArguments, int lineNumber) {
        Matcher matcher = params.getPattern().matcher(params.getLine());

        while (matcher.find()) {
            if (params.getMethodName() == MethodName.N_STR) {
                processNStrContent(matcher.group(1), lineNumber, extractedArguments);
            } else {
                String result = matcher.group(1);
                if (result != null) {
                    extractedArguments.add(result);
                }
            }
        }
    }

    private void processNStrContent(String nstrContent, int lineNumber, List<String> extractedArguments) {
        Arrays.stream(nstrContent.split(";"))
                .map(entry -> entry.split("="))
                .filter(parts -> parts.length == 2)
                .map(parts -> {
                    String languageCode = parts[0].trim();
                    String text = parts[1].trim().replace("'", "");
                    return String.format("%d: %s : %s", lineNumber, languageCode, text);
                })
                .forEach(extractedArguments::add);
    }

    protected void preProcessFile() {
        // Опциональный метод для переопределения
    }

    protected boolean preProcessLine(String line) {
        return true; // По умолчанию ничего не фильтруем
    }

    protected void postProcessExtractedArguments() {
        // Опциональный метод для переопределения
    }

    private Map<MethodName, List<String>> getExtractedArguments() {
        if (extractedArgumentsByMethod == null) {
            extractedArgumentsByMethod = new HashMap<>();
        }
        return extractedArgumentsByMethod;
    }
}