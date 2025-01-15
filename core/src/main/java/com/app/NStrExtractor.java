package com.app;

import com.app.v1.file.extractor.BslFileMethodArgumentExtractor;
import com.app.v1.file.extractor.MethodName;
import com.app.v1.file.writer.FileMethodArgumentWriter;
import com.app.v1.file.writer.FileMethodArgumentWriterImpl;
import com.app.v2.FileExtractor;

import java.io.File;
import java.nio.file.Path;

public class NStrExtractor {
    public static void main(String[] args) {
        String s = "/Users/igorsafronov/IdeaProjects/NStrExtractor/core/src/main/resources/ObjectModule.bsl"; // absolute path to file

        // version 1
        FileExtractor fileExtractor = new FileExtractor();
        fileExtractor.extractNStrToFile(Path.of(s));

        // version 2
        var bslFileMethodArgumentExtractor = new BslFileMethodArgumentExtractor(
                new File(s),
                new MethodName[]{MethodName.N_STR}
        );
        FileMethodArgumentWriter fileMethodArgumentWriter = new FileMethodArgumentWriterImpl();

        fileMethodArgumentWriter.write(bslFileMethodArgumentExtractor.extract());
    }
}