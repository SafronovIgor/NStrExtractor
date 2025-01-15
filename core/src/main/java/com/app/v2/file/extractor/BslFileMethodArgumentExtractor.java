package com.app.v2.file.extractor;

import java.io.File;

public class BslFileMethodArgumentExtractor extends AbstractFileMethodArgumentExtractor {

    public BslFileMethodArgumentExtractor(File file, MethodName[] methodNames) {
        super(file, methodNames);
    }

    @Override
    protected boolean preProcessLine(String line) {
        return line != null && !line.isEmpty() && !line.startsWith("//") && !line.startsWith("Var");
    }
}