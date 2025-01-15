package com.app.v2.file.extractor;

import java.util.regex.Pattern;

public class MatchExtractionParams {
    private final String line;
    private final Pattern pattern;
    private final MethodName methodName;

    private MatchExtractionParams(Builder builder) {
        this.line = builder.line;
        this.pattern = builder.pattern;
        this.methodName = builder.methodName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getLine() {
        return line;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public MethodName getMethodName() {
        return methodName;
    }

    public static class Builder {
        private String line;
        private Pattern pattern;
        private MethodName methodName;

        public Builder line(String line) {
            if (line == null || line.isEmpty()) {
                throw new IllegalArgumentException("Line cannot be null or empty");
            }
            this.line = line;
            return this;
        }

        public Builder pattern(Pattern pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern cannot be null");
            }
            this.pattern = pattern;
            return this;
        }

        public Builder methodName(MethodName methodName) {
            this.methodName = methodName;
            return this;
        }

        public MatchExtractionParams build() {
            if (line == null) {
                throw new IllegalStateException("Line is required and must be set");
            }
            if (pattern == null) {
                throw new IllegalStateException("Pattern is required and must be set");
            }
            return new MatchExtractionParams(this);
        }
    }
}