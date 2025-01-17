package com.app.file.extractor;

import java.util.regex.Pattern;

public enum MethodName {
    N_STR(Pattern.compile("NStr\\(\\s*\"([^\"]*)\""));

    private final Pattern pattern;

    MethodName(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}