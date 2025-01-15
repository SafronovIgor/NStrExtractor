package com.app.v1.file.writer;

import com.app.v1.file.extractor.MethodName;

import java.util.List;
import java.util.Map;

public interface FileMethodArgumentWriter {
    void write(Map<MethodName, List<String>> map);
}
