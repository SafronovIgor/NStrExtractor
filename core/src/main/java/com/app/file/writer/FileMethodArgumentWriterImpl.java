package com.app.file.writer;

import com.app.file.extractor.MethodName;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileMethodArgumentWriterImpl implements FileMethodArgumentWriter {
    private static final String OUTPUT_FILE_NAME = "V2_Extracted_Output.txt";

    @Override
    public void write(Map<MethodName, List<String>> map) {
        File outputFile = new File(OUTPUT_FILE_NAME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            for (Map.Entry<MethodName, List<String>> entry : map.entrySet()) {
                MethodName method = entry.getKey();
                List<String> arguments = entry.getValue();

                writer.write("[Method]: " + method.name());
                writer.newLine();

                for (String argument : arguments) {
                    writer.write(argument);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Failed to write extracted arguments to file: %s", OUTPUT_FILE_NAME),
                    e
            );
        }
    }
}