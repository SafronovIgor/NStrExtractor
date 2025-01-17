package com.app.file.writer;

import com.app.file.extractor.MethodName;

import java.util.List;
import java.util.Map;

public interface FileMethodArgumentWriter {
    void write(Map<MethodName, List<String>> map);
}
