package com.app.v1.file.extractor;

import java.util.regex.Pattern;

public enum MethodName {
    N_STR(Pattern.compile("NStr\\(\"(.*?)\"\\)"));

    private final Pattern pattern;

    MethodName(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}