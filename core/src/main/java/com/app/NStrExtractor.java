package com.app;

import java.nio.file.Path;

public class NStrExtractor {
    public static void main(String[] args) {
        String s = ""; // absolute path to file

        FileExtractor fileExtractor = new FileExtractor();
        fileExtractor.extractNStrToFile(Path.of(s));
    }
}