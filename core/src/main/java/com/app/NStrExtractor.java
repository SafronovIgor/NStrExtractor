package com.app;

import com.app.file.extractor.BslFileMethodArgumentExtractor;
import com.app.file.extractor.MethodName;
import com.app.file.writer.FileMethodArgumentWriterImpl;

import java.io.File;

public class NStrExtractor {
    public static void main(String[] args) {
        String s = "E:\\Java\\NStrExtractor\\core\\src\\main\\resources\\ObjectModule.bsl"; // absolute path to file
        var bslFileMethodArgumentExtractor = new BslFileMethodArgumentExtractor(
                new File(s),
                new MethodName[]{MethodName.N_STR}
        );
        var fileMethodArgumentWriter = new FileMethodArgumentWriterImpl();

        fileMethodArgumentWriter.write(bslFileMethodArgumentExtractor.extractArguments());
    }
}