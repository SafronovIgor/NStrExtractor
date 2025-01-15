package com.app.v2.file.writer;

import com.app.v2.file.extractor.MethodName;

import java.util.List;
import java.util.Map;

public interface FileMethodArgumentWriter {
    void write(Map<MethodName, List<String>> map);
}
