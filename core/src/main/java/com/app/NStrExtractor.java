package com.app;

import java.nio.file.Path;

public class NStrExtractor {
    public static void main(String[] args) {
        String s = ""; // absolute path to file
        new FileExtractor().extractNStrToFile(Path.of(s));
    }
}