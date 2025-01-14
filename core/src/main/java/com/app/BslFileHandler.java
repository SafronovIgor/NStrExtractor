package com.app;

import java.io.*;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BslFileHandler implements FileHandler {
    private static final Pattern NSTR_PATTERN = Pattern.compile("NStr\\(\"(.*?)\"\\)");

    @Override
    public void handle(Path pathToFile) {
        Path outputFile = Path.of(pathToFile.getParent().toString(), "NStr_Extracted_Output.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {

            processFile(reader, writer);

            System.out.println("Файл успешно обработан. Результаты сохранены в: " + outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обработке файла: " + e.getMessage(), e);
        }
    }

    private void processFile(BufferedReader reader, BufferedWriter writer) throws IOException {
        int lineNumber = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            processLine(line, lineNumber, writer);
        }
    }

    private void processLine(String line, int lineNumber, BufferedWriter writer) throws IOException {
        Matcher matcher = NSTR_PATTERN.matcher(line);

        while (matcher.find()) {
            String nstrContent = matcher.group(1);
            processNStrContent(nstrContent, lineNumber, writer);
        }
    }

    private void processNStrContent(String nstrContent, int lineNumber, BufferedWriter writer) throws IOException {
        String[] languageEntries = nstrContent.split(";");

        for (String entry : languageEntries) {
            String[] parts = entry.split("=");

            if (parts.length == 2) {
                String languageCode = parts[0].trim();
                String text = parts[1].trim().replace("'", "");

                writer.write(String.format("%d: %s : %s", lineNumber, languageCode, text));
                writer.newLine();
            }
        }
    }
}