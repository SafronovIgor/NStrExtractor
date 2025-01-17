package com.app.file.extractor;

import java.util.List;
import java.util.Map;

public interface FileMethodArgumentExtractor {
    Map<MethodName, List<String>> extractArguments();
}